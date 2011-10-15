package org.bullecarree.improv.referee;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

public class ProgressTimer {
    private Handler progressHandler = new Handler();
    
    private long referenceTime = 0;

    private long accumulatedTime = 0;
    
    private int durationInSeconds;
    
    private List<ProgressListener> progressListeners = new ArrayList<ProgressListener>();
    
    private boolean running = false;
    
    private Runnable updateProgressTime = new Runnable() {
        public void run() {
            if (running) {
                final long start = referenceTime;
                final long now = SystemClock.uptimeMillis();
                long millis =  now - start;
                
                referenceTime = now;
                
                accumulatedTime = accumulatedTime + millis;

                Log.d("time", "------------------------------------------");
                
                Log.d("time", "Accumulated time MS : " + accumulatedTime);
                
                int accumulatedSeconds = (int) (accumulatedTime / 1000);
    
                Log.d("time", "Accumulated S : " + accumulatedSeconds);
                Log.d("time", "Duration S : " + durationInSeconds);
                
                int remainingS = (durationInSeconds - accumulatedSeconds);
                
                Log.d("time", "Duration ? " + durationInSeconds);
                Log.d("time", "Remaining S : " + remainingS);
                
                int proportion = (((remainingS) * 100) / durationInSeconds);
                
                Log.d("time", "Proportion elapsed ? " + proportion);
                
                int progress = 100 - proportion;
                
                for (ProgressListener listener : progressListeners) {
                    listener.onTick(progress, accumulatedTime);
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
    
    public void setDurationInSeconds(int durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }
    
    public void start() {
        referenceTime = SystemClock.uptimeMillis();
        accumulatedTime = 0;
        progressHandler.removeCallbacks(updateProgressTime);
        progressHandler.postDelayed(updateProgressTime, 100);
        running = true;
    }
    
    public void reset() {
        referenceTime = 0;
        accumulatedTime = 0;
        progressHandler.removeCallbacks(updateProgressTime);
        running = false;
    }
    
    // TODO implement pause properly
    
    public void resume() {
        referenceTime = SystemClock.uptimeMillis();
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
