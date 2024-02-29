package com.tourify;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class ImageFetcher {

    private static final Handler handler = new Handler();

    public interface ImageFetchListener {
        void onImageFetched(Bitmap bitmap);
    }

    public static void fetchImage(final String urlString, final ImageFetchListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = null;
                try {
                    URL url = new URL(urlString);
                    InputStream in = url.openStream();
                    bitmap = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Bitmap finalBitmap = bitmap;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onImageFetched(finalBitmap);
                    }
                });
            }
        }).start();
    }


    public static void fetchImages(final List<String> urlStrings, final ImageFetchListener listener) {
        for (String urlString : urlStrings) {
            fetchImage(urlString, listener);
        }
    }
}
