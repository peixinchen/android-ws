package com.peixinchen.mion.api;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class GetArtifactPublicities {
    private Handler mainHandler;
    private Handler bgHandler;
    private Monitor monitor;

    public interface Monitor {
        public void onQuerySuccess(ArtifactPublicity result);
        public void onQueryFailure(final IOException e);
    }

    public static class ArtifactPublicity {
        @SerializedName("copywriting_list") public String[] taglines;
        @SerializedName("image_list") public String[] imageUrls;

        @Override
        public String toString() {
            return String.format("ArtifactPublicity{taglines=%s, imageUrls=%s}",
                    Arrays.toString(taglines), Arrays.toString(imageUrls));
        }
    }

    public GetArtifactPublicities() {
        HandlerThread bgThread = new HandlerThread("api_thread");
        bgThread.start();
        bgHandler = new Handler(bgThread.getLooper());
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public void setMonitor(Monitor monitor) {
        this.monitor = monitor;
    }

    public void get(final String apiUrl) {
        bgHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(apiUrl).build();
                    Response response = client.newCall(request).execute();
                    ResponseBody body = response.body();
                    if (body == null) {
                        throw new IOException();
                    }
                    String json = body.string();
                    Gson gson = new Gson();
                    final ArtifactPublicity result = gson.fromJson(json, ArtifactPublicity.class);
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            monitor.onQuerySuccess(result);
                        }
                    });
                } catch (final IOException e) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            monitor.onQueryFailure(e);
                        }
                    });
                }
            }
        });
    }
}
