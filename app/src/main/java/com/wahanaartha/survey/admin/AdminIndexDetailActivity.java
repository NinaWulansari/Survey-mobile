package com.wahanaartha.survey.admin;

import com.wahanaartha.survey.model.API;
import com.wahanaartha.survey.R;
import com.wahanaartha.survey.model.IndexAdminDetailQuestion;
import com.wahanaartha.survey.model.IndexAdminSurveyDetail;

import static com.wahanaartha.survey.admin.AdminIndexFragment.SURVEY_KEY;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdminIndexDetailActivity extends AppCompatActivity {

    @BindView(R.id.date_textview) TextView dateTextview;
    @BindView(R.id.survey_textview) TextView surveyTextview;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    public static final String RESPONDEN_ID = "respondenId";

    IndexAdminSurveyDetail surveys;
    List<IndexAdminDetailQuestion> selectedAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.responden_activity_history_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        refreshingData();
    }

    private void refreshingData() {

        API.getIndexAdminDetail(getIntent().getStringExtra(SURVEY_KEY)).enqueue(new Callback<IndexAdminSurveyDetail>() {
            @Override
            public void onResponse(Call<IndexAdminSurveyDetail> call, Response<IndexAdminSurveyDetail> response) {
                if (response.code() == 200) {
                    surveys = response.body();
                    String date = surveys.getCreatedAt();

                    DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                    DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");

                    String inputText = "2012-11-17T00:00:00.000-05:00";
                    try {
                        Date dap = inputFormat.parse(date);
                        String outputText = outputFormat.format(dap);
                        dateTextview.setText(outputText);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    surveyTextview.setText(surveys.getTitle());
                    selectedAnswer = surveys.getQuestions();
                    recyclerView.setAdapter(new SurveyQuestionAdapter());
                } else {
                    Toast.makeText(AdminIndexDetailActivity.this, "There was an error occured. Please check your internet connection", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<IndexAdminSurveyDetail> call, Throwable t) {
                Toast.makeText(AdminIndexDetailActivity.this, "There was an error occured. Please check your internet connection", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public class SurveyQuestionAdapter extends RecyclerView.Adapter<SurveyQuestionAdapter.SurveyQuestionViewHolder> {

        @Override
        public SurveyQuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_activity_index_detail_row, parent, false);

            return new SurveyQuestionViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(SurveyQuestionViewHolder holder, int position) {
            final IndexAdminDetailQuestion question = selectedAnswer.get(position);

            holder.questionTextView.setText(question.getTitle());
            if (question.getAnswers() != null) {
                holder.answerRecyclerView.setAdapter(new RecyclerView.Adapter<AnswerViewHolder>() {

                    @Override
                    public AnswerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_index_row_field, parent, false);
                        return new AnswerViewHolder(itemView);
                    }

                    @Override
                    public void onBindViewHolder(AnswerViewHolder holder, int position) {
                        holder.answerTextView.setText(question.getAnswers().get(position).getTitle());
                    }

                    @Override
                    public int getItemCount() {
                        return question.getAnswers().size();
                    }

                });
                holder.answerRecyclerView.setLayoutManager(new LinearLayoutManager(AdminIndexDetailActivity.this));
            }
        }

        @Override
        public int getItemCount() {
            return selectedAnswer.size();
        }

        class SurveyQuestionViewHolder extends RecyclerView.ViewHolder {

            TextView questionTextView;
            RecyclerView answerRecyclerView;

            SurveyQuestionViewHolder(View itemView) {
                super(itemView);
                questionTextView = (TextView)itemView.findViewById(R.id.question_textview);
                answerRecyclerView = (RecyclerView) itemView.findViewById(R.id.answer_recyclerview);
            }
        }

        class AnswerViewHolder extends RecyclerView.ViewHolder {

            TextView answerTextView;

            private AnswerViewHolder(View itemView) {
                super(itemView);
                answerTextView = (TextView)itemView.findViewById(R.id.textView);
            }
        }
    }

}
