package com.example.fireapp.Adapter;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fireapp.R;
import com.example.fireapp.chatActivity;
import com.firebase.ui.auth.data.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class User_Adapter extends RecyclerView.Adapter<User_Adapter.ViewHolder> {

    private Context context;
    private List<Users> Username;

    public User_Adapter(Context context, List<Users> Username) {

//        this.Username = Username;
        this.Username = Username;
        this.context = context;
    }

    @NonNull
    @Override
    public User_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull User_Adapter.ViewHolder holder, final int position) {
        TextView name = holder.mView.findViewById(R.id.username);
           // holder.mView.
        name.setText(Username.get(position).getUsername());
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("User");
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.orderByChild("username").equalTo(Username.get(position).getUsername())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String key = "";
                                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                                    key = childSnapshot.getKey();
                                }

                                Intent intent = new Intent(context, chatActivity.class);

                                intent.putExtra("userid", key);
                                context.startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        });
//        name.setText("sdfgh");

    }

    @Override
    public int getItemCount() {
        return Username.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

    }
}