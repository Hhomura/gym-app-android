package br.app.gym_app.view.activitys;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import br.app.gym_app.utils.SharedPreferencesManager;
import br.app.gym_app.view.R;
import br.app.gym_app.view.databinding.ActivityHomeBinding;
import br.app.gym_app.view.databinding.NavHeaderHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    private NavHeaderHomeBinding navHeaderBinding;
    private SharedPreferencesManager mSheredPreferences;
    DrawerLayout drawer;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        navHeaderBinding.imgUser.setImageResource(R.drawable.logo_test);
        navHeaderBinding.txtNameUser.setText(mSheredPreferences.getPreferences().getString("name", ""));
        navHeaderBinding.txtEmailUser.setText(mSheredPreferences.getPreferences().getString("email", ""));

        setSupportActionBar(binding.appBarHome.toolbar);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void init(){
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        drawer = binding.drawerLayout;
        navigationView = binding.navView;
        navHeaderBinding = NavHeaderHomeBinding.bind(navigationView.getHeaderView(0));
        mSheredPreferences = new SharedPreferencesManager(getApplicationContext());
    }
}