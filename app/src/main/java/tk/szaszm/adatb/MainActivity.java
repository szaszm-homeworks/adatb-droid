package tk.szaszm.adatb;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import tk.szaszm.adatb.model.News;

public class MainActivity extends Activity implements NewsFragment.OnListFragmentInteractionListener {
    private LinearLayout mainLayout;
    private TextView textView;
    private List<News> newsList = null;
    private boolean justLoggedIn = false;
    private FecskeSession fecske;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayout = findViewById(R.id.mainLayout);
        textView = findViewById(R.id.textView1);

        fecske = FecskeSession.getInstance();

        switchToNewsView();
    }

    private void loadNews()
    {
        if(newsList != null) return;

        FecskeSession fecske = FecskeSession.getInstance();
        if(fecske.getToken() == null) return;
        try {
            newsList = fecske.getNews();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void switchToNewsView()
    {
        textView.setText("Loading news...");
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                loadNews();

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if(newsList == null) {
                    System.out.println("newsList is unavailable");
                }

                mainLayout.removeAllViews();

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                NewsFragment newsFragment = NewsFragment.newInstance(newsList);
                transaction.add(R.id.mainLayout, newsFragment);
                transaction.commit();
            }
        }.execute();
    }

    @Override
    public void onListFragmentInteraction(News item) {
        NewsDialogFragment.newInstance(item.title, item.text).show(getFragmentManager(), NewsDialogFragment.TAG);
    }
}
