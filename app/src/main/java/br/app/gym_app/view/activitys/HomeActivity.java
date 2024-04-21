package br.app.gym_app.view.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import br.app.gym_app.presenter.HomeActivityPresenter;
import br.app.gym_app.utils.SharedPreferencesManager;
import br.app.gym_app.view.R;
import br.app.gym_app.view.databinding.ActivityHomeBinding;
import br.app.gym_app.view.databinding.NavHeaderHomeBinding;
import br.app.gym_app.view.interfaces.IHomeView;

public class HomeActivity extends AppCompatActivity implements IHomeView {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    private NavHeaderHomeBinding navHeaderBinding;
    private SharedPreferencesManager mSharedPreferences;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private HomeActivityPresenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        drawer = binding.drawerLayout;
        navigationView = binding.navView;
        navHeaderBinding = NavHeaderHomeBinding.bind(navigationView.getHeaderView(0));
        mSharedPreferences = new SharedPreferencesManager(getApplicationContext());

        init();
        setupNavigation();
        setupMenuClickListeners();
    }

    private void init() {
        navHeaderBinding.imgUser.setImageResource(R.drawable.logo_test);
        navHeaderBinding.txtNameUser.setText(mSharedPreferences.getPreferences().getString("name", ""));
        navHeaderBinding.txtEmailUser.setText(mSharedPreferences.getPreferences().getString("email", ""));
        setSupportActionBar(binding.appBarHome.toolbar);
        mPresenter = new HomeActivityPresenter(getApplicationContext(), this);
    }

    private void setupNavigation() {
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_erased, R.id.nav_logout)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void setupMenuClickListeners() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.nav_erased){
                    mPresenter.deleteUser();
                }
                if(id == R.id.nav_logout){
                    mPresenter.logOut();
                }
                drawer.closeDrawers();
                return true;
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onSuccess(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void redirectionLogin() {
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
        finish();
    }
}
