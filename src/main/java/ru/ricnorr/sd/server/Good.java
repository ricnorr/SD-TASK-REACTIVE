package ru.ricnorr.sd.server;

import org.bson.Document;

public class Good {

    final static String NAME = "name";
    final static String PRICE = "price";
    String name;
    int price;

    public Good(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public Good(Document document) {
        this.name = document.getString(NAME);
        this.price = document.getInteger(PRICE);
    }

    Document getDocument() {
        return new Document(NAME, name).append(PRICE, price);
    }

    String toString(CurrencyType currencyType) {
        int convertedPrice;
        switch (currencyType) {
            case RUBLE:
                convertedPrice = price * 60;
                break;
            default:{
                convertedPrice = price;
            }
        }
        return String.format("Product{name='%s', price=%d}", name, convertedPrice);

    }
}
