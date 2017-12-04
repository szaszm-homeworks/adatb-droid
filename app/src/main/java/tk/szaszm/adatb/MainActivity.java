package tk.szaszm.adatb;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.List;

import tk.szaszm.adatb.model.News;

public class MainActivity extends Activity implements NewsFragment.OnListFragmentInteractionListener {
    private LinearLayout mainLayout;
    private TextView textView;
    private List<News> newsList = null;
    private boolean justLoggedIn = false;
    private FecskeSession fecske;
    private ListView drawerList;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayout = findViewById(R.id.mainLayout);
        textView = findViewById(R.id.textView1);
        drawerList = findViewById(R.id.drawerList);
        toolbar = findViewById(R.id.mainToolbar);
        drawerLayout = findViewById(R.id.drawerLayout);

        setActionBar(toolbar);
        ActionBar actionbar = getActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.ic_action_menu_white);
        actionbar.setDisplayHomeAsUpEnabled(true);

        fecske = FecskeSession.getInstance();

        addDrawerElements();

        switchToNewsView();
    }

    private void addDrawerElements() {
        String[] items = { "News", "Labor1" };
        drawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items));

        drawerList.setOnItemClickListener((parent, view, position, id) -> {
            final String item = (String) parent.getItemAtPosition(position);
            if(item.equals("News")) switchToNewsView();
            else System.out.println(item);
            drawerLayout.closeDrawer(Gravity.START);
        });
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch(itemId) {
            case android.R.id.home:
                drawerLayout.openDrawer(Gravity.START); return true;
        }
        return true;
    }
}
