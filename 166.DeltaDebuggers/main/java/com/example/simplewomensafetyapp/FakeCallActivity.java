package com.example.simplewomensafetyapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class FakeCallActivity extends AppCompatActivity {

    private MediaPlayer player;
    private int secondsElapsed = 0;
    private Handler timerHandler = new Handler();
    private Runnable timerRunnable;
    private TextView callTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake_call);

        String[] callers = {"Mummy", "Papa", "Appa", "Samruuu", "Arya", "Dive Teacher"};
        String caller = callers[new Random().nextInt(callers.length)];

        TextView callerName = findViewById(R.id.txtCallerName);
        TextView callerNumber = findViewById(R.id.txtCallerNumber);
        callTimer = findViewById(R.id.txtTimer);
        ImageView profileImage = findViewById(R.id.imgProfile);
        ImageView iconImage = findViewById(R.id.imgIcons);
        Button btnAnswer = findViewById(R.id.btnAnswer);
        Button btnDecline = findViewById(R.id.btnDecline);

        callerName.setText(caller);
        callerNumber.setText("+91 9876543210 | India");
        profileImage.setImageResource(R.drawable.profile);
        iconImage.setImageResource(R.drawable.dial3);
        iconImage.setVisibility(ImageView.INVISIBLE);

        callTimer.setText("00:00");
        callTimer.setVisibility(TextView.GONE); // 👈 Hides timer initially

        playRingtone();

        btnAnswer.setOnClickListener(v -> {
            stopRingtone();
            iconImage.setVisibility(ImageView.VISIBLE);
            callTimer.setVisibility(TextView.VISIBLE); // 👈 Show timer after answer
            centerDeclineButton(btnDecline, btnAnswer);
            startCallTimer();
        });

        btnDecline.setOnClickListener(v -> {
            stopRingtone();
            Intent intent = new Intent(FakeCallActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
            // optional but recommended to finish FakeCallActivity
        });

    }

    private void playRingtone() {
        player = MediaPlayer.create(this, R.raw.ringtone);
        player.setLooping(true);
        player.start();
    }

    private void stopRingtone() {
        if (player != null && player.isPlaying()) {
            player.stop();
            player.release();
        }
    }

    private void startCallTimer() {
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                secondsElapsed++;
                int minutes = secondsElapsed / 60;
                int seconds = secondsElapsed % 60;
                callTimer.setText(String.format("%02d:%02d", minutes, seconds));
                timerHandler.postDelayed(this, 1000);
            }
        };
        timerHandler.post(timerRunnable);
    }

    private void centerDeclineButton(Button btnDecline, Button btnAnswer) {
        btnAnswer.setVisibility(Button.GONE);

        LinearLayout parentLayout = (LinearLayout) btnDecline.getParent();

        // Clear existing views
        parentLayout.removeAllViews();

        // Center content
        parentLayout.setGravity(Gravity.CENTER);

        // Layout params
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 16, 0, 0);
        btnDecline.setLayoutParams(params);

        // Add only decline button back
        parentLayout.addView(btnDecline);
    }

    @Override
    protected void onDestroy() {
        stopRingtone();
        timerHandler.removeCallbacks(timerRunnable);
        super.onDestroy();
    }
}



