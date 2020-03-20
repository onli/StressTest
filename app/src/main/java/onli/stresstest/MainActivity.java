package onli.stresstest;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    // For the benchmark, store when it started
    private long benchStart = 0;
    // For the stress test, store how many times the algorithm passed
    private int runs = 0;
    // For the stress test, store how long each run took
    private Vector<Long> durations = new Vector<Long>();
    // Set this to true when you want to end the stress test after the current run
    private boolean stopping = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter();
        filter.addAction("stresstest-done");
        filter.addAction("stress-run");
        filter.addAction("stress-end");

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);

        final Button button = findViewById(R.id.button_bench);
        final Button buttonStress = findViewById(R.id.button_stress);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                button.setText("Running...");
                button.setEnabled(false);
                buttonStress.setEnabled(false);
                benchStart = Calendar.getInstance().getTimeInMillis();


                new Thread(new Runnable() {
                    public void run() {
                        // a potentially time consuming task
                        Fannkuch f = new Fannkuch();
                        f.start();
                        Intent intent = new Intent("stresstest-done");
                        // You can also include some extra data.
                        intent.putExtra("end", Calendar.getInstance().getTimeInMillis());
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                    }
                }).start();

            }
        });

        buttonStress.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (buttonStress.getText().equals("Stop!")) {
                    buttonStress.setText("Stopping...");
                    buttonStress.setEnabled(false);
                    stopping = true;
                } else {
                    durations = new Vector<Long>();
                    runs = 0;
                    buttonStress.setText("Stop!");
                    button.setEnabled(false);
                    startLoad();
                }
            }
        });

    }

    private void startLoad() {
        benchStart = Calendar.getInstance().getTimeInMillis();
        new Thread(new Runnable() {
            public void run() {
                // a potentially time consuming task
                Fannkuch f = new Fannkuch();
                f.start();
                Intent intent = new Intent("stress-run");
                // You can also include some extra data.
                intent.putExtra("end", Calendar.getInstance().getTimeInMillis());
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            }
        }).start();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            final long duration = intent.getLongExtra("end", 0) - benchStart;
            if (intent.getAction().equals("stresstest-done")) {
                Button button = findViewById(R.id.button_bench);
                Button buttonStress = findViewById(R.id.button_stress);
                button.setEnabled(true);
                buttonStress.setEnabled(true);
                button.setText("Benchmark");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!isFinishing()){
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("Results are in")
                                    .setMessage("This took " + (duration / 1000) + " seconds")
                                    .setCancelable(false)
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Whatever...
                                        }
                                    }).show();
                        }
                    }
                });
            }
            if (intent.getAction().equals("stress-run")) {
                runs++;
                durations.add(duration / 1000);
                if (stopping) {
                    Button buttonStress = findViewById(R.id.button_stress);
                    buttonStress.setText("Stresstest");
                    buttonStress.setEnabled(true);
                    stopping = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!isFinishing()){
                                String addMsg = "";
                                for(Long duration: durations) {
                                    addMsg += duration + "s\n";
                                }
                                new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("Runs")
                                        .setMessage("This were " + runs + " runs \n" + addMsg)
                                        .setCancelable(false)
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Whatever...
                                            }
                                        }).show();
                            }
                        }
                    });
                } else {
                    startLoad();
                }
            }
        }
    };


    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
}
