package com.afroplatypus.olinia;


import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by moy on 3/20/17.
 */

public class MessageAdapter<T> extends ArrayAdapter<T> {


    public MessageAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view =  super.getView(position, convertView, parent);
        TextView textView = (TextView) view.findViewById(R.id.msg);
        if (textView.getText().charAt(0) == 'm') {
            view.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            textView.setBackgroundResource(R.drawable.message_received_style);
        } else {
            view.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            textView.setBackgroundResource(R.drawable.message_sent_style);
        }
        textView.setText(textView.getText().subSequence(1, textView.getText().length() - 1));

        return view;
    }
}

