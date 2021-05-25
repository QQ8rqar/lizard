package com.yuncommunity.lizard.conf;

import com.oldfeel.conf.BaseApplication;
import com.pgyersdk.crash.PgyCrashManager;

/**
 * Created by oldfeel on 16-10-17.
 */

public class MyApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        PgyCrashManager.register(this);
    }

    @Override
    public void initBaseConstant() {

    }
}
