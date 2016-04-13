package ru.saperov.myachart;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class SymbolStock extends AppCompatActivity {
    private ProgressBar spinner;
    ArrayList<Symbol> symbolArrayList;
    CustomSymbolAdapter adapter;
    ListView lvSymbol;
    private static final String TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symbol_stock);

        lvSymbol = (ListView) findViewById(R.id.lvSymbol);
        setTitle("Select company");
        populateSymbolList();
        lvSymbol.setOnItemClickListener(itemClickListener);
        registerForContextMenu(lvSymbol);

        spinner = (ProgressBar)findViewById(R.id.progressBar2);
        spinner.setVisibility(View.GONE);
       /* lvSymbol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "itemSelect: position = " + position + ", id = "
                        + id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
       /* lvSymbol.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "itemSelect: position = " + position);
                return false;
            }
        });*/
    }

    private void populateSymbolList() {
        symbolArrayList = Symbol.getSymbols();
        adapter = new CustomSymbolAdapter(this, symbolArrayList);
//        lvSymbol = (ListView) findViewById(R.id.lvSymbol);
        lvSymbol.setAdapter(adapter);
    }

    //обрабатываем нажатие на списке символов
    protected AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            view.setSelected(true);
            //Toast.makeText(getApplicationContext(), "Вы выбрали position " + position + " " + MainActivity.strSymbol.get(position), Toast.LENGTH_SHORT).show();
            MainActivity.numSymbol = position;
            MainActivity.symbolSelected = true;
            finish();
        }
    };

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        //Toast.makeText(getApplicationContext(), "Вы нажали на ListView " , Toast.LENGTH_SHORT).show();
        if (v.getId()==R.id.lvSymbol) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            MainActivity.symbolPosition = info.position;
            Log.d(TAG, "info position = " + info.position);
            menu.setHeaderTitle( MainActivity.strSymbol.get(info.position));
            menu.add(0, 1, 0, "You want buy stock?");
            menu.add(0, 2, 0, "You want sell stock?");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        String symFull;
        String [] symArr;
        switch (item.getItemId()) {
            case 1:{
                //Toast.makeText(getApplicationContext(), "Вы нажали на buy stock" , Toast.LENGTH_SHORT).show();
                symFull = MainActivity.strSymbol.get(MainActivity.symbolPosition);
                Log.d(TAG, symFull);
                symArr = symFull.split(";");
                Log.d(TAG, symArr[0]);
                //скачиваем данные с Yahoo
                new StockAsincTask().execute(symArr[0]);
                MainActivity.sellStock = false;
               // startActivity(new Intent(getApplicationContext(),BuySellActivity.class));
                break;
            }
            case 2:{
                //Toast.makeText(getApplicationContext(), "Вы нажали на sell stock" , Toast.LENGTH_SHORT).show();
                symFull = MainActivity.strSymbol.get(MainActivity.symbolPosition);
                Log.d(TAG, symFull);
                symArr = symFull.split(";");
                Log.d(TAG, symArr[0]);
                //скачиваем данные с Yahoo
                new StockAsincTask().execute(symArr[0]);
                MainActivity.sellStock = true;
               // startActivity(new Intent(getApplicationContext(),BuySellActivity.class));
                break;
            }
        }
        return super.onContextItemSelected(item);
    }

    public class StockAsincTask extends AsyncTask<String, Void, Void> {
        Stock stock;
      //  BigDecimal price;
      @Override
      protected void onPreExecute() {
          spinner.setVisibility(View.VISIBLE);
          //listView.setVisibility(View.GONE);
          super.onPreExecute();
      }

        @Override
        protected Void doInBackground(String... params) {
            try {
                stock = YahooFinance.get(params[0]);
                MainActivity.price = stock.getQuote().getPrice();
                Log.d(TAG, String.valueOf(stock));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            spinner.setVisibility(View.GONE);
            Log.d(TAG, String.valueOf(MainActivity.price));
            startActivity(new Intent(getApplicationContext(),BuySellActivity.class));
        }
    }

}
