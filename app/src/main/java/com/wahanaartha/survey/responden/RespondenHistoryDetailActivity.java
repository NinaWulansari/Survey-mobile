package com.wahanaartha.survey.responden;

import com.google.gson.Gson;
import com.wahanaartha.survey.model.API;
import com.wahanaartha.survey.R;
import com.wahanaartha.survey.model.HistoryDetailResponden;
import com.wahanaartha.survey.model.HistoryQuestionAnswer;
import com.wahanaartha.survey.model.LoginUser;

import static com.wahanaartha.survey.LoginActivity.MY_LOGIN_PREF;
import static com.wahanaartha.survey.LoginActivity.MY_LOGIN_PREF_KEY;
import static com.wahanaartha.survey.admin.AdminAddSurveyQuestionActivity.QuestionType.*;
import static com.wahanaartha.survey.responden.RespondenHistoryIndexFragment.ID;

import android.content.Context;
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
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RespondenHistoryDetailActivity extends AppCompatActivity {

    @BindView(R.id.date_textview) TextView dateTextview;
    @BindView(R.id.survey_textview) TextView surveyTextview;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    public static final String RESPONDEN_ID = "respondenId";

    HistoryDetailResponden responden;
    List<HistoryQuestionAnswer> selectedAnswer;

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
        LoginUser savedUser = new Gson().fromJson(RespondenHistoryDetailActivity.this.getSharedPreferences(MY_LOGIN_PREF, Context.MODE_PRIVATE).getString(MY_LOGIN_PREF_KEY, ""), LoginUser.class);
        String responden_id = savedUser.getId();

        API.getHistoryIndexDetail(getIntent().getStringExtra(ID),responden_id).enqueue(new Callback<HistoryDetailResponden>() {
            @Override
            public void onResponse(Call<HistoryDetailResponden> call, Response<HistoryDetailResponden> response) {
                if (response.code() == 200) {
                    responden = response.body();
                    String date = responden.getCreatedAt();

                    DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
                    DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    inputFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                    try {
                        Date dap = inputFormat.parse(date);
                        String outputText = outputFormat.format(dap);
                        dateTextview.setText(outputText);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    surveyTextview.setText(responden.getTitle());
                    selectedAnswer = responden.getQuestions();

                    recyclerView.setAdapter(new RespondenQuestionAdapter());
                } else {
                    Toast.makeText(RespondenHistoryDetailActivity.this, "There was an error occured. Please check your internet connection", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<HistoryDetailResponden> call, Throwable t) {
                Toast.makeText(RespondenHistoryDetailActivity.this, "There was an error occured. Please check your internet connection", Toast.LENGTH_LONG).show();
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

    public class RespondenQuestionAdapter extends RecyclerView.Adapter<RespondenQuestionAdapter.RespondenQuestionViewHolder> {

        @Override
        public RespondenQuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.responden_activity_history_detail_row, parent, false);

            return new RespondenQuestionViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RespondenQuestionViewHolder holder, int position) {
            final HistoryQuestionAnswer question = selectedAnswer.get(position);

            holder.questionTextView.setText(question.getTitle());
            holder.answerRecyclerView.setAdapter(new RecyclerView.Adapter<AnswerViewHolder>() {

                String[] answers;
                @Override
                public AnswerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.responden_activity_history_detail_row_field, parent, false);

                    return new AnswerViewHolder(itemView);
                }

                @Override
                public void onBindViewHolder(AnswerViewHolder holder, int position) {
                    holder.answerTextView.setText(question.getAnswers().getTitle());
                    if (answers != null)
                        holder.answerTextView.setText(answers[position]);
                }

                @Override
                public int getItemCount() {
                    if (question.getType().equals(CheckList.toString())) {
                        answers = question.getAnswers().getTitle().split("%");
                        return answers.length;
                    }
                    return 1;
                }
            });
            holder.answerRecyclerView.setLayoutManager(new LinearLayoutManager(RespondenHistoryDetailActivity.this));
        }

        @Override
        public int getItemCount() {
            return selectedAnswer.size();
        }


        class RespondenQuestionViewHolder extends RecyclerView.ViewHolder {

            TextView questionTextView;
            RecyclerView answerRecyclerView;

            RespondenQuestionViewHolder(View itemView) {
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
