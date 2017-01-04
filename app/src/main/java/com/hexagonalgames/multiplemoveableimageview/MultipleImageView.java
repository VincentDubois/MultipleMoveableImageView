package com.hexagonalgames.multiplemoveableimageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * TODO: document your custom view class.
 */
public class MultipleImageView extends View implements View.OnTouchListener {


    private static class Image {
        public Image(Bitmap bitmap, float x, float y) {
            this.bitmap = bitmap;
            this.x = x;
            this.y = y;
        }

        Bitmap bitmap;
        float x,y;
        float dx,dy;

        boolean contains(float x, float y){
            if (x<this.x || y < this.y) return false;
            return x< this.x+bitmap.getWidth() && y < this.y+bitmap.getHeight();
        }

        public void setOrigin(float x, float y) {
            dx = x-this.x;
            dy = y-this.y;
        }

        public void moveTo(float x, float y) {
            this.x = x-dx;
            this.y = y-dy;
        }
    }

    private Vector<Image> image;
    private Map<Integer,Image> moving; // pointer id -> image moving

    public void addBitmap(Bitmap bitmap, int x, int y){
        image.add(new Image(bitmap,x,y));
    }


    public MultipleImageView(Context context) {
        super(context);
        init(null, 0);
    }

    public MultipleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MultipleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        image = new Vector<>();
        moving = new HashMap<>();
        this.setOnTouchListener(this);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for(Image i : image) {
            canvas.drawBitmap(i.bitmap,i.x,i.y, null);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        boolean used = false;

        int action = motionEvent.getActionMasked();
        int ndx = motionEvent.getActionIndex();
        if (action == MotionEvent.ACTION_DOWN){
            float x = motionEvent.getX(ndx);
            float y = motionEvent.getY(ndx);
//            Log.d("touch","down "+ndx);

            Image img = findImageAt(x,y);
            if (img != null) {
                img.setOrigin(x, y);
                moving.put(motionEvent.getPointerId(ndx), img);
                used = true;
            }
        } else if (action == MotionEvent.ACTION_UP){
//            Log.d("up","down "+ndx);

            moving.remove(motionEvent.getPointerId(ndx));
            used = true;
        }

        for(int i = 0; i<motionEvent.getPointerCount(); ++i){
                int id = motionEvent.getPointerId(i);
//                Log.d("touch","moving "+ndx);

                Image img = moving.get(id);
                if (img != null){
                    img.moveTo(motionEvent.getX(i),motionEvent.getY(i));

                    // place image on top
                    image.remove(img);
                    image.add(img);

                    // schedule redrawing
                    this.invalidate();

                    used = true;
                }
            }

        return used;
    }

    private Image findImageAt(float x, float y) {
        Image result= null;
        for(Image i : image) if (i.contains(x,y)) result = i;
        return result;
    }


}
