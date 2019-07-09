package com.wahanaartha.survey.admin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.wahanaartha.survey.R;
import com.wahanaartha.survey.model.API;
import com.wahanaartha.survey.model.Survey;

import com.wahanaartha.survey.model.Answer;
import com.wahanaartha.survey.model.Question;

import java.util.ArrayList;
import static com.wahanaartha.survey.admin.AdminAddSurveyFragment.SURVEY_TITLE;
import static com.wahanaartha.survey.admin.AdminAddSurveyFragment.SURVEY_SEND_TO;

/**
 * Created by lely
 */

public class AdminAddSurveyQuestionActivity extends AppCompatActivity{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.question_type_spinner)
    Spinner questionTypeSpinner;

    public static enum QuestionType {
        MultipleChoice("MultipleChoice",0),
        CheckList("CheckList",1),
        SingleTextBox("SingleTextBox", 2);

        QuestionType(String name, int pos) {
            this.title = name;
            this.position = pos;
        }

        String title;
        int position;

        @Override
        public String toString() {
            return this.title;
        }
    }

    private QuestionType questionType;

    String surveyTitle;
    String sendTo;

    // this array to save temporary saved question, when user save and next question this array save the current question
    ArrayList<Question> questions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_survey_question);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        surveyTitle = getIntent().getStringExtra(SURVEY_TITLE);
        sendTo = getIntent().getStringExtra(SURVEY_SEND_TO);
        questionTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectQuestionWithType(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    void selectQuestionWithType(int i) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (i) {
            case 0 :
                ft.replace(R.id.container, new AdminMultipleChoiceQuestionFragment()).commit();
                break;
            case 1 :
                ft.replace(R.id.container, new AdminCheckListQuestionFragment()).commit();
                break;
            case 2:
                ft.replace(R.id.container, new AdminSingleTextBoxQuestionFragment()).commit();
                break;
        }
    }
    @OnClick(android.R.id.home)
    void back() {
            finish();
    }

    @OnClick(R.id.save_button)
    void save() {
        if (saveCurrentQuestion()) {
            justSave();
        } else {
            new AlertDialog.Builder(this).setMessage("Do you want to save all last question").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    justSave();
                }
            });
        }
    }

    boolean saveCurrentQuestion() {
        AdminSurveyQuestionFragment fragment = (AdminSurveyQuestionFragment)
                getSupportFragmentManager().findFragmentById(R.id.container);
        boolean edited = fragment.isEdited();

        Question question = fragment.getQuestion();
        if (question != null) {
            questions.add(question);
            selectQuestionWithType(0);
            return true;
        }
        return false;
    }

    @OnClick(R.id.save_and_next_question_button)
    void saveAndNextQuestion() {
        saveCurrentQuestion();
    }

    void justSave() {
        new AsyncTask<Void, Void, Void>() {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(AdminAddSurveyQuestionActivity.this);
                progressDialog.setMessage("Saving Questions");
                progressDialog.setIndeterminate(true);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                Survey post = new Survey(surveyTitle, sendTo, questions);
//                Log.i("INFO", new Gson().toJson(post));

                API.addsurvey(post).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                        if (response.code() == 200) {
                            progressDialog.dismiss();
                            Intent intent = new Intent();
                            intent.putExtra(AdminAddSurveyFragment.IS_SURVEY_ADDED, true);
                            setResult(RESULT_OK, intent);
                            questions.clear();
                            back();
                             } else {
                            Toast.makeText(AdminAddSurveyQuestionActivity.this,
                                    "There was an error occured. Your Survey will not be saved. Make sure you have a working internet connection.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(AdminAddSurveyQuestionActivity.this,
                                "There was an error occured. Your Survey will not be saved. Make sure you have a working internet connection.",
                                Toast.LENGTH_LONG).show();
                    }
                });
                return null;
            }

        }.execute();

    }
}