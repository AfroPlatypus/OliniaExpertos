package com.afroplatypus.olinia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SelectExpertActivity extends AppCompatActivity {
    ListView expertsList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_expert);
        expertsList = (ListView) findViewById(R.id.experts);
        adapter = new ArrayAdapter<String>(this, R.layout.expert);
        expertsList.setAdapter(adapter);

        //expertsList.addView(getLayoutInflater().inflate(R.layout.expert, null));
    }
}
