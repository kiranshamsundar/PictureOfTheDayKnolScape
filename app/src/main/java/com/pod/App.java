package com.pod;

import android.app.Application;

import com.pod.data.component.DaggerNetComponent;
import com.pod.data.component.NetComponent;
import com.pod.data.module.AppModule;
import com.pod.data.module.NetModule;

public class App extends Application {
    private NetComponent mNetComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mNetComponent = DaggerNetComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule("https://api.nasa.gov/"))
                .build();
    }

    public NetComponent getNetComponent() {
        return mNetComponent;
    }
}
