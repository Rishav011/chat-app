package com.example.fireapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.firebase.client.Firebase;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.fireapp.ui.main.SectionsPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;


public class Main2Activity extends AppCompatActivity {
    final Context context = this;
    public Firebase mRootRef;
    public FirebaseAuth mAuth;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.logout)
        {
            AuthUI.getInstance()
                    .signOut(Main2Activity.this)
                    .addOnCompleteListener(new OnCompleteListener<Void>(){

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            startActivity(new Intent(Main2Activity.this,MainActivity.class));
                            finish();

                        }
                    });
            return true;
        }
        else
            return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mAuth= FirebaseAuth.getInstance();
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Fireapp");

        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        boolean firstrun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("firstrun", true);
        if (firstrun){
           dialogbox();
            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("firstrun", false)
                    .commit();
        }


     }
    public void dialogbox(){
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.usernamedialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput =promptsView
                .findViewById(R.id.editTextDialogUserInput);
        final String result = userInput.getText().toString();

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                // Toast.makeText(context, userInput.getText().toString(), Toast.LENGTH_SHORT).show();
                                if(userInput.getText().toString().isEmpty())
                                    Toast.makeText(context, "Please Enter a valid username", Toast.LENGTH_SHORT).show();
                                else {
                                    mRootRef = new Firebase("https://fireapp-49e03.firebaseio.com/User");
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("username",userInput.getText().toString());
                                     mRootRef.child(mAuth.getCurrentUser().getUid()).setValue(hashMap);
                                    // Firebase childRef=mRootRef.child("Username");
                                    //mRootRef.push().setValue(userInput.getText().toString());
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

}