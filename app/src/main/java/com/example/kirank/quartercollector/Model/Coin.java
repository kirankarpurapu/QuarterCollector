package com.example.kirank.quartercollector.Model;

import java.util.Random;

/**
 * Created by kirank on 10/8/17.
 */

public class Coin {
    private final String nameOfCoin;
    private final long id;
    private boolean isSelected = false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected() {
        this.isSelected = true;
    }

    public void setUnSelected() {
        this.isSelected = false;
    }

    public Coin(final String nameOfCoin) {
        this.nameOfCoin = nameOfCoin;
        this.id = new Random().nextInt(0xFFFFFFF);
    }

    public Coin(final String nameOfCoin, long id) {
        this.nameOfCoin = nameOfCoin;
        this.id = id;
    }

    public Coin(final String nameOfCoin, long id, boolean isSelected) {
        this.nameOfCoin = nameOfCoin;
        this.id = id;
        this.isSelected = isSelected;
    }

    public Coin(final Coin otherCoin) {
        if (otherCoin == null) {
            throw new NullPointerException("got a null coin to create a new coin");
        } else {
            this.nameOfCoin = otherCoin.getNameOfCoin();
            this.isSelected = otherCoin.isSelected;
            this.id = otherCoin.getId();
        }
    }

    public String getNameOfCoin() {
        return this.nameOfCoin;
    }


    @Override
    public String toString() {
        return "Coin{" +
                "nameOfCoin='" + nameOfCoin + '\'' + '}';
    }

    public long getId() {
        return this.id;
    }
}
