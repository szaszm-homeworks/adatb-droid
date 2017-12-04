package tk.szaszm.adatb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.List;

import moe.banana.jsonapi2.JsonApiConverterFactory;
import moe.banana.jsonapi2.ResourceAdapterFactory;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tk.szaszm.adatb.event.EventDispatcher;
import tk.szaszm.adatb.event.LoginEvent;
import tk.szaszm.adatb.model.News;
import tk.szaszm.adatb.model.Users;

/**
 * Created by marci on 12/3/17.
 */

public class FecskeSession {
    private static FecskeSession instance = null;
    public static FecskeSession getInstance() {
        if(instance == null) instance = new FecskeSession();
        return instance;
    }

    private Retrofit retrofit;
    private FecskeInterface fecske;
    private Moshi moshi;
    private Gson gson;
    private EventDispatcher dispatcher;

    private FecskeSession()
    {
        JsonAdapter.Factory jsonApiAdapterFactory = ResourceAdapterFactory.builder()
                .add(News.class)
                .add(Users.class)
                .build();

        moshi = new Moshi.Builder()
                .add(jsonApiAdapterFactory)
                .build();

        gson = new GsonBuilder().create();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://fecske-dev.db.bme.hu/api/")
                .addConverterFactory(JsonApiConverterFactory.create(moshi))
                .build();

        fecske = retrofit.create(FecskeInterface.class);

        dispatcher = EventDispatcher.getInstance();
    }


    private String token;
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public void tryAuth(String username, String password) throws Exception {
        Call<ResponseBody> call = fecske.login(username, password);
        Response<ResponseBody> response = call.execute();
        ResponseBody responseBody = response.body();
        if (response.isSuccessful()) {
            assert responseBody != null;
            FecskeInterface.LoginResponse tokenContainer
                    = gson.fromJson(responseBody.string(), FecskeInterface.LoginResponse.class);
            token = tokenContainer.token;
        } else {
            throw new Exception("Invalid response: " + response.toString());
        }
        System.out.println("logged in, token: " + token);

        dispatcher.dispatch(new LoginEvent());
    }

    public List<News> getNews() throws Exception {
        Call<List<News>> call = fecske.news("Bearer " + token);
        Response<List<News>> response = call.execute();
        List<News> newsList = response.body();
        if (!response.isSuccessful()) {
            throw new Exception("Invalid response: " + response.toString());
        }

        return newsList;
    }
}
