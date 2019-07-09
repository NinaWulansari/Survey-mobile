package com.wahanaartha.survey.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.wahanaartha.survey.DividerItemDecoration;
import com.wahanaartha.survey.R;
import com.wahanaartha.survey.RecyclerItemClickListener;
import com.wahanaartha.survey.model.API;
import com.wahanaartha.survey.model.DetailAdminIndex;
import com.wahanaartha.survey.model.IndexAdmin;
import com.wahanaartha.survey.model.Survey;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.wahanaartha.survey.R.id.swipeRefreshLayout;

/**
 * Created by wahana on 10/3/17.
 */

public class AdminIndexFragmentSurvey extends Fragment{


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;

    public static final String ID = "id";

    public List<IndexAdmin> datas;
    public List<IndexAdmin> tempDatas;
    public EditText searchEditext;

    private AdminIndexAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.admin_fragment_index_survey, container, false);
        ButterKnife.bind(this, view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        searchEditext = ((AdminActivity)getActivity()).searchEdittext;
        searchEditext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<IndexAdmin> surveys = new ArrayList<>();
                if(tempDatas != null) {
                    for (IndexAdmin survey : datas) {
                        String dat = s.toString().toLowerCase();
                        if (survey.getTitle().toLowerCase().contains(dat)) {
                            surveys.add(survey);
                        }
                    }
                    tempDatas = surveys;
                    recyclerView.setAdapter(new AdminIndexAdapter(tempDatas));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(),AdminIndexDetailActivitySurvey.class);
                intent.putExtra(ID, tempDatas.get(position).getId());
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

    private void refreshingData() {

        API.getIndexsurvey().enqueue(new Callback<ArrayList<IndexAdmin>>() {
            @Override
            public void onResponse(Call<ArrayList<IndexAdmin>> call, Response<ArrayList<IndexAdmin>> response) {
                Log.i("Wahana App", "onResponse:" + response);

                if (response.code() == 200) {
                    datas = response.body();
                    tempDatas = datas;
                    Log.i("wahanaapp", "onResponse: " + datas);
                    recyclerView.setAdapter(new AdminIndexAdapter(datas));
                } else {

                    Toast.makeText(getActivity(),"There was an error occured. Please check your internet connection", Toast.LENGTH_LONG).show();
                }
                swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<ArrayList<IndexAdmin>> call, Throwable throwable) {
                Toast.makeText(getActivity(),"There was an error occured. Please check your internet connection", Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }



    public static class AdminIndexAdapter extends RecyclerView.Adapter<AdminIndexAdapter.AdminIndexViewHolder>{


        public final List<IndexAdmin> dataSet;
        public List<IndexAdmin> dataSetFilter;

        public AdminIndexAdapter(List<IndexAdmin> data) {
            this.dataSet = data;
            this.dataSetFilter = data;
        }


        @Override
        public AdminIndexViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.admin_index_row_survey, parent, false);

            return new AdminIndexViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(AdminIndexViewHolder holder, int position) {
            holder.textView.setText(dataSet.get(position).getTitle());
            holder.questionTotalTextview.setText(dataSet.get(position).getJml()+ " Questions");
        }

        @Override
        public int getItemCount()
        {
            return dataSet.size();
        }



        static class AdminIndexViewHolder extends RecyclerView.ViewHolder {

            public TextView textView;
            public TextView questionTotalTextview;

            public AdminIndexViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.text_view);
                itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                        contextMenu.add(0, getAdapterPosition(), 0, "Delete");



                    }
                });

                questionTotalTextview = itemView.findViewById(R.id.survey_title);
                questionTotalTextview.setVisibility(View.VISIBLE);

            }
        }
    }

}