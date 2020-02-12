package com.example.fireapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fireapp.Adapter.Message_Adapter;
import com.example.fireapp.Adapter.Users;
import com.example.fireapp.Adapter.message;
import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class chatActivity extends AppCompatActivity {
    Intent intent;
    TextView username;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    ImageButton sendButton;
    TextView messageText;
    FirebaseUser fuser;
    Message_Adapter message_adapter;
    List<message> messages;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        username = findViewById(R.id.username);
        sendButton = findViewById(R.id.btn_send);
        messageText  = findViewById(R.id.text_send);
        intent=getIntent();
        final String key=intent.getStringExtra("userid");
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("User").child(key);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = messageText.getText().toString();
                if(!msg.trim().isEmpty()){
                    sendMessage(fuser.getUid(),key,msg);
                }
                else{
                    Toast.makeText(chatActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                messageText.setText("");
            }
        });

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
//                try {
//                  //  Toast.makeText(chatActivity.this,key, Toast.LENGTH_SHORT).show();
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
                username.setText(user.getUsername());
            readMessages(fuser.getUid(),key);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void sendMessage(String sender,String receiver,String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message", message);

        reference.child("Chats").push().setValue(hashMap);
    }
    private void readMessages(final String myid, final String userid){
        messages = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messages.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    message chat = snapshot.getValue(message.class);

                        if(chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                                chat.getReceiver().equals(userid) && chat.getSender().equals(myid) ){
                            messages.add(chat);
                        }
                    message_adapter = new Message_Adapter(chatActivity.this,messages);
                    recyclerView.setAdapter(message_adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
