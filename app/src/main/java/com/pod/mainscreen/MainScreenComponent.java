package com.pod.mainscreen;


import com.pod.data.component.NetComponent;
import com.pod.util.CustomScope;

import dagger.Component;

@CustomScope
@Component(dependencies = NetComponent.class, modules = MainScreenModule.class)
public interface MainScreenComponent {
    void inject(PicOfTheDayActivity activity);
}
