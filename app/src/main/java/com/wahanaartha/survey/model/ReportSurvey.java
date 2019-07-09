package com.wahanaartha.survey.model;

/**
 * Created by andreyyoshuamanik on 3/14/17.
 * To make an app
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReportSurvey {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("questions")
    @Expose
    private List<ReportQuestion> questions = null;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("respondens")
    @Expose
    private List<ReportResponden> respondens = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ReportQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<ReportQuestion> questions) {
        this.questions = questions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ReportResponden> getRespondens() {
        return respondens;
    }

    public void setRespondens(List<ReportResponden> respondens) {
        this.respondens = respondens;
    }

}
