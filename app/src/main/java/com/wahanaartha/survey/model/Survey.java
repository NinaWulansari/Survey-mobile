package com.wahanaartha.survey.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Survey {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("jml")
    @Expose
    private String jml;

    @SerializedName("sendto")
    @Expose
    private String sendto;

    @SerializedName("questions")
    @Expose
    private List<Question> questions = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSendto(String sendto) {
        this.sendto = sendto;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Survey(String title, String sendto, List<Question> questions) {
        this.title = title;
        this.sendto = sendto;
        this.questions = questions;
    }

    public String getJml() {
        return jml;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

}
