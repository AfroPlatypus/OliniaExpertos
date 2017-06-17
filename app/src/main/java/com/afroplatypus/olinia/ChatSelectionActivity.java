package com.afroplatypus.olinia;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ChatSelectionActivity extends AppCompatActivity {

    public static final String CONVERSATION_CHILD = "conversations";
    String user_id;
    Intent chatIntent;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Intent intentLogIn, intentExpSel;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseListAdapter<Conversation> mFirebaseAdapter;
    private ListView mChatRecyclerView;

    private boolean click = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_selection);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid().trim();
        intentLogIn = new Intent(this, LogInActivity.class);
        intentExpSel = new Intent(this, SelectExpertActivity.class);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user == null) {
                    startActivity(intentLogIn);
                    ChatSelectionActivity.this.finish();
                }
            }
        };

        presenceSystem();

        mChatRecyclerView = (ListView) findViewById(R.id.chats);
        chatIntent = new Intent(this, ChatActivity.class);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mFirebaseDatabaseReference = database.getReference();
        getMessages();
        mChatRecyclerView.setAdapter(mFirebaseAdapter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentExpSel);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Log.d("USER ID", user_id);
    }

    private void presenceSystem() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference connectedReference = database.getReference("users/" + user_id).child("connected");

        final DatabaseReference connectedRef = database.getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                connectedReference.setValue(Boolean.TRUE);
                connectedReference.onDisconnect().setValue(Boolean.FALSE);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled at .info/connected");
            }
        });
    }


    private void getMessages() {
        DatabaseReference conversations = mFirebaseDatabaseReference.child(CONVERSATION_CHILD);
        Query conversations_query = conversations.orderByChild("user").equalTo(user_id);
        mFirebaseAdapter = new FirebaseListAdapter<Conversation>(this, Conversation.class, R.layout.chat, conversations_query) {
            @Override
            protected void populateView(final View v, final Conversation conversation, int position) {
                conversation.setKey(getRef(position).getKey());
                mFirebaseDatabaseReference.child("experts/" + conversation.getExpert()+"/name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ((TextView) v.findViewById(R.id.user)).setText(dataSnapshot.getValue(String.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                //((TextView) v.findViewById(R.id.user)).setText(conversation.getExpert());
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
