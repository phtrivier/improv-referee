package org.bullecarree.improv.referee;

import org.bullecarree.improv.model.Improv;
import org.bullecarree.improv.model.ImprovRenderer;
import org.bullecarree.improv.model.ImprovType;
import org.bullecarree.improv.reader.ImprovFileReader;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ImprovRefereeActivity extends Activity {
    
    private ProgressBar barTime;

    private ProgressTimer caucusTimer;
    
    private ProgressTimer improvTimer;
    
    private Improv currentImprov = new Improv();
    
    private ImprovFileReader improvReader = null;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        /* Improv setting (to be replaced by loading stuff, etc..) */
        /*
        currentImprov.setType(ImprovType.COMPARED);
        currentImprov.setTitle("Tant va la cruche à l'eau ...");
        currentImprov.setPlayerCount(2);
        currentImprov.setCategory("Roman-photo");
        currentImprov.setDuration(140); // 2m 20s
        */
        improvReader = new ImprovFileReader(getBaseContext());
        currentImprov = improvReader.readImprov();
        
        loadImprov(currentImprov);

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

        improvTimer = new ProgressTimer(currentImprov.getDuration());
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
        ImprovRenderer renderer = new ImprovRenderer(currentImprov);
        TextView c = (TextView) findViewById(R.id.improvCategory);
        c.setText("Catégorie : " + renderer.getCategory());
        
        c = (TextView) findViewById(R.id.improvPlayerCount);
        c.setText("Joueurs : " + renderer.getPlayerCount());
        
        c = (TextView) findViewById(R.id.improvType);
        c.setText("Type :" + renderer.getType());
        
        c = (TextView) findViewById(R.id.improvTitle);
        c.setText("Titre :" + renderer.getTitle());
        
        c = (TextView) findViewById(R.id.improvDuration);
        c.setText("Durée :" + renderer.getDuration());
    }
}