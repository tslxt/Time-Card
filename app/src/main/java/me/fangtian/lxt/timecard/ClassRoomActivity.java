package me.fangtian.lxt.timecard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mikepenz.iconics.context.IconicsLayoutInflater;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

import static me.fangtian.lxt.timecard.MyCourseActivity.COURSE_ID;

public class ClassRoomActivity extends AppCompatActivity {

    public static final String ACTION = "ACTION";
    private static final String ACTION_PRESENT = "签到";
    private static final int PRESENT_REQEUST = 1;

    private static final String ACTION_LEFT = "签退";
    private static final int LEFT_REQEUST = 0;

    public static final String RETURN_BARCODE = "RETURN_BARCODE";
    private static final String TAG = "tslxt_classroom";
    public static final String STUDENT = "STUDENT";

    //    private ArrayList<Student> students = getStudents();
    private ArrayList<Student> students = new ArrayList<>();
    private StudentListAdapter adapter;

    public String courseId = "";

    private boolean multipled = false;

    private ListView lv;
    private LinearLayout multipleBar;

    private MenuItem menuItem;

    private SwipeRefreshLayout swipeRefreshView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_room);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        courseId = getIntent().getStringExtra(COURSE_ID);
        Course course = DataProvider.courseMap.get(courseId);
        students = course.getStudents();
        toolbar.setTitle(course.getCourseName());


        setSupportActionBar(toolbar);

        multipleBar = (LinearLayout) findViewById(R.id.multiple_bar);

        multipleBar.setVisibility(View.INVISIBLE);

        adapter = new StudentListAdapter(
                this, R.layout.list_student, students);
        lv = (ListView) findViewById(R.id.listStudentView);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d(TAG, "onItemClick: " + position);
                Log.d(TAG, "onItemClick: " + id);
                Log.d(TAG, "onItemClick: " + students.get(position).getStudentId());
                Log.d(TAG, "onItemClick: " + view);

                if (multipled) {
//                    view.setBackgroundColor(Color.LTGRAY);
//                    students.get(position).getSelected() == true ? students.get(position).setSelected(false) :students.get(position).setSelected(true);
                    if (students.get(position).getSelected()) {
//                        view.setBackgroundColor(Color.TRANSPARENT);
                        students.get(position).setSelected(false);
                    } else {
//                        view.setBackgroundColor(Color.LTGRAY);
                        students.get(position).setSelected(true);
                    }

                    adapter.notifyDataSetChanged();


                } else {
                    Intent intent = new Intent(ClassRoomActivity.this, DetailStudentActivity.class);
                    ArrayList<String> student = new ArrayList<String>();
                    student.add(students.get(position).getStudentId());
                    student.add(students.get(position).getStudentName());
                    intent.putStringArrayListExtra(STUDENT, student);
                    startActivityForResult(intent, LEFT_REQEUST);
                }
            }
        });


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


        swipeRefreshView = (SwipeRefreshLayout) findViewById(R.id.srl);
        swipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "onRefresh: ");

                String url = ConfigApp.SERVER + ConfigApp.CLASS_UPDATE_API;
                url += "?";
                url += "clid=" + courseId;

                Log.d(TAG, "post: " + url);


                AsyncHttpClient client = new AsyncHttpClient();

                client.get(url, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if (statusCode == 200) {
                            int status = 0;
                            String info = "";
                            Log.d(TAG, "onSuccess: " + response.toString());
                            try {
                                status = Integer.parseInt(response.get("status").toString());
                                info = response.get("info").toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (status == 1) {
                                JSONArray data = new JSONArray();
                                try {
                                    data = (JSONArray) response.get("data");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.d(TAG, "onSuccess: " + data.length());

                                JSONObject std = new JSONObject();

                                ArrayList<Student> updated = new ArrayList<Student>();

                                for (int i =0; i < data.length(); i++) {
                                    try {
                                        std = (JSONObject) data.get(i);

                                        updated.add(new Student(std.get("stdid").toString(), std.getString("stdname").toString(),std.get("intime").toString(),std.get("outtime").toString()));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                adapter.clear();






                                for (int u = 0; u < updated.size(); u++) {
                                    adapter.add(updated.get(u));
                                }

                                Snackbar.make(findViewById(R.id.listStudentView), "数据更新成功", Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();

                                adapter.notifyDataSetChanged();


                            } else {
                        Snackbar.make(findViewById(R.id.listStudentView), info, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                            }

                        } else {
                            Snackbar.make(findViewById(R.id.listStudentView), " 数据更新失败，请稍后再试", Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                        }

                        swipeRefreshView.setRefreshing(false);
                    }
                });

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == PRESENT_REQEUST) {
            if (resultCode ==  RESULT_OK) {
//                Snackbar.make(findViewById(R.id.listStudentView), data.getStringExtra(RETURN_BARCODE) + "签到", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                int position = -1;

                for (int i=0; i<students.size();i++) {
//                Log.d("for loop", "students: " + i + students.get(i).getStudentId());
                Log.d("for loop", data.getStringExtra(RETURN_BARCODE) );
//                    if (Integer.parseInt(data.getStringExtra(RETURN_BARCODE)) == Integer.parseInt(students.get(i).getStudentId())) {
//                        position = i;
//                    }
                    if (data.getStringExtra(RETURN_BARCODE).equals(students.get(i).getStudentId())) {
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

                            } else {
                                Snackbar.make(findViewById(R.id.listStudentView), adapter.getItem(finalPosition).getStudentName() + " 上课签到失败: 服务不可用", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        }

                        @Override
                        public void onRetry(int retryNo) {
                            Toast.makeText(ClassRoomActivity.this, "网络异常,请检查您的网络设置", Toast.LENGTH_LONG).show();
                        }
                    });


                }else {
//                    Snackbar.make(findViewById(R.id.listStudentView), " 上课签到失败: 此学生不在本班列表", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();

                    final Date date = new Date();
                    final SimpleDateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    String url = ConfigApp.SERVER + ConfigApp.CHECKIN_API;
                    url += "?";
                    url += "stdid=" + data.getStringExtra(RETURN_BARCODE);
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
                                    Snackbar.make(findViewById(R.id.listStudentView), data.getStringExtra(RETURN_BARCODE) + " 上课签到成功", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                } else {
                                    Snackbar.make(findViewById(R.id.listStudentView), "上课签到失败: " + info, Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }

                            } else {
                                Snackbar.make(findViewById(R.id.listStudentView), adapter.getItem(finalPosition).getStudentName() + " 上课签到失败: 服务不可用", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        }
                    });
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
                    if (data.getStringExtra(RETURN_BARCODE).equals(students.get(i).getStudentId())) {
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

                            } else {
                                Snackbar.make(findViewById(R.id.listStudentView), adapter.getItem(finalPosition).getStudentName() + " 下课签到失败: 服务不可用", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        }

                        @Override
                        public void onRetry(int retryNo) {
                            Toast.makeText(ClassRoomActivity.this, "网络异常,请检查您的网络设置", Toast.LENGTH_LONG).show();
                        }
                    });


                }else {
                    Snackbar.make(findViewById(R.id.listStudentView), " 下课签到失败: 此学生不在本班列表", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_multiple, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        menuItem = item;

        if (multipled) {
            item.setTitle(R.string.multiple);
            multipled = false;
            showScanFloat(true);
            showMultipleBar(false);
            multipleSelect(false);
        } else {
            item.setTitle(R.string.cancle);
            multipled = true;
            showScanFloat(false);
            showMultipleBar(true);
        }


        return super.onOptionsItemSelected(item);
    }

    private void showScanFloat(boolean display) {
        if (display) {
            findViewById(R.id.do_present).setVisibility(View.VISIBLE);
            findViewById(R.id.do_left).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.do_present).setVisibility(View.INVISIBLE);
            findViewById(R.id.do_left).setVisibility(View.INVISIBLE);
        }
    }

    private void showMultipleBar(boolean display) {
        if (display) {
            multipleBar.setVisibility(View.VISIBLE);
        } else {
            multipleBar.setVisibility(View.INVISIBLE);
        }
    }


    public void allSelectClick(View view) {
        multipleSelect(true);

    }

    public void allCancleClick(View view) {
        multipleSelect(false);
    }

    private void multipleSelect(boolean choose) {
        for (int i = 0; i < lv.getAdapter().getCount(); i++) {
            Student st = (Student) lv.getAdapter().getItem(i);
            st.setSelected(choose);
        }
        adapter.notifyDataSetChanged();
    }



    public void multipleLeftClick(View view) {
        ArrayList<String> selections = new ArrayList<>();
        for (int i = 0; i < lv.getAdapter().getCount(); i++) {
            Student st = (Student) lv.getAdapter().getItem(i);
            if (st.getSelected()) selections.add(st.getStudentId().toString());
        }

        if (selections.size() > 0) {
            Toast.makeText(this, "你选择了 " + selections.size() + "条记录", Toast.LENGTH_SHORT).show();
            multipleLeft(selections);
        } else {
            Toast.makeText(this, "你什么都没有选择", Toast.LENGTH_SHORT).show();
        }
    }

    private void multipleLeft(ArrayList<String> selections) {

        final Date date = new Date();
        final SimpleDateFormat hmsformatter = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String url = ConfigApp.SERVER + ConfigApp.MUL_CHECK_OUT_API;
        url += "?";
        url += "stdid=" + selections.get(0);
        for (int i = 1; i < selections.size(); i++) {
            url += "~~~" + selections.get(i);
        }
        url += "&";
        url += "clid=" + courseId;
        url += "&";
        url += "status=" + 1;
        url += "&";
        url += "datetime=" + dateformatter.format(date);

        Log.d(TAG, "post: " + url);


        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    int status = 0;
                    String info = "";
                    Log.d(TAG, "onSuccess: " + response.toString());
                    try {
                        status = Integer.parseInt(response.get("status").toString());
                        info = response.get("info").toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (status == 1) {
//                        Snackbar.make(findViewById(R.id.listStudentView), adapter.getItem(finalPosition).getStudentName() + " 上课签到成功", Snackbar.LENGTH_LONG)
//                                .setAction("Action", null).show();
//                        adapter.getItem(finalPosition).setTimePresent(hmsformatter.format(date));
                        JSONArray data = new JSONArray();
                        try {
                            data = (JSONArray) response.get("data");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d(TAG, "onSuccess: " + data.length());

                        JSONObject std = new JSONObject();

                        int updated = 0;

                        for (int i =0; i < data.length(); i++) {
                            try {
                                std = (JSONObject) data.get(i);

                                if (std.get("outtime") != "") {
                                    for (int s=0; s < students.size(); s++) {

//                                        Log.d(TAG, "from server: " + std.get("stdid"));
//                                        Log.d(TAG, "from local: " + students.get(s).getStudentId());
//                                        Log.d(TAG, "onSuccess: " + students.get(s).getStudentId().equals(std.get("stdid")));
//                                        Log.d(TAG, "empty: " + (students.get(s).getTimeLeft().equals("")));
//                                        Log.d(TAG, "empty: " + (students.get(s).getTimeLeft()));

                                        if (students.get(s).getStudentId().equals(std.get("stdid")) && students.get(s).getTimeLeft().equals("")) {
//                                            Log.d(TAG, "yes");
                                            students.get(s).setTimeLeft(std.get("outtime").toString().substring(11));
//                                            Log.d(TAG, "outime: " + std.get("outtime").toString().substring(11));
                                            updated ++;
                                        }
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        Snackbar.make(findViewById(R.id.listStudentView), "更新" + updated + "条数据", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();

                        adapter.notifyDataSetChanged();
                        multipleSelect(false);
                        showMultipleBar(false);
                        showScanFloat(true);
                        multipled = false;

                        menuItem.setTitle(R.string.multiple);

                    } else {
//                        Snackbar.make(findViewById(R.id.listStudentView), adapter.getItem(finalPosition).getStudentName() + " 上课签到失败: " + info, Snackbar.LENGTH_LONG)
//                                .setAction("Action", null).show();
                    }

                } else {
                    Snackbar.make(findViewById(R.id.listStudentView), " 上课签到失败: 服务不可用", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            }

            @Override
            public void onRetry(int retryNo) {
                Toast.makeText(ClassRoomActivity.this, "网络异常,请检查您的网络设置", Toast.LENGTH_LONG).show();
            }
        });
    }
}
