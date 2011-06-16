package org.bullecarree.improv.referee;

import org.bullecarree.improv.model.Improv;
import org.bullecarree.improv.model.ImprovType;
import org.bullecarree.improv.referee.R;
import org.bullecarree.improv.referee.R.id;
import org.bullecarree.improv.referee.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ImprovRefereeActivity extends Activity {
    
    private ProgressBar barTime;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        /* Improv setting (to be replaced by loading stuff, etc..) */
        Improv improv0 = new Improv();
        
        improv0.setType(ImprovType.COMPARED);
        improv0.setTitle("Tant va la cruche à l'eau ...");
        improv0.setPlayerCount(2);
        improv0.setCategory("Roman-photo");
        improv0.setDuration(120);
        
        loadImprov(improv0);

        // Click handler (but start a background task to extends progress bar)
        
        barTime = (ProgressBar) findViewById(R.id.barTime);
        
        Button btnCaucus = (Button) findViewById(R.id.btnCaucus);
        btnCaucus.setOnClickListener(onCaucusClick);
    
    }

    private Handler caucusHandler = new Handler();
    
    private long caucusStartTime = 0;
    
    private final OnClickListener onCaucusClick = new OnClickListener() {
        
        @Override
        public void onClick(View v) {
            barTime.setProgress(0);
            caucusStartTime = SystemClock.uptimeMillis();
            caucusHandler.removeCallbacks(updateCaucusTime);
            caucusHandler.postDelayed(updateCaucusTime, 100);
        }
    };
    
    private Runnable updateCaucusTime = new Runnable() {
        public void run() {
            final long start = caucusStartTime;
            final long now = SystemClock.uptimeMillis(); 
            long millis =  now - start;
            int seconds = (int) (millis / 1000);

            // FIXME(pht) : make the duration of caucuses editable
            int progress = 100 - (((25 - seconds) * 100) / 25);
            
            barTime.setProgress(progress);
            
            // Don't schedule anything if progress is finished
            if (progress < 100) {
                caucusHandler.postAtTime(this, now + 1000);
            }
        };
    };
    
    
    private void loadImprov(Improv improv) {
        // TODO(pht) write a "renderer" whose job will be to decide the content
        // of each string, and actually test it... 
        TextView c = (TextView) findViewById(R.id.improvCategory);
        c.setText(improv.getType().toString());
        // Etc ... 
    }
}