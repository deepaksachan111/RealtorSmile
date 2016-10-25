package quaere.com.realtorsmile;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button loginBtn;
    TextView registerTv, frgtPswdTv;
    EditText emailEt, pswdEt;
    String email, password;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String login_url ;
    ProgressDialog dialog;
    SessionManager sessionManager ;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this);


        setContentView(R.layout.login_activity);
        loginBtn = (Button) findViewById(R.id.login_submitBtn);
        registerTv = (TextView) findViewById(R.id.login_registerTv);
        frgtPswdTv = (TextView) findViewById(R.id.login_frgetPswdTv);
        emailEt = (EditText) findViewById(R.id.login_usrNameEt);
        pswdEt = (EditText) findViewById(R.id.login_pswdEt);

       // sharedpreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        loginBtn.setOnClickListener(this);
        registerTv.setOnClickListener(this);
        frgtPswdTv.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_submitBtn:
               // startActivity(new Intent(LoginActivity.this, MainPage.class));
                login();
                break;
            case R.id.login_registerTv:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.login_frgetPswdTv:
                startActivity(new Intent(LoginActivity.this, MainPage.class));
                break;
        }
    }

    private void login() {
        email = emailEt.getText().toString();
        password = pswdEt.getText().toString();

        if (email.equals("") && password.equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Please enter UserId and Password",
                    Toast.LENGTH_SHORT).show();
            emailEt.requestFocus();
        } else if (email.equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Please enter User id", Toast.LENGTH_SHORT)
                    .show();
            emailEt.requestFocus();
        } else if (password.equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Please enter Password", Toast.LENGTH_SHORT).show();
            pswdEt.requestFocus();
        } else {
            login_url = "http://rsmile.quaeretech.com/RealtorSmile.svc/Login/" + email + "/" + password;
            ConnectivityManager conMgr = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
            if (netInfo == null) {
                // Animation.Description.setVisibility(View.INVISIBLE);
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("Connection Failed !")
                        .setMessage("Unable to connect.Please review your network settings.")
                        .setPositiveButton("OK", null).show();
            } else {

                SuccessfulLogin productTask = new SuccessfulLogin();
                    productTask.execute();
            }

        }
    }

    private class SuccessfulLogin extends AsyncTask<Void,Void,Void>{
        String login_response,login_response_code;
        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(LoginActivity.this, "", "Loading...", true,
                    false);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                HttpClient httpClient = new DefaultHttpClient();

                HttpGet httpget = new HttpGet(login_url);

                System.out.println("User name " + email + " Passwordd is " + password);
                HttpResponse httpResponse = httpClient.execute(httpget);
                HttpEntity httpEntity = httpResponse.getEntity();

                login_response = EntityUtils.toString(httpEntity);
                Log.v("login_response :", login_response);

                JSONArray jsonArray = new JSONArray(login_response);
                JSONObject object = jsonArray.getJSONObject(0);

                login_response_code = object.getString("MSG");
                Log.v("login_response_code :", login_response_code);


            } catch (Exception e) {
                Log.v("Login Exception.......", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                if (login_response_code.equals("1")) {
                    Log.v("inside onPost :", " "+1);



                     sessionManager.createLoginemail(email);

                    Intent intent = new Intent(LoginActivity.this, MainPage.class);
                    intent.putExtra("email",email);
                    startActivity(intent);
                    dialog.dismiss();
                    finish();
                } else {
                    Log.v("inside onPost :", " "+0);
                    Toast.makeText(LoginActivity.this, "Enter Valid Email and Password", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Server is Failed ", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }


}
}
