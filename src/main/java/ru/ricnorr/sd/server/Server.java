package ru.ricnorr.sd.server;

import io.reactivex.netty.protocol.http.server.HttpServer;
import rx.Observable;

import java.util.List;
import java.util.Map;

public class Server {
    public void start(int port) {
        HttpServer.newServer(port).start(((request, response) -> {
            var requestName = request.getDecodedPath();
            var requestParams = request.getQueryParameters();
            return switch (requestName) {
                case "/createUser" -> response.writeString(createUser(requestParams));
                case "/createGood" -> response.writeString(createGood(requestParams));
                case "/getUsers" -> response.writeString(getUsers());
                case "/getGoods" -> response.writeString(getGoods(requestParams));
                default -> response.writeString(Observable.just("Error"));
            };
        })).awaitShutdown();
    }

    private Observable<String> createUser(Map<String, List<String>> requestParams) {
        var idOptional = requestParams.get(User.ID).stream().findAny();
        if (idOptional.isEmpty()) {
            return Observable.just("Error");
        }
        var id = Integer.parseInt(idOptional.get());

        var currencyOptional = requestParams.get(User.CURRENCY).stream().findAny();
        if (currencyOptional.isEmpty()) {
            return Observable.just("Error");
        }
        var currency = currencyOptional.get();

        var user = new User(id, CurrencyType.valueOf(currency));
        ReactiveMongoDriver.addUser(user);
        return Observable.just("Created: " + user);
    }

    private Observable<String> createGood(Map<String, List<String>> requestParams) {
        var nameOptional = requestParams.get(Good.NAME).stream().findAny();
        if (nameOptional.isEmpty()) {
            return Observable.just("Error");
        }
        var name = nameOptional.get();

        var priceOptional = requestParams.get(Good.PRICE).stream().findAny();
        if (priceOptional.isEmpty()) {
            return Observable.just("Error");
        }
        var price = Integer.parseInt(priceOptional.get());

        var good = new Good(name, price);
        ReactiveMongoDriver.addGood(good);

        return Observable.just("Created: " + good.toString(CurrencyType.DOLLAR));
    }

    private Observable<String> getUsers() {
        return ReactiveMongoDriver.users().map(User::toString);
    }

    private Observable<String> getGoods(Map<String, List<String>> requestParams) {
        var userId = Integer.parseInt(requestParams.get(User.ID).stream().findAny().get());
        var goods = ReactiveMongoDriver.goods();

        return ReactiveMongoDriver.getUser(userId).map(u -> u.currencyType).flatMap(currencyType -> goods.map(g -> g.toString(currencyType)));
    }
}
