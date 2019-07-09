package com.wahanaartha.survey.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReportResponden {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("dealer_name")
    @Expose
    private String dealer_name;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("answers")
    @Expose
    private List<ReportSelectedAnswer> answers = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ReportSelectedAnswer> getSelectedAnswers() {
        return answers;
    }

    public String getDealer_name() {
        return dealer_name;
    }

    public String getCreated_at() {
        return created_at;
    }
}
