package com.wahanaartha.survey.responden;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wahanaartha.survey.R;
import com.wahanaartha.survey.model.API;
import com.wahanaartha.survey.model.Answer;
import com.wahanaartha.survey.model.LoginUser;
import com.wahanaartha.survey.model.Responden;
import com.wahanaartha.survey.model.RespondenAnswer;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;
import static com.wahanaartha.survey.LoginActivity.MY_LOGIN_PREF;
import static com.wahanaartha.survey.LoginActivity.MY_LOGIN_PREF_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class RespondenQuestionFinishFragment extends Fragment {

    ArrayList<Answer> answers = new ArrayList<>();

    public RespondenQuestionFinishFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.responden_fragment_index_question_finish, container, false);
        ButterKnife.bind(this, view);

        return view;
    }


    @OnClick(R.id.save_button)
    void save() {
        LoginUser savedUser = new Gson().fromJson(getActivity().getSharedPreferences(MY_LOGIN_PREF, Context.MODE_PRIVATE).getString(MY_LOGIN_PREF_KEY, ""), LoginUser.class);

        String id_responden =  savedUser.getId();
        Responden responden = new Responden();
        responden.setIdResponden(id_responden);
        responden.setIdSurvey(((RespondenSurveyActivity)getActivity()).survey.getId());
        responden.setAnswers(((RespondenSurveyActivity)getActivity()).answers);
//        ArrayList tes = answers;
//        Log.i(TAG, "jawaban: ");
        API.addResponden(responden).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 200) {
                    Toast.makeText(getActivity(), "Input Data Success", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                } else {
                    Toast.makeText(getActivity(),
                            "There was an error occured. Your Survey will not be saved. Make sure you have a working internet connection.",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Toast.makeText(getActivity(),
                        "There was an error occured. Your Survey will not be saved. Make sure you have a working internet connection.",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}