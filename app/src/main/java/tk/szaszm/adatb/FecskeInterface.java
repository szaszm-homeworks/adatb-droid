package tk.szaszm.adatb;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import tk.szaszm.adatb.model.News;


public interface FecskeInterface {
    @FormUrlEncoded
    @POST("auth/login")
    Call<ResponseBody> login(@Field("loginName") String loginName, @Field("password") String password);

    class LoginResponse {
        public String token;
    }


    @GET("news")
    Call<List<News>> news(@Header("Authorization") String authorization);

}
