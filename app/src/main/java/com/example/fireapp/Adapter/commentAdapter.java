package com.example.fireapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fireapp.R;
import com.example.fireapp.model.Comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class commentAdapter extends RecyclerView.Adapter<commentAdapter.ViewHolder> {
    private ArrayList<Comment> mDataset;
    private Context context;

    public commentAdapter(ArrayList<Comment> myDataset,Context context){
        mDataset = myDataset;
        this.context = context;
    }
    public static  class ViewHolder extends RecyclerView.ViewHolder {
        public TextView usernameTextView;
        public TextView commentTextView;
        public TextView dateTextView;
        public TextView timeTextView;
        public ImageView profileImage;

        public ViewHolder(View v){
            super(v);
            usernameTextView = v.findViewById(R.id.usernameTextView);
            commentTextView = v.findViewById(R.id.commentsTextView);
            dateTextView = v.findViewById(R.id.dateTextView);
            timeTextView = v.findViewById(R.id.timeTextView);
            profileImage = v.findViewById(R.id.profileImage);

        }
    }

    @NonNull
    @Override
    public commentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = mDataset.get(position);
        long dateTime = comment.getTimestamp();
        SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm a");
        String newDate = formatDate.format(dateTime);
        String newTime = formatTime.format(dateTime);
        if(comment.user != null){
            holder.usernameTextView.setText(comment.user.getUsername());
            holder.commentTextView.setText(comment.getComment());
            holder.dateTextView.setText(newDate);
            holder.timeTextView.setText(newTime);
            try {
                Glide.with(context).load(comment.user.getImageUrl()).into(holder.profileImage);

            } catch (Exception e) {

            }
        }

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
    public void addComment(Comment comment) {
        mDataset.add(0, comment);
        notifyDataSetChanged();
    }


}
