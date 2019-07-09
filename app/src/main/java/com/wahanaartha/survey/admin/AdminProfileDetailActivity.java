package com.wahanaartha.survey.admin;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wahanaartha.survey.R;
import com.wahanaartha.survey.model.API;
import com.wahanaartha.survey.model.DetailUser;
import com.wahanaartha.survey.model.GroupModel;
import com.wahanaartha.survey.model.LoginUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.wahanaartha.survey.R.id.add;
import static com.wahanaartha.survey.R.id.dealerName;
import static com.wahanaartha.survey.R.id.email_profile;
import static com.wahanaartha.survey.R.id.name_profile;
import static com.wahanaartha.survey.R.id.pass_profile;
import static com.wahanaartha.survey.R.id.password;
import static com.wahanaartha.survey.R.id.recyclerView;
import static com.wahanaartha.survey.R.id.toolbar;
import static com.wahanaartha.survey.R.id.username;
import static com.wahanaartha.survey.R.id.username_profile;
import static com.wahanaartha.survey.R.id.view_container;
import static com.wahanaartha.survey.admin.AdminProfileFragment.DATA_USER;

/**
 * Created by Nina on 10/10/2017.
 */


public class AdminProfileDetailActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_ADD = 88;
    public static final String EDIT_USER = "edit_user";
    public ArrayList<LoginUser> data;
    @BindView(R.id.username_profile) EditText usernameProfile;
    @BindView(R.id.pass_profile) EditText passProfile;
    @BindView(R.id.gruop_user) EditText groupUser;
    @BindView(R.id.email_profile) EditText emailProfile;
    @BindView(R.id.dealer_name) EditText dealerName;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipe;
//    @BindView(R.id.phone_profile) EditText phoneProfile;
//    @BindView(R.id.address_profile) EditText addressProfile;

    String editUser;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_detail_profile);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getTitle());

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        editUser = getIntent().getStringExtra(DATA_USER);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminProfileDetailActivity.this, AdminProfileEditActivity.class);
                intent.putExtra(EDIT_USER,editUser);
                startActivity(intent);
            }
        });

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getProfile();
            }
        });

        getProfile();
    }


    void getProfile(){
        API.getDetailUser(getIntent().getStringExtra(DATA_USER)).enqueue(new Callback<DetailUser>() {
            @Override
            public void onResponse(Call<DetailUser> call, Response<DetailUser> response) {
                Log.i("DETAIL", "onResponse: "+response.body());
                DetailUser detailUser = response.body();
                usernameProfile.setText(detailUser.getUsername());
                passProfile.setText(detailUser.getPassword());
                groupUser.setText(detailUser.getTitle());
                emailProfile.setText(detailUser.getEmail());
                dealerName.setText(detailUser.getDealer_name());
//                phoneProfile.setText(detailUser.getNoHp());
//                addressProfile.setText(detailUser.getAddress());
                swipe.setRefreshing(false);
            }


            @Override
            public void onFailure(Call<DetailUser>call, Throwable t) {
                swipe.setRefreshing(false);
            }
        });
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, AdminProfileDetailActivity.class);
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
