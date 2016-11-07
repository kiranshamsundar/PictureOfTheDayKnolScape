package com.pod.mainscreen;


import com.pod.util.CustomScope;

import dagger.Module;
import dagger.Provides;

@Module
public class PicOfTheDayScreenModule {
    private final PicOfTheDayScreenContract.View mView;


    public PicOfTheDayScreenModule(PicOfTheDayScreenContract.View mView) {
        this.mView = mView;
    }

    @Provides
    @CustomScope
    PicOfTheDayScreenContract.View providesMainScreenContractView() {
        return mView;
    }
}
