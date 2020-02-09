package com.example.ac_twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;


public class TwitterUsers extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView listView;
    private ArrayList<String> tUsers;
    private ArrayAdapter arrayAdapter;
    private String followedUser="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);
        listView = findViewById(R.id.listView);
        tUsers = new ArrayList();
        arrayAdapter = new ArrayAdapter(TwitterUsers.this, android.R.layout.simple_list_item_checked, tUsers);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(this);

        try {
            ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
            parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> users, ParseException e) {
                    if (users.size() > 0 && e == null) {
                        for (ParseUser user : users) {
                            tUsers.add(user.getUsername());
                        }
                        listView.setAdapter(arrayAdapter);
                        if(ParseUser.getCurrentUser().getList("fanOf")!=null){
                            for(String twitterUser:tUsers){
                                followedUser = followedUser + twitterUser +"\n";
                                if(ParseUser.getCurrentUser().getList("fanOf").contains
                                        (twitterUser)){
                                    listView.setItemChecked(tUsers.indexOf(twitterUser),true);
                                    FancyToast.makeText(TwitterUsers.this,
                                            ParseUser.getCurrentUser().getUsername()+" is following "+
                                            followedUser,Toast.LENGTH_LONG, FancyToast.INFO,true).show();
                                }
                            }
                        }
                    }

                }
            });


        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout_item:
                ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        Intent intent = new Intent(TwitterUsers.this,Login.class);
                        startActivity(intent);
                        finish();
                    }
                });
            case R.id.sendTweet:
                Intent intent = new Intent(TwitterUsers.this,SendTweetActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckedTextView checkedTextView = (CheckedTextView) view;
        if(checkedTextView.isChecked()){
            FancyToast.makeText(TwitterUsers.this,tUsers.get(position)+" ' is followed", FancyToast.LENGTH_SHORT,
                    FancyToast.INFO,true).show();
            ParseUser.getCurrentUser().add("fanOf",tUsers.get(position));
        }else{
            FancyToast.makeText(TwitterUsers.this,tUsers.get(position)+" ' is not followed", FancyToast.LENGTH_SHORT,
                    FancyToast.INFO,true).show();
            ParseUser.getCurrentUser().getList("fanOf").remove(tUsers.get(position));
            List currentUserFanOfList = ParseUser.getCurrentUser().getList("fanOf");
            ParseUser.getCurrentUser().remove("fanOf");
            ParseUser.getCurrentUser().put("fanOf", currentUserFanOfList);
        }
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    FancyToast.makeText(TwitterUsers.this," saved", FancyToast.LENGTH_SHORT,
                            FancyToast.SUCCESS,true).show();
                }else{
                    FancyToast.makeText(TwitterUsers.this,e.getMessage()+"", FancyToast.LENGTH_SHORT,
                            FancyToast.ERROR,true).show();
                }
            }
        });
    }
}
