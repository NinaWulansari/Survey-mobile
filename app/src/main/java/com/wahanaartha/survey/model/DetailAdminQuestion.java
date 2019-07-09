package com.wahanaartha.survey.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 10/17/2017.
 */

public class DetailAdminQuestion implements Parcelable {
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
    private List<DetailAdminAnswer> answers = null;


    protected DetailAdminQuestion(Parcel in) {
        id = in.readString();
        title = in.readString();
        type = in.readString();
    }

    public static final Creator<DetailAdminQuestion> CREATOR = new Creator<DetailAdminQuestion>() {
        @Override
        public DetailAdminQuestion createFromParcel(Parcel in) {
            return new DetailAdminQuestion(in);
        }

        @Override
        public DetailAdminQuestion[] newArray(int size) {
            return new DetailAdminQuestion[size];
        }
    };

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

    public List<DetailAdminAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<DetailAdminAnswer> answers) {
        this.answers = answers;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(type);
    }

    public static DetailAdminQuestion get(int position) {
        return null;
    }
}
