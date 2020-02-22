package com.example.fireapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fireapp.Adapter.User_Adapter;
import com.example.fireapp.model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class tab3 extends Fragment {
    private RecyclerView recyclerView;
    private static final String TAG = "tab3";
    private List<Users> mUsers;
    private Context ctx;
    private User_Adapter user_adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.tab3,container,false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mUsers = new ArrayList<>();
        ctx=container.getContext();
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        readUsers();

        return view;
    }

    private void readUsers() {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("User");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Users users = snapshot.getValue(Users.class);
                    if(snapshot.exists() && !users.getId().equals(firebaseUser.getUid()))
                    {

                        // if(!users.getId().equals(firebaseUser.getUid())) {
                        Log.i(TAG, "onDataChange: "+ snapshot.getKey());
                        mUsers.add(users);
                        Log.i(TAG, "onDataChange: "+snapshot.getValue());

                        // }
                    }


                }
                user_adapter = new User_Adapter(ctx,mUsers, true);
                recyclerView.setAdapter(user_adapter);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
