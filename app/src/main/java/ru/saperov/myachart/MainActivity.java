package ru.saperov.myachart;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;
import yahoofinance.quotes.fx.FxQuote;
import yahoofinance.quotes.fx.FxSymbols;

public class MainActivity extends AppCompatActivity {
    LineChart lineChart;
    private ProgressBar spinner;
    private static final String TAG = "myLogs";
    public static ArrayList<String> strSymbol = new ArrayList<String>();
    public static int numSymbol;
    public static boolean symbolSelected = false;
    FxQuote usdrub;
    Stock google;
    List<HistoricalQuote> googleHistQuotes;
    ArrayList<Entry> entries = new ArrayList<>();
    ArrayList<String> labels = new ArrayList<>();
    public static int symbolPosition;
    public static boolean sellStock;
    public static BigDecimal price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
        //------------------------------------

        lineChart = (LineChart) findViewById(R.id.chart);
        //creating list of entry
//        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(4f, 0));
        entries.add(new Entry(8f, 1));
        entries.add(new Entry(6f, 2));
        entries.add(new Entry(3f, 3));
        entries.add(new Entry(5.4f, 4));
        entries.add(new Entry(10f, 5));

        LineDataSet dataSet = new LineDataSet(entries, "#Changing stock price");
        dataSet.setColor(Color.GREEN);
        //creating labels
//        ArrayList<String> labels = new ArrayList<>();
        labels.add("january");
        labels.add("february");
        labels.add("march");
        labels.add("april");
        labels.add("may");
        labels.add("june");

        //lineChart.setDrawGridBackground(true);
        //lineChart
       // lineChart.setBackgroundColor(Color.BLACK);

       // lineChart.setGridBackgroundColor(Color.RED);
        //set data
        //lineChart.set
        LineData data = new LineData(labels, dataSet);
        lineChart.setData(data);
       // lineChart.setDescription("Описание");
        lineChart.animateY(1000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_run) {
            return true;
        }*/

        switch (id) {
            case R.id.action_run: RunScoreActivity();
                break;
            case R.id.action_sym_st: ChusingSimbol();
                break;
            case R.id.action_cur_pr: ChusingCurrency();
                break;
            case R.id.action_about: RunAbout();
        }

        return super.onOptionsItemSelected(item);
    }

    private void RunAbout(){
        startActivity(new Intent(getApplicationContext(),AboutActivity.class));
    }

    private void RunScoreActivity() {
        startActivity(new Intent(getApplicationContext(),ScoreActivity.class));
    }

    private void ChusingSimbol() {
        //Здесь выбираем из списка Symbol
        strSymbol.clear();
        InputStream is = getResources().openRawResource(R.raw.sym3);
        InputStreamReader inputStreamReader = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String line;
        try {
            while ((line = reader.readLine())!=null){
                // line = reader.readLine();
                strSymbol.add(line);
               // Log.d(TAG, line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        startActivity(new Intent(getApplicationContext(),SymbolStock.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "MainActivity: onResume()");
        if(symbolSelected){
            //Toast.makeText(getApplicationContext(), "Вы выбрали position " + numSymbol + " " + strSymbol.get(numSymbol), Toast.LENGTH_SHORT).show();

        //скачиваем данные с Yahoo
            new GetAsincTask().execute("");
        }
        symbolSelected=false;
    }

    public class GetAsincTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            spinner.setVisibility(View.VISIBLE);
            //listView.setVisibility(View.GONE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
               // usdrub = YahooFinance.getFx("USDRUB=X");
                Calendar from = Calendar.getInstance();
                Calendar to = Calendar.getInstance();
                from.add(Calendar.YEAR, -1); // from 1 year ago

                String yhoo = strSymbol.get(numSymbol);
                String[] yhooArr = yhoo.split(";");


                google = YahooFinance.get(yhooArr[0]);
                Log.d(TAG, "google=" + google);
                //google = YahooFinance.get("YHOO");
                googleHistQuotes = google.getHistory(from, to, Interval.WEEKLY);
            } catch (IOException e) {
               // e.printStackTrace();
                Toast.makeText(getApplicationContext(), "IO Error  " + e, Toast.LENGTH_SHORT).show();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            entries.clear();
            labels.clear();
            double Cost = 0;
            String myAnnotation = null;
            spinner.setVisibility(View.GONE);
           // Log.d(TAG, String.valueOf(usdrub));

            // Log.d(TAG, String.valueOf(google.getHistory()));
            for(int i=0;i<googleHistQuotes.size();i++){
                Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(String.valueOf(googleHistQuotes.get(googleHistQuotes.size()-i-1)));  //вытаскиваем подстроку из строки regex - пом
                while(m.find()) {
                    Cost = Double.parseDouble(m.group(1));
                    Log.d(TAG, String.valueOf(m.group(1)));
                }
                Matcher m2 = Pattern.compile("\\@([^)]+)\\:").matcher(String.valueOf(googleHistQuotes.get(googleHistQuotes.size()-i-1)));  //вытаскиваем подстроку из строки regex - пом
                while(m2.find()) {
                    myAnnotation = String.valueOf(m2.group(1));
                    Log.d(TAG, String.valueOf(m2.group(1)));
                }
                //изменяем график
                entries.add(new Entry((float) Cost, i));
                labels.add(myAnnotation);
                LineDataSet dataSet = new LineDataSet(entries, strSymbol.get(numSymbol));
                dataSet.setColor(Color.GREEN);
                LineData data = new LineData(labels, dataSet);
                lineChart.setData(data);
                lineChart.animateY(1000);

            }

          /*  for(int i=0;i<googleHistQuotes.size();i++){
                Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(String.valueOf(googleHistQuotes.get(googleHistQuotes.size()-i-1)));  //вытаскиваем подстроку из строки regex - пом
                while(m.find()) {
                    Cost = Double.parseDouble(m.group(1));
                    Log.d(TAG, String.valueOf(m.group(1)));
                }
                Matcher m2 = Pattern.compile("\\@([^)]+)\\:").matcher(String.valueOf(googleHistQuotes.get(googleHistQuotes.size()-i-1)));  //вытаскиваем подстроку из строки regex - пом
                while(m2.find()) {
                    myAnnotation = String.valueOf(m2.group(1));
                    Log.d(TAG, String.valueOf(m2.group(1)));
                }
                //изменяем график
                entries.add(new Entry((float) Cost, i));
                labels.add(myAnnotation);
                LineDataSet dataSet = new LineDataSet(entries, strSymbol.get(numSymbol));
                dataSet.setColor(Color.GREEN);
                LineData data = new LineData(labels, dataSet);
                lineChart.setData(data);
                lineChart.animateY(1000);

            }*/

        }

    }

    private void ChusingCurrency() {

        new GetACurrTask().execute("USDRUB=X");
    }

    public class GetACurrTask extends AsyncTask<String, Void, Void> {
        FxQuote usdrub;

        @Override
        protected Void doInBackground(String... params) {
            try {
                usdrub = YahooFinance.getFx(params[0]);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d(TAG, String.valueOf(usdrub));
        }
    }
}
