package com.example.fireapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fireapp.R;

import com.example.fireapp.model.message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.List;

public class Message_Adapter  extends RecyclerView.Adapter<Message_Adapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT=1;
    public static final int MSG_TYPE_RIGHT=0;
    private Context context;
    private List<message> messages;
    FirebaseUser fUser;

    public Message_Adapter(Context context, List<message> messages) {

//        this.Username = Username;
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public Message_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.sent_message, parent, false);
            return new Message_Adapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.recieved_message, parent, false);
            return new Message_Adapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull Message_Adapter.ViewHolder holder, final int position) {
        message chat =messages.get(position);
        holder.show_message.setText(chat.getMessage());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView show_message;

        View mView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message=itemView.findViewById(R.id.show_message);
            mView = itemView;
        }

    }

    @Override
    public int getItemViewType(int position) {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (messages.get(position).getSender().equals(fUser.getUid())){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
