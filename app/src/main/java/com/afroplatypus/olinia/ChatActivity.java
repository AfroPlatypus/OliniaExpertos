package com.afroplatypus.olinia;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends AppCompatActivity {

    public static final String MESSAGES_CHILD = "messages";
    // Firebase instance variables
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseListAdapter<Message> mFirebaseAdapter;
    private ListView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        // Set default username is anonymous.

        // Initialize ProgressBar and RecyclerView.
        mMessageRecyclerView = (ListView) findViewById(R.id.list);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);

        // New child entries
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseAdapter = new FirebaseListAdapter<Message>(this, Message.class, R.layout.message, mFirebaseDatabaseReference.child(MESSAGES_CHILD)) {
            @Override
            protected void populateView(View v, Message message, int position) {
                ((TextView) v.findViewById(R.id.msg)).setText(message.getBody());
                if (message.getSender().equals("Moises")) {
                    v.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    v.findViewById(R.id.msg).setBackgroundResource(R.drawable.message_received_style);
                } else {
                    v.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                    v.findViewById(R.id.msg).setBackgroundResource(R.drawable.message_sent_style);
                }
            }
        };

        mMessageRecyclerView.setAdapter(mFirebaseAdapter);
    }
}
