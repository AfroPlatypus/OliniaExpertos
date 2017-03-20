package com.afroplatypus.olinia;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

public class LoadActivity extends Activity {
    private TextView phrase;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        phrase = (TextView) findViewById(R.id.phrase);


        progressBar.setProgress(67);


        String[] phrases = this.getResources().getStringArray(R.array.phrases);
        String randomStr = phrases[new Random().nextInt(phrases.length)];
        phrase.setText(randomStr);
    }
}
