package com.example.fireapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fireapp.R;
import com.example.fireapp.feedFragment;
import com.example.fireapp.model.Image;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class feedAdapter extends RecyclerView.Adapter<feedAdapter.ViewHolder> {
    private ArrayList<Image> mDataset;
    private feedFragment mfeedFragment;
    private Context context;


    public feedAdapter(ArrayList<Image> myDataset, feedFragment feedFragment,Context context) {
        mDataset=myDataset;
        mfeedFragment=feedFragment;
        this.context=context;
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ImageView mImageView;
        public Button mLikeButton;
        public TextView timeStamp;


        public ViewHolder(View v){
            super(v);
            mTextView = v.findViewById(R.id.textView2);
            mImageView = v.findViewById(R.id.imageView);
            mLikeButton=v.findViewById(R.id.likeButton);
            timeStamp=v.findViewById(R.id.timeStamp);
        }
    }

    @NonNull
    @Override
    public feedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_image,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull feedAdapter.ViewHolder holder, int position) {
        final Image image = mDataset.get(position);
        SimpleDateFormat formatTime = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        long time = image.getTimestamp();
        String newTime = formatTime.format(time);
        if (image.user != null) {
            holder.mTextView.setText(image.user.username);
            holder.timeStamp.setText(newTime);


        }
        Picasso.get().load(image.downloadUrl).fit().into(holder.mImageView);

        holder.mLikeButton.setText("Like (" + image.likes + ")");
        if(image.hasLiked) {
            holder.mLikeButton.setBackgroundColor(ContextCompat.getColor(context,R.color.colorAccent));
        } else {
            holder.mLikeButton.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
        }
        holder.mLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mfeedFragment.setLiked(image);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
    public void addImage(Image image){
        mDataset.add(0,image);
        notifyDataSetChanged();
    }
}
