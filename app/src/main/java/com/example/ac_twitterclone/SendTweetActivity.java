package com.example.ac_twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SendTweetActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtTweet;
    private Button btnSendTweet;

    private ListView viewTweetListView;
    private Button btnViewTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_tweet);
        edtTweet = findViewById(R.id.edtSendTweet);
        btnSendTweet=findViewById(R.id.btnSendTweet);

        viewTweetListView=findViewById(R.id.viewTweetListView);
        btnViewTweets=findViewById(R.id.btnViewTweets);

        btnSendTweet.setOnClickListener(this);
        btnViewTweets.setOnClickListener(this);
    }
    public void sendTweet(View view){
        ParseObject parseObject = new ParseObject("MyTweet");
        parseObject.put("tweet", edtTweet.getText().toString());
        parseObject.put("username", ParseUser.getCurrentUser().getUsername());
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null){
                    FancyToast.makeText(SendTweetActivity.this, ParseUser.getCurrentUser()
                            .getUsername()+"'s tweet"+"("+edtTweet.getText().toString()+")", Toast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                }else {
                    FancyToast.makeText(SendTweetActivity.this,e.getMessage(),FancyToast.LENGTH_SHORT,FancyToast.ERROR,true).show();
                }
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        sendTweet(view);
    }
}
