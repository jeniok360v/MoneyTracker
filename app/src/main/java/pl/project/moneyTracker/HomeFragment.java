package pl.project.moneyTracker;

import android.app.ActivityOptions;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pl.cyfrogen.budget.R;
import pl.project.moneyTracker.firebase.ListDataSet;
import pl.project.moneyTracker.firebase.UserProfileViewModelFactory;
import pl.project.moneyTracker.firebase.WalletEntriesViewModelFactory;
import pl.project.moneyTracker.firebase.models.User;
import pl.project.moneyTracker.libraries.Gauge;
import pl.project.moneyTracker.firebase.models.WalletEntry;

public class HomeFragment extends BaseFragment {
    private User userData;
    private ListDataSet<WalletEntry> walletEntryListDataSet;

    public static final CharSequence TITLE = "Home";
    private ListView favoriteListView;
    private FloatingActionButton addEntryButton;
    private Gauge gauge;
    private ItemCategoriesListViewAdapter adapter;
    private ArrayList<CategoryModelHome> categoryModelsHome;
    private TextView totalBalanceTextView;
    private TextView gaugeLeftBalanceTextView;
    private TextView gaugeLeftLine1TextView;
    private TextView gaugeLeftLine2TextView;
    private TextView gaugeRightBalanceTextView;
    private TextView gaugeRightLine1TextView;
    private TextView gaugeRightLine2TextView;
    private TextView gaugeBalanceLeftTextView;

    public static HomeFragment newInstance() {

        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        System.out.println("#FRAGMENT VIEW");
        categoryModelsHome = new ArrayList<>();

        gauge = view.findViewById(R.id.gauge);
        gauge.setValue(50);

        totalBalanceTextView = view.findViewById(R.id.total_balance_textview);
        gaugeLeftBalanceTextView = view.findViewById(R.id.gauge_left_balance_text_view);
        gaugeLeftLine1TextView = view.findViewById(R.id.gauge_left_line1_textview);
        gaugeLeftLine2TextView = view.findViewById(R.id.gauge_left_line2_textview);
        gaugeRightBalanceTextView = view.findViewById(R.id.gauge_right_balance_text_view);
        gaugeRightLine1TextView = view.findViewById(R.id.gauge_right_line1_textview);
        gaugeRightLine2TextView = view.findViewById(R.id.gauge_right_line2_textview);
        gaugeBalanceLeftTextView = view.findViewById(R.id.left_balance_textview);


        favoriteListView = view.findViewById(R.id.favourite_categories_list_view);
        adapter = new ItemCategoriesListViewAdapter(categoryModelsHome, getActivity().getApplicationContext());
        favoriteListView.setAdapter(adapter);

        addEntryButton = view.findViewById(R.id.add_wallet_entry_fab);
        addEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(getActivity(), addEntryButton, addEntryButton.getTransitionName());
                    startActivity(new Intent(getActivity(), AddBudgetEntryActivity.class), options.toBundle());
                } else {
                    startActivity(new Intent(getActivity(), AddBudgetEntryActivity.class));
                }

            }
        });

        WalletEntriesViewModelFactory.getModel(getUid(), getActivity()).observe(this, new Observer<ListDataSet<WalletEntry>>() {

            @Override
            public void onChanged(@Nullable ListDataSet<WalletEntry> walletEntryListDataSet) {
                HomeFragment.this.walletEntryListDataSet = walletEntryListDataSet;
                dataUpdated();
            }
        });

        UserProfileViewModelFactory.getModel(getUid(), getActivity()).observe(this, new Observer<User>() {

            @Override
            public void onChanged(@Nullable User user) {
                HomeFragment.this.userData = user;
                dataUpdated();
            }
        });




    }

    private void dataUpdated() {
        if (userData == null || walletEntryListDataSet == null) return;

        List<WalletEntry> entryList = new ArrayList<>(walletEntryListDataSet.getList());


        Calendar startDate = getStartDate(userData);
        Calendar endDate = getEndDate(userData);

        DateFormat dateFormat = new SimpleDateFormat("dd-MM");

        long sum = 0;
        for (WalletEntry walletEntry : entryList) {
            sum += walletEntry.balanceDifference;
        }


        Iterator<WalletEntry> iterator = entryList.iterator();
        while (iterator.hasNext()) {
            long timestamp = -iterator.next().timestamp;
            if (timestamp < startDate.getTimeInMillis() || timestamp > endDate.getTimeInMillis())
                iterator.remove();
        }


        long expensesSumInDateRange = 0;
        long incomesSumInDateRange = 0;

        HashMap<CategoryModel, Long> categoryModels = new HashMap<>();
        for (WalletEntry walletEntry : entryList) {
            if (walletEntry.balanceDifference > 0) {
                incomesSumInDateRange += walletEntry.balanceDifference;
                continue;
            }
            expensesSumInDateRange += walletEntry.balanceDifference;
            CategoryModel categoryModel = DefaultCategoryModels.searchCategory(walletEntry.categoryID);
            if (categoryModels.get(categoryModel) != null)
                categoryModels.put(categoryModel, categoryModels.get(categoryModel) + walletEntry.balanceDifference);
            else
                categoryModels.put(categoryModel, walletEntry.balanceDifference);

        }

        categoryModelsHome.clear();
        for (Map.Entry<CategoryModel, Long> categoryModel : categoryModels.entrySet()) {
            categoryModelsHome.add(new CategoryModelHome(categoryModel.getKey(), categoryModel.getKey().getCategoryVisibleName(getContext()), Currency.DEFAULT, categoryModel.getValue()));
        }

        Collections.sort(categoryModelsHome, new Comparator<CategoryModelHome>() {
            @Override
            public int compare(CategoryModelHome o1, CategoryModelHome o2) {
                return Long.compare(o1.getMoney(), o2.getMoney());
            }
        });


        adapter.notifyDataSetChanged();
        totalBalanceTextView.setText(Currency.DEFAULT.formatString(sum));

        boolean showLimit = false;
        if (showLimit) {

        } else {
            gaugeLeftBalanceTextView.setText(Currency.DEFAULT.formatString(incomesSumInDateRange));
            gaugeLeftLine1TextView.setText("Incomes");
            gaugeLeftLine2TextView.setVisibility(View.INVISIBLE);
            gaugeRightBalanceTextView.setText(Currency.DEFAULT.formatString(expensesSumInDateRange));
            gaugeRightLine1TextView.setText("Expenses");
            gaugeRightLine2TextView.setVisibility(View.INVISIBLE);

            gauge.setPointStartColor(ContextCompat.getColor(getContext(), R.color.gauge_income));
            gauge.setPointEndColor(ContextCompat.getColor(getContext(), R.color.gauge_income));
            gauge.setStrokeColor(ContextCompat.getColor(getContext(), R.color.gauge_expense));
            gauge.setValue((int) (incomesSumInDateRange * 100 / (incomesSumInDateRange - expensesSumInDateRange)));

            gaugeBalanceLeftTextView.setText(dateFormat.format(startDate.getTime()) + " - " +
                    dateFormat.format(endDate.getTime()));


        }
    }

    private Calendar getStartDate(User userData) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

        return cal;
    }

    private Calendar getEndDate(User userData) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        cal.add(Calendar.DAY_OF_YEAR, 6);
        return cal;
    }
}
