package com.ec.managementsystem.moduleView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.ec.managementsystem.R;
import com.ec.managementsystem.moduleView.login.FinalLogginActivity;

public class Splash extends BaseActivity {

    private ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        this.initializeComponents();
    }

    private void initializeComponents() {
        this.goToActivity();
    }

    private void goToActivity() {

        progressbar = findViewById(R.id.progressbar_splash);
        new Thread() {
            public void run() {
                try {
                    progressbar.setVisibility(View.VISIBLE);
                    sleep(2000);
                    //progressbar.setVisibility(View.GONE);
                    startActivity(Splash.this, FinalLogginActivity.class, true, null);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
}
