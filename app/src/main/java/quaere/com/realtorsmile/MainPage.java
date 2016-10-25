package quaere.com.realtorsmile;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.HashMap;

import quaere.com.realtorsmile.ContactActivity.ContactsActivity;
import quaere.com.realtorsmile.DocumentsActivity.DocumentActivity;
import quaere.com.realtorsmile.ListingActivities.ListingActivity;
import quaere.com.realtorsmile.Profile.MyProfile;
import quaere.com.realtorsmile.UserActivities.UserActivities;

/**
 * Created by intex on 12/26/2015.
 */
public class MainPage extends Activity implements View.OnClickListener{
    LinearLayout contactLayout,propertiesLayout,activitiesLayout,potentialsLayout,docsLayout,profileLayout,leadsLayout, logoutlayout;
    String userId;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page_demo);

        sessionManager = new SessionManager(this);

        contactLayout = (LinearLayout) findViewById(R.id.contact_layout);
        propertiesLayout = (LinearLayout) findViewById(R.id.properties_layout);
        activitiesLayout = (LinearLayout) findViewById(R.id.activities_layout);
        potentialsLayout = (LinearLayout) findViewById(R.id.potentials_layout);
        docsLayout = (LinearLayout) findViewById(R.id.documents_layout);
        leadsLayout = (LinearLayout) findViewById(R.id.listing_layout);
        profileLayout=(LinearLayout)findViewById(R.id.profile_layout);
        logoutlayout =(LinearLayout)findViewById(R.id.logout_layout);


        contactLayout.setOnClickListener(this);
        propertiesLayout.setOnClickListener(this);
        activitiesLayout.setOnClickListener(this);
        potentialsLayout.setOnClickListener(this);
        docsLayout.setOnClickListener(this);
        leadsLayout.setOnClickListener(this);
        profileLayout.setOnClickListener(this);
        logoutlayout.setOnClickListener(this);

        HashMap<String,String> email = sessionManager.getUserDetails();
            userId = email.get(SessionManager.KEY_EMAIL);

        Toast.makeText(getApplicationContext(),"your email is "+userId,Toast.LENGTH_LONG).show();

       // userId = getIntent().getStringExtra("email");

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.contact_layout :
              //startActivityForResult(new Intent(MainPage.this, ContactsActivity.class), 2);
                Intent i = new Intent(MainPage.this,ContactsActivity.class);
                i.putExtra("userId", userId);
                startActivity(i);
               // startActivity(new Intent(MainPage.this, ContactsActivity.class));

                break;
            case R.id.properties_layout :
                Intent i1 = new Intent(MainPage.this, PropertiesActivity.class);
                i1.putExtra("userId",userId);
                startActivity(i1);
                Toast.makeText(getApplicationContext(),"Properties Layout",Toast.LENGTH_SHORT).show();
                break;
            case R.id.activities_layout :
                Intent i3 = new Intent(MainPage.this,UserActivities.class);
                i3.putExtra("userId", userId);
                startActivity(i3);
                Toast.makeText(getApplicationContext(),"Activities Layout",Toast.LENGTH_SHORT).show();
                break;
            case R.id.potentials_layout :
                Toast.makeText(getApplicationContext(),"Potentials Layout",Toast.LENGTH_SHORT).show();
                break;
            case R.id.documents_layout :
                startActivity(new Intent(MainPage.this, DocumentActivity.class));
                Toast.makeText(getApplicationContext(),"Documents Layout",Toast.LENGTH_SHORT).show();
                break;
            case R.id.listing_layout :
                startActivity(new Intent(MainPage.this, ListingActivity.class));
                Toast.makeText(getApplicationContext(),"Listing Layout",Toast.LENGTH_LONG).show();
                break;
            case R.id.profile_layout :

                Intent pr = new Intent(MainPage.this, MyProfile.class);
                pr.putExtra("userId",userId);
                startActivity(pr);
                Toast.makeText(getApplicationContext(),"Profile Page",Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout_layout :

                 sessionManager.logoutUser();
                //startActivity(new Intent(this,SwipeImageMainActivity.class));

                Toast.makeText(getApplicationContext(),"User Logout",Toast.LENGTH_LONG).show();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
