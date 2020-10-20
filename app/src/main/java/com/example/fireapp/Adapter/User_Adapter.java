package com.example.fireapp.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fireapp.R;
import com.example.fireapp.chatActivity;
import com.example.fireapp.model.Users;
import com.example.fireapp.model.message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class User_Adapter extends RecyclerView.Adapter<User_Adapter.ViewHolder> {

    private Context context;
    private List<Users> Username;
    String theLastMessage;

    public User_Adapter(Context context, List<Users> Username, boolean b) {

//        this.Username = Username;
        this.Username = Username;
        this.context = context;
    }

    @NonNull
    @Override
    public User_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull User_Adapter.ViewHolder holder, final int position) {
        final Users user = Username.get(position);
        TextView name = holder.mView.findViewById(R.id.username);
        // holder.mView.
        name.setText(user.getUsername());
        if (user.getImageUrl() == null) {
            holder.profileImage.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(context).load(user.getImageUrl()).into(holder.profileImage);
        }
        lastMessage(user.getId(), holder.last_msg);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, chatActivity.class);
                intent.putExtra("userid", user.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Username.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public ImageView profileImage;
        private TextView last_msg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            profileImage = mView.findViewById(R.id.profileImage);
            last_msg = mView.findViewById(R.id.last_msg);
        }

    }

    //check for last message
    private void lastMessage(final String userid, final TextView last_msg) {
        theLastMessage = "XUy11PDbDcpwEE6H0Prv";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    message chat = snapshot.getValue(message.class);
                    try {
                        if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                                chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())) {
                            theLastMessage = chat.getMessage();
                        }
                    } catch (Exception e) {
                    }
                }
                switch (theLastMessage) {
                    case "XUy11PDbDcpwEE6H0Prv":
                        last_msg.setText("");
                        break;

                    default:
                        last_msg.setText(theLastMessage);
                }
                theLastMessage = "XUy11PDbDcpwEE6H0Prv";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}