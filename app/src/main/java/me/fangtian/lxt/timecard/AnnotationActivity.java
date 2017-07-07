package me.fangtian.lxt.timecard;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.Base64;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsImageView;
import com.rm.freedrawview.FreeDrawView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class AnnotationActivity extends AppCompatActivity {


    private static final String TAG = "lxt";



//    private ArrayList<String> urls;

    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    private CropImageView cropImageView;
    private FreeDrawView freeDrawView;
    private ImageView mongtageView;

    private LinearLayout toolsBar;
    private ConstraintLayout cropperBar;
    private LinearLayout freeDrawBar;

    private IconicsImageView annotationButton;

    private ImageButton cropButton;
    private ImageButton freeDrawButton;
    private ImageButton textButton;
    private ImageButton starButton;
    private Button uploadButton;

    private RectBitmap rectBitmap ;
    private Bitmap currentBitmap;

    private TextView annotationText;
    private EditText editAnnotation;

    private LinearLayout annotationBar;

    private DragFrameLayout dragFrameLayout;


    private LinearLayout starBar;
    private TextView seekBarProgress;
    private SeekBar starSeekBar;

    private ProgressBar imageUploadProgress;

    private WebView webView;
    private IconicsImageView closeWeb;
    private IconicsImageView cancel_anno;

    private Boolean imageLoaded = true;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotation);

        toolsBar = (LinearLayout) findViewById(R.id.toolsBar);
        freeDrawBar = (LinearLayout) findViewById(R.id.freeDrawBar);
        cropperBar = (ConstraintLayout) findViewById(R.id.cropperBar);

        starBar = (LinearLayout) findViewById(R.id.starBar);
        seekBarProgress = (TextView) findViewById(R.id.seekbarProgress);
        starSeekBar = (SeekBar) findViewById(R.id.starSeekBar);

        imageUploadProgress = (ProgressBar) findViewById(R.id.imageUploadProgress);


        annotationButton = (IconicsImageView) findViewById(R.id.annotationButton);
        cancel_anno = (IconicsImageView) findViewById(R.id.cancel_anno);

        dragFrameLayout = (DragFrameLayout) findViewById(R.id.dragFrameLayout);



        pager = (ViewPager) findViewById(R.id.viewPager);

        cropImageView = (CropImageView) findViewById(R.id.cropImageView);
        mongtageView = (ImageView) findViewById(R.id.montageView);
        freeDrawView = (FreeDrawView) findViewById(R.id.freeDrawView);

        webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        closeWeb = (IconicsImageView) findViewById(R.id.closeWeb);


        pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {

                return ConfigApp.exercises.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(final ViewGroup container, int position) {

                setTitle(ConfigApp.exercises.get(position).getStdname() + "提交的作业");

                IconicsDrawable loading = new IconicsDrawable(AnnotationActivity.this)
                                        .icon(CommunityMaterial.Icon.cmd_image)
                                        .color(Color.GRAY)
                                        .sizeDp(200);
                final IconicsDrawable error = new IconicsDrawable(AnnotationActivity.this)
                                        .icon(CommunityMaterial.Icon.cmd_alert)
                                        .color(Color.GRAY)
                                        .sizeDp(200);




                Log.d(TAG, "instantiateItem: " + String.valueOf(ConfigApp.exercises.get(position).getAnswerpic()));


                PinchImageView piv = new PinchImageView(AnnotationActivity.this);

                Picasso.with(AnnotationActivity.this)
                        .load(String.valueOf(ConfigApp.exercises.get(position).getAnswerpic()))
                        .placeholder(loading)
                        .error(error)
                        .into(piv, new com.squareup.picasso.Callback() {

                            @Override
                            public void onSuccess() {
                                imageLoaded = true;
                            }

                            @Override
                            public void onError() {
                                imageLoaded = false;
                            }
                        });



                container.addView(piv);
                return piv;

            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                ((ViewPager) container).removeView((View) object);
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }
        };

        pager.setAdapter(pagerAdapter);

        annotationBar = (LinearLayout) findViewById(R.id.annotationBar);

        annotationText = (TextView) findViewById(R.id.annotationText);
        editAnnotation = (EditText) findViewById(R.id.editAnnotation);


        editAnnotation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                annotationText.setText(editAnnotation.getText());
            }
        });

        dragFrameLayout.setDragFrameController(new DragFrameLayout.DragFrameLayoutController() {
            @Override
            public void onDragDrop(boolean captured) {
                annotationText.animate()
                        .translationZ(captured ? 50 : 0)
                        .setDuration(100);
                Log.d(TAG, captured ? "Drag" : "Drop");

            }
        });

        dragFrameLayout.addDragView(annotationText);

        starSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                seekBarProgress.setText(String.valueOf(seekBar.getProgress()));

                Log.d(TAG, "onProgressChanged: " + i);
                try {
                    seekBarProgress.setText((CharSequence) ConfigApp.correctionstandard.get(String.valueOf(seekBar.getProgress())));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void annotationHandler(View view) {
        Log.d(TAG, "annotationHandler: ");

        if (imageLoaded == false) {
            Toast.makeText(this, "图片读取失败，请检查网络设置是否正确", Toast.LENGTH_SHORT).show();
            return;
        }

        annotationButton.setVisibility(View.INVISIBLE);
        toolsBar.setVisibility(View.VISIBLE);
        cancel_anno.setVisibility(View.VISIBLE);


        Picasso.with(AnnotationActivity.this)
                .load(String.valueOf(ConfigApp.exercises.get(pager.getCurrentItem()).getAnswerpic()))
                .into(montageTarget);
        pager.setVisibility(View.INVISIBLE);
        mongtageView.setVisibility(View.VISIBLE);

    }

    public void cropButtonHandler(View view) {
        toolsBar.setVisibility(View.INVISIBLE);
        cancel_anno.setVisibility(View.INVISIBLE);
        cropperBar.setVisibility(View.VISIBLE);
        mongtageView.setVisibility(View.INVISIBLE);
        cropImageView.setVisibility(View.VISIBLE);

//        Picasso.with(this)
//                .load(urls.get(pager.getCurrentItem()))
//                .into(target);

        cropImageView.setImageBitmap(currentBitmap);


    }

    public void rotationHandler(View view) {
        cropImageView.rotateImage(90);
    }

    public void canceCropHandler(View view) {
        toolsBar.setVisibility(View.VISIBLE);
        cancel_anno.setVisibility(View.VISIBLE);
        cropperBar.setVisibility(View.INVISIBLE);
        mongtageView.setVisibility(View.VISIBLE);
        cropImageView.setVisibility(View.INVISIBLE);
    }

    public void confirmCropHandler(View view) {
        toolsBar.setVisibility(View.VISIBLE);
        cancel_anno.setVisibility(View.VISIBLE);
        cropperBar.setVisibility(View.INVISIBLE);


        Bitmap cropped = cropImageView.getCroppedImage();
        Log.d(TAG, "confirmCropHandler: " + cropped.getWidth());
        Log.d(TAG, "confirmCropHandler: " + cropped.getHeight());
        mongtageView.setImageBitmap(cropped);
        mongtageView.setVisibility(View.VISIBLE);
        cropImageView.setVisibility(View.INVISIBLE);

        currentBitmap = cropped;
        rectBitmap = UtilsGetRectBitmap.GetRectBitmap(mongtageView.getWidth(), mongtageView.getHeight(), cropped.getWidth(), cropped.getHeight());
    }

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Log.d(TAG, "target: " + bitmap.getWidth());
            Log.d(TAG, "target: " + bitmap.getHeight());
            cropImageView.setImageBitmap(bitmap);
            pager.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Log.d(TAG, "onBitmapFailed: ");
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            Log.d(TAG, "onPrepareLoad: ");
        }
    };

    private Target montageTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

            Log.d(TAG, "montageTarget: " + bitmap.getWidth());
            Log.d(TAG, "montageTarget: " + bitmap.getHeight());

            Log.d(TAG, "montageView: " + mongtageView.getWidth());
            Log.d(TAG, "montageView: " + mongtageView.getHeight());
//
//
//            Log.d(TAG, "freedraw: " + freeDrawView.getWidth());
//            Log.d(TAG, "freedraw: " + freeDrawView.getHeight());


            mongtageView.setImageBitmap(bitmap);

            rectBitmap = UtilsGetRectBitmap.GetRectBitmap(mongtageView.getWidth(), mongtageView.getHeight(), bitmap.getWidth(), bitmap.getHeight());

            currentBitmap = bitmap;
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };



    public void freeDrawHandler(View view) {
        toolsBar.setVisibility(View.INVISIBLE);
        cancel_anno.setVisibility(View.INVISIBLE);
        freeDrawBar.setVisibility(View.VISIBLE);

        freeDrawView.setVisibility(View.VISIBLE);
    }

    public void redColorHandler(View view) {
        freeDrawView.setPaintColor(Color.RED);
    }

    public void greenColorHandler(View view) {
        freeDrawView.setPaintColor(Color.GREEN);
    }

    public void blueColorHandler(View view) {
        freeDrawView.setPaintColor(Color.BLUE);
    }

    public void reverHandler(View view) {

        freeDrawView.undoLast();
    }

    public void confirmDrawHandler(View view) {
        toolsBar.setVisibility(View.VISIBLE);
        cancel_anno.setVisibility(View.VISIBLE);
        freeDrawBar.setVisibility(View.INVISIBLE);
        freeDrawView.setVisibility(View.INVISIBLE);

        freeDrawView.getDrawScreenshot(new FreeDrawView.DrawCreatorListener() {
            @Override
            public void onDrawCreated(Bitmap draw) {
                Bitmap newBit = Bitmap.createBitmap(draw, rectBitmap.getStartX(), rectBitmap.getStartY(), rectBitmap.getWidth(), rectBitmap.getHeight());;
                currentBitmap = UtilsGetRectBitmap.mergeBitmap(currentBitmap, newBit);
                mongtageView.setImageBitmap(currentBitmap);
                freeDrawView.clearDraw();
            }

            @Override
            public void onDrawCreationError() {

            }
        });
    }

    public void textHandler(View view) {
        toolsBar.setVisibility(View.INVISIBLE);
//        cropperBar.setVisibility(View.VISIBLE);
    }


    public void annotationConfirmHandler(View view) {
        annotationBar.setVisibility(View.INVISIBLE);
        Log.d(TAG, "annotationConfirmHandler: " + dragFrameLayout.getWidth());
        Log.d(TAG, "annotationConfirmHandler: " + dragFrameLayout.getHeight());

        Bitmap bitmap = Bitmap.createBitmap(dragFrameLayout.getWidth(), dragFrameLayout.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        dragFrameLayout.draw(canvas);

        Bitmap newBit = Bitmap.createBitmap(bitmap, rectBitmap.getStartX(), rectBitmap.getStartY(), rectBitmap.getWidth(), rectBitmap.getHeight());;
        currentBitmap = UtilsGetRectBitmap.mergeBitmap(currentBitmap, newBit);
        mongtageView.setImageBitmap(currentBitmap);

        annotationText.setText("");
        editAnnotation.setText("");

        dragFrameLayout.setVisibility(View.INVISIBLE);
        annotationBar.setVisibility(View.INVISIBLE);
        toolsBar.setVisibility(View.VISIBLE);
        cancel_anno.setVisibility(View.VISIBLE);

    }

    public void textButtonHandler(View view) {
        dragFrameLayout.setVisibility(View.VISIBLE);
        annotationBar.setVisibility(View.VISIBLE);
        toolsBar.setVisibility(View.INVISIBLE);
        cancel_anno.setVisibility(View.INVISIBLE);
    }

    public void starButtonHandler(View view) {
        toolsBar.setVisibility(View.INVISIBLE);
        cancel_anno.setVisibility(View.INVISIBLE);
        starBar.setVisibility(View.VISIBLE);
    }

    public void starConfirmHandler(View view) {
        toolsBar.setVisibility(View.VISIBLE);
        cancel_anno.setVisibility(View.VISIBLE);
        starBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        Log.d(TAG, "onKeyDown: " + keyCode);

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            finish();
            Intent intent = new Intent(AnnotationActivity.this, HomeActivity.class);
            startActivity(intent);

        }

        return false;
    }

    public void uploadHandler(View view) {

        Log.d(TAG, "uploadHandler: " + seekBarProgress.getText());

//        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1
//                    );
//            return;
//        }

        if (seekBarProgress.getText().equals("无分")) {
            Toast.makeText(this, "当前作业还没有给分，请批示打分后上传！", Toast.LENGTH_SHORT).show();
        }else {
//            if (MediaStore.Images.Media.insertImage(getContentResolver(), currentBitmap, "fangtian", "testNote") == null ){
//                Log.d(TAG, "uploadHandler: " + false);
//            }else {
//                Log.d(TAG, "uploadHandler: " + true);
//                toolsBar.setVisibility(View.INVISIBLE);
//                pager.setVisibility(View.VISIBLE);
//                mongtageView.setVisibility(View.INVISIBLE);
//                annotationButton.setVisibility(View.VISIBLE);
//                Toast.makeText(this, "图片保存成功", Toast.LENGTH_LONG).show();
//
//
//                ConfigApp.exercises.remove(pager.getCurrentItem());
//
//                pagerAdapter.notifyDataSetChanged();
//
//                if (ConfigApp.exercises.size()==0) {
//                    Toast.makeText(AnnotationActivity.this, "恭喜你，所有作业都批改完了", Toast.LENGTH_LONG).show();
//                }
//
//
//
//
//            }


            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            try {
                currentBitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
                byte[] byteArray = stream.toByteArray();

                RequestParams params = new RequestParams();
                params.put("qid", ConfigApp.exercises.get(pager.getCurrentItem()).getQid());
                params.put("stdid", ConfigApp.exercises.get(pager.getCurrentItem()).getStdid());
                params.put("clid", ConfigApp.exercises.get(pager.getCurrentItem()).getClid());
                params.put("score", seekBarProgress.getText());
                params.put("correctedpic", Base64.encodeToString(byteArray, Base64.DEFAULT));
                Log.d(TAG, "onClick: " + params.toString());

                AsyncHttpClient client = new AsyncHttpClient();
                client.post(ConfigApp.ANNOSERVER, params, responseHandler);
            }catch (Exception e) {
                Log.d(TAG, "uploadHandler: " + e);
                Toast.makeText(AnnotationActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }








        }



    }

    private JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
        @Override
        public void onStart() {
            super.onStart();
            imageUploadProgress.setVisibility(View.VISIBLE);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

            Log.d(TAG, "onSuccess: " + response.toString());

            if (statusCode == 200) {
                int status = 0;
                String info = "";
                JSONObject data = new JSONObject();
                try {
                    status = Integer.parseInt(response.get("status").toString());
                    info = response.get("info").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AnnotationActivity.this, "数据解析失败", Toast.LENGTH_LONG).show();
                }

                if (status == 0) {
                    Toast.makeText(AnnotationActivity.this, info, Toast.LENGTH_LONG).show();
                } else {
                    imageUploadProgress.setVisibility(View.INVISIBLE);

                    toolsBar.setVisibility(View.INVISIBLE);
                    cancel_anno.setVisibility(View.INVISIBLE);
                    pager.setVisibility(View.VISIBLE);
                    mongtageView.setVisibility(View.INVISIBLE);
                    annotationButton.setVisibility(View.VISIBLE);
                    Toast.makeText(AnnotationActivity.this, "作业批改提交成功", Toast.LENGTH_LONG).show();


                    ConfigApp.exercises.remove(pager.getCurrentItem());

                    pagerAdapter.notifyDataSetChanged();

                    seekBarProgress.setText("无分");
//            starSeekBar.setProgress(0);

                    if (ConfigApp.exercises.size() == 0) {
                        Toast.makeText(AnnotationActivity.this, "恭喜你，所有作业都批改完了", Toast.LENGTH_LONG).show();
                    }
                }

            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers,String responseString, Throwable error) {
            imageUploadProgress.setVisibility(View.INVISIBLE);
            Log.d(TAG, "onFailure: " + statusCode);
            Log.d(TAG, "onFailure: " + responseString);
            Toast.makeText(AnnotationActivity.this, "网络异常，请查看手机是否联网" + responseString, Toast.LENGTH_LONG).show();
            Log.d(TAG, "onFailure: " + responseString);
        }

        @Override
        public void onRetry(int retryNo) {
            super.onRetry(retryNo);
        }
    };


    public void displayExerButton(View view) {
//        Toast.makeText(this, ConfigApp.showQstUrl + ConfigApp.exercises.get(pager.getCurrentItem()).getQid(), Toast.LENGTH_SHORT).show();
        webView.setVisibility(View.VISIBLE);
        closeWeb.setVisibility(View.VISIBLE);
        webView.loadUrl(ConfigApp.showQstUrl + ConfigApp.exercises.get(pager.getCurrentItem()).getQid());
        toolsBar.setVisibility(View.INVISIBLE);
        cancel_anno.setVisibility(View.INVISIBLE);
    }

    public void closeWebHandler(View view) {
        webView.setVisibility(View.INVISIBLE);
        closeWeb.setVisibility(View.INVISIBLE);
        toolsBar.setVisibility(View.VISIBLE);
        cancel_anno.setVisibility(View.VISIBLE);
    }

    public void cancelAnno(View view) {
        toolsBar.setVisibility(View.INVISIBLE);
        cancel_anno.setVisibility(View.INVISIBLE);
        pager.setVisibility(View.VISIBLE);
        mongtageView.setVisibility(View.INVISIBLE);
        annotationButton.setVisibility(View.VISIBLE);

    }
}
