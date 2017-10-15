package com.example.kirank.quartercollector.Data;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by kirank on 10/14/17.
 */

@Table(database = CoinCollectorDB.class)
public class CoinDBObject extends BaseModel {

    @Column
    @PrimaryKey
    private long id;

    @Column
    private String coinName;

    @Column
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(final boolean selected) {
        isSelected = selected;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(final String coinName) {
        this.coinName = coinName;
    }
}
