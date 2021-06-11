package com.yuncommunity.lizard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.oldfeel.base.BaseBaseAdapter;
import com.oldfeel.utils.LogUtil;
import com.pgyersdk.update.PgyUpdateManager;
import com.yuncommunity.lizard.base.MyActivity;
import com.yuncommunity.lizard.item.LizardItem;
import com.yuncommunity.lizard.item.PeelingComponentItem;
import com.yuncommunity.lizard.list.RecordList;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends MyActivity {
    public static final int REQUEST_ADD = 124;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    private RecordList recordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, recordList = RecordList.newInstance())
                .commit();

        PgyUpdateManager.register(this);
    }

    @OnClick(R.id.fab)
    public void fab() {
        Intent intent = new Intent(this, AddActivity.class);
        startActivityForResult(intent, REQUEST_ADD);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Spinner lizard = (Spinner) menu.findItem(R.id.action_lizard).getActionView();
        final LizardAdapter lizardAdapter = new LizardAdapter();
        lizard.setAdapter(lizardAdapter);
        lizard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                recordList.setLizard(lizardAdapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner peeling = (Spinner) menu.findItem(R.id.action_peeling).getActionView();
        final PeelingAdapter peelingAdapter = new PeelingAdapter();
        peeling.setAdapter(peelingAdapter);
        peeling.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                recordList.setPeeling(peelingAdapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return super.onPrepareOptionsMenu(menu);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_ADD) {
            LogUtil.showLog("supportInvalidateOptionsMenu");
            supportInvalidateOptionsMenu();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    class LizardAdapter extends BaseBaseAdapter<LizardItem> {

        public LizardAdapter() {
            super(LizardItem.class);
            list = dbHelper.getLizardList();
            LizardItem item = new LizardItem();
            item.name = "全部";
            list.add(0, item);
        }

        @Override
        public View getView(LayoutInflater inflater, ViewGroup parent, int position) {
            TextView textView = (TextView) inflater.inflate(R.layout.item_menu_spinner, parent, false);
            textView.setText(getItem(position).name);
            return textView;
        }
    }

    class PeelingAdapter extends BaseBaseAdapter<PeelingComponentItem> {
        public PeelingAdapter() {
            super(PeelingComponentItem.class);
            String[] pcs = getResources().getStringArray(R.array.peeling_components);
            list = new ArrayList<>();
            addItem("全部");
            for (int i = 0; i < pcs.length; i++) {
                addItem(pcs[i]);
            }
        }

        private void addItem(String name) {
            PeelingComponentItem item = new PeelingComponentItem();
            item.name = name;
            list.add(item);
        }

        @Override
        public View getView(LayoutInflater inflater, ViewGroup parent, int position) {
            TextView textView = (TextView) inflater.inflate(R.layout.item_menu_spinner, parent, false);
            textView.setText(getItem(position).name);
            return textView;
        }
    }

}
