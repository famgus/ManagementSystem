package com.ec.managementsystem.util;

import android.app.Application;
import android.content.Context;
import android.media.MediaPlayer;

import java.io.File;

public final class MyApplication extends Application {

    private static Context context;
    private static MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        try {
            super.onCreate();
            MyApplication.context = getApplicationContext();
        } catch (Exception e) {
            Utils.CreateLogFile("MyApplication.onCreate: " + e.getMessage());
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // MultiDex.install(this);
    }

    public static Context GetAppContext() {
        return MyApplication.context;
    }

    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public static void setMediaPlayer(MediaPlayer mediaPlayer) {
        MyApplication.mediaPlayer = mediaPlayer;
    }

    /*public static void clearCache(){
        try {
            File[] files = context.getCacheDir().listFiles();
            for (File file : files) {
                file.delete();
            }
        } catch (Exception e) {

        }
    }*/

    public static void clearCache() {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
        }
    }

    public static void deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                deleteDir(new File(dir, children[i]));
            }
            dir.delete();
        } else if (dir != null && dir.isFile()) {
            dir.delete();
        }
    }
}
