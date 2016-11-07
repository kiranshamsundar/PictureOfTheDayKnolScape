package com.pod.mainscreen;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.pod.App;
import com.pod.data.Picture;
import com.pod.data.PictureInfo;

import org.joda.time.DateTime;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import retrofit2.Retrofit;
import retrofit2.http.GET;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PicOfTheDayPresenter implements PicOfTheDayScreenContract.Presenter {

    public Retrofit retrofit;
    private PicOfTheDayScreenContract.View mView;
    private App app;
    private PictureInfo picInfo;
    private String NEED_SERVER_REQUEST = "needToHitServer";

    @Inject
    public PicOfTheDayPresenter(Retrofit retrofit, PicOfTheDayScreenContract.View mView) {
        this.retrofit = retrofit;
        this.mView = mView;
        this.app = mView.getApplicationRef();
    }

    @Override
    public void loadPicOfTheDay() {

        if (!isRequiredToHitServer()) {
            showFromCache();
            return;
        }
        retrofit.create(GetPicOfTheDayService.class).getPicOfTheDay().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Picture>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(Picture picture) {
                        mView.showPicOfTheDay(Uri.parse(picture.getUrl()));
                        picInfo = new PictureInfo(picture.getExplanation(), picture.getDate(), picture.getTitle());
                        mView.showPictureInfo(picInfo);
                        new ImageDownloader().execute(picture.getUrl(), picture.getDate());
                    }
                });
    }

    private void showFromCache() {
        Log.e("", "No need to hit server");
        SharedPreferences prefShared = app.getSharedPreferences(NEED_SERVER_REQUEST, Context.MODE_PRIVATE);
        String date = prefShared.getString("DateOfPic", "");
        String title = prefShared.getString("Title", "");
        String explanation = prefShared.getString("Explanation", "");

        mView.showPictureInfo(new PictureInfo(explanation, date, title));
        mView.showCachedPic();
    }

    private void savePicInfoWithDate(Bitmap bitMap) {
        saveImageToInternalStorage(bitMap);
        SharedPreferences prefShared = app.getSharedPreferences(NEED_SERVER_REQUEST, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefShared.edit();
        editor.putString("DateOfPic", picInfo.getDate());
        editor.putString("Title", picInfo.getTitle());
        editor.putString("Explanation", picInfo.getExplanation());
        editor.apply();
    }

    private Bitmap getBitMap(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            InputStream is = url.openStream();
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean saveImageToInternalStorage(Bitmap image) {
        try {
            FileOutputStream fos = app.openFileOutput("pic.png", Context.MODE_PRIVATE);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isRequiredToHitServer() {
        SharedPreferences prefShared = app.getSharedPreferences(NEED_SERVER_REQUEST, Context.MODE_PRIVATE);
        String lastSavedDate = prefShared.getString("DateOfPic", "");
        long lastDateInMillSecs = milliseconds(lastSavedDate);
        DateTime lastDate = new DateTime(lastDateInMillSecs);
        Log.e("", "Last saved date :" + lastDate.toString());
        DateTime nextDay = lastDate = lastDate.plusDays(1);
        Log.e("", "Last saved date plus 1 day :" + lastDate.toString());
        if (DateTime.now().isBefore(nextDay)) {
            Log.e("", "Not hitting server");
            return false;
        } else {
            Log.e("", "Hitting server");
            return true;
        }
    }

    public interface GetPicOfTheDayService {
        @GET("/planetary/apod")
        Observable<Picture> getPicOfTheDay();
    }

    private long milliseconds(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date mDate = sdf.parse(date);
            long timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
            return timeInMilliseconds;
        } catch (ParseException e) {
        }
        return 0;
    }

    private class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... param) {
            // TODO Auto-generated method stub
            return getBitMap(param[0]);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            savePicInfoWithDate(result);
        }
    }
}
