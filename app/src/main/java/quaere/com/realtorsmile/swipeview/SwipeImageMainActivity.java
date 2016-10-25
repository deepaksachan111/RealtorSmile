package quaere.com.realtorsmile.swipeview;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import quaere.com.realtorsmile.LoginActivity;
import quaere.com.realtorsmile.MainPage;
import quaere.com.realtorsmile.NetworkConnectionchecker;
import quaere.com.realtorsmile.R;
import quaere.com.realtorsmile.SessionManager;


public class SwipeImageMainActivity extends BaseSampleActivity {
	Button next;
    TextView login;
	ViewPager mPager;
    String email;
    SharedPreferences sharedpreferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.swipe_layout);
        next = (Button)findViewById(R.id.nextBtn);
        login = (TextView)findViewById(R.id.loginTextView);

      //  userId = getIntent().getStringExtra("email");

       // Toast.makeText(getApplicationContext(), "you email is " + email, Toast.LENGTH_LONG).show();

        mAdapter = new TestFragmentAdapter(getSupportFragmentManager());

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);

        Boolean isInternetPresent = false;
        NetworkConnectionchecker connectionchecker ;
      /*  requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/

            connectionchecker = new NetworkConnectionchecker(getApplicationContext());

            isInternetPresent = connectionchecker.isConnectingToInternet();

            // check for Internet status
            if (isInternetPresent) {
                // Internet Connection is Present
                // make HTTP requests
               /* showAlertDialog(LoginActivity.this, "Internet Connection",
                        "You have internet connection", true);*/
                final SessionManager session = new SessionManager(this);
                HashMap<String, String> user = session.getUserDetails();
                // name
                String name = user.get(SessionManager.KEY_EMAIL);
                if(name != null){
                    startActivity(new Intent(this,MainPage.class));
                    finish();
                }else {

                  startActivity(new Intent(this,LoginActivity.class));
                }

            } else {
                // Internet connection is not present
                // Ask user to connect to Internet
                showAlertDialog(SwipeImageMainActivity.this, "No Internet Connection", "You don't have internet connection.", false);

            }
        next.setOnClickListener(new OnClickListener() {

            @Override

            public void onClick(View v) {
                Intent i = new Intent(SwipeImageMainActivity.this,MainPage.class);
                i.putExtra("email", email);
                startActivity(i);
            }
        });
        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SwipeImageMainActivity.this, LoginActivity.class));
            }
        });

        }

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        Dialog dialog;

        final AlertDialog.Builder alertbuider = new AlertDialog.Builder(this);
        // AlertDialog alertDialog = AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertbuider.setTitle(title);

        // Setting Dialog Message
        alertbuider.setMessage(message);

        // Setting alert dialog icon
        alertbuider.setIcon((status) ? R.drawable.success : R.drawable.fail);

        // Setting OK Button
        alertbuider.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
       /* alertbuider.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            }
        });*/
        AlertDialog alertDialog = alertbuider.create();
        dialog = alertDialog;
        // Showing Alert Message
        dialog.show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
// dont call **super**, if u want disable back button in current screen.
    }

    }


