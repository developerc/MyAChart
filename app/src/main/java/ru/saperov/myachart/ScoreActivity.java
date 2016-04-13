package ru.saperov.myachart;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ScoreActivity extends AppCompatActivity {
    ArrayList<StockModel> arrayOfStock;
    CustomStockAdapter adapter;
    ListView lvListStock;
    SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_NAME = "Nickname";
    public static final String APP_PREFERENCES_MONEY = "Money";
    private static DatabaseHelper mDatabaseHelper;
    public static ArrayList<String> strStock = new ArrayList<String>();
    public static ArrayList<String> strCnt = new ArrayList<String>();
    public static ArrayList<String> strPrice = new ArrayList<String>();
    Button btnName;
    EditText etName;
    TextView tvHwMoney, tvTotal;
    private static final String TAG = "myLogs";
    final String BROKER_NAME = "Broker_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        mDatabaseHelper = new DatabaseHelper(this);

        setTitle("Score");
        showFromDataBase();
        populateStockList();

        etName = (EditText) this.findViewById(R.id.etName);
        tvHwMoney = (TextView) findViewById(R.id.tvHwMoney);
        tvTotal = (TextView) findViewById(R.id.tvTotal);

        btnName=(Button) this.findViewById(R.id.btnOK);
        btnName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBrokerName();
            }
        });

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        loadBrokerName();
    }

    public void setBrokerName(){
       /* sPref = this.getSharedPreferences("pref", 0);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(BROKER_NAME, etName.getText().toString());
        ed.commit();*/
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_NAME, etName.getText().toString());
        editor.putFloat(APP_PREFERENCES_MONEY, 1000f);
        editor.commit();
        mDatabaseHelper.delAllStock();
        loadBrokerName();
    }

    public void loadBrokerName(){
       /* sPref = this.getSharedPreferences("pref", 0);
        etName.setText(sPref.getString(BROKER_NAME, ""));*/
        float totalSum;
        if (mSettings.contains(APP_PREFERENCES_NAME)) {
        etName.setText(mSettings.getString(APP_PREFERENCES_NAME, ""));
        tvHwMoney.setText("You have money: $" + mSettings.getFloat(APP_PREFERENCES_MONEY, 0));
            totalSum = getStockSum() + mSettings.getFloat(APP_PREFERENCES_MONEY, 0);
            tvTotal.setText("Total: $" + totalSum);

        } else {

        tvHwMoney.setText("You have money: $0");
        }
    }

    public float getStockSum(){
        float stockSum = 0f;
        List<StockWrapper> lstWrapperStock;
        ArrayList<String> lstPrice = new ArrayList<String>();
        ArrayList<String> lstCnt = new ArrayList<String>();
        lstPrice.clear();
        lstCnt.clear();
        if(mDatabaseHelper.getStockCount() == 0) {
            return 0f;
        } else {
            lstWrapperStock = mDatabaseHelper.getAllStock();
            for (int i = 0; i < lstWrapperStock.size(); i++) {
                lstPrice.add(lstWrapperStock.get(i).getPrice());
                lstCnt.add(lstWrapperStock.get(i).getCnt());
                stockSum = stockSum + Float.valueOf(lstWrapperStock.get(i).getPrice()) * Integer.valueOf(lstWrapperStock.get(i).getCnt());
                Log.d(TAG, i + " lstPrice=" + lstWrapperStock.get(i).getPrice() + " lstCnt=" + lstWrapperStock.get(i).getCnt() + " Итого=" + stockSum);
            }
            return stockSum;
        }
    }

    private void showFromDataBase() {
        strStock.clear();
        strCnt.clear();
        strPrice.clear();
        if(mDatabaseHelper.getStockCount()>0){
            List<StockWrapper> stockList= mDatabaseHelper.getAllStock();
            for(int i=0; i<stockList.size(); i++){
                strStock.add(stockList.get(i).getStock());
                strCnt.add(stockList.get(i).getCnt());
                strPrice.add(stockList.get(i).getPrice());
            }
        }
    }

    private void populateStockList() {
        if(mDatabaseHelper.getStockCount()>0){
            arrayOfStock = StockModel.getStock();
            adapter = new CustomStockAdapter(this, arrayOfStock);
            lvListStock = (ListView) findViewById(R.id.lvStock);
            lvListStock.setAdapter(adapter);
        } else {
            arrayOfStock = StockModel.getNoStock();
            adapter = new CustomStockAdapter(this, arrayOfStock);
            lvListStock = (ListView) findViewById(R.id.lvStock);
            lvListStock.setAdapter(adapter);
        }
    }
}
