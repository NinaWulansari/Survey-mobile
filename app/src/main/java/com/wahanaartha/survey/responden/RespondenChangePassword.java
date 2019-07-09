package com.wahanaartha.survey.responden;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.wahanaartha.survey.R;
import com.wahanaartha.survey.model.API;
import com.wahanaartha.survey.model.LoginUser;
import com.wahanaartha.survey.model.MyProfile;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.wahanaartha.survey.responden.RespondenProfileFragment.ID;

/**
 * Created by Ratri on 19/10/2017.
 */

public class RespondenChangePassword extends AppCompatActivity{
    public ArrayList<LoginUser> data;

    @BindView(R.id.edit_pass)
    EditText edit_pass;
    @BindView(R.id.confirm_pass)
    EditText confirm_pass;
    @BindView(R.id.idProfile)
    EditText idProfile;
    @BindView(R.id.old_pass)
    EditText old_pass;

    Toolbar toolbar;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.responden_activity_profile_change_password);

        ButterKnife.bind(this);

        getIDUser();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getTitle());

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    void getIDUser(){
        API.getMyProfile(getIntent().getStringExtra(ID)).enqueue(new Callback<MyProfile>() {
            @Override
            public void onResponse(Call<MyProfile> call, Response<MyProfile> response) {
                Log.i("EDIT", "onResponse: "+response.body());
                MyProfile profile = response.body();
                idProfile.setText(profile.getId());
                old_pass.setText(profile.getPassword());
            }

            @Override
            public void onFailure(Call<MyProfile> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.button_save_pass)
    void savePass(){
        String id = idProfile.getText().toString();
        String pass_old = old_pass.getText().toString();
        String password_edit = edit_pass.getText().toString();
        String password_confirm = confirm_pass.getText().toString();

        if (password_edit.equals("") ) {
            Toast.makeText(this, "New Password Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
        }else if(password_confirm.equals("") ){
            Toast.makeText(this, "Confirm Password Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
        }else if(password_edit.length()<6){
            Toast.makeText(this, "New Password Harus Lebih Dari 6 Karakter", Toast.LENGTH_SHORT).show();
        }else if(password_edit.equals(password_confirm)){
            API.editPass(id, password_confirm).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.code() == 200){
                        Toast.makeText(RespondenChangePassword.this, "success", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(RespondenChangePassword.this, "failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(RespondenChangePassword.this, "Mohon Cek Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "New Password & Confirm Password Harus Sama", Toast.LENGTH_SHORT).show();
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, RespondenChangePassword.class);
        context.startActivity(starter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
















