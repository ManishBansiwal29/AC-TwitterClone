package com.example.ac_twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    private ImageView imgSignUp;
    private EditText edtSignUpEmail,edtSignUpUsername,edtSignUpPassword;
    private Button btnSignUp,btnSignUpLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgSignUp=findViewById(R.id.imgSignUp);
        edtSignUpEmail=findViewById(R.id.edtSignUpEmail);
        edtSignUpUsername=findViewById(R.id.edtSignUpUsername);
        edtSignUpPassword=findViewById(R.id.edtSignUpPassword);
        btnSignUp=findViewById(R.id.btnSignUp);
        btnSignUpLogin=findViewById(R.id.btnSignUpLogin);

        btnSignUp.setOnClickListener(this);
        btnSignUpLogin.setOnClickListener(this);
        if (ParseUser.getCurrentUser()!=null){
            //ParseUser.getCurrentUser().logOut();
            transitionToSocialMedia();
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSignUp:

                final ParseUser appUser = new ParseUser();
                appUser.setEmail(edtSignUpEmail.getText().toString());
                appUser.setUsername(edtSignUpUsername.getText().toString());
                appUser.setPassword(edtSignUpPassword.getText().toString());
                final ProgressDialog dialog = new ProgressDialog(this);
                dialog.setMessage("siging up" + edtSignUpUsername.getText().toString());
                dialog.show();
                appUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            FancyToast.makeText(SignUp.this,appUser.getUsername(),
                                    FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                            transitionToSocialMedia();
                        }else{
                            FancyToast.makeText(SignUp.this, e.getMessage(),
                                    FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                        }
                        dialog.dismiss();
                    }
                });

                break;
            case R.id.btnSignUpLogin:
                Intent intent = new Intent(SignUp.this,Login.class);
                startActivity(intent);
                break;
        }
    }
    private void transitionToSocialMedia(){
        Intent intent = new Intent(SignUp.this, TwitterUsers.class);
        startActivity(intent);
    }
}



