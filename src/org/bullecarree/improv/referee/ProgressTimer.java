package org.bullecarree.improv.referee;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.os.SystemClock;

public class ProgressTimer {
    private Handler progressHandler = new Handler();
    
    private long progressStartTime = 0;

    private int durationInSeconds;
    
    private List<ProgressListener> progressListeners = new ArrayList<ProgressListener>();
    
    private boolean running = false;
    
    private Runnable updateProgressTime = new Runnable() {
        public void run() {
            if (running) {
                final long start = progressStartTime;
                final long now = SystemClock.uptimeMillis(); 
                long millis =  now - start;
                int seconds = (int) (millis / 1000);
    
                int progress = 100 - (((durationInSeconds - seconds) * 100) / durationInSeconds);
                
                for (ProgressListener listener : progressListeners) {
                    listener.onTick(progress, millis);
                }
                
                // Don't schedule anything if progress is finished
                if (progress < 100) {
                    progressHandler.postAtTime(this, now + 1000);
                }
            }
        };
    };
    
    public ProgressTimer(int durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }
    
    public void start() {
        progressStartTime = SystemClock.uptimeMillis();
        progressHandler.removeCallbacks(updateProgressTime);
        progressHandler.postDelayed(updateProgressTime, 100);
        running = true;
    }
    
    public void reset() {
        progressStartTime = 0;
        progressHandler.removeCallbacks(updateProgressTime);
        running = false;
    }
    
    // TODO implement pause properly
    
    public void resume() {
        progressStartTime = SystemClock.uptimeMillis();
        progressHandler.removeCallbacks(updateProgressTime);
        progressHandler.postDelayed(updateProgressTime, 100);
        running = true;
    }
    
    public void stop() {
        running = false;
    }
    
    public boolean isRunning() {
        return running;
    }
    
    public void addProgressListener(ProgressListener listener) {
        this.progressListeners.add(listener);
    }
    
}
