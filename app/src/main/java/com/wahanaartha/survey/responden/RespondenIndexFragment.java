package com.wahanaartha.survey.responden;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.wahanaartha.survey.admin.AdminActivity;
import com.wahanaartha.survey.admin.AdminIndexFragment;
import com.wahanaartha.survey.model.API;
import com.wahanaartha.survey.R;
import com.wahanaartha.survey.model.IndexSurveyResponden;
import com.wahanaartha.survey.model.LoginUser;
import com.wahanaartha.survey.model.Survey;

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
public class RespondenIndexFragment extends Fragment {
    public static final String SURVEY_KEY = "survey";
    public TextView textView;
    public List<IndexSurveyResponden> tempDatas;
    public EditText searchEditext;
    public RespondenIndexFragment() {
    }

    public List<IndexSurveyResponden> datas;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.responden_fragment_index, container, false);
        ButterKnife.bind(this, view);

        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        searchEditext = ((RespondenActivity)getActivity()).searchEdittext;
        searchEditext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<IndexSurveyResponden> surveys = new ArrayList<>();
                if(tempDatas != null){
                    for (IndexSurveyResponden survey: datas) {
                        String dat = s.toString().toLowerCase();
                        if (survey.getTitle().toLowerCase().contains(dat)) {
                            surveys.add(survey);
                        }
                    }
                    tempDatas = surveys;
                    recyclerView.setAdapter(new RespondenAdapter(tempDatas));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(),RespondenSurveyActivity.class);
                intent.putExtra(SURVEY_KEY, tempDatas.get(position).getId());
                startActivity(intent);
            }
        }));
        refreshingData();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshingData();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        refreshingData();
        super.onResume();
    }

    private void refreshingData() {

        LoginUser savedUser = new Gson().fromJson(getActivity().getSharedPreferences(MY_LOGIN_PREF, Context.MODE_PRIVATE).getString(MY_LOGIN_PREF_KEY, ""), LoginUser.class);

       String group_name =  savedUser.getTitle();
        String id = savedUser.getId();

       API.getIndexSurveyResponden(group_name,id).enqueue(new Callback<ArrayList<IndexSurveyResponden>>() {
           @Override
           public void onResponse(Call<ArrayList<IndexSurveyResponden>> call, Response<ArrayList<IndexSurveyResponden>> response) {
               Log.i("Wahana App", "onResponse:" + response);
               if (response.code() == 200) {
               } else {
                   Toast.makeText(getActivity(),"Data Kosong", Toast.LENGTH_LONG).show();
               }
               datas = response.body();
               tempDatas = datas;
               Log.i("wahanaapp", "onResponse: " + datas);
               recyclerView.setAdapter(new RespondenAdapter(datas));
               swipeRefreshLayout.setRefreshing(false);
           }

           @Override
           public void onFailure(Call<ArrayList<IndexSurveyResponden>> call, Throwable throwable) {
               Toast.makeText(getActivity(),"Data Kosong", Toast.LENGTH_LONG).show();
               swipeRefreshLayout.setRefreshing(false);
           }
       });
    }


    public static class RespondenAdapter extends RecyclerView.Adapter<RespondenAdapter.RespondenViewHolder> {

        List<IndexSurveyResponden> dataSet;

        public RespondenAdapter(List<IndexSurveyResponden> data) {
            this.dataSet = data;
        }

        @Override
        public RespondenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.responden_fragment_index_row, parent, false);

            return new RespondenViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RespondenViewHolder holder, int position) {

            IndexSurveyResponden survey = dataSet.get(position);

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
