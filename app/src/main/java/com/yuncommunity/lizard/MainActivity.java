package com.yuncommunity.lizard;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuItem;

import com.pgyersdk.update.PgyUpdateManager;
import com.yuncommunity.lizard.base.MyActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends MyActivity {

    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        PgyUpdateManager.register(this);
    }

    @OnClick(R.id.fab)
    public void fab() {
        openActivity(AddActivity.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_chart:
                openActivity(ChartActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
