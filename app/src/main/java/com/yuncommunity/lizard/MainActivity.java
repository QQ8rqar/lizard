package com.yuncommunity.lizard;

import android.os.Bundle;

import com.pgyersdk.update.PgyUpdateManager;
import com.yuncommunity.lizard.base.MyActivity;

public class MainActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PgyUpdateManager.register(this);
    }
}
