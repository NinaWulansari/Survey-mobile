package com.wahanaartha.survey.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wahanaartha.survey.R;
import com.wahanaartha.survey.model.API;
import com.wahanaartha.survey.model.GroupModel;
import com.wahanaartha.survey.model.LoginUser;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.data;
import static android.app.Activity.RESULT_OK;

public class AdminAddSurveyFragment extends Fragment {

    public static final String SURVEY_TITLE = "surveytitle";
    public static final String SURVEY_SEND_TO = "surveysendto";
    public static final int ADD_SURVEY = 1;
    public static final String IS_SURVEY_ADDED = "issurveyadded";

    ArrayList<GroupModel> data;
    String title,id;
    String id_group;

    @BindView(R.id.surveyRoles)
    Spinner spinner;

    @BindView(R.id.surveyTitle) EditText surveyTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.admin_fragment_add_survey, container, false);
        ButterKnife.bind(this, v);
        API.getGroup().enqueue(new Callback<ArrayList<GroupModel>>() {

            @Override
            public void onResponse(Call<ArrayList<GroupModel>> call, Response<ArrayList<GroupModel>> response) {
                if(response.code() == 200){
                    data = response.body();
                    final String [] values = new String[data.size()];
                    for (int i = 0; i < data.size()  ; i++) {
                        title = data.get(i).getTitle();
                        id = data.get(i).getId();
                        values[i] = title;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_custom, values);
                    adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    spinner.setAdapter(adapter);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent,
                                                   View view, int pos, long id) {
                            id_group = data.get(pos).getId();
//                            Toast.makeText(getActivity(),"id group" + id_group, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<ArrayList<GroupModel>> call, Throwable throwable) {

            }
        });
        return v;
    }

    @OnClick(R.id.btnNextSurvey)
    void nextSurvey()
    {
        if (!surveyTitle.getText().toString().equals("")) {
            Intent intent = new Intent(getActivity(), AdminAddSurveyQuestionActivity.class);
            intent.putExtra(SURVEY_TITLE, surveyTitle.getText().toString());
            intent.putExtra(SURVEY_SEND_TO,id_group);
            startActivityForResult(intent, ADD_SURVEY);
        } else {
            new AlertDialog.Builder(getActivity()).setMessage("Title must be filled").show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_SURVEY && resultCode == RESULT_OK && data.getBooleanExtra(IS_SURVEY_ADDED, false)) {
            ((AdminActivity)getActivity()).title.setText("Index Survey");
            ((AdminActivity)getActivity()).searchButton.setVisibility(View.VISIBLE);
            ((AdminActivity)getActivity()).navigationView.setCheckedItem(R.id.nav_index_survey);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame, new AdminIndexFragmentSurvey()).commit();
        }
    }
}
