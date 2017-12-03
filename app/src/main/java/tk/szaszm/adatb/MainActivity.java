package tk.szaszm.adatb;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import tk.szaszm.adatb.model.News;

public class MainActivity extends Activity {
    private TextView textView;
    private boolean newsLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FecskeSession fecske = FecskeSession.getInstance();
        if(fecske.getToken() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        // TEMPORARY (to aid development)
        /*
        new AsyncTask<Void, Void, Void>() {
            List<News> newsList = null;
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    fecske.tryAuth("testLogin", "qwe");
                    newsList = loadNews();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                showNews(newsList);
            }
        }
        .execute();
        */


        textView = findViewById(R.id.textView1);
    }

    private List<News> loadNews()
    {
        if(newsLoaded) return null;

        FecskeSession fecske = FecskeSession.getInstance();
        if(fecske.getToken() == null) return null;
        List<News> newsList = null;
        try {
            newsList = fecske.getNews();
        } catch (Exception e) {
            e.printStackTrace();
        }

        newsLoaded = true;
        return newsList;
    }

    private void showNews(List<News> newsList)
    {
        StringBuilder sb = new StringBuilder();

        for (News news : newsList) {
            sb.append(news.text);
            sb.append('\n');
        }

        textView.setText(sb.toString());
    }
}
