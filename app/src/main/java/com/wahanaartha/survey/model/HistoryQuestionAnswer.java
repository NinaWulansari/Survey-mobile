package com.wahanaartha.survey.model;

/**
 * Created by wahana on 10/18/17.
 */
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HistoryQuestionAnswer {

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
    private HistorySelectedAnswer answers;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HistorySelectedAnswer getAnswers() {
        return answers;
    }

    public void setAnswers(HistorySelectedAnswer answers) {
        this.answers = answers;
    }



}

