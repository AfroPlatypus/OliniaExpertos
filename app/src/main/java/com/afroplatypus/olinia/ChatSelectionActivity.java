package com.afroplatypus.olinia;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChatSelectionActivity extends AppCompatActivity {

    public static final String CONVERSATION_CHILD = "conversations";

    //TODO Change to user id
    String user_id = "Moises";
    Intent chatIntent;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseListAdapter<Conversation> mFirebaseAdapter;
    private ListView mChatRecyclerView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Intent intentLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_selection);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        intentLogIn = new Intent(this, LogInActivity.class);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if(user == null){
                    startActivity(intentLogIn);
                    ChatSelectionActivity.this.finish();
                }
            }
        };

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

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

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
