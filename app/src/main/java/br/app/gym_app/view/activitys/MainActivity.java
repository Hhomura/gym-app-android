package br.app.gym_app.view.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import br.app.gym_app.utils.SharedPreferencesManager;
import br.app.gym_app.view.R;

public class MainActivity extends AppCompatActivity {

    SharedPreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferencesManager = new SharedPreferencesManager(getApplicationContext());
        Log.e("OIE", preferencesManager.getPreferences().getString("email", "Nope"));
    }
}