package com.pod.mainscreen;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pod.App;
import com.pod.R;
import com.pod.data.PictureInfo;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;

import javax.inject.Inject;

public class PicOfTheDayActivity extends AppCompatActivity implements PicOfTheDayScreenContract.View {
    private ImageView pictureOfTheDayView;
    private TextView titleOfPic;
    private TextView date;
    private TextView explaination;

    @Inject
    PicOfTheDayPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pictureOfTheDayView = (ImageView) findViewById(R.id.pic_of_day);
        titleOfPic = (TextView) findViewById(R.id.title);
        explaination = (TextView) findViewById(R.id.explanation);
        date = (TextView) findViewById(R.id.date);

        DaggerPicOfDayScreenComponent.builder()
                .netComponent(((App) getApplicationContext()).getNetComponent())
                .picOfTheDayScreenModule(new PicOfTheDayScreenModule(this))
                .build().inject(this);

        mainPresenter.loadPicOfTheDay();
    }

    @Override
    public void showPicOfTheDay(Uri imageUri) {
        Picasso.with(this).load(imageUri).error(R.mipmap.nasalogo)
                .placeholder(R.mipmap.nasalogo).into(pictureOfTheDayView);
    }

    @Override
    public void showCachedPic() {
        Bitmap bitmap = null;
        try {
            File filePath = getApplicationContext().getFileStreamPath("pic.png");
            FileInputStream fi = new FileInputStream(filePath);
            bitmap = BitmapFactory.decodeStream(fi);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        pictureOfTheDayView.setImageBitmap(bitmap);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getApplicationContext(), "Unexpected Error" + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPictureInfo(PictureInfo pictureInfo) {
        titleOfPic.setText(pictureInfo.getTitle());
        date.setText(pictureInfo.getDate());
        explaination.setText(pictureInfo.getExplanation());
    }

    @Override
    public App getApplicationRef() {
        return (App) getApplication();
    }
}
