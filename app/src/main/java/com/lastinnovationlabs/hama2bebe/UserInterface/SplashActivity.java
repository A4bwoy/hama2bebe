package com.lastinnovationlabs.hama2bebe.UserInterface;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.Wave;
import com.lastinnovationlabs.hama2bebe.R;
import com.lastinnovationlabs.hama2bebe.util.NetworkCheck;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private final static int LOADING_DURATION = 2500;
    SpinKitView progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        progressBar = findViewById(R.id.cv_progressbar);
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        progressBar.setVisibility(View.VISIBLE);

        startProcess();


    }

    // Starting process
    private void startProcess() {
        progressBar.setVisibility(View.VISIBLE);

        // Checking internet connection
        if (!NetworkCheck.isConnect(this)) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
            builder.setTitle("No internet connection!");
            builder.setCancelable(false);

            builder.setPositiveButton("Retry", (dialog, id) -> retryOpenApplication());
            builder.setNegativeButton("Exit", (dialog, id) -> closeApp());
            builder.show();
        } else {
            // Checking server connection
            startActivityMainDelay();
        }
    }

    private void closeApp() {
        SplashActivity.super.onBackPressed();

    }

    // Delay for SplashScreen
    private void startActivityMainDelay() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, RegisterActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        };
        new Timer().schedule(task, LOADING_DURATION);
    }

    // Delay for next retry
    private void retryOpenApplication() {
        progressBar.setVisibility(View.VISIBLE);

        new Handler().postDelayed(() -> startProcess(), LOADING_DURATION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startProcess();
    }
}