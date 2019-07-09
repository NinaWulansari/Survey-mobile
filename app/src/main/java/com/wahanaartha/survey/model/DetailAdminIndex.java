package com.wahanaartha.survey.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by User on 10/11/2017.
 */

public class DetailAdminIndex {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("jml")
    @Expose
    private Integer jml;

    @SerializedName("sendto")
    @Expose
    private String sendto;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("questions")
    @Expose
    private List<DetailAdminQuestion> questions = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getJml() {
        return jml;
    }

    public void setJml(Integer jml) {
        this.jml = jml;
    }

    public String getSendto() {
        return sendto;
    }

    public void setSendto(String sendto) {
        this.sendto = sendto;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<DetailAdminQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<DetailAdminQuestion> questions) {
        this.questions = questions;
    }
}


