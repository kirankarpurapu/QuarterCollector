package com.example.kirank.quartercollector.Data;

import com.example.kirank.quartercollector.Model.Coin;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kirank on 10/8/17.
 */

public class CoinSource {

    private static ArrayList<Coin> coins = new ArrayList<>();

    static {
        final List<CoinDBObject> coinDBObjectList = SQLite.select().from(CoinDBObject.class).queryList();

        coinDBObjectList.forEach(coinDBObject ->
                coins.add(new Coin(coinDBObject.getCoinName(), coinDBObject.getId(), coinDBObject.isSelected())));
    }

    private CoinSource() {
    }

    public static ArrayList<Coin> getCoins() {
        return coins;
    }

    public static void addCoin(final Coin coin) {

        if (coin != null) {
            coins.add(coin);
            final CoinDBObject coinDBObject = new CoinDBObject();
            coinDBObject.setCoinName(coin.getNameOfCoin());
            coinDBObject.setId(coin.getId());
            coinDBObject.setSelected(false);
            coinDBObject.save();

        } else {
            throw new NullPointerException("Cannot add a null coin to the data source");
        }
    }

    public static int getSize() {

        return coins.size();
    }

    public static boolean isNamePresent(final String newCoinNameString) {

        return coins.stream().map(Coin::getNameOfCoin).anyMatch(name -> name.equals(newCoinNameString));
    }

    public static void setAllCoinsAsUnselected() {

        coins.forEach(Coin::setUnSelected);
    }

    public static Coin getCoinWithId(final long coinId) {
        for (Coin coin : coins) {
            if (coin.getId() == coinId) {
                return new Coin(coin);
            }
        }
        return null;
    }

    public static void setCoinWithIdAsSelected(final long coinId) {

//        final List<CoinDBObject> coinDBObjectCollection = SQLite.select().
//                from(CoinDBObject.class).where(CoinDBObject_Table.id.eq(coinId)).queryList();
//
//        for(CoinDBObject coinDBObject : coinDBObjectCollection) {
//            coinDBObject.setSelected(true);
//            coinDBObject.save();
//        }

//        coins = SQLite.select().
//                from(CoinDBObject.class).queryList().
//                stream().map(coinDBObject -> new Coin(coinDBObject.getCoinName(), coinDBObject.getId(), coinDBObject.isSelected())).
//                collect(Collectors.toCollection(ArrayList::new));

        coins.stream().filter(coin -> coin.getId() == coinId).forEach(Coin::setSelected);
    }

    public static void deleteCoinWithId(final Long coinId) {

        final List<CoinDBObject> coinDBObjectCollection = SQLite.select().
                from(CoinDBObject.class).where(CoinDBObject_Table.id.eq(coinId)).queryList();

        coinDBObjectCollection.forEach(BaseModel::delete);

        for (int i = 0; i < coins.size(); i++) {
            if (coins.get(i).getId() == coinId) {
                coins.remove(i);
                break;
            }
        }
    }

    public static void setCoinWithIdAsUnSelected(final long coinId) {

//        final List<CoinDBObject> coinDBObjectCollection = SQLite.select().
//                from(CoinDBObject.class).where(CoinDBObject_Table.id.eq(coinId)).queryList();
//
//        for(CoinDBObject coinDBObject : coinDBObjectCollection) {
//            coinDBObject.setSelected(false);
//            coinDBObject.save();
//        }
//
//        coins = SQLite.select().
//                from(CoinDBObject.class).queryList().
//                stream().map(coinDBObject -> new Coin(coinDBObject.getCoinName(), coinDBObject.getId(), coinDBObject.isSelected())).
//                collect(Collectors.toCollection(ArrayList::new));

        coins.stream().filter(coin -> coin.getId() == coinId).forEach(Coin::setUnSelected);
    }
}
