package br.app.gym_app.view.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import br.app.gym_app.domain.FirebaseDomain;
import br.app.gym_app.utils.SharedPreferencesManager;
import br.app.gym_app.view.R;

public class SplashScreenActivity extends AppCompatActivity {

    View container;
    private SharedPreferencesManager mSharedPreferencesManager;
    private FirebaseDomain domain;

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
                if(domain.getmUser() != null){
                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                    ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(SplashScreenActivity.this);
                    startActivity(i,activityOptions.toBundle());
                    finish();
                }else{
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(SplashScreenActivity.this);
                    startActivity(i,activityOptions.toBundle());
                    finish();
                }
            }
        }, 3000);
    }

    public void init(){
        container = findViewById(R.id.content_splash);
        mSharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());
        domain = new FirebaseDomain(getApplicationContext());
        domain.setFirebaseUser(domain.getmAuth().getCurrentUser());
    }
}