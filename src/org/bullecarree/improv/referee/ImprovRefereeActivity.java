package org.bullecarree.improv.referee;

import org.bullecarree.improv.model.Improv;
import org.bullecarree.improv.model.ImprovRenderer;
import org.bullecarree.improv.model.ImprovType;
import org.bullecarree.improv.reader.ImprovFileReader;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ImprovRefereeActivity extends Activity {

    protected static final long CAUCUS_DURATION_MS = 20 * 1000;

    private ProgressBar barTimeProgress;

    private ProgressTimer caucusTimer;

    private ProgressTimer improvTimer;

    private Improv currentImprov = new Improv();

    private ImprovFileReader improvReader = null;

    private ImprovRenderer renderer;

    private TextView barTimeMessage;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        renderer = new ImprovRenderer(getString(R.string.typeCompared),
                getString(R.string.typeMixt), getString(R.string.unlimited), getString(R.string.categoryFree));

        improvReader = new ImprovFileReader(getBaseContext());
        currentImprov = improvReader.readImprov();

        loadImprov(currentImprov);

        barTimeProgress = (ProgressBar) findViewById(R.id.barTime);
        barTimeMessage = (TextView) findViewById(R.id.barTimeMessage);

        ProgressListener updateProgressBar = new ProgressListener() {
            public void onTick(int progress, long durationMillis) {
                barTimeProgress.setProgress(progress);

                long remainingMs = 0;
                if (caucusTimer.isRunning()) {
                    remainingMs = CAUCUS_DURATION_MS - durationMillis;
                } else {
                    remainingMs = (currentImprov.getDuration() * 1000)
                            - durationMillis;
                }

                Log.d("prout",
                        "Duration millis : " + String.valueOf(durationMillis));
                Log.d("rpout", "Remaining ms : " + remainingMs);
                int remainingS = (int) remainingMs / 1000;
                Log.d("rpout", "Remaining s : " + remainingS);

                barTimeMessage.setText(renderer.displayTime(remainingS));
            };

        };

        caucusTimer = new ProgressTimer((int) CAUCUS_DURATION_MS / 1000);
        caucusTimer.addProgressListener(updateProgressBar);

        improvTimer = new ProgressTimer(currentImprov.getDuration());
        improvTimer.addProgressListener(updateProgressBar);

        // Set button to start the caucus ; each button
        // should stop the other timer
        final Button btnCaucus = (Button) findViewById(R.id.btnCaucus);
        final Button btnImprov = (Button) findViewById(R.id.btnImprov);
        final Button btnReset = (Button) findViewById(R.id.btnReset);

        btnCaucus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                barTimeProgress.setProgress(0);
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
                if (improvTimer.isRunning()) {
                    btnCaucus.setEnabled(false);
                    improvTimer.stop();
                    btnImprov.setText(getString(R.string.btnImprov));
                } else {
                    btnCaucus.setEnabled(false);
                    btnImprov.setText(getString(R.string.btnPause));
                    barTimeProgress.setProgress(0);
                    caucusTimer.stop();
                    improvTimer.start();
                }
            }
        });

        btnReset.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                barTimeProgress.setProgress(0);
                barTimeMessage.setText(renderer.displayTime(currentImprov
                        .getDuration()));
                btnImprov.setText(getString(R.string.btnImprov));
                improvTimer.reset();
            }
        });

        // TODO(pht) add a "Kill" button to stop all timers once and for all.
        // TODO(pht) add a "Pause" button to pause whatever timer is available

    }

    private void loadImprov(Improv improv) {
        renderer.setImprov(improv);
        TextView c = (TextView) findViewById(R.id.improvCategory);
        c.setText(getString(R.string.titleCategory) + " : "
                + renderer.getCategory());

        c = (TextView) findViewById(R.id.improvPlayerCount);
        c.setText(getString(R.string.titlePlayerCount) + " : "
                + renderer.getPlayerCount());

        c = (TextView) findViewById(R.id.improvType);
        c.setText(getString(R.string.titleType) + " : " + renderer.getType());

        c = (TextView) findViewById(R.id.improvTitle);
        c.setText(getString(R.string.titleType) + " : " + renderer.getTitle());

        c = (TextView) findViewById(R.id.improvDuration);
        c.setText(getString(R.string.titleDuration) + " : "
                + renderer.getDuration());
    }
}