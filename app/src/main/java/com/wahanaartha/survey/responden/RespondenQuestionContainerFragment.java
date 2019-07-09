package com.wahanaartha.survey.responden;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wahanaartha.survey.R;
import com.wahanaartha.survey.admin.AdminAddSurveyQuestionActivity.QuestionType;
import com.wahanaartha.survey.model.Answer;
import com.wahanaartha.survey.model.Question;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class RespondenQuestionContainerFragment extends Fragment implements RespondenQuestionAnswer {

    public static final String SURVEY_KEY = "survey";
    private Answer selectedAnswer;

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.question_textview) TextView questionTextView;
    @BindView(R.id.scrollView) ScrollView scrollView;

    Question question;
    EditText singleTextBoxEdittext;
    String[] checklistArray;
    Button dateTimeButton;

    @Override
    public void setAnswer(Answer answer) {
        selectedAnswer = answer;
    }

    @Override
    public Answer getAnswer() {
        if (selectedAnswer != null)
            selectedAnswer.setId(selectedAnswer.getId());

        if (question.getType().equals(QuestionType.MultipleChoice.toString())) {
            if (selectedAnswer != null)
                return selectedAnswer;
        } else if (question.getType().equals(QuestionType.CheckList.toString())) {
            String answerTitle = "";
            for (String a : checklistArray) {
                if (a != null) {
                    answerTitle += a + "%";
                }
            }

            if (!answerTitle.equals("")) {
                Answer answer = new Answer();
                answer.setTitle(answerTitle);
                answer.setQuestion_id(question.getId());
                answer.setAnswerIsSelectedAnswer("1");
                return answer;
            }
        } else if (question.getType().equals(QuestionType.SingleTextBox.toString())) {
            String answerTitle = singleTextBoxEdittext.getText().toString();
            if (!answerTitle.equals("")) {
                Answer answer = new Answer();
                answer.setTitle(answerTitle);
                answer.setQuestion_id(question.getId());
                answer.setAnswerIsSelectedAnswer("1");
                return answer;
            }
        }

        return null;
    }

    public RespondenQuestionContainerFragment() {
        // Required empty public constructor
    }

    public static RespondenQuestionContainerFragment getInstance(Question question) {
        RespondenQuestionContainerFragment fragment = new RespondenQuestionContainerFragment();
        fragment.question = question;
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.responden_fragment_insert_question_container, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        questionTextView.setText(question.getTitle());

        if (question.getType().equals(QuestionType.MultipleChoice.toString())) {
            createMultipleChoices();
        } else if (question.getType().equals(QuestionType.CheckList.toString())) {
           createCheckList();
        } else if (question.getType().equals( QuestionType.SingleTextBox.toString())) {
            createSingleTextBox();
        }
    }

    private void createMultipleChoices() {

        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View linearLayout = inflater.inflate(R.layout.responden_fragment_insert_multiple_choice_view, scrollView, true);
        RadioGroup radioGroup = (RadioGroup)linearLayout.findViewById(R.id.radio_group);

        int i = 0;
        for(Answer answer : question.getAnswers()){
            View innerLayout = inflater.inflate(R.layout.responden_fragment_insert_multiple_choice_radio_button, radioGroup, true);
            RadioButton radioButton = (RadioButton)innerLayout.findViewById(R.id.radio_button);
            radioButton.setId(i);
            radioButton.setText(answer.getTitle());

            radioButton.setId(i);
            if (selectedAnswer != null && answer.getTitle().equals(selectedAnswer.getTitle())) {
                if(Build.VERSION.SDK_INT>=21)
                {

                    ColorStateList colorStateList = new ColorStateList(
                            new int[][]{

                                    new int[]{-android.R.attr.state_enabled}, //disabled
                                    new int[]{android.R.attr.state_enabled} //enabled
                            },
                            new int[] {

                                    Color.BLACK //disabled
                                    ,Color.parseColor("#b41f0b") //enabled

                            }
                    );


                    radioButton.setButtonTintList(colorStateList);//set the color tint list
                    radioButton.invalidate(); //could not be necessary
                }
                radioButton.setChecked(true);
            }
            i++;
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                selectedAnswer = question.getAnswers().get(i);
            }
        });
    }

    private void createSingleTextBox() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.responden_fragment_insert_single_text_box_view, scrollView, true);
        singleTextBoxEdittext = (EditText)view.findViewById(R.id.edit_text);

        if (selectedAnswer != null) {
            singleTextBoxEdittext.setText(selectedAnswer.getTitle());
        }
    }

    private void createCheckList() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View rootView = inflater.inflate(R.layout.responden_checklist_layout, scrollView, true);
        LinearLayout linearLayout = rootView.findViewById(R.id.linearLayout);

        checklistArray = new String[question.getAnswers().size()];

        int i = 0;
        for(Answer answer : question.getAnswers()){
//            Toast.makeText(getActivity(), "Jawaban " + answer.getTitle(), Toast.LENGTH_SHORT).show();
            View innerLayout = inflater.inflate(R.layout.responden_checklist_view, (ViewGroup) linearLayout, true);
            CheckBox cb = (CheckBox) innerLayout.findViewById(R.id.checkboxSurvey);
            cb.setId(i);
            cb.setText(answer.getTitle());

            cb.setId(i);
            if (selectedAnswer != null && answer.getTitle().equals(selectedAnswer.getTitle())) {
                cb.setChecked(true);
            }

            final int finalI = i;
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (compoundButton.isChecked()) {
                        checklistArray[finalI] = question.getAnswers().get(finalI).getTitle();
                    } else {
                        checklistArray[finalI] = null;
                    }
                }
            });

            i++;
        }
    }


}
