package com.yuncommunity.lizard;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.oldfeel.base.UploadMultiImageFragment;
import com.oldfeel.utils.DialogUtil;
import com.oldfeel.utils.ETUtil;
import com.oldfeel.utils.StringUtil;
import com.yuncommunity.lizard.base.MyActivity;
import com.yuncommunity.lizard.conf.Constant;
import com.yuncommunity.lizard.item.RecordItem;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by oldfeel on 16-10-17.
 */
public class AddActivity extends MyActivity {

    @Bind(R.id.weight)
    EditText weight;
    @Bind(R.id.length)
    EditText length;
    @Bind(R.id.peeling_component)
    Spinner peelingComponent;
    @Bind(R.id.start_time)
    TextView startTime;
    @Bind(R.id.stop_time)
    TextView stopTime;
    private UploadMultiImageFragment imageList;
    RecordItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);
        item = (RecordItem) getIntent().getSerializableExtra("item");
        showTitle(item == null ? "添加" : "编辑");
        ButterKnife.bind(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.images, imageList = UploadMultiImageFragment.newInstance(R.drawable.ic_image_add, 1, Constant.IMG_DIR))
                .commit();
        imageList.setNumColumns(4);
        imageList.setMaxCount(99);

        ETUtil.hideSoftKeyboard(this);

        putDataToView();
    }

    private void putDataToView() {
        if (item == null) { // 添加
            startTime.setText(StringUtil.getCurrentDate());
        } else { // 编辑
            weight.setText(item.weight + "");
            length.setText(item.length + "");
            peelingComponent.setSelection(getSelection(R.array.peeling_components, item.peeling_component));
            startTime.setText(item.start_time);
            stopTime.setText(item.stop_time);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_complete:
                complete();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void complete() {
        if (item == null)
            item = new RecordItem();
        item.length = Double.valueOf(getString(length));
        item.weight = Double.valueOf(getString(weight));
        item.peeling_component = getString(peelingComponent);
        item.start_time = getString(startTime);
        item.stop_time = getString(stopTime);
        item.images = imageList.getImages();
        dbHelper.createOrUpdate(item);
    }

    @OnClick(R.id.start_time)
    public void startTime() {
        DialogUtil.getInstance().showDatePickerDialog(this, new DialogUtil.OnDatePickerListener() {
            @Override
            public void onDatePicker(String date) {
                startTime.setText(date);
            }
        });
    }

    @OnClick(R.id.stop_time)
    public void stopTime() {
        DialogUtil.getInstance().showDatePickerDialog(this, new DialogUtil.OnDatePickerListener() {
            @Override
            public void onDatePicker(String date) {
                stopTime.setText(date);
            }
        });
    }
}
