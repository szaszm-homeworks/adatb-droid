package tk.szaszm.adatb;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import tk.szaszm.adatb.model.Events;
import tk.szaszm.adatb.model.News;
import tk.szaszm.adatb.model.StudentRegistrations;
import tk.szaszm.adatb.model.Users;


public interface FecskeInterface {
    @FormUrlEncoded
    @POST("auth/login")
    Call<ResponseBody> login(@Field("loginName") String loginName, @Field("password") String password);

    class LoginResponse {
        public String token;
    }


    @GET("news")
    Call<List<News>> news(@Header("Authorization") String authorization);


    @GET("users/{id}")
    Call<Users> users(@Header("Authorization") String authorization, @Path("id") int id);

    @GET("student-registrations/{id}")
    Call<StudentRegistrations> studentRegistrations(@Header("Authorization") String authorization, @Path("id") int id);

    @GET("events/{id}")
    Call<Events> events(@Header("Authorization") String authorization, @Path("id") int id);
}
