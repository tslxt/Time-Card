package me.fangtian.lxt.timecard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.zxing.Result;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ClassRoomActivity extends AppCompatActivity {

    public static final String ACTION = "ACTION";
    private static final String ACTION_PRESENT = "签到";
    private static final int PRESENT_REQEUST = 1;

    private static final String ACTION_LEFT = "签退";
    private static final int LEFT_REQEUST = 0;

    public static final String RETURN_BARCODE = "RETURN_BARCODE";
    private static final String TAG = "tslxt_classroom";

    //    private ArrayList<Student> students = getStudents();
    private ArrayList<Student> students = new ArrayList<>();
    private StudentListAdapter adapter;

    public String courseId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_room);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);



        courseId = getIntent().getStringExtra(MyCourseActivity.COURSE_ID);
        Course course = DataProvider.courseMap.get(courseId);
        students = course.getStudents();
        toolbar.setTitle(course.getCourseName());


        setSupportActionBar(toolbar);

        adapter = new StudentListAdapter(
                this, R.layout.list_student, students);
        ListView lv = (ListView) findViewById(R.id.listStudentView);
        lv.setAdapter(adapter);


        FloatingActionButton doPresent = (FloatingActionButton) findViewById(R.id.do_present);
        doPresent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassRoomActivity.this, BarCodeScanActivity.class);
                intent.putExtra(ACTION, ACTION_PRESENT);
                startActivityForResult(intent, PRESENT_REQEUST);
            }
        });

        FloatingActionButton doLeft = (FloatingActionButton) findViewById(R.id.do_left);
        doLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassRoomActivity.this, BarCodeScanActivity.class);
                intent.putExtra(ACTION, ACTION_LEFT);
                startActivityForResult(intent, LEFT_REQEUST);
            }
        });

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PRESENT_REQEUST) {
            if (resultCode ==  RESULT_OK) {
//                Snackbar.make(findViewById(R.id.listStudentView), data.getStringExtra(RETURN_BARCODE) + "签到", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                int position = -1;

                for (int i=0; i<students.size();i++) {
//                Log.d("for loop", "students: " + i + students.get(i).getStudentId());
//                Log.d("for loop", data.getStringExtra(RETURN_BARCODE) );
                    if (Integer.parseInt(data.getStringExtra(RETURN_BARCODE)) == Integer.parseInt(students.get(i).getStudentId())) {
                        position = i;
                    }
                }



//            Log.d("for loop", "position " + position);

                if (position != -1) {
                    final Date date = new Date();
                    final SimpleDateFormat hmsformatter = new SimpleDateFormat("HH:mm:ss");
                    SimpleDateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    String url = ConfigApp.SERVER + ConfigApp.CHECKIN_API;
                    url += "?";
                    url += "stdid=" + adapter.getItem(position).getStudentId();
                    url += "&";
                    url += "clid=" + courseId;
                    url += "&";
                    url += "status=" + 0;
                    url += "&";
                    url += "datetime=" + dateformatter.format(date);

                    Log.d(TAG, "post: " + url);


                    AsyncHttpClient client = new AsyncHttpClient();

                    final int finalPosition = position;
                    client.get(url, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            if (statusCode == 200) {
                                int status = 0;
                                String info = "";
                                try {
                                    status = Integer.parseInt(response.get("status").toString());
                                    info = response.get("info").toString();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                if (status == 1) {
                                    Snackbar.make(findViewById(R.id.listStudentView), adapter.getItem(finalPosition).getStudentName() + " 上课签到成功", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                    adapter.getItem(finalPosition).setTimePresent(hmsformatter.format(date));
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Snackbar.make(findViewById(R.id.listStudentView), adapter.getItem(finalPosition).getStudentName() + " 上课签到失败: " + info, Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }

                            }
                        }

                    });


                }else {
                    Snackbar.make(findViewById(R.id.listStudentView), " 上课签到失败: 此学生不在本班列表", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }


        }
        if (requestCode == LEFT_REQEUST) {
            if (resultCode ==  RESULT_OK) {
//                Snackbar.make(findViewById(R.id.listStudentView), data.getStringExtra(RETURN_BARCODE) + "签退", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                int position = -1;

                for (int i=0; i<students.size();i++) {
//                Log.d("for loop", "students: " + i + students.get(i).getStudentId());
//                Log.d("for loop", data.getStringExtra(RETURN_BARCODE) );
                    if (Integer.parseInt(data.getStringExtra(RETURN_BARCODE)) == Integer.parseInt(students.get(i).getStudentId())) {
                        position = i;
                    }
                }

//            Log.d("for loop", "position " + position);

                if (position != -1) {
                    final Date date = new Date();
                    final SimpleDateFormat hmsformatter = new SimpleDateFormat("HH:mm:ss");
                    SimpleDateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    String url = ConfigApp.SERVER + ConfigApp.CHECKIN_API;
                    url += "?";
                    url += "stdid=" + adapter.getItem(position).getStudentId();
                    url += "&";
                    url += "clid=" + courseId;
                    url += "&";
                    url += "status=" + 1;
                    url += "&";
                    url += "datetime=" + dateformatter.format(date);

                    Log.d(TAG, "post: " + url);


                    AsyncHttpClient client = new AsyncHttpClient();

                    final int finalPosition = position;
                    client.get(url, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            if (statusCode == 200) {
                                int status = 0;
                                String info = "";
                                try {
                                    status = Integer.parseInt(response.get("status").toString());
                                    info = response.get("info").toString();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                if (status == 1) {
                                    Snackbar.make(findViewById(R.id.listStudentView), adapter.getItem(finalPosition).getStudentName() + " 下课签到成功", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                    adapter.getItem(finalPosition).setTimeLeft(hmsformatter.format(date));
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Snackbar.make(findViewById(R.id.listStudentView), adapter.getItem(finalPosition).getStudentName() + " 下课签到失败: " + info, Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }

                            }
                        }

                    });


                }else {
                    Snackbar.make(findViewById(R.id.listStudentView), " 下课签到失败: 此学生不在本班列表", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        }
    }
}
