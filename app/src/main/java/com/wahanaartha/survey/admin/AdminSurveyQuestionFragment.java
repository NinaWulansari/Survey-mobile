package com.wahanaartha.survey.admin;

import com.wahanaartha.survey.model.Question;

/**
 * Created by lely
 */

public interface AdminSurveyQuestionFragment {

    boolean isEdited();

    Question getQuestion();
}
