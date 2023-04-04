package com.example.bottomnavigatoin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.btnNav);


        fragementRepl(new HomeFragment());





        bottomNavigationView.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {

                switch ( item.getItemId()) {
                    case R.id.home:

                        fragementRepl(new HomeFragment());
                    break;

                    case R.id.chatting:

                        fragementRepl(new ChattingFragment());

                        break;

                    case R.id.favorite:

                        fragementRepl(new FavoriteFragment());

                        break;

                    case R.id.user:

                        fragementRepl(new UserFragment());

                        break;



                }


            }
        });
    }

    private void fragementRepl(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();

    }




}