package com.darkwinter.bookfilms;

import Model.*;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin;
    private EditText txtEmail;
    private EditText txtPassword;
    private TextView txtRegister;
    public ConnectDB connectDB = new ConnectDB();
    private ProgressDialog loginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginProgress = new ProgressDialog(this, R.style.Theme_MyDialog);
        connectDB.setActivity(this);
        addControls();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString();
                String pass = txtPassword.getText().toString();
                if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(pass)) {
                    loginProgress.setTitle("Login with email");
                    loginProgress.setMessage("Please waiting");
                    loginProgress.setCanceledOnTouchOutside(false);
                    loginProgress.show();
                    connectDB.pushCurrentUser(email, pass, new ConnectDB.OnLoadedUser() {
                        @Override
                        public void onLoadedUser(Task task) {
                            if (task.isSuccessful()) {
                                loginProgress.dismiss();
                                Log.d("this is task", task.toString());
                                callStartScreen();
                            }
                        }

                    });
                }
//
            }
        });
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callRegisterScreen();

            }
        });
    }
    private void addControls() {
        btnLogin = findViewById(R.id.btnLogIn);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        txtRegister = findViewById(R.id.txtRegister);


    }
    private void callStartScreen(){
        Intent startIntent = new Intent(LoginActivity.this, StartActivity.class);
        startActivity(startIntent);
    }

    private void callRegisterScreen(){
        Intent regIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(regIntent);
        finish();
    }
}
