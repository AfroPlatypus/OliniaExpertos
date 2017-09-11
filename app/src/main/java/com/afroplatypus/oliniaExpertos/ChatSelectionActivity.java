package com.afroplatypus.oliniaExpertos;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid().trim();
        intentLogIn = new Intent(this, LogInActivity.class);
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
    }

    private void presenceSystem() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference connectedReference = database.getReference("experts/" + user_id).child("connected");

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
        Query conversations_query = conversations.orderByChild("expert").equalTo(user_id);
        mFirebaseAdapter = new FirebaseListAdapter<Conversation>(this, Conversation.class, R.layout.chat, conversations_query) {
            @Override
            protected void populateView(final View v, final Conversation conversation, int position) {
                conversation.setKey(getRef(position).getKey());
                mFirebaseDatabaseReference.child("users/" + conversation.getUser() + "/name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (((TextView) v.findViewById(R.id.user)).getText() == "") {
                            ((TextView) v.findViewById(R.id.user)).setText(dataSnapshot.getValue(String.class));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chatIntent.putExtra("conversation_key", conversation.getKey());
                        chatIntent.putExtra("user_key", conversation.getUser());
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
