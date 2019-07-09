package com.wahanaartha.survey.admin;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.wahanaartha.survey.R;
import com.wahanaartha.survey.model.API;
import com.wahanaartha.survey.model.DataDealer;
import com.wahanaartha.survey.model.DataUser;
import com.wahanaartha.survey.model.GroupModel;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.wahanaartha.survey.admin.AdminProfileFragment.DATA_USER;


/**
 * Created by Nina on 10/17/2017.
 */

public class AdminProfileAddUser extends AppCompatActivity {
    @BindView(R.id.dealerName) Spinner spinner;
    @BindView(R.id.input_name) EditText nameProfile;
    @BindView(R.id.input_username) EditText usernameProfile;
    @BindView(R.id.input_password) EditText passProfile;
    @BindView(R.id.input_email) EditText emailProfile;
    @BindView(R.id.groupUser) Spinner spinnerGroup;
    ArrayList<DataDealer> data;
    ArrayList<GroupModel> group;
    String tambah;
    String titleUser;
    Toolbar toolbar;
    @BindView(R.id.input_layout_name) TextInputLayout inputLayoutName;
    @BindView(R.id.input_layout_username) TextInputLayout inputLayoutUsername;
    @BindView(R.id.input_layout_password) TextInputLayout inputLayoutPassword;
    @BindView(R.id.input_layout_email) TextInputLayout inputLayoutEmail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_profile_user);
        ButterKnife.bind(this);

        API.getDealer().enqueue(new Callback<ArrayList<DataDealer>>() {

            @Override
            public void onResponse(Call<ArrayList<DataDealer>> call, Response<ArrayList<DataDealer>> response) {
                if (response.code() == 200) {
                    data = response.body();
                    String[] values = new String[data.size()];
                    for (int i = 0; i < data.size(); i++) {
                        String title = data.get(i).getDealer_name();
//                        String id_dealer = data.get(i).getDealer_id();
                        values[i] = title;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(AdminProfileAddUser.this, R.layout.spinner_custom, values);
                    adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    spinner.setAdapter(adapter);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent,
                                                   View view, int pos, long id) {
                            String name_promotion_selected = parent.getItemAtPosition(pos).toString();
                            //TODO REMOVE
                            tambah = data.get(pos).getDealer_id();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<ArrayList<DataDealer>> call, Throwable throwable) {
                Toast.makeText(AdminProfileAddUser.this, "Cek koneksi internet", Toast.LENGTH_SHORT).show();
            }
        });

        API.getGroup().enqueue(new Callback<ArrayList<GroupModel>>() {
            @Override
            public void onResponse(Call<ArrayList<GroupModel>> call, Response<ArrayList<GroupModel>> response) {
                if (response.code() == 200) {
                    group = response.body();
                    String[] values = new String[group.size()];
                    for (int i = 0; i < group.size(); i++) {
                        String groupUser = group.get(i).getTitle();
                        values[i] = groupUser;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(AdminProfileAddUser.this, R.layout.spinner_custom, values);
                    adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    spinnerGroup.setAdapter(adapter);

                    spinnerGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent,
                                                   View view, int pos, long id) {
                            String name_promotion_selected = parent.getItemAtPosition(pos).toString();
                            //TODO REMOVE
                            titleUser = group.get(pos).getId();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ArrayList<GroupModel>> call, Throwable t) {
                Toast.makeText(AdminProfileAddUser.this, "Cek koneksi internet", Toast.LENGTH_SHORT).show();
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getTitle());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    @OnClick(R.id.add_user)
    void doAdd() {
        doAddUser();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void doAddUser(){
        if(isValid())
        {
            DataUser dataUser = new DataUser();
            dataUser.setName(nameProfile.getText().toString());
            dataUser.setUsername(usernameProfile.getText().toString());
            dataUser.setPassword(passProfile.getText().toString());
            dataUser.setEmail(emailProfile.getText().toString());
            dataUser.setDealerId(tambah);
            dataUser.setGroupId(titleUser);

            API.adduser(dataUser).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.i("TAMBAH", "onResponse: " + response);
                    if (response.code() == 200) {
                        Toast.makeText(AdminProfileAddUser.this, "success", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AdminProfileAddUser.this, "failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(AdminProfileAddUser.this, "cek connection", Toast.LENGTH_SHORT).show();
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
}
