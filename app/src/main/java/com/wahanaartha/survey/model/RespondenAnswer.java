package com.wahanaartha.survey.model;

/**
 * Created by wahana on 10/14/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RespondenAnswer {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("selected_answer")
    @Expose
    private String selectedAnswer;
    @SerializedName("question_id")
    @Expose
    private String questionId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(String selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

}
