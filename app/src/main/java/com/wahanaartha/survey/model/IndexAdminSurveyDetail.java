package com.wahanaartha.survey.model;

/**
 * Created by wahana on 10/19/17.
 */
import java.util.List;
import java.util.Queue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IndexAdminSurveyDetail {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("jml")
    @Expose
    private Integer jml;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("questions")
    @Expose
    private List<IndexAdminDetailQuestion> questions = null;

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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<IndexAdminDetailQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<IndexAdminDetailQuestion> questions) {
        this.questions = questions;
    }

}
