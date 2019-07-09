package com.wahanaartha.survey.admin;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.wahanaartha.survey.model.API;
import com.wahanaartha.survey.R;
import com.wahanaartha.survey.Utility;
import com.wahanaartha.survey.CustomSpinnerAdapter;
import com.wahanaartha.survey.model.ReportSurvey;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminReportSurveyFragment extends Fragment {

    private static final int MY_PERMISSION_REQUEST_WRITE_STORAGE = 100;

    @BindView(R.id.survey_title_spinner) Spinner surveyTitleSpinner;
    @BindView(R.id.month_spinner) Spinner monthSpinner;
    @BindView(R.id.year_spinner) Spinner yearSpinner;

    public AdminReportSurveyFragment() {
        // Required empty public constructor
    }

    ReportSurvey selectedSurvey;
    List<ReportSurvey> surveys;
    String selectedMonth;
    String selectedYear;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.admin_fragment_report_survey, container, false);
        ButterKnife.bind(this, view);

        final ArrayList<String> months = Utility.getMonths();

        Date earlierDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(earlierDate);
        int earlierYear = calendar.get(Calendar.YEAR);
        calendar.setTime(new Date());
        int todayYear = calendar.get(Calendar.YEAR);

        final ArrayList<String> years = new ArrayList<>();
        for (int i = earlierYear; i <= todayYear; i++) {
            years.add("" + i);
        }

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Fetching data");
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        API.getInstance().create(API.SurveyService.class).getSurveyForReport().enqueue(new Callback<List<ReportSurvey>>() {
            @Override
            public void onResponse(Call<List<ReportSurvey>> call, Response<List<ReportSurvey>> response) {
                if (response.code() == 200) {
                    surveys = response.body();
                    ArrayList<String> surveyTitles = new ArrayList<>();
                    surveyTitles.add("All Survey");
                    for (ReportSurvey survey:surveys) {
                        surveyTitles.add(survey.getTitle());
                    }

                    surveyTitleSpinner.setAdapter(new CustomSpinnerAdapter(getActivity(), surveyTitles));
                    monthSpinner.setAdapter(new CustomSpinnerAdapter(getActivity(), months));
                    yearSpinner.setAdapter(new CustomSpinnerAdapter(getActivity(), years));
                } else {
                    Toast.makeText(getActivity(), "There was an error occured. Please check your connection", Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<ReportSurvey>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "There was an error occured. Please check your connection", Toast.LENGTH_LONG).show();
            }
        });

        surveyTitleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    ReportSurvey survey = new ReportSurvey();
                    survey.setTitle("All Survey");
                    selectedSurvey = survey;

                    return;
                }
                selectedSurvey = surveys.get(i-1);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(months.get(i).equals("Januari")){
                    selectedMonth = "01";
                }else if(months.get(i).equals("Februari")){
                    selectedMonth = "02";
                }else if(months.get(i).equals("Maret")){
                    selectedMonth = "03";
                }else if(months.get(i).equals("April")){
                    selectedMonth = "04";
                }else if(months.get(i).equals("Mei")){
                    selectedMonth = "05";
                }else if(months.get(i).equals("Juni")){
                    selectedMonth = "06";
                }else if(months.get(i).equals("Juli")){
                    selectedMonth = "07";
                }else if(months.get(i).equals("Agustus")){
                    selectedMonth = "08";
                }else if(months.get(i).equals("September")){
                    selectedMonth = "09";
                }else if(months.get(i).equals("Oktober")){
                    selectedMonth = "10";
                }else if(months.get(i).equals("November")){
                    selectedMonth = "11";
                }else if(months.get(i).equals("Desember")){
                    selectedMonth = "12";
                }
//                selectedMonth = months.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedYear = years.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return view;
    }

    @OnClick(R.id.submit_button)
    void submit() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    MY_PERMISSION_REQUEST_WRITE_STORAGE);
        } else {
            getData();
        }
    }

    void getData(){
        final ExcelStuff excelStuff = new ExcelStuff(getActivity());

//        progressDialog.show();
        String tes = selectedSurvey.getTitle();
//        Toast.makeText(getActivity(), "value is "+tes, Toast.LENGTH_LONG).show();
        if (selectedSurvey.getTitle().equals("All Survey")) {
            API.getInstance().create(API.SurveyService.class).getAllReportForMonthYear(selectedMonth + "/" + selectedYear)
                    .enqueue(new Callback<List<ReportSurvey>>() {
                        @Override
                        public void onResponse(Call<List<ReportSurvey>> call, Response<List<ReportSurvey>> response) {
                            Log.i("Respon Dari All Survey", "onResponse: " + response);

                            if (response.code() == 200) {
                               excelStuff.createReport(response.body(), selectedMonth, selectedYear);
                            } else {
                                Toast.makeText(getActivity(), "Data Kosong", Toast.LENGTH_LONG).show();
                            }
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<List<ReportSurvey>> call, Throwable t) {
                            Toast.makeText(getActivity(), "There was an error occured. Please check your internet connection", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    });
        }
        else {
            API.getInstance().create(API.SurveyService.class).getReportForMonthYear(selectedMonth + "/" + selectedYear, selectedSurvey.getId())
                    .enqueue(new Callback<List<ReportSurvey>>() {
                        @Override
                        public void onResponse(Call<List<ReportSurvey>> call, Response<List<ReportSurvey>> response) {
                            Log.i("DATA REPORT", "onResponse: " + response);
                            if (response.code() == 200) {
                                excelStuff.createReport(response.body(), selectedMonth, selectedYear);
                            } else {
                                Toast.makeText(getActivity(), "Data Kosong", Toast.LENGTH_LONG).show();
                            }
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<List<ReportSurvey>> call, Throwable t) {
                            Toast.makeText(getActivity(), "There was an error occured. Please check your internet connection", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_WRITE_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getData();
                } else {
                    Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}