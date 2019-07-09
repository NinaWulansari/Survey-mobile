package com.wahanaartha.survey.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nina on 10/12/2017.
 */

public class DataDealer {
    @SerializedName("dealer_id")
    @Expose
    private String dealer_id;
    @SerializedName("no_dealer")
    @Expose
    private String no_dealer;
    @SerializedName("dealer_name")
    @Expose
    private String dealer_name;

    public String getDealer_id() {
        return dealer_id;
    }

    public String getNo_dealer() {
        return no_dealer;
    }

    public String getDealer_name() {
        return dealer_name;
    }
}
