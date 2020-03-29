package com.example.fireapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fireapp.Adapter.feedAdapter;
import com.example.fireapp.model.Image;
import com.example.fireapp.model.Like;
import com.example.fireapp.model.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

public class feedFragment extends Fragment {
    Button uploadButton;
    private int PICK_IMAGE_REQUEST = 7;
    private Uri uri;
    Bitmap bitmap;
    StorageReference storageReference;
    String downloadUrl;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    feedAdapter mAdapter;
    ArrayList<Image> images = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        //upload image
        uploadButton = view.findViewById(R.id.uploadButton);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
        //save image to database
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();

        //setup recycler view
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new feedAdapter(images, this, getContext());
        recyclerView.setAdapter(mAdapter);

        //get the latest images
        Query imagesQuery = reference.child("Feed_Images").orderByKey().limitToFirst(100);
        imagesQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final Image image = dataSnapshot.getValue(Image.class);

                //get the image user
                reference.child("User/" + image.userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Users user = dataSnapshot.getValue(Users.class);
                        image.user = user;
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Query likesQuery = reference.child("likes").orderByChild("imageId").equalTo(image.key);
                likesQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Like like = dataSnapshot.getValue(Like.class);
                        image.addLike();
                        if (like.userId.equals(mUser.getUid())) {
                            image.hasLiked = true;
                            image.userLike = dataSnapshot.getKey();
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        Like like = dataSnapshot.getValue(Like.class);
                        image.removeLike();
                        if (like.userId.equals(mUser.getUid())) {
                            image.hasLiked = false;
                            image.userLike = null;
                        }
                        mAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                mAdapter.addImage(image);
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
        return view;
    }

    //get extension of file
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getActivity().getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    //select image
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), uri);
            } catch (Exception e) {
            }
            if (uri != null) {
                //this is for image file name
                storageReference = FirebaseStorage.getInstance().getReference().child("Feed_Image");
                final StorageReference filepath = storageReference.child(System.currentTimeMillis() +
                        "." + GetFileExtension(uri));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                byte[] dataa = baos.toByteArray();

                UploadTask uploadTask = filepath.putBytes(dataa);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                downloadUrl = uri.toString();
                                long time = Calendar.getInstance().getTimeInMillis();
                                String key = reference.child("Feed_Images").push().getKey();
                                Image image = new Image(key, mUser.getUid(), downloadUrl, time);
                                reference.child("Feed_Images").child(key).setValue(image);
                                Toast.makeText(getContext(), "Upload successful!", Toast.LENGTH_SHORT).show();
                                //Toast.makeText(accountActivity.this, "success!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Upload failed!", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Toast.makeText(getContext(), "Error! Please try again!", Toast.LENGTH_SHORT).show();

            }
        }
    }

    //like
    public void setLiked(Image image) {
        if (!image.hasLiked) {
            // add new Like
            image.hasLiked = true;
            Like like = new Like(image.key, mUser.getUid());
            String key = reference.child("likes").push().getKey();
            reference.child("likes").child(key).setValue(like);
            image.userLike = key;
        } else {
            // remove Like
            image.hasLiked = false;
            if (image.userLike != null) {
                reference.child("likes").child(image.userLike).removeValue();
            }
        }
    }

}
