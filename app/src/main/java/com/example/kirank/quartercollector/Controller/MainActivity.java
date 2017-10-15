package com.example.kirank.quartercollector.Controller;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.kirank.quartercollector.Controller.Adapter.MainActivityAdapter;
import com.example.kirank.quartercollector.Controller.Interface.CoinClickListener;
import com.example.kirank.quartercollector.Data.CoinSource;
import com.example.kirank.quartercollector.Model.Coin;
import com.example.kirank.quartercollector.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private MainActivityAdapter mainActivityAdapter;
    private boolean someCoinsSelected = false;
    private Toolbar toolbar;
    private ArrayList<Long> idsOfSelectedCoins = new ArrayList<>();
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        CoinSource.getCoins().forEach(coin -> System.out.println(coin.getNameOfCoin() + ", " + coin.getId()));

        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bindViews();
        setOnclickListeners();
        prepareRecyclerView();
    }

    private void prepareRecyclerView() {
        this.mainActivityAdapter = new MainActivityAdapter(this, new CoinClickListener() {
            @Override
            public void clickedOn(final long coinId) {
                clickedOnCoinAt(coinId);
            }

            @Override
            public void longClickedOn(final long coinId, final View longClickedOn) {
                longClickedOnCoinAt(coinId, longClickedOn);

            }
        }, CoinSource.getCoins());
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.recyclerView.setAdapter(mainActivityAdapter);
    }

    private void longClickedOnCoinAt(final long coinId, final View longClickedOn) {

        if (searchView.getQuery().toString().length() != 0) {
            //show the popup menu here
            final PopupMenu deletePopupMenu = new PopupMenu(MainActivity.this, longClickedOn);
            deletePopupMenu.getMenuInflater().inflate(R.menu.popup_menu, deletePopupMenu.getMenu());

            deletePopupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.popup_menu_delete:
                        idsOfSelectedCoins.add(coinId);
                        deleteCoins();
                        return true;
                }
                return false;
            });
            deletePopupMenu.show();

        } else {
            //modify coin view
            final boolean isSelected = CoinSource.getCoinWithId(coinId).isSelected();
            if (!isSelected) {
                CoinSource.setCoinWithIdAsSelected(coinId);
                //notify dataset changed
                this.mainActivityAdapter.notifyDataSetChanged();
            }

            //modify menu
            this.someCoinsSelected = true;
            this.idsOfSelectedCoins.add(coinId);
            invalidateOptionsMenu();
        }
    }

    private void clickedOnCoinAt(final long coinId) {

        final boolean isSelected = CoinSource.getCoinWithId(coinId).isSelected();
        if (isSelected) {
            CoinSource.setCoinWithIdAsUnSelected(coinId);
            this.idsOfSelectedCoins.remove(Long.valueOf(coinId));
            if (this.idsOfSelectedCoins.size() == 0) {
                this.someCoinsSelected = false;
            }

            invalidateOptionsMenu();
            this.mainActivityAdapter.notifyDataSetChanged();
        }
    }

    private void setOnclickListeners() {

        fab.setOnClickListener(view -> {
            final EditText newCoinName = new EditText(MainActivity.this);
            newCoinName.setHint("enter name of the coin");

            final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).
                    setTitle("Add a new coin")
                    .setView(newCoinName)
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {
                            final String newCoinNameString = newCoinName.getText().toString();
                            if (validateNewCoinName(newCoinNameString)) {
                                CoinSource.addCoin(new Coin(newCoinNameString));
                                mainActivityAdapter.notifyDataSetChanged();
                                recyclerView.smoothScrollToPosition(CoinSource.getSize() - 1);

                            } else {
                                final Snackbar snackbar = Snackbar.make(coordinatorLayout, "The coin is already present!", Snackbar.LENGTH_SHORT);
                                snackbar.setAction("Add another", v -> fab.callOnClick());
                                snackbar.show();
                            }
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create();
            dialog.show();
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        });
    }

    private boolean validateNewCoinName(final String newCoinNameString) {
        boolean returnValue = true;
        final boolean alreadyPresent = CoinSource.isNamePresent(newCoinNameString);
        if (alreadyPresent) {
            returnValue = false;
        }
        return returnValue;
    }

    private void bindViews() {

        this.coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_layout);
        this.fab = (FloatingActionButton) findViewById(R.id.fab);
        this.recyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final MenuItem deleteItem = menu.findItem(R.id.action_delete);
        final MenuItem cancelItem = menu.findItem(R.id.action_cancel);

        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        MenuItemCompat.collapseActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                mainActivityAdapter.getFilter().filter(newText);
                return true;
            }
        });

        final int countOfCoinsSelected = this.idsOfSelectedCoins.size();

        if (countOfCoinsSelected == 1) {
            final Coin selectedCoin = CoinSource.getCoinWithId(idsOfSelectedCoins.get(0));
            this.toolbar.setTitle("Selected " + (selectedCoin != null ? selectedCoin.getNameOfCoin() : "Nothing."));
        } else if (countOfCoinsSelected > 1) {
            this.toolbar.setTitle("selected " + countOfCoinsSelected + " coins");
        } else if (countOfCoinsSelected == 0) {
            this.toolbar.setTitle("Quarter Collector");
        }

        if (this.someCoinsSelected) {
            searchItem.setVisible(false);
            deleteItem.setVisible(true);
            cancelItem.setVisible(true);
        } else {
            searchItem.setVisible(true);
            deleteItem.setVisible(false);
            cancelItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return false;
        } else if (id == R.id.action_delete) {
            deleteSelectedCoins();
            return true;
        } else if (id == R.id.action_cancel) {
            this.someCoinsSelected = false;
            this.idsOfSelectedCoins.clear();
            invalidateOptionsMenu();
            setAllCoinsAsUnselected();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setAllCoinsAsUnselected() {
        CoinSource.setAllCoinsAsUnselected();
        this.mainActivityAdapter.notifyDataSetChanged();
    }

    private void deleteSelectedCoins() {

        final AlertDialog.Builder deleteAlertDialogBuilder = new AlertDialog.Builder(MainActivity.this).
                setTitle("Deleted selected " + this.idsOfSelectedCoins.size() + "coin(s)?").
                setView(null).
                setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        deleteCoins();
                    }
                }).
                setNegativeButton("Cancel", null);
        final AlertDialog deleteAlertDialog = deleteAlertDialogBuilder.create();
        deleteAlertDialog.show();
    }

    private void deleteCoins() {
        final Set<Coin> duplicateCoinSet = new HashSet<>();

        final int countCoinsToBeDeleted = this.idsOfSelectedCoins.size();
        idsOfSelectedCoins.forEach(id -> duplicateCoinSet.add(new Coin(CoinSource.getCoinWithId(id))));

        Log.d(this.getClass().getSimpleName(), "deleting " + this.idsOfSelectedCoins.size() + " number of coins");
        idsOfSelectedCoins.forEach(id -> Log.d(this.getClass().getSimpleName(), "location : " + id +
                " coin name " + CoinSource.getCoinWithId(id).getNameOfCoin()));

        // removing a list of items using multiple calls to remove is not possible because the items
        // get shifted and the locations get changed, so sorting the list in reverse will fix this issue
        Collections.sort(idsOfSelectedCoins, Collections.reverseOrder());

        this.idsOfSelectedCoins.forEach(CoinSource::deleteCoinWithId);
        this.mainActivityAdapter.notifyDataSetChanged();
        this.recyclerView.scrollToPosition(0);
        this.someCoinsSelected = false;
        this.idsOfSelectedCoins.clear();

        invalidateOptionsMenu();

        final String message = (countCoinsToBeDeleted == 1) ?
                "Deleted " + duplicateCoinSet.iterator().next().getNameOfCoin() :
                "Deleted " + countCoinsToBeDeleted + " coin(s)";
        final Snackbar snackbar =
                Snackbar.make(this.coordinatorLayout, message, Snackbar.LENGTH_LONG);

        snackbar.setAction("Redo", view -> {
            duplicateCoinSet.forEach(CoinSource::addCoin);
            setAllCoinsAsUnselected();
            mainActivityAdapter.notifyDataSetChanged();
            duplicateCoinSet.clear();
        });
        snackbar.show();
    }
}
