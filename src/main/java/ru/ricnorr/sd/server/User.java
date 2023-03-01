package ru.ricnorr.sd.server;

import org.bson.Document;

public class User {
    static final String ID = "id";
    static final String CURRENCY = "currency";
    int id;
    CurrencyType currencyType;


    public User(int id, CurrencyType currencyType) {
        this.id = id;
        this.currencyType = currencyType;
    }

    public User(Document document) {
        this.id = document.getInteger(ID);
        this.currencyType = CurrencyType.valueOf(document.getString(CURRENCY));
    }

    public Document getDocument() {
        return new Document(ID, id).append(CURRENCY, currencyType);
    }

    @Override
    public String toString() {
        return String.format("User{id=%d,currencyType=%s}", id, currencyType);
    }
}
