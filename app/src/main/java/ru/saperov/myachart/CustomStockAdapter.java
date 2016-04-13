package ru.saperov.myachart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by saperov on 20.03.16.
 */
public class CustomStockAdapter extends ArrayAdapter<StockModel>{
    public CustomStockAdapter(Context context, ArrayList<StockModel> stockModels) {
        super(context, 0, stockModels);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        StockModel stockModel = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_stock, parent, false);
        }
        TextView tvStock = (TextView) convertView.findViewById(R.id.tvStock);
        TextView tvCnt = (TextView) convertView.findViewById(R.id.tvCnt);
        TextView tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);

        tvStock.setText(stockModel.myStock);
        tvCnt.setText(stockModel.myCnt);
        tvPrice.setText(stockModel.myPrice);

        return convertView;
    }
}
