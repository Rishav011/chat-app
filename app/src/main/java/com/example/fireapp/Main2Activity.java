package com.example.fireapp;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.fireapp.ui.main.SectionsPagerAdapter;


public class Main2Activity extends AppCompatActivity {
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
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Fireapp");

        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

//       // Toolbar toolbar =findViewById(R.id.toolbar);
//        toolbar.setSubtitle("Test Subtitle");
//        toolbar.inflateMenu(R.menu.main_menu);
        /*toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
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
                return false;
            }
        });*/

     }

}