package com.afroplatypus.olinia;


import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class MessageAdapter extends ArrayAdapter<Message> {


    public MessageAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = getItem(position);
        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view.findViewById(R.id.msg);
        textView.setText(message.getBody());
        if (message.getSender().equals("Moises")) {
            view.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            view.findViewById(R.id.msg).setBackgroundResource(R.drawable.message_received_style);
        } else {
            view.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            view.findViewById(R.id.msg).setBackgroundResource(R.drawable.message_sent_style);
        }
        return view;
    }
}