package br.app.gym_app.domain;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class FirebaseDomain {


    private final FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore db;
    private Context context;

    public FirebaseDomain (Context context){
        FirebaseApp.initializeApp(context);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        this.context = context;
    }

    public FirebaseAuth getmAuth(){
        return mAuth;
    }

    public void setFirebaseUser(FirebaseUser mUser){
        this.mUser = mUser;
    }

    public FirebaseUser getmUser(){
        return mUser;
    }

    public FirebaseFirestore getFirebaseFireStore(){
        return this.db;
    }

    public void checkConnection() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Toast.makeText(context, "Logado", Toast.LENGTH_SHORT).show();
        }
    }
}
