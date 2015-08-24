package net.dicesoft.colorpicker_android;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.util.Log;

/**
 * Created by stephen on 8/21/15.
 */
public class MusicThread extends Thread {
    private static final String TAG = "MusicThread";
    private MediaPlayer mp;
    private AssetManager assets;

    public MusicThread(Context ctx) {
        assets = ctx.getAssets();
    }

    @Override
    public void run() {
        Log.v(TAG, "Music Thread");
        startMusic();
    }

    private void startMusic() {
        mp = new MediaPlayer();
            while (!Thread.currentThread().isInterrupted()) {
                if (!mp.isPlaying()) {
                    try {
                        AssetFileDescriptor descriptor = assets.openFd("music/tatu-dangerous_and_moving.mid");
                        mp.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                        descriptor.close();

                        mp.prepare();
                        mp.setVolume(1f, 1f);
                        mp.setLooping(true);

                        mp.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        if (Thread.currentThread().isInterrupted()) {
            mp.stop();
            mp.release();
            try {
                this.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
