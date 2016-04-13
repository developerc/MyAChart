package ru.saperov.myachart;

import java.util.ArrayList;

/**
 * Created by saperov on 20.03.16.
 */
public class StockModel {
    public String myStock;
    public String myCnt;
    public String myPrice;
    static ArrayList<StockModel> stockArray;

    public StockModel(String myStock, String myCnt, String myPrice) {
        this.myStock=myStock;
        this.myCnt=myCnt;
        this.myPrice=myPrice;
    }

    public static ArrayList<StockModel> getNoStock(){
        stockArray = new ArrayList<StockModel>();
        stockArray.add(new StockModel("You have not stock", "0", "0"));
        return stockArray;
    }

    public static ArrayList<StockModel> getStock(){
        stockArray = new ArrayList<StockModel>();
        for (int i=0; i<ScoreActivity.strStock.size(); i++) {
            stockArray.add(new StockModel(ScoreActivity.strStock.get(i), ScoreActivity.strCnt.get(i), ScoreActivity.strPrice.get(i)));
        }

        return stockArray;
    }
}
