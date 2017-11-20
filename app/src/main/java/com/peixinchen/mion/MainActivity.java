package com.peixinchen.mion;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.peixinchen.mion.api.GetArtifactPublicities;
import com.peixinchen.mion.models.ShareImageItem;
import com.peixinchen.mion.models.SharedItem;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, GetArtifactPublicities.Monitor {
    private GridViewAdapter gridViewAdapter;
    private GetArtifactPublicities.ArtifactPublicity result;
    private SharedItem sharedItem = new SharedItem();
    private List<ShareImageItem> shareImageItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(this);

        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridViewAdapter = new GridViewAdapter(this, R.layout.grid_view_item_layout);
        gridView.setAdapter(gridViewAdapter);

        GetArtifactPublicities api = new GetArtifactPublicities();
        api.setMonitor(this);
        api.get("http://182.254.132.183/api/item.json");
    }

    // UI Listeners
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        String tagline = result.taglines[checkedId];
        Log.d("API", tagline);
        sharedItem.tagline = tagline;
    }

    public void onCheckboxClicked(View view) {
        int position = (int)view.getTag();
        ShareImageItem shareImageItem = shareImageItemList.get(position);
        if (shareImageItem.isChecked == false) {
            shareImageItem.isChecked = true;
            if (sharedItem.imageUrlList.contains(shareImageItem.imageUrl) == false) {
                sharedItem.imageUrlList.add(shareImageItem.imageUrl);
            }
        } else {
            shareImageItem.isChecked = false;
            if (sharedItem.imageUrlList.contains(shareImageItem.imageUrl) == true) {
                sharedItem.imageUrlList.remove(shareImageItem.imageUrl);
            }
        }
    }

    public void onShareButtonClick(View view) {
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    999);
            return;
        }

        final List<String> filenameList = new ArrayList<>();
        for (String imageUrl: sharedItem.imageUrlList) {
            Picasso.with(this)
                    .load(imageUrl)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            try {
                                File file = File.createTempFile("moin_", ".jpg", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
                                Log.d("API", file.getAbsolutePath());
                                FileOutputStream os = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, os);
                                os.close();

                                synchronized (this) {
                                    filenameList.add(file.getAbsolutePath());
                                    if (filenameList.size() != sharedItem.imageUrlList.size()) {
                                        return;
                                    }
                                }

                                WechatShareUtil.share(MainActivity.this, sharedItem.tagline, filenameList);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                        }
                    });
        }
        //WechatShareUtil.share();
    }

    // api.GetArtifactPublicities.Monitor
    @Override
    public void onQuerySuccess(GetArtifactPublicities.ArtifactPublicity result) {
        /*
        for (String imageUrl: Arrays.asList(result.imageUrls)) {
            Log.d("API", imageUrl);
        }
        */
        this.result = result;

        if (result == null || result.taglines == null || result.imageUrls == null) {
            return;
        }

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        for (int i = 0; i < result.taglines.length; i++) {
            String tagline = result.taglines[i];
            RadioButton radioButton = new RadioButton(this);
            radioButton.setId(i);
            radioButton.setText(tagline);
            if (i == 0) {
                radioButton.setChecked(true);
            }
            radioGroup.addView(radioButton);
        }

        for (String imageUrl: result.imageUrls) {
            ShareImageItem shareImageItem = new ShareImageItem(false, imageUrl);
            shareImageItemList.add(shareImageItem);
        }

        gridViewAdapter.addAll(shareImageItemList);
    }

    @Override
    public void onQueryFailure(final IOException e) {
        e.printStackTrace();
    }
}
