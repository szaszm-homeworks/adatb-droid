package tk.szaszm.adatb;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by marci on 12/4/17.
 */

public class NewsDialogFragment extends DialogFragment {
    public static final String TAG = "NewsDialogFragment";
    private String title;
    private String text;

    public static NewsDialogFragment newInstance(String title, String text)
    {
        NewsDialogFragment newsDialogFragment = new NewsDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("text", text);
        newsDialogFragment.setArguments(args);

        return newsDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        title = args.getString("title");
        text = args.getString("text");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.news)
                .setView(getContentView())
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> { })
                .create();
    }

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_news_dialog, null);
        TextView title = contentView.findViewById(R.id.newsItemTitle);
        TextView content = contentView.findViewById(R.id.newsItemContent);
        title.setText(this.title);
        content.setText(this.text);
        return contentView;
    }
}
