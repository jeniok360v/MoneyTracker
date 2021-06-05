package pl.project.moneyTracker;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.example.budget.R;

public class OptionsFragment extends Fragment {
    public static final CharSequence TITLE = "Options";

    public static OptionsFragment newInstance() {

        return new OptionsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_options, container, false);
    }

}
