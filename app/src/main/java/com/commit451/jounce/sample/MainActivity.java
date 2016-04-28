package com.commit451.jounce.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.commit451.jounce.DebouncerMap;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.list)
    RecyclerView mList;

    CheeseAdapter mCheeseAdapter;

    private final CheeseAdapter.Listener mCheeseAdapterListener = new CheeseAdapter.Listener() {
        @Override
        public void onItemClicked(Cheese cheese) {
            //Get the current debounced value
            Integer tapCount = mBasketItemDebouncerMap.getValue(cheese);
            if (tapCount != null) {
                tapCount = tapCount + 1;
                mBasketItemDebouncerMap.setValue(cheese, tapCount);
            } else {
                mBasketItemDebouncerMap.setValue(cheese, 1);
            }
        }
    };

    private DebouncerMap<Cheese, Integer> mBasketItemDebouncerMap = new DebouncerMap<Cheese, Integer>(700) {
        @Override
        public void onValueChanged(final Cheese key, Integer value) {
            //Do the thing that you wanted to debounce
            Snackbar.make(getWindow().getDecorView(), key.getName() + " was clicked " + value + " times", Snackbar.LENGTH_SHORT)
                    .show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mToolbar.setTitle(R.string.app_name);
        mToolbar.inflateMenu(R.menu.search);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                return true;
            }
        });
        mCheeseAdapter = new CheeseAdapter(mCheeseAdapterListener);
        mList.setAdapter(mCheeseAdapter);
        mList.setLayoutManager(new GridLayoutManager(this, 2));
        loadCheeses();
    }

    private void loadCheeses() {
        ArrayList<Cheese> cheeses = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            cheeses.add(Cheeses.getRandomCheese());
        }
        mCheeseAdapter.setData(cheeses);
    }
}
