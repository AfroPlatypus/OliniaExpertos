package com.afroplatypus.olinia;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatSelectionActivity extends AppCompatActivity {

    public static final String CONVERSATION_CHILD = "conversations";

    //TODO Change to user id
    String user_id = "Moises";
    Intent chatIntent;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseListAdapter<Conversation> mFirebaseAdapter;
    private ListView mChatRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_selection);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mChatRecyclerView = (ListView) findViewById(R.id.chats);
        chatIntent = new Intent(this, ChatActivity.class);
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        getMessages();
        mChatRecyclerView.setAdapter(mFirebaseAdapter);
    }


    private void getMessages() {
        mFirebaseAdapter = new FirebaseListAdapter<Conversation>(this, Conversation.class, R.layout.chat, mFirebaseDatabaseReference.child(CONVERSATION_CHILD)) {
            @Override
            protected void populateView(View v, final Conversation conversation, int position) {
                conversation.setKey(getRef(position).getKey());
                if (conversation.getUser1().equals(user_id)) {
                    ((TextView) v.findViewById(R.id.user)).setText(conversation.getUser2());
                } else {
                    ((TextView) v.findViewById(R.id.user)).setText(conversation.getUser1());
                }
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chatIntent.putExtra("conversation_key", conversation.getKey());
                        startActivity(chatIntent);
                    }
                });
            }
        };
    }

}
