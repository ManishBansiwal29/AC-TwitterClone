package com.example.ac_twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

        btnSendTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseObject parseObject = new ParseObject("MyTweet");
                parseObject.put("tweet", edtTweet.getText().toString());
                parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                final ProgressDialog dialog = new ProgressDialog(SendTweetActivity.this);
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
        });
        btnViewTweets.setOnClickListener(this);
    }
//    public void sendTweet(){
//
//    }

    @Override
    public void onClick(View v) {
        final ArrayList<HashMap<String,String>> tweetList = new ArrayList<>();
        final SimpleAdapter adapter = new SimpleAdapter(SendTweetActivity.this,tweetList,android.R.layout.simple_expandable_list_item_2,new String[]{"tweetUsername","tweetValue"},new int[]{android.R.id.text1,android.R.id.text2});
        try{
            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("MyTweet");
            parseQuery.whereContainedIn("username",ParseUser.getCurrentUser().getList("fanOf"));
            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if(objects.size()>0 && e==null){
                        for(ParseObject tweetObjects:objects){
                            HashMap<String,String> userTweet = new HashMap<>();
                            userTweet.put("tweetUsername",tweetObjects.getString("username"));
                            userTweet.put("tweetValue",tweetObjects.getString("tweet"));
                            tweetList.add(userTweet);
                        }
                        viewTweetListView.setAdapter(adapter);
                    }
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
      }
  }
