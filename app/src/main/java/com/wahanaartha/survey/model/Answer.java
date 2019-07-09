package com.wahanaartha.survey.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by lely on 8/21/16.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Answer {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("selected_answer")
    @Expose
    private String answerIsSelectedAnswer;

    @SerializedName("question_id")
    @Expose
    private String question_id;

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

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

    public String getAnswerIsSelectedAnswer() {
        return answerIsSelectedAnswer;
    }

    public void setAnswerIsSelectedAnswer(String answerIsSelectedAnswer) {
        this.answerIsSelectedAnswer = answerIsSelectedAnswer;
    }
}
