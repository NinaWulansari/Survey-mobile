package com.wahanaartha.survey.admin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.wahanaartha.survey.R;
import com.wahanaartha.survey.model.Answer;
import com.wahanaartha.survey.model.Question;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminMultipleChoiceQuestionFragment extends Fragment implements AdminSurveyQuestionFragment{

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.question_edittext) EditText editText;

    public static final String QUESTION_KEY = "question";
    public static final String QUESTION_KEY_TYPE = "question_type";

    private boolean edited;
    private Question savedQuestion;

    public AdminMultipleChoiceQuestionFragment() {
        // Required empty public constructor
    }

    public MultipleChoiceAdapter adapter = new MultipleChoiceAdapter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.admin_fragment_multiple_choice_question, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    @Override
    public boolean isEdited() {
        return edited;
    }

    @Override
    public Question getQuestion() {
        String questionTitle = editText.getText().toString();

        if (!questionTitle.equals("")) {
            int id = getActivity().getIntent().getIntExtra(QUESTION_KEY, -1);
            Question question = savedQuestion != null ? savedQuestion : new Question();
            question.setType("MultipleChoice");

            question.setTitle(editText.getText().toString());

            ArrayList<Answer> answers = new ArrayList<>();
            for (Answer answer : adapter.answers) {
                if (answer.getTitle().equals("")) {
                    new AlertDialog.Builder(getActivity()).setMessage("All answer must be filled").show();
                    return null;
                }
                answers.add(answer);
            }

            question.setAnswers(answers);
            return question;
        } else {

            new AlertDialog.Builder(getActivity()).setMessage("QuestionApi must be filled").show();
            return null;
        }

    }

    public class MultipleChoiceAdapter extends RecyclerView.Adapter<MultipleChoiceAdapter.MultipleChoiceViewHolder> {

        public ArrayList<Answer> answers;

        public MultipleChoiceAdapter() {
            answers = new ArrayList<>();
            answers.add(new Answer());
        }

        public MultipleChoiceAdapter(ArrayList<Answer> answers) {
            this.answers = answers;
        }

        @Override
        public MultipleChoiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.admin_fragment_multiple_choice_answer_row, parent, false);

            return new MultipleChoiceViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MultipleChoiceViewHolder holder, int position) {

            holder.addLayout.setVisibility(position == answers.size() ? View.VISIBLE : View.GONE);
            holder.answerLayout.setVisibility(position == answers.size() ? View.GONE :View.VISIBLE);

            holder.answerEditText.setText(answers.get(position < answers.size() ? position : 0).getTitle());

            if (position == answers.size() - 1) {
                if(holder.answerEditText.requestFocus()) {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }

            holder.answerEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    int pos = holder.getAdapterPosition();
                    if (pos < answers.size()) {
                        answers.get(pos).setTitle(editable.toString());
                    }
                    if (!editable.toString().equals("")) {
                        edited = true;
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return answers.size() + (savedQuestion == null ? 1 : 0);
        }

        void addRow() {
            for (Answer s:answers) {
                if (s.getTitle() == null || s.getTitle().equals("")) {
                    Toast.makeText(getActivity(), "Please fill the answer first", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            answers.add(new Answer());
            notifyItemInserted(answers.size() - 1);

            recyclerView.scrollToPosition(answers.size());
        }

        void deleteRowAtPosition(int position) {
            if (position >= 0) {
                answers.remove(position);
                notifyItemRemoved(position);
            }
        }

        public class MultipleChoiceViewHolder extends RecyclerView.ViewHolder {

            EditText answerEditText;
            ImageButton deleteButton;

            RelativeLayout answerLayout;
            RelativeLayout addLayout;

            ImageButton addButton;

            MultipleChoiceViewHolder(View itemView) {
                super(itemView);

                answerEditText = (EditText) itemView.findViewById(R.id.answer_edittext);
                deleteButton = (ImageButton) itemView.findViewById(R.id.delete_button);
                answerLayout = (RelativeLayout) itemView.findViewById(R.id.answer_layout);
                addLayout = (RelativeLayout) itemView.findViewById(R.id.add_layout);
                addButton = (ImageButton) itemView.findViewById(R.id.add_button);

                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (answers.size() == 1) {
                            Toast.makeText(getActivity(), "Must be at least one answer", Toast.LENGTH_LONG).show();
                            return;
                        }
                        deleteRowAtPosition(getAdapterPosition());
                    }
                });

                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addRow();
                    }
                });

                if (savedQuestion != null) {
                    deleteButton.setVisibility(View.GONE);
                    addButton.setVisibility(View.GONE);
                }
            }
        }
    }
}
