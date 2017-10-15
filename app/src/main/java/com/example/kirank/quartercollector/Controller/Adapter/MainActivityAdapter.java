package com.example.kirank.quartercollector.Controller.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.kirank.quartercollector.Controller.Interface.CoinClickListener;
import com.example.kirank.quartercollector.Model.Coin;
import com.example.kirank.quartercollector.R;

import java.util.ArrayList;

/**
 * Created by kirank on 10/8/17.
 */

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.CoinViewHolder>
        implements Filterable {

    private Context context;
    private CoinClickListener coinClickListener;
    private ArrayList<Coin> filteredCoinsList;
    private final ArrayList<Coin> mainCoinsList;


    public MainActivityAdapter(final Context context, final CoinClickListener coinClickListener, final ArrayList<Coin> mainCoinsList) {
        this.context = context;
        this.coinClickListener = coinClickListener;
        this.mainCoinsList = mainCoinsList;
        this.filteredCoinsList = mainCoinsList;
    }

    @Override
    public CoinViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View itemView = LayoutInflater.from(context).
                inflate(R.layout.coin_card, parent, false);
        final CoinViewHolder coinViewHolder = new CoinViewHolder(itemView);
        return coinViewHolder;
    }

    @Override
    public void onBindViewHolder(final CoinViewHolder holder, final int position) {
        final Coin coin = filteredCoinsList.get(position);
        holder.coinName.setText(coin.getNameOfCoin());
        if (coin.isSelected()) {
            holder.overlayView.setVisibility(View.VISIBLE);
        } else {
            holder.overlayView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return filteredCoinsList.size();
    }

    class CoinViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private TextView coinName;
        private View overlayView;

        CoinViewHolder(final View itemView) {
            super(itemView);
            this.coinName = (TextView) itemView.findViewById(R.id.coin_name);
            this.overlayView = itemView.findViewById(R.id.selected_overlay);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            coinClickListener.clickedOn(filteredCoinsList.get(getAdapterPosition()).getId());
        }

        @Override
        public boolean onLongClick(final View view) {
            Log.d(MainActivityAdapter.this.getClass().getSimpleName(), "long clicked on " + filteredCoinsList.get(getAdapterPosition()));
            coinClickListener.longClickedOn(filteredCoinsList.get(getAdapterPosition()).getId(), view);
            return true;
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(final CharSequence constraint) {
                final String searchString = constraint.toString().toLowerCase().trim();
                final FilterResults filterResults = new FilterResults();

                if (searchString.isEmpty()) {
                    filteredCoinsList = mainCoinsList;
                    filterResults.values = filteredCoinsList;
                    return filterResults;

                } else {
                    ArrayList<Coin> localFilteredList = new ArrayList<>();
                    for (final Coin coin : mainCoinsList) {
                        if (coin.getNameOfCoin().toLowerCase().contains(searchString)) {
                            localFilteredList.add(coin);
                        }
                    }
                    filterResults.values = localFilteredList;
                    return filterResults;
                }
            }

            @Override
            protected void publishResults(final CharSequence constraint, final FilterResults results) {
                filteredCoinsList = (ArrayList<Coin>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
