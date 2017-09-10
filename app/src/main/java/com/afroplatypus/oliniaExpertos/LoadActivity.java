package com.afroplatypus.oliniaExpertos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

public class LoadActivity extends Activity {
    private final int seconds = 1;
    private TextView phrase;
    private ProgressBar progressBar;
    private Intent chatSelectionIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        phrase = (TextView) findViewById(R.id.phrase);


        String[] phrases = this.getResources().getStringArray(R.array.phrases);
        String randomStr = phrases[new Random().nextInt(phrases.length)];
        phrase.setText(randomStr);


        chatSelectionIntent = new Intent(this, ChatSelectionActivity.class);

        new Thread(new Runnable() {
            public void run() {
                try {
                    for (int i = 0; i < 100; ++i) {
                        progressBar.setProgress(progressBar.getProgress() + 1);
                        Thread.sleep(seconds * 10);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    onContinue();
                }
            }
        }).start();
    }

    @Override
    public void onBackPressed() {

    }

    public void onContinue() {
        startActivity(chatSelectionIntent);
        LoadActivity.this.finish();
    }
}
