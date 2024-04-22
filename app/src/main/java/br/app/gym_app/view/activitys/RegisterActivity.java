package br.app.gym_app.view.activitys;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import br.app.gym_app.presenter.ResgisterActivityPresenter;
import br.app.gym_app.view.R;
import br.app.gym_app.view.interfaces.IRegisterView;

public class RegisterActivity extends AppCompatActivity implements IRegisterView {

    private ResgisterActivityPresenter mPresenter;
    private EditText nameUser, emailUser, passwordUser, confirmPasswordUser;
    private Button concludeButton;
    private ImageView backButton, imgRegister;
    Uri imgUri;
    ActivityResultLauncher<String> getContentLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
        listeners();

    }

    public void init(){
        nameUser = findViewById(R.id.edt_name_user);
        emailUser = findViewById(R.id.edt_observation_exercise);
        passwordUser = findViewById(R.id.edt_password_user);
        confirmPasswordUser = findViewById(R.id.edt_password_confirm);
        concludeButton = findViewById(R.id.btn_create_account);
        backButton = findViewById(R.id.imb_back);
        imgRegister = findViewById(R.id.img_register);
        mPresenter = new ResgisterActivityPresenter(getApplicationContext(), this);
    }

    public void listeners(){
        concludeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkPasswords(passwordUser.getText().toString(), confirmPasswordUser.getText().toString())){
                    mPresenter.createAccountFirebase(emailUser.getText().toString(), passwordUser.getText().toString(), nameUser.getText().toString(), imgUri);
                }else{
                    passwordUser.setError(getString(R.string.error_passwords));
                    confirmPasswordUser.setError(getString(R.string.error_passwords));
                }

            }
        });

        imgRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContentLauncher.launch("image/*");
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getContentLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            imgUri = result;
                            imgRegister.setImageURI(imgUri);
                        }
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && data != null && data.getData() != null){
            imgUri = data.getData();
            imgRegister.setImageURI(imgUri);
        }
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
    public void redirectedLogin() {
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
        finish();
    }

    public boolean checkPasswords(String password, String confirmPassword){
        return password.equalsIgnoreCase(confirmPassword);
    }
}