package com.yuncommunity.lizard;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.oldfeel.base.BaseBaseAdapter;
import com.oldfeel.base.UploadMultiImageFragment;
import com.oldfeel.utils.DialogUtil;
import com.oldfeel.utils.ETUtil;
import com.oldfeel.utils.StringUtil;
import com.yuncommunity.lizard.base.MyActivity;
import com.yuncommunity.lizard.conf.Constant;
import com.yuncommunity.lizard.item.LizardItem;
import com.yuncommunity.lizard.item.RecordItem;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by oldfeel on 16-10-17.
 */
public class AddActivity extends MyActivity {

    private static final int REQUEST_CAMERA = 124;
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
    @Bind(R.id.lizard)
    Spinner lizard;
    @Bind(R.id.lizard_add)
    ImageButton lizardAdd;
    private UploadMultiImageFragment imageList;
    RecordItem item;

    LizardAdapter lizardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);
        item = (RecordItem) getIntent().getSerializableExtra("item");
        if (item == null)
            item = new RecordItem();

        showTitle(item.id == 0 ? "添加" : "编辑");
        ButterKnife.bind(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.images, imageList = UploadMultiImageFragment.newInstance(R.drawable.ic_image_add, 1, Constant.IMG_DIR))
                .commit();
        imageList.setNumColumns(4);
        imageList.setMaxCount(99);
        imageList.setImages(item.getImages());

        lizardAdapter = new LizardAdapter();
        lizard.setAdapter(lizardAdapter);
        lizard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item.lizard_id = lizardAdapter.getItem(position).id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        putDataToView();

        ETUtil.hideSoftKeyboard(this);
    }

    private void putDataToView() {
        if (item.id == 0) { // 添加
            startTime.setText(StringUtil.getCurrentDate());
        } else { // 编辑
            weight.setText(item.weight);
            length.setText(item.length);
            peelingComponent.setSelection(getSelection(R.array.peeling_components, item.peeling_component));
            startTime.setText(item.start_time);
            stopTime.setText(item.stop_time);

            for (int i = 0; i < lizardAdapter.getCount(); i++) {
                if (lizardAdapter.getItem(i).id == item.lizard_id) {
                    lizard.setSelection(i);
                }
            }
            lizard.setEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (item.id == 0) {
            menu.findItem(R.id.action_delete).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_complete:
                complete();
                break;
            case R.id.action_delete:
                isDelete();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void isDelete() {
        new AlertDialog.Builder(this)
                .setTitle("警告!")
                .setMessage("删除后无法恢复,是否确认删除?")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.delete(item);
                        resultOk();
                    }
                })
                .setNegativeButton("否", null)
                .show();
    }

    private void resultOk() {
        setResult(RESULT_OK);
        onBackPressed();
    }

    private void complete() {
        item.length = getString(length);
        item.weight = getString(weight);
        item.peeling_component = getString(peelingComponent);
        item.start_time = getString(startTime);
        item.stop_time = getString(stopTime);
        item.images = imageList.getImages();
        dbHelper.createOrUpdate(item);
        resultOk();
    }

    @OnClick(R.id.lizard_add)
    public void lizardAdd() {
        View view = LayoutInflater.from(this).inflate(R.layout.lizard_add, null);
        Button submit = (Button) view.findViewById(R.id.submit);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        final TextView birthday = (TextView) view.findViewById(R.id.birthday);
        final Spinner sex = (Spinner) view.findViewById(R.id.sex);
        final EditText name = (EditText) view.findViewById(R.id.name);
        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.getInstance().showDatePickerDialog(AddActivity.this, new DialogUtil.OnDatePickerListener() {
                    @Override
                    public void onDatePicker(String date) {
                        birthday.setText(date);
                    }
                });
            }
        });
        final Dialog dialog = new AlertDialog.Builder(this).setTitle("添加蜥蜴")
                .setView(view)
                .create();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ETUtil.isHaveNull(birthday, name)) {
                    return;
                }
                LizardItem temp = dbHelper.createLizard(getString(name), getString(sex), getString(birthday));
                lizardAdapter.add(temp);
                lizard.setSelection(lizardAdapter.getCount() - 1);
                dialog.cancel();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
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

    class LizardAdapter extends BaseBaseAdapter<LizardItem> {
        public LizardAdapter() {
            super(LizardItem.class);
            list = dbHelper.getLizardList();
        }

        @Override
        public View getView(LayoutInflater inflater, ViewGroup parent, int position) {
            View view = inflater.inflate(R.layout.item_lizard, parent, false);
            ImageView delete = (ImageView) view.findViewById(R.id.delete);
            TextView text = (TextView) view.findViewById(R.id.text);
            final LizardItem lizardItem = getItem(position);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(AddActivity.this)
                            .setTitle("警告!")
                            .setMessage("删除 " + lizardItem.name + " 后无法回复,是否确定删除?")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dbHelper.delete(lizardItem);
                                    remove(lizardItem);
                                }
                            })
                            .setNegativeButton("否", null)
                            .show();
                }
            });
            text.setText(lizardItem.name);
            return view;
        }
    }
}
