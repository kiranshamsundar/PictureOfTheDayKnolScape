package com.pod.mainscreen;

import android.net.Uri;

import com.pod.App;
import com.pod.data.PictureInfo;

public interface PicOfTheDayScreenContract {

    interface View {
        void showPicOfTheDay(Uri imageUri);

        void showCachedPic();

        void showError(String message);

        void showPictureInfo(PictureInfo pictureInfo);

        App getApplicationRef();
    }

    interface Presenter {
        void loadPicOfTheDay();
    }
}
