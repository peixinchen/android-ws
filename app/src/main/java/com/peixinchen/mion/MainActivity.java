package com.peixinchen.mion;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, RadioGroup.OnCheckedChangeListener, GetArtifactPublicities.Monitor {
    private GridViewAdapter gridViewAdapter;
    private GetArtifactPublicities.ArtifactPublicity result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(this);

        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridViewAdapter = new GridViewAdapter(this, R.layout.grid_view_item_layout);
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener(this);

        GetArtifactPublicities api = new GetArtifactPublicities();
        api.setMonitor(this);
        api.get("http://182.254.132.183/api/item.json");
    }

    // UI Listeners
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        Log.d("API", result.taglines[checkedId]);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ImageView imageView = view.findViewById(R.id.imageView);
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        Log.d("API", bitmap.toString());
    }

    public void onShareButtonClick(View view) {
        WechatShareUtil.share();
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

        gridViewAdapter.addAll(Arrays.asList(result.imageUrls));
    }

    @Override
    public void onQueryFailure(final IOException e) {
        e.printStackTrace();
    }
}
