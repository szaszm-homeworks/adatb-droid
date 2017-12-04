package tk.szaszm.adatb;

import android.util.Base64;

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
import tk.szaszm.adatb.event.EventDispatcher;
import tk.szaszm.adatb.event.LoginEvent;
import tk.szaszm.adatb.model.DeliverableTemplates;
import tk.szaszm.adatb.model.Deliverables;
import tk.szaszm.adatb.model.EventTemplates;
import tk.szaszm.adatb.model.Events;
import tk.szaszm.adatb.model.ExerciseCategories;
import tk.szaszm.adatb.model.ExerciseSheets;
import tk.szaszm.adatb.model.News;
import tk.szaszm.adatb.model.Roles;
import tk.szaszm.adatb.model.StudentRegistrations;
import tk.szaszm.adatb.model.TokenData;
import tk.szaszm.adatb.model.Users;

/**
 * Created by marci on 12/3/17.
 */

public class FecskeSession {
    private static final String BASE_URL = "https://fecske-dev.db.bme.hu/api/";
    private static final String jwtSecret = "SuperSecret_Tuturu_Pumpuru";

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
                .add(Deliverables.class)
                .add(DeliverableTemplates.class)
                .add(Events.class)
                .add(EventTemplates.class)
                .add(ExerciseCategories.class)
                .add(Roles.class)
                .add(StudentRegistrations.class)
                .add(ExerciseSheets.class)
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
    private TokenData tokenData;

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public TokenData getTokenData() { return tokenData; }
    private String getAuthToken() { return "Bearer " + token; }

    private void parseJwtToken(String token) throws IOException {
        String[] split = token.split("\\.");
        String str = new String(Base64.decode(split[1], 0));

        JsonAdapter<TokenData> adapter = moshi.adapter(TokenData.class);
        tokenData = adapter.fromJson(str);

        System.out.println(str);
    }

    private <T> T loadWhatever(Call<T> call) throws Exception {
        Response<T> response = call.execute();
        T result = response.body();
        if(!response.isSuccessful()) throw new Exception("Invalid response: " + response.toString());
        return result;
    }

    public void tryAuth(String username, String password) throws Exception {
        Call<ResponseBody> call = fecske.login(username, password);
        ResponseBody responseBody = loadWhatever(call);

        assert responseBody != null;
        FecskeInterface.LoginResponse tokenContainer
                = gson.fromJson(responseBody.string(), FecskeInterface.LoginResponse.class);
        token = tokenContainer.token;

        System.out.println("logged in, token: " + token);
        parseJwtToken(token);

        dispatcher.dispatch(new LoginEvent());
    }

    public List<News> getNews() throws Exception {
        Call<List<News>> call = fecske.news(getAuthToken());
        return loadWhatever(call);
    }

    public Users getUser(int userId) throws Exception {
        Call<Users> call = fecske.users(getAuthToken(), userId);
        return loadWhatever(call);
    }

    public StudentRegistrations getStudentRegistration(int id) throws Exception {
        Call<StudentRegistrations> call = fecske.studentRegistrations(getAuthToken(), id);
        return loadWhatever(call);
    }

    public Events getEvent(int id) throws Exception {
        Call<Events> call = fecske.events(getAuthToken(), id);
        return loadWhatever(call);
    }
}
