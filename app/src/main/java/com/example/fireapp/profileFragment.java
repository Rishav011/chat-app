package com.example.fireapp;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.fireapp.model.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class profileFragment extends Fragment {
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    ImageView profileImage;
    TextView textView;
    EditText usernameEditText;
    Button updateButton;
    Button updateImageButton;
    View view;
    Uri uri;
    Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 7;
    StorageReference storageReference;
    String imgData;
    HashMap<String, Object> hashMap;
    UploadTask uploadTask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        hashMap = new HashMap<>();
        initialise();
        setUserDetails();
        updateImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                if (username.trim().isEmpty()) {
                    Toast.makeText(getContext(), "Username can't be empty!", Toast.LENGTH_SHORT).show();
                } else {
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String uname = usernameEditText.getText().toString();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("username", uname);
                            reference.updateChildren(map);
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    Toast.makeText(getActivity(), "Username updated successfully!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    //initialization
    private void initialise() {
        profileImage = view.findViewById(R.id.profileImage);
        textView = view.findViewById(R.id.textView);
        usernameEditText = view.findViewById(R.id.usernameEditText);
        updateButton = view.findViewById(R.id.updateButton);
        updateImageButton = view.findViewById(R.id.updateImageButton);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());
    }

    private void setUserDetails() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                try {
                    textView.setText("Welcome, " + user.getUsername());
                    Glide.with(getContext()).load(user.getImageUrl()).into(profileImage);
                } catch (Exception e) {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void uploadImage() {
        if (uri != null) {
            //this is for image file name
            storageReference = FirebaseStorage.getInstance().getReference().child("Image_File");
            final StorageReference filepath = storageReference.child(System.currentTimeMillis() +
                    "." + GetFileExtension(uri));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
            byte[] data = baos.toByteArray();
            uploadTask = filepath.putBytes(data);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imgData = uri.toString();
                            hashMap.put("ImageUrl", imgData);
                            reference.updateChildren(hashMap);
                            Toast.makeText(getActivity(), "Profile picture updated successfully", Toast.LENGTH_SHORT).show();
                            //Toast.makeText(accountActivity.this, "success!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        } else {
            Toast.makeText(getActivity(), "No image selected!", Toast.LENGTH_SHORT).show();
        }
    }

    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
            } catch (Exception e) {
            }

        }
        if (uploadTask != null && uploadTask.isInProgress()) {
            Toast.makeText(getActivity(), "Upload in progress", Toast.LENGTH_SHORT).show();
        } else {
            uploadImage();

        }
    }


}
