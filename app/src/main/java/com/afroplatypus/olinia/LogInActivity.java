package com.afroplatypus.olinia;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class LogInActivity extends AppCompatActivity {

    TextView anon;
    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        anon = (TextView) findViewById(R.id.txtAnon);
        intent = new Intent(this, LoadActivity.class);
        anon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
                LogInActivity.this.finish();
            }
        });
    }
}
