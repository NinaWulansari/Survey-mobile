package com.wahanaartha.survey;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wahanaartha.survey.admin.AdminActivity;
import com.wahanaartha.survey.model.LoginUser;
import com.wahanaartha.survey.responden.RespondenActivity;

import java.util.Calendar;
import java.util.Date;

import static com.wahanaartha.survey.LoginActivity.MY_LOGIN_PREF;
import static com.wahanaartha.survey.LoginActivity.MY_LOGIN_PREF_KEY;

/**
 * Created by lely on 9/7/17.
 */

public class SplashScreenActivity extends AppCompatActivity {

    public static final int TIME_OUT = 2000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen_activity);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                LoginUser savedUser = new Gson().fromJson(getSharedPreferences(MY_LOGIN_PREF, Context.MODE_PRIVATE).getString(MY_LOGIN_PREF_KEY, ""), LoginUser.class);

                if (savedUser != null && savedUser.getStatus().equals("Success") && savedUser.getLoginDateTime() != null) {
                    Date loginDateTime = savedUser.getLoginDateTime();
                    Calendar now = Calendar.getInstance();
                    Calendar loginCalendar = Calendar.getInstance();
                    loginCalendar.setTime(loginDateTime);
                    loginCalendar.add(Calendar.HOUR, -6);

                    if (loginCalendar.before(now)) {

                        if(savedUser.getTitle().equals("Admin IT")){
                            startActivity(new Intent(SplashScreenActivity.this, AdminActivity.class));
                        }else{
                            startActivity(new Intent(SplashScreenActivity.this, RespondenActivity.class));
                        }
                        finish();
                    } else {

                        Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                        startActivity(i);

                        // close this activity
                        finish();
                    }

                } else {
                    Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(i);

                    // close this activity
                    finish();
                }
            }
        },TIME_OUT);
    }


}
