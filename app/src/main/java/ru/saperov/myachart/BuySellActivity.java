package ru.saperov.myachart;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BuySellActivity extends AppCompatActivity {
    TextView tvHat;
    EditText etCnt;
    private static DatabaseHelper mDatabaseHelper;
    private static final String TAG = "myLogs";
    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_sell);

        tvHat = (TextView) findViewById(R.id.tvSellBuyPrice);
        if(MainActivity.sellStock) {
        setTitle("Selling stock");
            tvHat.setText("You selling stock " + MainActivity.strSymbol.get(MainActivity.symbolPosition) + "\n" + "price:" + String.valueOf(MainActivity.price));
        } else {
            setTitle("Buying stock");
            tvHat.setText("You buying stock " + MainActivity.strSymbol.get(MainActivity.symbolPosition) + "\n" + " price:" + String.valueOf(MainActivity.price));
        }
        etCnt = (EditText) findViewById(R.id.etCnt);
        mDatabaseHelper = new DatabaseHelper(this);
        mSettings = getSharedPreferences(ScoreActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public void onButtonOKClick(View view){
        //Toast.makeText(this, "Зачем вы нажали?", Toast.LENGTH_SHORT).show();
        float stockSum;
        try {

            if(MainActivity.sellStock) {

                //нужно sum вычислять по новой цене
                float sum = mDatabaseHelper.getIdStock(new StockWrapper(MainActivity.strSymbol.get(MainActivity.symbolPosition), etCnt.getText().toString(), String.valueOf(MainActivity.price)));
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putFloat(ScoreActivity.APP_PREFERENCES_MONEY, mSettings.getFloat(ScoreActivity.APP_PREFERENCES_MONEY, 0) + sum);
                editor.commit();

                mDatabaseHelper.delSymbolStock(new StockWrapper(MainActivity.strSymbol.get(MainActivity.symbolPosition), etCnt.getText().toString(), String.valueOf(MainActivity.price)));

                Toast.makeText(getApplicationContext(), "you sold  stock price $" + sum, Toast.LENGTH_SHORT).show();
                Log.d(TAG, String.valueOf(sum));
            } else {
                Integer.parseInt(etCnt.getText().toString());
                stockSum = Integer.valueOf(etCnt.getText().toString()) * MainActivity.price.floatValue();
                //Уменьшаем имеющуюся сумму в деньгах на величину стоимости купленных акций
                SharedPreferences.Editor editor = mSettings.edit();
                if (mSettings.getFloat(ScoreActivity.APP_PREFERENCES_MONEY, 0) - stockSum >= 0) {
                    editor.putFloat(ScoreActivity.APP_PREFERENCES_MONEY, mSettings.getFloat(ScoreActivity.APP_PREFERENCES_MONEY, 0) - stockSum);
                    mDatabaseHelper.addStock(new StockWrapper(MainActivity.strSymbol.get(MainActivity.symbolPosition), etCnt.getText().toString(), String.valueOf(MainActivity.price)));
                    Toast.makeText(getApplicationContext(), "you bought  stock price $" + stockSum, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "you have not enough money!", Toast.LENGTH_SHORT).show();
                }
                editor.commit();

         /*   if(mSettings.getFloat(ScoreActivity.APP_PREFERENCES_MONEY, 0) - stockSum >= 0)
            Toast.makeText(getApplicationContext(), "you bought  stock price $" + stockSum , Toast.LENGTH_SHORT).show();*/
                Log.d(TAG, etCnt.getText().toString() + "*" + String.valueOf(MainActivity.price) + "=" + stockSum);
            }
        } catch(NumberFormatException e) {
            Log.d(TAG, "Exception No integer value");
        } catch(NullPointerException e) {
            Log.d(TAG, "Exception No integer value");
        }
       // mDatabaseHelper.addStock(MainActivity.strSymbol.get(MainActivity.symbolPosition),);
    }
}
