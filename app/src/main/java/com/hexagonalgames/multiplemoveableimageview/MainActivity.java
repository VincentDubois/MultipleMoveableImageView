package com.hexagonalgames.multiplemoveableimageview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.escargot);
        MultipleImageView multipleImageView = (MultipleImageView) findViewById(R.id.view);

        for(int i = 0; i< 20; ++i){
            multipleImageView.addBitmap(bm,200,200);
        }
    }
}
