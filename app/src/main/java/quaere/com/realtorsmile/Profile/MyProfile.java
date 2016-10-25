package quaere.com.realtorsmile.Profile;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import quaere.com.realtorsmile.R;

/**
 * Created by QServer on 5/3/2016.
 */
public class MyProfile extends Activity implements View.OnTouchListener{
    boolean isClicked=false;
    boolean flag= true;
    private TextView user_profile_name;
    private LinearLayout linear_profile, linearprofile_show;
    private ImageView profile_hide,profile_show;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
       // getSupportActionBar().hide();
        user_profile_name=(TextView)findViewById(R.id.user_profile_name);
        profile_hide =(ImageView)findViewById(R.id.profile_hide);
        profile_show =(ImageView)findViewById(R.id.profile_show);
        linear_profile=(LinearLayout)findViewById(R.id.linear_profile);
        linearprofile_show=(LinearLayout)findViewById(R.id.linearprofile_show);
                  String getname = getIntent().getStringExtra("userId");
        user_profile_name.setText(getname);
        showprofile();


        //LinearLayout mLayoutTab = (LinearLayout)findViewById(R.id.linear_profile);


    }

    private void showprofile(){
        linear_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag){
                    linearprofile_show.setVisibility(View.VISIBLE);
                    profile_hide.setVisibility(View.VISIBLE);
                    profile_show.setVisibility(View.INVISIBLE);
                    flag =false;
                }else{
                    linearprofile_show.setVisibility(View.GONE);
                    profile_hide.setVisibility(View.INVISIBLE);
                    profile_show.setVisibility(View.VISIBLE);
                    flag = true;
                }
            }
        });


        profile_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    linearprofile_show.setVisibility(View.VISIBLE);
                    profile_hide.setVisibility(View.VISIBLE);
                    profile_show.setVisibility(View.INVISIBLE);
                    flag = false;
                } else {
                    linearprofile_show.setVisibility(View.GONE);
                    profile_hide.setVisibility(View.INVISIBLE);
                    profile_show.setVisibility(View.VISIBLE);
                    flag = true;
                }
            }
        });

        profile_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    linearprofile_show.setVisibility(View.VISIBLE);
                    profile_hide.setVisibility(View.VISIBLE);
                    profile_show.setVisibility(View.INVISIBLE);
                    flag = false;
                } else {
                    linearprofile_show.setVisibility(View.GONE);
                    profile_hide.setVisibility(View.INVISIBLE);
                    profile_show.setVisibility(View.VISIBLE);
                    flag = true;
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            linearprofile_show.setVisibility(View.GONE);
            System.out.println("Touch Down X:" + event.getX() + " Y:" + event.getY());
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {

            linearprofile_show.setVisibility(View.VISIBLE);
            System.out.println("Touch Up X:" + event.getX() + " Y:" + event.getY());
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            linearprofile_show.setVisibility(View.GONE);
            System.out.println("Touch Down X:" + event.getX() + " Y:" + event.getY());
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {

            linearprofile_show.setVisibility(View.VISIBLE);
            System.out.println("Touch Up X:" + event.getX() + " Y:" + event.getY());
        }
        return super.onTouchEvent(event);
    }
}
