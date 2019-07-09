package com.wahanaartha.survey.responden;


import android.content.Context;
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

import com.google.gson.Gson;
import com.wahanaartha.survey.DividerItemDecoration;
import com.wahanaartha.survey.RecyclerItemClickListener;
import com.wahanaartha.survey.model.API;
import com.wahanaartha.survey.R;
import com.wahanaartha.survey.model.HistoryIndexResponden;
import com.wahanaartha.survey.model.LoginUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.wahanaartha.survey.LoginActivity.MY_LOGIN_PREF;
import static com.wahanaartha.survey.LoginActivity.MY_LOGIN_PREF_KEY;


/**
 * A simple {@link Fragment} subclass.
 */
public class RespondenHistoryIndexFragment extends Fragment {
    public static final String ID = "id";

    public RespondenHistoryIndexFragment() {
    }

    public List<HistoryIndexResponden> datas;
    public List<HistoryIndexResponden> tempDatas;
    public EditText searchEditext;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.responden_fragment_history, container, false);
        ButterKnife.bind(this, view);

        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        refreshingData();
        searchEditext = ((RespondenActivity)getActivity()).searchEdittext;
        searchEditext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<HistoryIndexResponden> surveys = new ArrayList<>();
                if(tempDatas != null){
                    for (HistoryIndexResponden survey: datas) {
                        String dat = s.toString().toLowerCase();
                        if (survey.getTitle().toLowerCase().contains(dat)) {
                            surveys.add(survey);
                        }
                    }
                    tempDatas = surveys;
                    recyclerView.setAdapter(new RespondenHistoryAdapter(tempDatas));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(),RespondenHistoryDetailActivity.class);
                intent.putExtra(ID, tempDatas.get(position).getId());
                startActivity(intent);
            }
        }));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshingData();
            }
        });
        return view;
    }

    private void refreshingData() {

        LoginUser savedUser = new Gson().fromJson(getActivity().getSharedPreferences(MY_LOGIN_PREF, Context.MODE_PRIVATE).getString(MY_LOGIN_PREF_KEY, ""), LoginUser.class);

        String group_name =  savedUser.getTitle();
        String id = savedUser.getId();

        API.getHistoryIndex(group_name,id).enqueue(new Callback<ArrayList<HistoryIndexResponden>>() {
            @Override
            public void onResponse(Call<ArrayList<HistoryIndexResponden>> call, Response<ArrayList<HistoryIndexResponden>> response) {
                Log.i("Wahana App", "onResponse:" + response);
                if (response.code() == 200) {
                    datas = response.body();
                    tempDatas = datas;
                    Log.i("wahanaapp", "onResponse: " + datas);
                    recyclerView.setAdapter(new RespondenHistoryAdapter(datas));
                } else {
                    Toast.makeText(getActivity(),"Data Kosong", Toast.LENGTH_LONG).show();
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ArrayList<HistoryIndexResponden>> call, Throwable throwable) {
                Toast.makeText(getActivity(),"Mohon Cek Koneksi Internet Anda", Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    public static class RespondenHistoryAdapter extends RecyclerView.Adapter<RespondenHistoryAdapter.RespondenViewHolder> {

        List<HistoryIndexResponden> dataSet;

        public RespondenHistoryAdapter(List<HistoryIndexResponden> data) {
            this.dataSet = data;
        }

        @Override
        public RespondenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.responden_fragment_history_row, parent, false);

            return new RespondenViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RespondenViewHolder holder, int position) {

            HistoryIndexResponden survey = dataSet.get(position);

            holder.textView.setText(survey.getTitle());
            holder.jmlTextView.setText(survey.getJml() + " Questions");
        }

        @Override
        public int getItemCount() {
            if(dataSet == null){
                return 0;
            }else{
                return dataSet.size();
            }
        }

        public class RespondenViewHolder extends RecyclerView.ViewHolder {

            public TextView textView;
            public TextView jmlTextView;

            public RespondenViewHolder(View itemView) {
                super(itemView);
                textView = (TextView)itemView.findViewById(R.id.text_view);
                jmlTextView = (TextView) itemView.findViewById(R.id.jml);
            }
        }
    }
}
