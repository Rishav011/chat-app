package com.example.fireapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class DetailsActivity extends AppCompatActivity {
    EditText usernameEditText;
    Button imageButton;
    Button proceedButton;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    HashMap<String, Object> hashMap;
    private int PICK_IMAGE_REQUEST = 7;
    private Uri uri;
    StorageReference storageReference;
    String imgData;
    Bitmap bitmap;
    ProgressBar progressBar;
    FirebaseUser mUser;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        usernameEditText = findViewById(R.id.usernameEditText);
        imageButton = findViewById(R.id.imageButton);
        proceedButton = findViewById(R.id.proceedButton);
        progressBar = findViewById(R.id.progressBar);
        hashMap = new HashMap<>();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        userId = mUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference("User").child(userId);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                proceed();
            }
        });

    }

    private void proceed() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String userId = mUser.getUid();
        String username = usernameEditText.getText().toString();
        if (mAuth.getCurrentUser() != null) {
            progressBar.setVisibility(View.GONE);
            uploadImage();
            uploadData(username, userId);
            Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(DetailsActivity.this, Main2Activity.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (Exception e) {
            }

        }
    }

    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

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
            filepath.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imgData = uri.toString();
                            hashMap.put("ImageUrl", imgData);
                            reference.setValue(hashMap);
                            //Toast.makeText(accountActivity.this, "success!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        } else {
            Toast.makeText(this, "sorry!!", Toast.LENGTH_SHORT).show();
        }

    }

    private void uploadData(String username, String userId) {
        hashMap.put("username", username);
        hashMap.put("id", userId);
        reference.setValue(hashMap);
    }
}
