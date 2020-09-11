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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class accountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText cnfPasswordEditText;
    private Button signUpButton;
    private EditText usernameEditText;
    private Button imgBtn;
    private ProgressBar progressBar;
    private int PICK_IMAGE_REQUEST = 7;
    private Uri uri;
    StorageReference storageReference;
    HashMap<String, Object> hashMap;
    DatabaseReference reference;
    String imgData;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        mAuth = FirebaseAuth.getInstance();
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        cnfPasswordEditText = findViewById(R.id.cnfPasswordEditText);
        signUpButton = findViewById(R.id.signUpButton);
        imgBtn = findViewById(R.id.imgBtn);
        progressBar = findViewById(R.id.progressBar);
        hashMap = new HashMap<>();
        imgData = "";
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                signup();
            }
        });
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.getCurrentUser();

    }

    public void signup() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String cnfPassword = cnfPasswordEditText.getText().toString();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(cnfPassword)) {
            Toast.makeText(this, "Fields are Empty", Toast.LENGTH_SHORT).show();

        } else if (password.equals(cnfPassword)) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(accountActivity.this, "Sign in successful", Toast.LENGTH_SHORT).show();
                        mAuth.getCurrentUser();
                        FirebaseUser mUser = mAuth.getCurrentUser();
                        String userId = mUser.getUid();
                        reference = FirebaseDatabase.getInstance().getReference("User").child(userId);
                        String username = usernameEditText.getText().toString();

                        if (mAuth.getCurrentUser() != null) {
                            uploadImage();
                            uploadData(username, userId);
                        }

                        startActivity(new Intent(accountActivity.this, Main2Activity.class));
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(accountActivity.this, "Sign in failed", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        } else {
            Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            }catch (Exception e){
                Log.i("Error:","Some error encountered please try again");
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
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,25,baos);
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
