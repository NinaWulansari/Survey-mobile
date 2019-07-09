package com.wahanaartha.survey.model;

/**
 * Created by wahana on 10/18/17.
 */
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HistoryDetailResponden {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("sendto")
    @Expose
    private String sendto;
    @SerializedName("questions")
    @Expose
    private List<HistoryQuestionAnswer> questions = null;

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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getSendto() {
        return sendto;
    }

    public void setSendto(String sendto) {
        this.sendto = sendto;
    }

    public List<HistoryQuestionAnswer> getQuestions() {
        return questions;
    }

    public void setQuestions(List<HistoryQuestionAnswer> questions) {
        this.questions = questions;
    }

}
