package com.wahanaartha.survey.responden;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.wahanaartha.survey.R;
import com.wahanaartha.survey.model.API;
import com.wahanaartha.survey.model.Survey;
import com.wahanaartha.survey.model.Answer;
import com.wahanaartha.survey.model.Question;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.wahanaartha.survey.responden.RespondenIndexFragment.SURVEY_KEY;
//import static com.wahanaartha.survey.responden.InterviewerStartSurveyActivity.RESPONDEN_KEY;

public class RespondenSurveyActivity extends AppCompatActivity {
    public interface UploadingListener {
        public void finishUploading(boolean finish);
    }

    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.prev_button) FloatingActionButton prevButton;
    @BindView(R.id.next_button) FloatingActionButton nextButton;
    @BindView(R.id.toolbarResponden) Toolbar toolbarResponden;

//    @BindView(R.id.prev_layout) RelativeLayout prevLayout;
//    @BindView(R.id.prev_offset_layout) RelativeLayout prevOffsetLayout;
//    @BindView(R.id.next_layout) RelativeLayout nextLayout;
//    @BindView(R.id.next_offset_layout) RelativeLayout nextOffsetLayout;

    int currentPosition = 0;

    Survey survey;
//    Responden responden;
    ArrayList<Answer> answers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.responden_activity_insert_survey);
        ButterKnife.bind(this);
        setSupportActionBar(toolbarResponden);

        API.getRespondenIndexDetail(getIntent().getStringExtra(SURVEY_KEY)).enqueue(new Callback<Survey>() {
            @Override
            public void onResponse(Call<Survey> call, Response<Survey> response) {
                Log.i("Wahanaapp", "onResponse : " + response.body());
                if (response.code() == 200) {
                    survey = response.body();
                    viewPager.setAdapter(new RespondenSurveyPagerAdapter(getSupportFragmentManager()));
//                    viewPager.setPageTransformer(true, new DepthPageTransformer());
                    setCurrentPosition(getCurrentPosition());
                } else {
                    Toast.makeText(RespondenSurveyActivity.this, "There was an error occured. Please check your internet connection.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Survey> call, Throwable t) {

                Toast.makeText(RespondenSurveyActivity.this, "Gagal", Toast.LENGTH_LONG).show();
            }
        });

    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int pos) {
        int questionSize = survey.getQuestions().size();
        if (pos < questionSize + 1) {
            viewPager.setCurrentItem(pos, true);
        } else {
            return;
        }
        this.currentPosition = pos;
        prevButton.setVisibility(this.currentPosition == 0 || this.currentPosition == questionSize ? View.INVISIBLE : View.VISIBLE);
        nextButton.setVisibility(this.currentPosition == questionSize ? View.INVISIBLE : View.VISIBLE);
    }

    class RespondenSurveyPagerAdapter extends FragmentPagerAdapter {

        List<Question> questions;

        public RespondenSurveyPagerAdapter(FragmentManager fm) {
            super(fm);
            questions = survey.getQuestions();
        }

        @Override
        public Fragment getItem(int position) {

            if (position == questions.size()) {
                return new RespondenQuestionFinishFragment();
            }

            Question question = questions.get(position);

            return RespondenQuestionContainerFragment.getInstance(question);
        }

        @Override
        public int getCount() {
            return questions.size() + 1;
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "You must finish all the question", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.next_button)
    void next() {
        RespondenQuestionAnswer fragment = (RespondenQuestionAnswer)getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + viewPager.getCurrentItem());

        Answer answer = fragment.getAnswer();
        if (answer == null) {
            new AlertDialog.Builder(this).setMessage("Please set the answer").show();
            return;
        } else  {
            answers.add(getCurrentPosition(), answer);
        }
        setCurrentPosition(getCurrentPosition() + 1);
    }

    @OnClick(R.id.prev_button)
    void previous() {
        answers.remove(getCurrentPosition() - 1);
        setCurrentPosition(getCurrentPosition() - 1);
    }
}
