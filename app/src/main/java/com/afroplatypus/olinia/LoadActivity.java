package com.afroplatypus.olinia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

public class LoadActivity extends Activity {
    public static final String MESSAGES_CHILD = "messages";
    private final int seconds = 3;
    private TextView phrase;
    private ProgressBar progressBar;
    private Intent chatIntent;
    private DatabaseReference mFirebaseDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        phrase = (TextView) findViewById(R.id.phrase);


        String[] phrases = this.getResources().getStringArray(R.array.phrases);
        String randomStr = phrases[new Random().nextInt(phrases.length)];
        phrase.setText(randomStr);


        chatIntent = new Intent(this, ChatActivity.class);

        new Thread(new Runnable() {
            @Override
            public void run() {
                mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
                chatIntent.putExtra("messages", getMessages());
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                try {
                    for (int i = 0; i < 100; ++i) {
                        progressBar.setProgress(progressBar.getProgress() + 1);
                        Thread.sleep(seconds * 10);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    onContinue();
                }
            }
        }).start();
    }

    @Override
    public void onBackPressed() {

    }

    public void onContinue() {
        startActivity(chatIntent);
        LoadActivity.this.finish();
    }

    private Vector<Message> getMessages() {
        final Vector<Message> messages = new Vector<>();
        mFirebaseDatabaseReference.child(MESSAGES_CHILD).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<Message>> t = new GenericTypeIndicator<ArrayList<Message>>() {
                };
                ArrayList<Message> messagesArray = dataSnapshot.getValue(t);
                for (int i = 0; i < messagesArray.size(); ++i) {
                    messages.add(messagesArray.get(i));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return messages;
    }
}
