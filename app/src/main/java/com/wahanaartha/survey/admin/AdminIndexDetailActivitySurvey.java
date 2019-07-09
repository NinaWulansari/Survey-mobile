package com.wahanaartha.survey.admin;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
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
import android.widget.TextView;
import android.widget.Toast;

import com.wahanaartha.survey.DividerItemDecoration;
import com.wahanaartha.survey.model.API;
import com.wahanaartha.survey.model.DetailAdminAnswer;
import com.wahanaartha.survey.model.DetailAdminIndex;
import com.wahanaartha.survey.model.DetailAdminQuestion;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.wahanaartha.survey.R;


import static com.wahanaartha.survey.admin.AdminAddSurveyQuestionActivity.QuestionType.CheckList;
import static com.wahanaartha.survey.admin.AdminAddSurveyQuestionActivity.QuestionType.MultipleChoice;
import static com.wahanaartha.survey.admin.AdminAddSurveyQuestionActivity.QuestionType.SingleTextBox;
import static com.wahanaartha.survey.admin.AdminIndexFragmentSurvey.ID;


/**
 * Created by User on 10/9/2017.
 */

public class AdminIndexDetailActivitySurvey extends AppCompatActivity {

    @BindView(R.id.recycler_view_index) RecyclerView recyclerView;
    @BindView(R.id.textview_title_detail) TextView titleDetail;
    @BindView(R.id.textview_create_at) TextView createdAtDetail;
    @BindView(R.id.textview_sendto) TextView sendTo;

    Toolbar toolbar;

    DetailAdminIndex indexModel;
    List<DetailAdminQuestion> questionModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_index_detail_survey);

        ButterKnife.bind(this);


        toolbar = (Toolbar) findViewById(R.id.toolbar_index_detail);
        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        recyclerView.addItemDecoration(new DividerItemDecoration(AdminIndexDetailActivitySurvey.this, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        refreshingData();



    }

    private void refreshingData(){
        API.getIndexDetail(getIntent().getStringExtra(ID)).enqueue(new Callback<DetailAdminIndex>() {
            @Override
            public void onResponse(Call<DetailAdminIndex> call, Response<DetailAdminIndex> response) {
                Log.i("index survey", "onResponse: " + response);

                if(response.code()==200){
                    indexModel = response.body();

                    titleDetail.setText(indexModel.getTitle());
                    sendTo.setText(indexModel.getSendto());

                    String date = indexModel.getCreatedAt();
                    DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
                    DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    inputFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                    try {
                        Date dates = inputFormat.parse(date);
                        String outputText = outputFormat.format(dates);
                        createdAtDetail.setText(outputText+ " ");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    questionModel = indexModel.getQuestions();
                    recyclerView.setAdapter(new IndexDetailAdapter());

                }else{
                    Toast.makeText(AdminIndexDetailActivitySurvey.this, "..", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<DetailAdminIndex> call, Throwable t) {
                Toast.makeText(AdminIndexDetailActivitySurvey.this, "gagal", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private class IndexDetailAdapter extends RecyclerView.Adapter<IndexDetailAdapter.IndexDetailViewHolder> {


        @Override
        public IndexDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
           View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_activity_index_detail_row_survey, parent, false);

            return new IndexDetailViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(IndexDetailViewHolder holder, final int position) {
            final DetailAdminQuestion question = questionModel.get(position);

            holder.questionTextView.setText(question.getTitle());
            holder.typeTextView.setText(question.getType());

            if(question.getAnswers()!=null) {
                holder.answerRecyclerView.setAdapter(new RecyclerView.Adapter<AnswerViewHolder>() {
                    @Override
                    public AnswerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_activity_index_detail_row_answer_survey, parent, false);
                        return new AnswerViewHolder(itemView);
                    }

                    @Override
                    public void onBindViewHolder(AnswerViewHolder holder, int position) {
                            holder.answerTextView.setText(question.getAnswers().get(position).getTitle());
                    }

                    @Override
                    public int getItemCount(){
                        return question.getAnswers().size();
                    }
                });
                holder.answerRecyclerView.setLayoutManager(new LinearLayoutManager(AdminIndexDetailActivitySurvey.this));
            }else{
                holder.answerRecyclerView.setVisibility(View.GONE);
            }

        }

        @Override
        public int getItemCount() {
            if(questionModel == null){
                return 0;
            }else{
                return questionModel.size();
            }
        }

        class IndexDetailViewHolder extends RecyclerView.ViewHolder {

            TextView questionTextView;
            TextView  typeTextView;
            RecyclerView answerRecyclerView;

            public IndexDetailViewHolder(View itemView) {
                super(itemView);

                typeTextView = itemView.findViewById(R.id.textview_question_type);
                questionTextView = itemView.findViewById(R.id.textview_question);
                answerRecyclerView = itemView.findViewById(R.id.recycler_view_answer);
            }
        }

        class AnswerViewHolder extends RecyclerView.ViewHolder{

            TextView answerTextView;

            public AnswerViewHolder(View itemView) {
                super(itemView);

                answerTextView = itemView.findViewById(R.id.textview_answer);
            }
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}

