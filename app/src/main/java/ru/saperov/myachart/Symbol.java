package ru.saperov.myachart;

import java.util.ArrayList;

/**
 * Created by saperov on 13.03.16.
 */
public class Symbol {
    public String mySymbol;
    static ArrayList<Symbol> symbols;

    public Symbol(String mySymbol) {this.mySymbol=mySymbol;}

    public static ArrayList<Symbol> getSymbols(){
        symbols = new ArrayList<Symbol>();
        for(int i=0; i<MainActivity.strSymbol.size(); i++)
            symbols.add(new Symbol(MainActivity.strSymbol.get(i)));

                return symbols;
    }
}
