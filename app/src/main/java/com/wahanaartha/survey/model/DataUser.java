package com.wahanaartha.survey.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nina on 10/10/2017.
 */

public class DataUser {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("group_id")
    @Expose
    private String groupId;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("dealer_id")
    @Expose
    private String dealerId;
    @SerializedName("no_hp")
    @Expose
    private String noHp;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("title")
    @Expose
    private String title;

    public String getTitle() {
        return title;
    }

    public DataUser(String name, String username, String password, String groupId, String email, String dealerId, String noHp, String address, String image ) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.groupId = groupId;
        this.email = email;
        this.dealerId = dealerId;
        this.noHp = noHp;
        this.address = address;
        this.image = image;
    }

    public DataUser(){

    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String id) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
