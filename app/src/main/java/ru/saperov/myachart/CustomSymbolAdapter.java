package ru.saperov.myachart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by saperov on 13.03.16.
 */
public class CustomSymbolAdapter extends ArrayAdapter<Symbol> {
    public CustomSymbolAdapter(Context context, ArrayList<Symbol> symbols){
        super(context, 0, symbols);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Symbol symbol = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_symbol, parent, false);
        }

        TextView tvSymbol = (TextView) convertView.findViewById(R.id.tvSymbol);
        tvSymbol.setText((CharSequence) symbol.mySymbol);

        return convertView;
    }
}
