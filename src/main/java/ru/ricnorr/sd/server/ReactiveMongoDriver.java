package ru.ricnorr.sd.server;

import com.mongodb.client.model.Filters;
import com.mongodb.rx.client.MongoClient;
import com.mongodb.rx.client.MongoClients;
import rx.Observable;
import rx.Subscription;

class ReactiveMongoDriver {
    final static String DATABASE_NAME = "software-design-rx";
    final static String USERS_COLLECTION = "users";
    final static String GOODS_COLLECTION = "goods";
    private static final MongoClient client = createMongoClient();

    static Subscription addUser(User user) {
        return client.getDatabase(DATABASE_NAME)
                .getCollection(USERS_COLLECTION)
                .insertOne(user.getDocument())
                .subscribe();
    }

    static Subscription addGood(Good good) {
        return client
                .getDatabase(DATABASE_NAME)
                .getCollection(GOODS_COLLECTION)
                .insertOne(good.getDocument())
                .subscribe();
    }

    static Observable<User> users() {
        return client
                .getDatabase(DATABASE_NAME)
                .getCollection(USERS_COLLECTION)
                .find()
                .toObservable()
                .map(User::new);
    }

    static Observable<Good> goods() {
        return client
                .getDatabase(DATABASE_NAME)
                .getCollection(GOODS_COLLECTION)
                .find()
                .toObservable()
                .map(Good::new);
    }

    static Observable<User> getUser(int id) {
        return client
                .getDatabase(DATABASE_NAME)
                .getCollection(USERS_COLLECTION)
                .find(Filters.eq(User.ID, id))
                .toObservable()
                .map(User::new);
    }

    static private MongoClient createMongoClient() {
        return MongoClients.create("mongodb://localhost:27017");
    }
}
