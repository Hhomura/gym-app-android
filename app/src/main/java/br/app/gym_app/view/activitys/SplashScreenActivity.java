package br.app.gym_app.view.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import br.app.gym_app.utils.SharedPreferencesManager;
import br.app.gym_app.view.R;

public class SplashScreenActivity extends AppCompatActivity {

    View container;
    boolean finish = false;
    private SharedPreferencesManager mSharedPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        init();
        ObjectAnimator animator = ObjectAnimator.ofFloat(container, "translationY", -370);
        animator.setDuration(800);
        animator.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //Verificação de usuário já logado
                if(!mSharedPreferencesManager.getPreferences().getString("email", "").equals("")){
                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                    ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(SplashScreenActivity.this);
                    startActivity(i,activityOptions.toBundle());
                    finish = true;
                }else{
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(SplashScreenActivity.this);
                    startActivity(i,activityOptions.toBundle());
                    finish = true;
                }
            }
        }, 3000);
    }

    public void init(){
        container = findViewById(R.id.content_splash);
        mSharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(finish){
            finish();
        }
    }
}