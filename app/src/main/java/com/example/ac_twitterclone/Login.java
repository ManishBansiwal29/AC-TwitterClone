package com.example.ac_twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private ImageView imgLogin;
    private EditText edtLoginEmail,edtLoginPassword;
    private Button btnLogin,btnLoginSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        imgLogin=findViewById(R.id.imgLogin);
        edtLoginEmail=findViewById(R.id.edtLoginEmail);
        edtLoginPassword=findViewById(R.id.edtLoginPassword);
        btnLogin=findViewById(R.id.btnLogin);
        btnLoginSignUp=findViewById(R.id.btnLoginSignUp);

        btnLogin.setOnClickListener(this);
        btnLoginSignUp.setOnClickListener(this);
        if(ParseUser.getCurrentUser()!=null){
            ParseUser.getCurrentUser().logOut();
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnLogin:
                ParseUser.logInInBackground(edtLoginEmail.getText().toString(),
                        edtLoginPassword.getText().toString(), new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e) {
                                if(user != null && e==null){
                                    FancyToast.makeText(Login.this,  user.getUsername()+ " logged in successfully", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                                    transitionToSocialMedia();
                                }else{
                                    FancyToast.makeText(Login.this, e.getMessage(),
                                            FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                                }
                            }
                        });
                break;
            case R.id.btnLoginSignUp:
                transitionToSignUpActivity();
                break;
        }

    }



    public void transitionToSignUpActivity(){
        Intent intent = new Intent(Login.this, SignUp.class);
        startActivity(intent);
    }
    private void transitionToSocialMedia(){
        Intent intent = new Intent(Login.this, TwitterUsers.class);
        startActivity(intent);
    }
}
