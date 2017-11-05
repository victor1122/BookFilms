package com.darkwinter.bookfilms;

import Model.*;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.Task;


public class RegisterActivity extends AppCompatActivity {
    private EditText txtEmail;
    private EditText txtName;
    private EditText txtDOB;
    private EditText txtPassword;
    private EditText txtZipCode;
    private EditText txtPhone;
    private Button btnRegister;
    private ConnectDB connectDB = new ConnectDB();
    private ProgressDialog loginProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        addControls();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString();
                String name  = txtName.getText().toString();
                String pass = txtPassword.getText().toString();
                String DOB = txtDOB.getText().toString();
                String phone = txtPhone.getText().toString();
                String Zipcode = txtZipCode.getText().toString();
                if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(pass)) {
                    loginProgress.setTitle("Register is in process");
                    loginProgress.setMessage("Please waiting ...");
                    loginProgress.setCanceledOnTouchOutside(false);
                    loginProgress.show();
                    connectDB.saveDataUser(email, name, pass, DOB, phone, Zipcode, new ConnectDB.OnLoadedUser() {
                        @Override
                        public void onLoadedUser(Task task) {
                            if(task.isSuccessful()){
                                loginProgress.dismiss();
                                Intent intent = new Intent(RegisterActivity.this, StartActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }

            }
        });

    }
    private void addControls() {
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        txtName = findViewById(R.id.txtName);
        txtDOB = findViewById(R.id.txtBirth);
        txtPhone = findViewById(R.id.txtPhone);
        txtZipCode = findViewById(R.id.txtZipcode);
        btnRegister = findViewById(R.id.btnRegister);
        
    }
}
