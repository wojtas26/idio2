package cz.idio;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import cz.idio.api.TempDataHolder;
import cz.idio.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "data";
    private AppBarConfiguration appBarConfiguration;
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
      //  setSupportActionBar(binding.toolbar);

        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(MainActivity.this, fab);
            popup.inflate(R.menu.menu_fab);
            popup.setOnMenuItemClickListener(item -> {
                // Handle menu item click events
                switch (item.getItemId()) {
                    case R.id.menu_option1:
                        binding.fab.getPointerIcon();
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main, new FirstFragment()).commit();
                        return true;
                    case R.id.menu_option2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main, new CantFragment()).commit();
                        return true;
                    case R.id.menu_option3:
                        // Handle option 3 click
                        return true;
                    default:
                        return false;
                }
            });
            popup.show();
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

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

}