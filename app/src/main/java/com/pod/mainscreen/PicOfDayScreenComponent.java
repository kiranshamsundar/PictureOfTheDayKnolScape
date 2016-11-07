package com.pod.mainscreen;


import com.pod.data.component.NetComponent;
import com.pod.util.CustomScope;

import dagger.Component;

@CustomScope
@Component(dependencies = NetComponent.class, modules = PicOfTheDayScreenModule.class)
public interface PicOfDayScreenComponent {
    void inject(PicOfTheDayActivity activity);
}
