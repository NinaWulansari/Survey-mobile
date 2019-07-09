package com.wahanaartha.survey;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wahanaartha.survey.admin.AdminActivity;
import com.wahanaartha.survey.model.API;
import com.wahanaartha.survey.model.LoginUser;
import com.wahanaartha.survey.responden.RespondenActivity;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lely on 9/7/17.
 */


public class LoginActivity extends AppCompatActivity {

    public static final String MY_LOGIN_PREF = "myLoginPref";
    public static final String MY_LOGIN_PREF_KEY = "loginPrefKey";

    @BindView(R.id.username) EditText usernameEdittext;
    @BindView(R.id.password) EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnLogin)
    void login() {
        final String username = usernameEdittext.getText().toString();
        String password = passwordEditText.getText().toString();

        if (username.equals("") || password.equals("")) {
            Toast.makeText(this, "Username & password Must Be Filled", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.i("INFO", new Gson().toJson(new LoginUser(username, password)));

        API.login(new LoginUser(username, password)).enqueue(new Callback<LoginUser>() {
            @Override
            public void onResponse(Call<LoginUser> call, Response<LoginUser> response) {
                if (response.code() == 200) {
                    LoginUser user = response.body();
                    user.setLoginDateTime(new Date());

                    // Save data login
                    getSharedPreferences(MY_LOGIN_PREF, Context.MODE_PRIVATE).edit().putString(MY_LOGIN_PREF_KEY, new Gson().toJson(user)).apply();

                    if (!user.getStatus().equals("Success")) {
                        Toast.makeText(LoginActivity.this, user.getStatus(), Toast.LENGTH_LONG).show();
                    } else {
                        if(user.getTitle().equals("Admin IT")){
                            startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                        }else{
                            startActivity(new Intent(LoginActivity.this, RespondenActivity.class));
                        }
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "Wrong Username Or Password", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginUser> call, Throwable t) {
                Toast.makeText(LoginActivity.this,"Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
