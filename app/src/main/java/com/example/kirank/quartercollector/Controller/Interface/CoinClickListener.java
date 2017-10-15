package com.example.kirank.quartercollector.Controller.Interface;

import android.view.View;

/**
 * Created by kirank on 10/8/17.
 */

public interface CoinClickListener {
    void clickedOn(long coinId);
    void longClickedOn(long coinId, View longClickedOn);
}
