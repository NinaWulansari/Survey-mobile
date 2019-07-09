package com.wahanaartha.survey.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wahanaartha.survey.R;
import com.wahanaartha.survey.RecyclerItemClickListener;
import com.wahanaartha.survey.model.API;
import com.wahanaartha.survey.model.IndexSurveyResponden;
import com.wahanaartha.survey.model.LoginUser;
import com.wahanaartha.survey.responden.RespondenActivity;
import com.wahanaartha.survey.responden.RespondenIndexFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminProfileFragment extends Fragment {
    public List<LoginUser> datas;
    public List<LoginUser> tempDatas;
    public EditText searchEditext;

    public static final String DATA_USER = "user";
    public static final int ADD_USER = 1;

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipe;

    ArrayList<LoginUser> mListAll = new ArrayList<>();
    boolean isFiltered;
    ArrayList<Integer> mListMapFilter = new ArrayList<>();
    String mQuery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.admin_fragment_profile, container, false);

        ButterKnife.bind(this, view);

        getData();

        searchEditext = ((AdminActivity)getActivity()).searchEdittext;
        searchEditext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<LoginUser> surveys = new ArrayList<>();
                if(tempDatas != null){
                    for (LoginUser survey: datas) {
                        String dat = s.toString().toLowerCase();
                        if (survey.getName().toLowerCase().contains(dat)) {
                            surveys.add(survey);
                        }
                    }
                    tempDatas = surveys;
                    recyclerView.setAdapter(new UserAdapter(tempDatas));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(),AdminProfileDetailActivity.class);
                intent.putExtra(DATA_USER, tempDatas.get(position).getId());
                startActivity(intent);
            }
        }));

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        return view;
    }

    @OnClick(R.id.fab)
    void goAddUser(){
        goAdd();
    }

    void goAdd(){

        Intent intent = new Intent(getActivity(), AdminProfileAddUser.class);
        startActivityForResult(intent, ADD_USER);
    }

    public void getData(){
        API.getDataUser().enqueue(new Callback<ArrayList<LoginUser>>() {
            @Override
            public void onResponse(Call<ArrayList<LoginUser>> call, Response<ArrayList<LoginUser>> response) {
                if (response.code()==200){
                    datas = response.body();
                    tempDatas = datas;
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    Log.i("Profile", "onResponse: "+datas);
                    recyclerView.setAdapter(new UserAdapter(datas));

                }
                swipe.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ArrayList<LoginUser>> call, Throwable t) {
                Toast.makeText(getActivity(), "profile", Toast.LENGTH_SHORT).show();
                swipe.setRefreshing(false);
            }
        });
    }


    public static class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

        List<LoginUser> dataSet;

        public UserAdapter(List<LoginUser> data) {
            this.dataSet = data;
        }

        @Override
        public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list, parent, false);

            return new UserViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(UserViewHolder holder, int position) {

            LoginUser survey = dataSet.get(position);
            holder.textViewJudul.setText(survey.getName());
            holder.textViewDeskripsi.setText(survey.getTitle());

        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }

        public class UserViewHolder extends RecyclerView.ViewHolder {

            public TextView textViewJudul;
            public TextView textViewDeskripsi;

            public UserViewHolder(View itemView) {
                super(itemView);
                textViewJudul = (TextView)itemView.findViewById(R.id.textViewJudul);
                textViewDeskripsi = (TextView) itemView.findViewById(R.id.textViewDeskripsi);

            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

    }
}
