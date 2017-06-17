package com.afroplatypus.olinia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SelectExpertActivity extends AppCompatActivity {
    public static final String EXPERTS_CHILD = "experts";
    ListView expertsList;
    String user_id;
    Intent chatIntent;
    private FirebaseListAdapter<Expert> adapter;
    private FirebaseAuth mAuth;
    private DatabaseReference mFirebaseDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_expert);
        expertsList = (ListView) findViewById(R.id.experts);
        expertsList.setAdapter(adapter);

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        chatIntent = new Intent(this, ChatActivity.class);
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid().trim();


        getExperts();
        expertsList.setAdapter(adapter);
    }

    public void getExperts() {
        Query experts_query = mFirebaseDatabaseReference.child(EXPERTS_CHILD).orderByChild("connected").equalTo(true);
        adapter = new FirebaseListAdapter<Expert>(this, Expert.class, R.layout.expert, experts_query) {
            @Override
            protected void populateView(View v, final Expert expert, int position) {
                expert.setId(getRef(position).getKey());
                ((TextView) v.findViewById(R.id.txt_name)).setText(expert.getName());
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        expertClicked(expert);
                    }
                });
            }
        };
    }

    public void expertClicked(final Expert expert) {
        Log.d("User ID", user_id);
        Log.d("Expert ID", expert.getId());
        mFirebaseDatabaseReference.child("conversations").orderByChild("expert").equalTo(expert.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean already = false;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("user").getValue().toString().equals(user_id)) {
                        // TODO Open current conversation
                        //chatIntent.putExtra("conversation_key", ds.getRef().getKey());
                        //startActivity(chatIntent);
                        //SelectExpertActivity.this.finish();
                        already = true;
                        break;
                    }
                }
                if (already) {
                    // TODO Toast?
                } else {
                    Conversation conversation = new Conversation(user_id, expert.getId());
                    String conversation_key = mFirebaseDatabaseReference.child("conversations").push().getKey();
                    mFirebaseDatabaseReference.child("conversations").child(conversation_key).setValue(conversation);
                    chatIntent.putExtra("conversation_key", conversation_key);
                    startActivity(chatIntent);
                    SelectExpertActivity.this.finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
