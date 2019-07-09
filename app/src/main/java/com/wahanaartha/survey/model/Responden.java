package com.wahanaartha.survey.model;

/**
 * Created by wahana on 10/14/17.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Responden {

    @SerializedName("id_responden")
    @Expose
    private String idResponden;
    @SerializedName("id_survey")
    @Expose
    private String idSurvey;
    @SerializedName("answers")
    @Expose
    private List<Answer> answers = null;

    public String getIdResponden() {
        return idResponden;
    }

    public void setIdResponden(String idResponden) {
        this.idResponden = idResponden;
    }

    public String getIdSurvey() {
        return idSurvey;
    }

    public void setIdSurvey(String idSurvey) {
        this.idSurvey = idSurvey;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

}

