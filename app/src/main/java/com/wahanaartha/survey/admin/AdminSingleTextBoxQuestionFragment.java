package com.wahanaartha.survey.admin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.wahanaartha.survey.model.API;
import com.wahanaartha.survey.R;
import com.wahanaartha.survey.model.Question;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.wahanaartha.survey.admin.AdminAddSurveyQuestionActivity.QuestionType.SingleTextBox;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminSingleTextBoxQuestionFragment extends Fragment implements AdminSurveyQuestionFragment{


    @BindView(R.id.question_edittext)
    EditText editText;
    private boolean edited;
    private Question savedQuestion;

    public AdminSingleTextBoxQuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.admin_fragment_single_text_box_question, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public boolean isEdited() {
        return edited;
    }

    @Override
    public Question getQuestion() {

        String questionTitle = editText.getText().toString();

        if (!questionTitle.equals("")) {
            Question question = savedQuestion != null ? savedQuestion : new Question();
            question.setTitle(editText.getText().toString());
            question.setType("SingleTextBox");
            return question;
        } else {
            new AlertDialog.Builder(getActivity()).setMessage("QuestionApi must be filled").show();
            return null;
        }
    }
}
