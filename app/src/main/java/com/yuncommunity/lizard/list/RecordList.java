package com.yuncommunity.lizard.list;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oldfeel.base.BaseList;
import com.yuncommunity.lizard.AddActivity;
import com.yuncommunity.lizard.MainActivity;
import com.yuncommunity.lizard.R;
import com.yuncommunity.lizard.base.DBHelper;
import com.yuncommunity.lizard.item.LizardItem;
import com.yuncommunity.lizard.item.PeelingComponentItem;
import com.yuncommunity.lizard.item.RecordItem;

import java.util.ArrayList;

/**
 * Created by oldfeel on 16-10-18.
 */
public class RecordList extends BaseList<RecordItem> {
    private PeelingComponentItem peelingItem;
    private LizardItem lizardItem;

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), AddActivity.class);
        intent.putExtra("item", getItem(position));
        startActivityForResult(intent, MainActivity.REQUEST_ADD);
    }

    @Override
    public void initHeaderView() {

    }

    @Override
    public View getItemView(LayoutInflater inflater, ViewGroup parent, int position) {
        View view = inflater.inflate(R.layout.item_record, parent, false);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView length = (TextView) view.findViewById(R.id.length);
        TextView peeling = (TextView) view.findViewById(R.id.peeling);
        TextView time = (TextView) view.findViewById(R.id.time);
        ImageView image = (ImageView) view.findViewById(R.id.image);
        RecordItem item = getItem(position);
        imageLoader.displayImage(item.getFirstImage(), image, options);
        name.setText(item.getNameAndAge());
        length.setText("体重:" + item.weight + "g 体长:" + item.length + "cm");
        peeling.setText("脱皮部位:" + item.peeling_component);
        time.setText(item.start_time + "/" + item.stop_time);
        return view;
    }

    public static RecordList newInstance() {
        RecordList fragment = new RecordList();
        fragment.isAddFoot = false;
        fragment.isLocal = true;
        return fragment;
    }

    @Override
    public void getData(int page) {
        if (page == 0) {
            clear();
        }
        if (list == null)
            list = new ArrayList<>();

        try {
            list.addAll(DBHelper.getInstance(getActivity()).getRecordList(lizardItem, peelingItem, page++));
        } catch (Exception e) {
            e.printStackTrace();
        }
        notifyDataSetChanged(true);

        refreshComplete();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == MainActivity.REQUEST_ADD) {
            getData(0);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setLizard(LizardItem item) {
        lizardItem = item;
        getData(0);
    }

    public void setPeeling(PeelingComponentItem item) {
        peelingItem = item;
        getData(0);
    }
}
