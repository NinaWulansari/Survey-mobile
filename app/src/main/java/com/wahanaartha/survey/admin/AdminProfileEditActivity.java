package com.wahanaartha.survey.admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wahanaartha.survey.R;
import com.wahanaartha.survey.model.API;
import com.wahanaartha.survey.model.DataUser;
import com.wahanaartha.survey.model.DetailUser;
import com.wahanaartha.survey.model.LoginUser;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.wahanaartha.survey.admin.AdminProfileDetailActivity.EDIT_USER;
import static com.wahanaartha.survey.admin.AdminProfileFragment.DATA_USER;

/**
 * Created by Nina on 10/10/2017.
 */


public class AdminProfileEditActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_ADD = 88;
    public ArrayList<LoginUser> data;
    @BindView(R.id.name_profile) EditText nameProfile;
    @BindView(R.id.username_profile) EditText usernameProfile;
    @BindView(R.id.pass_profile) EditText passProfile;
    @BindView(R.id.email_profile) EditText emailProfile;
    @BindView(R.id.id_user) EditText idProfile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_edit_profile_user);

        ButterKnife.bind(this);

        TextView cancel = (TextView) findViewById(R.id.cancel_edit);
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

       API.getDetailUser(getIntent().getStringExtra(EDIT_USER)).enqueue(new Callback<DetailUser>() {
           @Override
           public void onResponse(Call<DetailUser> call, Response<DetailUser> response) {
               Log.i("EDIT", "onResponse: "+response.body());
               DetailUser detailUser = response.body();
               idProfile.setText(detailUser.getId());
               nameProfile.setText(detailUser.getName());
               usernameProfile.setText(detailUser.getUsername());
               passProfile.setText(detailUser.getPassword());
               emailProfile.setText(detailUser.getEmail());
//               if (response.code() == 200){
//                   Toast.makeText(AdminProfileEditActivity.this, "update success", Toast.LENGTH_SHORT).show();
//               } else{
//                   Toast.makeText(AdminProfileEditActivity.this, "update failed", Toast.LENGTH_SHORT).show();
//               }
           }

           @Override
           public void onFailure(Call<DetailUser>call, Throwable t) {
               Toast.makeText(AdminProfileEditActivity.this, "update failed", Toast.LENGTH_SHORT).show();
           }
       });

        TextView done = (TextView) findViewById(R.id.done_edit);
        done.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                doEdit();
            }
        });

    }

    private void doEdit(){
        if(isValid()){
            String id = idProfile.getText().toString();
            String name = nameProfile.getText().toString();
            String username = usernameProfile.getText().toString();
            String password = passProfile.getText().toString();
            String email = emailProfile.getText().toString();

            API.edituser(id, name, username, password, email).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
//                        Log.i("UPDATE", "onResponse: " + response);
                    if (response.code() == 200){
                        Toast.makeText(AdminProfileEditActivity.this, "Swipe Down for Update Profile", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AdminProfileEditActivity.this, "failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(AdminProfileEditActivity.this, "cek koneksi", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean isValid() {
        boolean valid = true;
        String nama = nameProfile.getText().toString();
        String username = usernameProfile.getText().toString();
        String password = passProfile.getText().toString();
        String email = emailProfile.getText().toString();

        //validasi nama
        if(nama.isEmpty()){
            nameProfile.setError("Nama belum diisi");
            valid = false;
        }
        else if (nama.length()<3)
        {
            nameProfile.setError("Nama minimal 3 karakter");
            valid = false;
        }
        else
        {
            nameProfile.setError(null);
        }

        //validasi username
        if(username.isEmpty()){
            usernameProfile.setError("Username belum diisi");
            valid = false;
        }
        else if (username.length()<3)
        {
            usernameProfile.setError("Username minimal 3 karakter");
            valid = false;
        }
        else if(username.contains(" "))
        {
            usernameProfile.setError("Username tidak boleh mengandung spasi");
            valid = false;
        }
        else
        {
            usernameProfile.setError(null);
        }

        //validasi password
        if(password.isEmpty()){
            passProfile.setError("Password belum diisi");
            valid = false;
        }
        else if (password.length()<6)
        {
            passProfile.setError("Password minimal 6 karakter");
            valid = false;
        }
        else
        {
            passProfile.setError(null);
        }

        //validasi email
        if(email.isEmpty()){
            emailProfile.setError("Email belum diisi");
            valid = false;
        }
        else if(!isValidEmail(email))
        {
            emailProfile.setError("Invalid Email");
            valid = false;
        }
        else
        {
            emailProfile.setError(null);
        }
        return valid;
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, AdminProfileEditActivity.class);
        context.startActivity(starter);
    }

}
