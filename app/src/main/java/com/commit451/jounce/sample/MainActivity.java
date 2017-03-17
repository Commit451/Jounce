package com.commit451.jounce.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.commit451.jounce.Debouncer;
import com.commit451.jounce.DebouncerMap;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.list)
    RecyclerView list;

    CheeseAdapter adapter;

    private final CheeseAdapter.Listener adapterListener = new CheeseAdapter.Listener() {
        @Override
        public void onItemClicked(Cheese cheese) {
            //Get the current debounced value
            Integer tapCount = debouncerMap.getValue(cheese);
            if (tapCount != null) {
                tapCount = tapCount + 1;
                debouncerMap.setValue(cheese, tapCount);
            } else {
                debouncerMap.setValue(cheese, 1);
            }
        }
    };

    private DebouncerMap<Cheese, Integer> debouncerMap = new DebouncerMap<Cheese, Integer>(700) {
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
        toolbar.setTitle(R.string.app_name);
        toolbar.inflateMenu(R.menu.search);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                return true;
            }
        });
        adapter = new CheeseAdapter(adapterListener);
        list.setAdapter(adapter);
        list.setLayoutManager(new GridLayoutManager(this, 2));
        long oneSecond = TimeUnit.SECONDS.toMillis(1);
        final Debouncer<Void> debouncer = new Debouncer<Void>(oneSecond) {

            @Override
            public void onValueSet(Void value) {
                Snackbar.make(getWindow().getDecorView(), "Toolbar was clicked 1 second ago", Snackbar.LENGTH_SHORT)
                        .show();
            }
        };
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                debouncer.setValue(null);
            }
        });
        loadCheeses();
    }

    private void loadCheeses() {
        ArrayList<Cheese> cheeses = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            cheeses.add(Cheeses.getRandomCheese());
        }
        adapter.setData(cheeses);
    }
}
