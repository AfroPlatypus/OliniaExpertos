package com.afroplatypus.olinia;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends AppCompatActivity {

    public static final String MESSAGES_CHILD = "messages";

    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseListAdapter<Message> mFirebaseAdapter;
    private ListView mMessageRecyclerView;
    private Button sendButton;
    private TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mMessageRecyclerView = (ListView) findViewById(R.id.list);
        sendButton = (Button) findViewById(R.id.send);
        txt = (TextView) findViewById(R.id.txt);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Change Receiver and Sender
                Message message = new Message(
                        txt.getText().toString(),
                        "Receiver",
                        "Sender"
                );
                mFirebaseDatabaseReference.child(MESSAGES_CHILD).push().setValue(message);
                txt.setText("");
            }
        });

        txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txt.getText().toString().trim().length() > 0) {
                    sendButton.setEnabled(true);
                } else {
                    sendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        getMessages();
        mMessageRecyclerView.setAdapter(mFirebaseAdapter);
    }

    private void getMessages() {
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
    }
}
