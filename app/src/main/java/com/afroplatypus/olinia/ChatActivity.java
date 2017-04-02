package com.afroplatypus.olinia;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    private ListView mMessageRecyclerView;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        mMessageRecyclerView = (ListView) findViewById(R.id.list);
        messageAdapter = new MessageAdapter(this, R.layout.message, R.id.msg);
        showMessages((ArrayList<Message>) getIntent().getExtras().get("messages"));
        mMessageRecyclerView.setAdapter(messageAdapter);
    }

    private void showMessages(ArrayList<Message> messages) {
        for (int i = 0; i < messages.size(); ++i) {
            messageAdapter.add(messages.get(i));
        }
    }
}
