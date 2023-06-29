package info.jeonju.stylerent.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import info.jeonju.stylerent.R;
import info.jeonju.stylerent.fragments.ChattingFragment;
import info.jeonju.stylerent.fragments.FavoriteFragment;
import info.jeonju.stylerent.fragments.HomeFragment;
import info.jeonju.stylerent.fragments.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.btnNav);
        HomeFragment homeFragment = new HomeFragment();
        FavoriteFragment favoriteFragment = new FavoriteFragment();
        UserFragment userFragment = new UserFragment();
        ChattingFragment chattingFragment = new ChattingFragment();

        fragementRepl(homeFragment);
        currentFragment = homeFragment;

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        if (currentFragment instanceof HomeFragment) {
                            return true; // Already on the selected fragment, no need to do anything
                        }
                        fragementRepl(homeFragment);
                        currentFragment = homeFragment;
                        break;

                    case R.id.chatting:
                        if (currentFragment instanceof ChattingFragment) {
                            return true; // Already on the selected fragment, no need to do anything
                        }
                        fragementRepl(chattingFragment);
                        currentFragment = chattingFragment;
                        break;

                    case R.id.favorite:
                        if (currentFragment instanceof FavoriteFragment) {
                            return true; // Already on the selected fragment, no need to do anything
                        }
                        fragementRepl(favoriteFragment);
                        currentFragment = favoriteFragment;
                        break;

                    case R.id.user:
                        if (currentFragment instanceof UserFragment) {
                            return true; // Already on the selected fragment, no need to do anything
                        }
                        fragementRepl(userFragment);
                        currentFragment = userFragment;
                        break;
                }
                return true;
            }
        });
    }

    private void fragementRepl(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }
}
