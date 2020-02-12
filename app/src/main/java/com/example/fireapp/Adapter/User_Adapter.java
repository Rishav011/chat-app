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
        final Users user = Username.get(position);
        TextView name = holder.mView.findViewById(R.id.username);
           // holder.mView.
        name.setText(Username.get(position).getUsername());
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("User");
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (context,chatActivity.class);
                intent.putExtra("userid",user.getId());
                context.startActivity(intent);
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