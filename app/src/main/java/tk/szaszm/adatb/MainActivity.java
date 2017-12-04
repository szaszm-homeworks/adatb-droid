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

import java.util.ArrayList;
import java.util.List;

import moe.banana.jsonapi2.ResourceIdentifier;
import tk.szaszm.adatb.event.EventDispatcher;
import tk.szaszm.adatb.event.EventLoadedEvent;
import tk.szaszm.adatb.model.Events;
import tk.szaszm.adatb.model.News;
import tk.szaszm.adatb.model.StudentRegistrations;
import tk.szaszm.adatb.model.Users;

public class MainActivity extends Activity implements NewsFragment.OnListFragmentInteractionListener {
    private LinearLayout mainLayout;
    private TextView textView;
    private List<News> newsList = null;
    private boolean justLoggedIn = false;
    private FecskeSession fecske;
    private ListView drawerList;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ArrayList<DrawerItem> drawerItems = new ArrayList<>();
    private ArrayList<Events> eventsList = new ArrayList<>();

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

        setupDrawerElements();

        asyncLoadUser();

        switchToNewsView();
    }

    private void setupDrawerElements() {
        drawerItems.clear();
        drawerItems.add(new DrawerItem("News", item -> switchToNewsView()));
        drawerList.setAdapter(new ArrayAdapter<DrawerItem>(this, android.R.layout.simple_list_item_1, drawerItems));

        drawerList.setOnItemClickListener((parent, view, position, id) -> {
            final DrawerItem item = (DrawerItem) parent.getItemAtPosition(position);
            System.out.println("Selected DrawerItem: " + item.label);
            item.click();
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

    public Events getEventByIndex(int eventIndex) {
        return eventsList.get(eventIndex);
    }

    static class RetrieveEventAsyncTask extends AsyncTask<Void, Void, Void>
    {
        int eventId;

        Events event = null;
        FecskeSession fecske;
        EventDispatcher dispatcher;

        public RetrieveEventAsyncTask(FecskeSession fecske, EventDispatcher dispatcher, int id)
        {
            this.fecske = fecske;
            this.dispatcher = dispatcher;
            eventId = id;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                event = fecske.getEvent(eventId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dispatcher.dispatch(new EventLoadedEvent(event));
        }

        public Events getEvent() {
            return event;
        }
    }

    private void asyncLoadUser()
    {
        int userId = fecske.getTokenData().userId;
        new AsyncTask<Void, Void, Void>() {
            Users user;
            StudentRegistrations studentRegistration;
            List<Integer> eventIds;
            List<Events> events = new ArrayList<>();

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    user = fecske.getUser(userId);
                    System.out.println("loaded user " + user.displayName + "\t" + user.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                List<Integer> studentRegistrationIds = new ArrayList<>();
                for (ResourceIdentifier resourceIdentifier : user.StudentRegistrations) {
                    studentRegistrationIds.add(Integer.valueOf(resourceIdentifier.getId()));
                }

                // fecske doesn't handle multiple semesters yet
                int studentRegistrationId = studentRegistrationIds.get(0);

                System.out.println("StudentRegistrationId: " + studentRegistrationId);

                try {
                    studentRegistration = fecske.getStudentRegistration(studentRegistrationId);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println("StudentRegistration: " + studentRegistration + ", course code: " + studentRegistration.neptunCourseCode);

                eventIds = new ArrayList<>();
                for (ResourceIdentifier resourceIdentifier : studentRegistration.Events) {
                    eventIds.add(Integer.valueOf(resourceIdentifier.getId()));
                }

                return null;
            }


            @Override
            protected void onPostExecute(Void aVoid) {
                EventDispatcher loadDispatcher = new EventDispatcher();
                loadDispatcher.listen(EventLoadedEvent.class, ev -> {
                    synchronized (events) {
                        events.add(ev.getEvent());
                        if(events.size() == eventIds.size()) {
                            MainActivity.this.eventsList.clear();
                            for (Events event : events) {
                                final Events evt = event;
                                final String label = event.getLabel();
                                drawerItems.add(new DrawerItem(label, item -> {
                                    switchToEventView(evt);
                                    System.out.println(label + " clicked");
                                }));

                                MainActivity.this.eventsList.add(evt);
                            }
                        }
                    }

                });

                for (Integer eventId : eventIds) {
                    RetrieveEventAsyncTask task = new RetrieveEventAsyncTask(fecske, loadDispatcher, eventId);
                    task.execute();
                }
            }
        }.execute();
    }

    public void switchToEventView(Events evt)
    {
        mainLayout.removeAllViews();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        int eventIndex = eventsList.indexOf(evt);
        EventFragment fragment = EventFragment.newInstance(eventIndex, this);
        transaction.add(R.id.mainLayout, fragment);
        transaction.commit();
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
