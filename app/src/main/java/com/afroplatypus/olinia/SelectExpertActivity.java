package com.afroplatypus.olinia;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SelectExpertActivity extends AppCompatActivity {
    public static final String EXPERTS_CHILD = "experts";
    ListView expertsList;
    private FirebaseListAdapter<Expert> adapter;
    private DatabaseReference mFirebaseDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_expert);
        expertsList = (ListView) findViewById(R.id.experts);
        expertsList.setAdapter(adapter);

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        getExperts();
        expertsList.setAdapter(adapter);
    }

    public void getExperts() {
        Query experts_query = mFirebaseDatabaseReference.child(EXPERTS_CHILD).orderByChild("connected").equalTo(true);
        adapter = new FirebaseListAdapter<Expert>(this, Expert.class, R.layout.expert, experts_query) {
            @Override
            protected void populateView(View v, Expert expert, int position) {
                ((TextView) v.findViewById(R.id.txt_name)).setText(expert.getName());
            }
        };
    }
}
