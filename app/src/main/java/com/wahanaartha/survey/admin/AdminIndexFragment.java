package com.wahanaartha.survey.admin;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wahanaartha.survey.DividerItemDecoration;
import com.wahanaartha.survey.RecyclerItemClickListener;
import com.wahanaartha.survey.model.API;
import com.wahanaartha.survey.R;
import com.wahanaartha.survey.model.Survey;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminIndexFragment extends Fragment {
    public static final String SURVEY_KEY = "survey";
    public TextView textView;
    public AdminIndexFragment() {
    }

    public List<Survey> datas;
    public List<Survey> tempDatas;
    public EditText searchEditext;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.admin_fragment_index, container, false);
        ButterKnife.bind(this, view);

        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(),AdminIndexDetailActivity.class);
                intent.putExtra(SURVEY_KEY, datas.get(position).getId());
                startActivity(intent);
            }
        }));
        searchEditext = ((AdminActivity)getActivity()).searchEdittext;
        searchEditext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<Survey> surveys = new ArrayList<>();
                for (Survey survey: datas) {
                    String dat = s.toString().toLowerCase();
                    if (survey.getTitle().toLowerCase().contains(dat)) {
                        surveys.add(survey);
                    }
                }
                tempDatas = surveys;
                recyclerView.setAdapter(new AdminIndexAdapter(tempDatas));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        refreshingData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshingData();
            }
        });


        return view;
    }

    private void refreshingData() {

       API.getIndexAdmin().enqueue(new Callback<ArrayList<Survey>>() {

           @Override
           public void onResponse(Call<ArrayList<Survey>> call, Response<ArrayList<Survey>> response) {
               if (response.code() == 200) {
                   datas = response.body();
                   tempDatas = datas;
                   Log.i("wahanaapp", "onResponse: " + datas);
                   recyclerView.setAdapter(new AdminIndexAdapter(datas));
               } else {

                   Toast.makeText(getActivity(),"Data Kosong", Toast.LENGTH_LONG).show();
               }
               swipeRefreshLayout.setRefreshing(false);
           }

           @Override
           public void onFailure(Call<ArrayList<Survey>> call, Throwable throwable) {
               Toast.makeText(getActivity(),"Mohon Cek Koneksi Internet Anda", Toast.LENGTH_LONG).show();
               swipeRefreshLayout.setRefreshing(false);
           }
       });
    }


    public static class AdminIndexAdapter extends RecyclerView.Adapter<AdminIndexAdapter.AdminViewHolder> {

        List<Survey> dataSet;

        public AdminIndexAdapter(List<Survey> data) {
            this.dataSet = data;
        }

        @Override
        public AdminViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.responden_fragment_index_row, parent, false);

            return new AdminViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(AdminViewHolder holder, int position) {

            Survey survey = dataSet.get(position);

            holder.textView.setText(survey.getTitle());
            holder.jmlTextView.setText(survey.getJml() + " Questions");
        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }

        public class AdminViewHolder extends RecyclerView.ViewHolder {

            public TextView textView;
            public TextView jmlTextView;

            public AdminViewHolder(View itemView) {
                super(itemView);
                textView = (TextView)itemView.findViewById(R.id.text_view);
                jmlTextView = (TextView) itemView.findViewById(R.id.jml);
            }
        }
    }
}
