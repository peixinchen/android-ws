package com.peixinchen.mion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.peixinchen.mion.api.GetArtifactPublicities;

import java.io.IOException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, RadioGroup.OnCheckedChangeListener, GetArtifactPublicities.Monitor {
    private GridViewAdapter gridViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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

        if (result == null || result.taglines == null || result.imageUrls == null) {
            return;
        }

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        for (String tagline: result.taglines) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(tagline);
            radioGroup.addView(radioButton);
        }

        gridViewAdapter.addAll(Arrays.asList(result.imageUrls));
    }

    @Override
    public void onQueryFailure(final IOException e) {
        e.printStackTrace();
    }
}
