package com.jayseeofficial.camelcuproller;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String BLUE_VALUE = "blue";
    @Bind(R.id.blue_die)
    TextView blueDie;

    private static final String GREEN_VALUE = "green";
    @Bind(R.id.green_die)
    TextView greenDie;

    private static final String ORANGE_VALUE = "orange";
    @Bind(R.id.orange_die)
    TextView orangeDie;

    private static final String WHITE_VALUE = "white";
    @Bind(R.id.white_die)
    TextView whiteDie;

    private static final String YELLOW_VALUE = "yellow";
    @Bind(R.id.yellow_die)
    TextView yellowDie;

    Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (savedInstanceState != null) {
            blueDie.setText(savedInstanceState.getString(BLUE_VALUE));
            greenDie.setText(savedInstanceState.getString(GREEN_VALUE));
            orangeDie.setText(savedInstanceState.getString(ORANGE_VALUE));
            whiteDie.setText(savedInstanceState.getString(WHITE_VALUE));
            yellowDie.setText(savedInstanceState.getString(YELLOW_VALUE));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BLUE_VALUE, blueDie.getText().toString());
        outState.putString(GREEN_VALUE, greenDie.getText().toString());
        outState.putString(ORANGE_VALUE, orangeDie.getText().toString());
        outState.putString(WHITE_VALUE, whiteDie.getText().toString());
        outState.putString(YELLOW_VALUE, yellowDie.getText().toString());
    }

    @OnClick(R.id.btn_roll_die)
    public void onButtonRollDieClicked() {
        if (!blueDie.getText().equals("") &&
                !greenDie.getText().equals("") &&
                !orangeDie.getText().equals("") &&
                !whiteDie.getText().equals("") &&
                !yellowDie.getText().equals("")) {
            Toast.makeText(this, "All dice have already been rolled", Toast.LENGTH_LONG).show();
            return;
        }
        int dieNum = random.nextInt(5);
        final TextView theDie;
        switch (dieNum) {
            case 0:
                theDie = blueDie;
                break;
            case 1:
                theDie = greenDie;
                break;
            case 2:
                theDie = orangeDie;
                break;
            case 3:
                theDie = whiteDie;
                break;
            case 4:
                theDie = yellowDie;
                break;
            default:
                theDie = null;
                break;
        }
        if (!theDie.getText().equals("")) {
            // We don't want to change the value if it's already been rolled, so we try again.
            onButtonRollDieClicked();
            return;
        } else {
            // This is an absolute mess...
            final Animation animation = AnimationUtils.loadAnimation(this, R.anim.hyperspace_jump);
            animation.setAnimationListener(new Animation.AnimationListener() {
                boolean randomizing = false;
                AsyncTask<Void, Void, Void> rnumTask = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        while (randomizing) {
                            final int rnum = random.nextInt(3);
                            runOnUiThread(new Runnable() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void run() {
                                    theDie.setText(Integer.toString(rnum + 1));
                                }
                            });
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        return null;
                    }
                };

                @Override
                public void onAnimationStart(Animation animation) {
                    randomizing = true;
                    rnumTask.execute();
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    randomizing = false;
                    try {
                        Thread.sleep(120);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    rnumTask.cancel(true);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            theDie.startAnimation(animation);
        }
    }

    @OnClick(R.id.btn_reset)
    public void onButtonResetClicked() {
        blueDie.setText("");
        greenDie.setText("");
        yellowDie.setText("");
        whiteDie.setText("");
        orangeDie.setText("");
    }
}
