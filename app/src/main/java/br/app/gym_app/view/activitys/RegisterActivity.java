package br.app.gym_app.view.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import br.app.gym_app.presenter.ResgisterActivityPresenter;
import br.app.gym_app.view.R;
import br.app.gym_app.view.interfaces.IRegisterView;

public class RegisterActivity extends AppCompatActivity implements IRegisterView {

    private ResgisterActivityPresenter mPresenter;
    private EditText nameUser, emailUser, passwordUser, confirmPasswordUser;
    private Button concludeButton;
    private ImageView backButton, imgRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
        listeners();

    }

    public void init(){
        nameUser = findViewById(R.id.edt_name_user);
        emailUser = findViewById(R.id.edt_email_user);
        passwordUser = findViewById(R.id.edt_password_user);
        confirmPasswordUser = findViewById(R.id.edt_password_confirm);
        concludeButton = findViewById(R.id.btn_create_account);
        backButton = findViewById(R.id.imb_back);
        mPresenter = new ResgisterActivityPresenter(getApplicationContext(), this);
    }

    public void listeners(){
        concludeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkPasswords(passwordUser.getText().toString(), confirmPasswordUser.getText().toString())){
                    mPresenter.createAccountFirebase(emailUser.getText().toString(), passwordUser.getText().toString(), nameUser.getText().toString());
                }else{
                    passwordUser.setError(getString(R.string.error_passwords));
                    confirmPasswordUser.setError(getString(R.string.error_passwords));
                }

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onSuccess(String msg) {

    }

    @Override
    public void onError(String msg) {

    }

    @Override
    public void redirectedLogin() {
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
        finish();
    }

    public boolean checkPasswords(String password, String confirmPassword){
        return password.equalsIgnoreCase(confirmPassword);
    }
}