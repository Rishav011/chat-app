package com.example.fireapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fireapp.Adapter.commentAdapter;
import com.example.fireapp.model.Comment;
import com.example.fireapp.model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    EditText commentEditText;
    ImageView postComment;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
   DatabaseReference reference;
   ArrayList<Comment> comments = new ArrayList<>();
   commentAdapter commentAdapter;
   Intent intent;
   String postid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        commentEditText = findViewById(R.id.commentEditText);
        postComment = findViewById(R.id.postComment);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        intent = getIntent();
        postid = intent.getStringExtra("postid");
        Log.i("postid",postid);
        commentAdapter = new commentAdapter(comments,CommentsActivity.this);
        recyclerView.setAdapter(commentAdapter);
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query commentsQuery = databaseReference.child("Comments").orderByChild("postid").equalTo(postid);
        commentsQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final Comment comment = dataSnapshot.getValue(Comment.class);
                databaseReference.child("User/" + comment.id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Users user = dataSnapshot.getValue(Users.class);
                        comment.user = user;
                        commentAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                commentAdapter.addComment(comment);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        postComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String comment = commentEditText.getText().toString();
               if(!comment.trim().isEmpty())
                PostComment(mUser.getUid(),comment,postid);
               else
                   Toast.makeText(CommentsActivity.this, "Please enter comment!", Toast.LENGTH_SHORT).show();
               commentEditText.setText("");
            }
        });

    }

    private void PostComment(String id,String comment,String postid) {
        reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap= new HashMap<>();
        long timestamp = Calendar.getInstance().getTimeInMillis();
        hashMap.put("id",id);
        hashMap.put("comment",comment);
        hashMap.put("timestamp",timestamp);
        hashMap.put("postid",postid);
        reference.child("Comments").push().setValue(hashMap);
    }
}
