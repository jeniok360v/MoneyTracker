package pl.cyfrogen.moneyTracker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import pl.cyfrogen.budget.R;

public class ItemCategoriesListViewAdapter extends ArrayAdapter<CategoryModelHome> implements View.OnClickListener {

    private ArrayList<CategoryModelHome> dataSet;
    Context context;


    public ItemCategoriesListViewAdapter(ArrayList<CategoryModelHome> data, Context context) {
        super(context, R.layout.favorites_listview_row, data);
        this.dataSet = data;
        this.context = context;

    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.favorites_listview_row, parent, false);

        CategoryModelHome dataModel = getItem(position);
        CategoryModel categoryModel = dataModel.getCategoryModel();

        TextView categoryNameTextView = listItem.findViewById(R.id.item_category);
        TextView sumTextView = listItem.findViewById(R.id.item_sum);
        ImageView iconImageView = listItem.findViewById(R.id.icon_imageview);

        iconImageView.setImageResource(categoryModel.getIconResourceID());
        iconImageView.setBackgroundTintList(ColorStateList.valueOf(categoryModel.getIconColor()));

        categoryNameTextView.setText(dataModel.getCategoryName());
        sumTextView.setText(dataModel.getCurrency().formatString(dataModel.getMoney()));
        if (dataModel.getMoney() < 0)
            sumTextView.setTextColor(ContextCompat.getColor(context, R.color.gauge_expense));
        else
            sumTextView.setTextColor(ContextCompat.getColor(context, R.color.gauge_income));
        return listItem;
    }
}
