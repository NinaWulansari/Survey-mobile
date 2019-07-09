package com.wahanaartha.survey.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.poi.ss.formula.functions.Index;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by wahana on 9/26/17.
 */

public class API {

    static Retrofit retrofit;
    public static String baseURL = "http://192.168.18.92:8080/";
//    public static String baseURL ="http://mobile-dev.wahanaartha.com:8080/";
    public static Retrofit getInstance() {
        if (retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build();

            Gson gson = new GsonBuilder().setLenient().create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseURL + "survey_dms/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofit;
    }

    public static Call<LoginUser> login(LoginUser loginUser) {
        SurveyService service = getInstance().create(SurveyService.class);
        return service.login(loginUser);
    }

    public static Call<ArrayList<GroupModel>> getGroup() {
        SurveyService service = getInstance().create(SurveyService.class);
        return service.getGroup();
    }

    public static Call<String> addsurvey(Survey survey){
        SurveyService service = getInstance().create(SurveyService.class);
        return service.addsurvey(survey);
    }

    public static Call<ArrayList<IndexSurveyResponden>> getIndexSurveyResponden( String group_name, String id) {
        SurveyService service = getInstance().create(SurveyService.class);
        return service.getIndexResponden(group_name,id);
    }

    public static Call<Survey> getRespondenIndexDetail(String id) {
        SurveyService service = getInstance().create(SurveyService.class);
        return service.getRespondenIndexDetail(id);
    }

    public static Call<ArrayList<HistoryIndexResponden>> getHistoryIndex( String group_name, String id) {
        SurveyService service = getInstance().create(SurveyService.class);
        return service.getHistoryIndex(group_name,id);
    }

    public static Call<HistoryDetailResponden> getHistoryIndexDetail(String id, String responden_id) {
        SurveyService service = getInstance().create(SurveyService.class);
        return service.getHistoryIndexDetail(id, responden_id);
    }

    public static Call<String> addResponden(Responden responden){
        SurveyService service = getInstance().create(SurveyService.class);
        return service.addResponden(responden);
    }

    public static Call<ArrayList<Survey>> getIndexAdmin() {
        SurveyService service = getInstance().create(SurveyService.class);
        return service.getIndexAdmin();
    }

    public static Call<IndexAdminSurveyDetail> getIndexAdminDetail(String id) {
        SurveyService service = getInstance().create(SurveyService.class);
        return service.getIndexAdminDetail(id);
    }

    public static Call<MyProfile> getMyProfile(String id){
        SurveyService service = getInstance().create(SurveyService.class);
        return service.getMyProfile(id);
    }

    public static Call<String> editPass(String id, String password){
        SurveyService service = getInstance().create(SurveyService.class);
        return service.editPass(id, password);
    }

    public static Call<ArrayList<IndexAdmin>> getIndexsurvey() {
        SurveyService service = getInstance().create(SurveyService.class);
        return service.indexsurvey();
    }

    public static Call<DetailAdminIndex> getIndexDetail(String id){
        SurveyService service = getInstance().create(SurveyService.class);
        return  service.detailindex(id);
    }

    public static Call<ArrayList<DataDealer>> getDealer() {
        SurveyService service = getInstance().create(SurveyService.class);
        return service.getDealer();
    }

    public static Call<String> adduser(DataUser user){
        SurveyService service = getInstance().create(SurveyService.class);
        return service.adduser(user);
    }

    public static Call<String> edituser(String id, String name, String username, String password, String email){
        SurveyService service = getInstance().create(SurveyService.class);
        return service.edituser(id, name, username, password, email);
    }

    public static Call<DetailUser> getDetailUser(String id){
        SurveyService service = getInstance().create(SurveyService.class);
        return service.getDetailUser(id);
    }

    public static Call<ArrayList<LoginUser>> getDataUser() {
        SurveyService service = getInstance().create(SurveyService.class);
        return service.getDataUser();
    }

    public interface SurveyService{

        @POST("login")
        Call<LoginUser> login(@Body LoginUser loginUser);

        @GET("getgroup")
        Call<ArrayList<GroupModel>> getGroup();

        @POST("addsurvey")
        Call<String> addsurvey(@Body Survey survey);

        @GET("getIndexSurveyResponden")
        Call<ArrayList<IndexSurveyResponden>> getIndexResponden(@Query("group_name") String group_name, @Query("id") String id);

        @GET("getAllSurveyForReport")
        Call<List<ReportSurvey>> getSurveyForReport();

        @GET("getAllRespondenWithID")
        Call<List<ReportSurvey>> getReportForMonthYear(
                @Query("monthYear") String monthYear,
                @Query("surveyID") String surveyId);

        @GET("getAllRespondenWith")
        Call<List<ReportSurvey>> getAllReportForMonthYear(
                @Query("monthYear") String monthYear);

        @GET("getHistoryIndex")
        Call<ArrayList<HistoryIndexResponden>> getHistoryIndex(
                @Query("group_name") String group_name,
                @Query("id") String id);

        @GET("getHistoryIndexDetail")
        Call<HistoryDetailResponden> getHistoryIndexDetail(
                @Query("id") String id,
                @Query("responden_id") String responden_id);

        @POST("addResponden")
        Call<String> addResponden(@Body Responden responden);

        @GET("getIndexAdmin")
        Call<ArrayList<Survey>> getIndexAdmin();

        @GET("getIndexDetailSurvey")
        Call<Survey> getRespondenIndexDetail(
                @Query("id") String id);

        @GET("getDetailAdminSurvey")
        Call<IndexAdminSurveyDetail> getIndexAdminDetail(
                @Query("id") String id);

        @GET("getMyProfile")
        Call<MyProfile> getMyProfile(
                @Query("id")String id);

        @FormUrlEncoded
        @POST("editPass")
        Call<String> editPass(@Field("id") String id,
                              @Field("password") String password);

        @GET("indexsurvey")
        Call<ArrayList<IndexAdmin>> indexsurvey();

        @GET("getIndexDetailSurvey")
        Call<DetailAdminIndex> detailindex(@Query("id") String id);

        @POST("adduser")
        Call<String> adduser(@Body DataUser user);

        @FormUrlEncoded
        @POST("edit")
        Call<String> edituser(@Field("id") String id,
                              @Field("name") String name,
                              @Field("username") String username,
                              @Field("password") String password,
                              @Field("email") String email);

        @GET("getDataUser")
        Call<ArrayList<LoginUser>> getDataUser();

        @GET("getDealer")
        Call<ArrayList<DataDealer>> getDealer();

        @GET("getDetailUser")
        Call<DetailUser> getDetailUser(@Query("id")String id);

    }
}


