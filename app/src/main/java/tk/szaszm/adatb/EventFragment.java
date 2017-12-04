package tk.szaszm.adatb;


import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tk.szaszm.adatb.model.Events;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventFragment extends Fragment {
    private int eventIndex;
    private MainActivity activity;

    private TextView placeView;
    private TextView demonstratorView;
    private TextView exerciseTypeView;
    private TextView gradeView;
    private TextView gradeLabel;


    public EventFragment() {
        // Required empty public constructor
    }


    public static EventFragment newInstance(int eventIndex, MainActivity activity) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putInt("eventIndex", eventIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventIndex = getArguments().getInt("eventIndex");
        }
        activity = (MainActivity) getActivity();
    }

    private Events getEvent() { return activity.getEventByIndex(eventIndex); }

    void initView()
    {
        Events event = getEvent();

        placeView.setText(event.location);
        demonstratorView.setText(event.getDemonstrator().displayName);
        exerciseTypeView.setText(event.getEventTemplate().getExerciseCategory().type);
        gradeView.setText(Integer.toString(event.grade) + (event.finalized ? " (final)" : " (not final)"));
        gradeLabel.setTypeface(null, event.finalized ? Typeface.BOLD : Typeface.NORMAL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_event, container, false);
        
        placeView = layout.findViewById(R.id.eventPlace);
        demonstratorView = layout.findViewById(R.id.eventDemonstrator);
        exerciseTypeView = layout.findViewById(R.id.eventExerciseType);
        gradeView = layout.findViewById(R.id.eventGrade);
        gradeLabel = layout.findViewById(R.id.eventGradeLabel);

        initView();

        return layout;
    }

}
