package org.bullecarree.improv.referee;

import org.bullecarree.improv.model.Improv;
import org.bullecarree.improv.model.ImprovType;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ImprovRefereeActivity extends Activity {
    
    private ProgressBar barTime;

    private ProgressTimer caucusTimer;
    
    private ProgressTimer improvTimer;
    

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

        barTime = (ProgressBar) findViewById(R.id.barTime);
        
        ProgressListener updateProgressBar = new ProgressListener() {
            public void onTick(int progress, long durationMillis) {
                barTime.setProgress(progress);
                // TODO(pht) update a LABEL (the label of the progress bar ?)
                // to indicate how much time has passed / is remaining...
            };
            
        }; 
        
        caucusTimer = new ProgressTimer(25);
        caucusTimer.addProgressListener(updateProgressBar);

        improvTimer = new ProgressTimer(improv0.getDuration());
        improvTimer.addProgressListener(updateProgressBar);

        // Set button to start the caucus ; each button
        // should stop the other timer
        final Button btnCaucus = (Button) findViewById(R.id.btnCaucus);
        final Button btnImprov = (Button) findViewById(R.id.btnImprov);

        btnCaucus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                barTime.setProgress(0);
                // It should not be possible to interupt a caucus, to restart
                // it, unless I use the Kill button
                if (!caucusTimer.isRunning() && !improvTimer.isRunning()) {
                    btnCaucus.setEnabled(false);
                    improvTimer.stop();
                    caucusTimer.start();
                }
            }
        });

        btnImprov.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                barTime.setProgress(0);
                // To try : I should not be able to restart an improv that is restarted
                // (unless I use the KILL button)
                if (!improvTimer.isRunning()) {
                    btnCaucus.setEnabled(false);
                    caucusTimer.stop();
                    improvTimer.start();
                }
            }
        });

        // TODO(pht) add a "Kill" button to stop all timers once and for all.
        // TODO(pht) add a "Pause" button to pause whatever timer is available
        
    }
     
    private void loadImprov(Improv improv) {
        // TODO(pht) write a "renderer" whose job will be to decide the content
        // of each string, and actually test it... 
        TextView c = (TextView) findViewById(R.id.improvCategory);
        c.setText(improv.getType().toString());
        // Etc ... 
    }
}