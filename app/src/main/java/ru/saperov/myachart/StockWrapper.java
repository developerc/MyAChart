package ru.saperov.myachart;

/**
 * Created by saperov on 20.03.16.
 */
public class StockWrapper {
    private int _id;
    private String mStock;
    private String mCnt;
    private String mPrice;

    // Пустой констуктор
    public StockWrapper(){

    }

    // Конструктор с параметрами
    public StockWrapper(int id, String stock, String cnt, String price){
        this._id = id;
        this.mStock = stock;
        this.mCnt = cnt;
        this.mPrice = price;
    }

    // Конструктор с параметрами
    public StockWrapper(String stock, String cnt, String price){
        this.mStock = stock;
        this.mCnt = cnt;
        this.mPrice = price;
    }

    // Создание геттеров-сеттеров
    public int getID() {
        return this._id;
    }

    public void setID(int id) {
        this._id = id;
    }

    public String getStock(){
        return this.mStock;
    }

    public void setStock(String stock){
        this.mStock=stock;
    }

    public String getCnt(){
        return this.mCnt;
    }

    public void setCnt(String cnt){
        this.mCnt=cnt;
    }

    public String getPrice(){
        return this.mPrice;
    }

    public void setPrice(String price){
        this.mPrice=price;
    }
}
