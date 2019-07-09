package com.wahanaartha.survey.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wahanaartha.survey.admin.AdminAddSurveyQuestionActivity;

import java.util.List;

/**
 * Created by lely
 */
public class Question {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("answers")
    @Expose
    private List<Answer> answers = null;

    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

}
