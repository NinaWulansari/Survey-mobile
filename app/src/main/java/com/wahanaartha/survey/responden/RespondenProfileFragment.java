package com.wahanaartha.survey.responden;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wahanaartha.survey.R;
import com.wahanaartha.survey.model.API;
import com.wahanaartha.survey.model.LoginUser;
import com.wahanaartha.survey.model.MyProfile;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.wahanaartha.survey.LoginActivity.MY_LOGIN_PREF;
import static com.wahanaartha.survey.LoginActivity.MY_LOGIN_PREF_KEY;

/**
 * Created by Ratri on 18/10/2017.
 */

public class RespondenProfileFragment extends Fragment {

    public static final String ID = "id";
    public List<LoginUser> datas;

    String id;
    public RespondenProfileFragment(){

    }

    @BindView(R.id.name_profile)
    TextView name_profile;
    @BindView(R.id.username_profile)
    TextView username_profile;
    @BindView(R.id.group_user)
    TextView group_user;
    @BindView(R.id.email_profile)
    TextView email_profile;
    @BindView(R.id.dealer_name)
    TextView dealer_name;


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.responden_fragment_profile, container, false);
        ButterKnife.bind(this, view);

        getData();

        return view;

    }

    public void getData(){
        LoginUser savedUser = new Gson().fromJson(getActivity().getSharedPreferences(MY_LOGIN_PREF, Context.MODE_PRIVATE).getString(MY_LOGIN_PREF_KEY, ""), LoginUser.class);

        id =  savedUser.getId();

        API.getMyProfile(id).enqueue(new Callback<MyProfile>() {
            @Override
            public void onResponse(Call<MyProfile> call, Response<MyProfile> response) {
                Log.i("Get Data", "onResponse:" + response);
                if (response.code() == 200) {

                    MyProfile profile = response.body();
                    name_profile.setText(profile.getName());
                    username_profile.setText(profile.getUsername());
                    group_user.setText(profile.getGroup_user());
                    email_profile.setText(profile.getEmail());
                    dealer_name.setText(profile.getDealer_name());
                }else{
                    Toast.makeText(getActivity(),"There was an error occured. Please check your internet connection", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MyProfile> call, Throwable t) {
                Toast.makeText(getActivity(),"There was an error occured. Please check your internet connection", Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnClick(R.id.button_change_pass)
    void changePassword() {

        Intent intent = new Intent(getActivity(), RespondenChangePassword.class);
        intent.putExtra(ID,id);
        startActivity(intent);
    }
}
