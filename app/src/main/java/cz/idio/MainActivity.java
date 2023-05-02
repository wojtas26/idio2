package cz.idio;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import cz.idio.api.TempDataHolder;
import cz.idio.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "data";
    private ActivityMainBinding binding;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Zkontrolujte, zda je uživatel přihlášen
        if (!isLoggedIn()) {
            // Přesměrujte na LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new FirstFragment());
        BottomNavigationView fab = binding.bottomNavigationView;
        fab.setOnItemSelectedListener(item -> {
                // Handle menu item click events
                switch (item.getItemId()) {
                    case R.id.menu_attendance:
                        replaceFragment(new FirstFragment());
                        break;
                    case R.id.menu_catering:
                        replaceFragment(new CantFragment());
                        break;
                    case R.id.menu_setting:
                        replaceFragment(new SettingFragment());
                        break;
                    case R.id.menu_info:
                        replaceFragment(new InfoFragment());
                        break;

                }
            return true;
            });
    }


    private boolean isLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String value = TempDataHolder.getInstance().get("temporary_key");
        boolean autoLogin = sharedPreferences.getBoolean("auto_login", false);
        if(autoLogin){
           value = sharedPreferences.getString("key", "");
        }
        return value != null ;
    }

private void replaceFragment(Fragment fragment){
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.replace(R.id.frameLayout2,fragment);
    fragmentTransaction.commit();
}
}