package me.fangtian.lxt.timecard;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by lxt on 2017/6/24.
 */

public class UtilsGetRectBitmap {
    private static final String TAG = "UtilsGetRectBitmap";

//    private RectBitmap rectBitmap;

    public static RectBitmap GetRectBitmap (int screenWidth, int screenHeight, int bitmapWidth, int bitmapHeight) {

//        Log.d(TAG, "GetRectBitmap: " + screenWidth);
//        Log.d(TAG, "GetRectBitmap: " + screenHeight);
//        Log.d(TAG, "GetRectBitmap: " + bitmapWidth);
//        Log.d(TAG, "GetRectBitmap: " + bitmapHeight);

        RectBitmap rectBitmap = new RectBitmap();

        float screenRatio = (float) screenWidth / (float) screenHeight;

        float bitmapRatio = (float) bitmapWidth / (float) bitmapHeight;


        if (screenRatio < bitmapRatio) {
            rectBitmap.setStartX(0);
            rectBitmap.setWidth(screenWidth);
            rectBitmap.setHeight((int) (((float) screenWidth/ (float) bitmapWidth) * (float) bitmapHeight));
            rectBitmap.setStartY((screenHeight - (int) (((float) screenWidth/ (float) bitmapWidth) * (float) bitmapHeight))/2);
        } else if (screenRatio > bitmapRatio) {
            rectBitmap.setStartY(0);
            rectBitmap.setHeight(screenHeight);
            rectBitmap.setWidth((int) (((float) screenHeight/ (float) bitmapHeight) * (float) bitmapWidth));
            rectBitmap.setStartX((screenWidth - (int) (((float) screenHeight/ (float) bitmapHeight) * (float) bitmapWidth))/2);
        } else {
            rectBitmap.setStartX(0);
            rectBitmap.setStartY(0);
            rectBitmap.setWidth(screenWidth);
            rectBitmap.setHeight(screenHeight);
        }

//        Log.d(TAG, "GetRectBitmap: " + rectBitmap.getStartX());
//        Log.d(TAG, "GetRectBitmap: " + rectBitmap.getStartY());
//        Log.d(TAG, "GetRectBitmap: " + rectBitmap.getWidth());
//        Log.d(TAG, "GetRectBitmap: " + rectBitmap.getHeight());

        return rectBitmap;
    }

    /**
     * 把两个位图覆盖合成为一个位图，以底层位图的长宽为基准
     * @param backBitmap 在底部的位图
     * @param frontBitmap 盖在上面的位图
     * @return
     */
    public static Bitmap mergeBitmap(Bitmap backBitmap, Bitmap frontBitmap) {

        if (backBitmap == null || backBitmap.isRecycled()
                || frontBitmap == null || frontBitmap.isRecycled()) {
            Log.e(TAG, "backBitmap=" + backBitmap + ";frontBitmap=" + frontBitmap);
            return null;
        }
        Bitmap bitmap = backBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);
        Rect baseRect  = new Rect(0, 0, backBitmap.getWidth(), backBitmap.getHeight());
        Rect frontRect = new Rect(0, 0, frontBitmap.getWidth(), frontBitmap.getHeight());
        canvas.drawBitmap(frontBitmap, frontRect, baseRect, null);
        return bitmap;
    }

}
