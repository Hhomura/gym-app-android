package br.app.gym_app.view.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.storage.StorageReference;

import br.app.gym_app.domain.FirebaseDomain;
import br.app.gym_app.presenter.LoginActivityPresenter;
import br.app.gym_app.view.R;
import br.app.gym_app.view.interfaces.ILoginView;

public class LoginActivity extends AppCompatActivity implements ILoginView {

    private Intent intent;
    private TextView txt_register;
    private Button btn_login;
    private LoginActivityPresenter mPresenter;
    private EditText edt_email, edt_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        listeners();

    }

    public void init(){
        txt_register = findViewById(R.id.txt_register);
        btn_login = findViewById(R.id.btn_login);
        mPresenter = new LoginActivityPresenter(this, getApplicationContext());
        edt_email = findViewById(R.id.email_login);
        edt_password = findViewById(R.id.password_login);
    }

    public void listeners(){
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.signIn(edt_email.getText().toString(), edt_password.getText().toString());
            }
        });

        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
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
    public void redirectedMainActivity() {
        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(i);
        finish();
    }

}