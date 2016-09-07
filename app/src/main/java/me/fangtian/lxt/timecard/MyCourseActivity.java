package me.fangtian.lxt.timecard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyCourseActivity extends AppCompatActivity {

    public static final String COURSE_ID = "COURSE_ID";
    private static final String TAG = "tslxt_mycourse";
    //    public List<Course> courses = null;
//    private List<Course> courses = DataProvider.courseList;
//    public static List<Course> courses = new ArrayList<>();
//    public static Map<String, Course> courseMap = new HashMap<>();
    private String teacherName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        DataProvider.clearData();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_course);

        String data = getIntent().getStringExtra(LoginActivity.COURSEDATA);
        JSONObject dataJson;

        try {
            dataJson = new JSONObject(data);
            teacherName = dataJson.get("name").toString();
            JSONArray list = (JSONArray) dataJson.get("list");
//            Log.d(TAG, "list: " + list.length());
            for (int i = 0; i < list.length(); i++) {
//                Log.d(TAG, "for " + i);
                JSONObject courseJson = (JSONObject) list.get(i);
                JSONArray listStudent = (JSONArray) courseJson.get("stdlist");
//                Log.d(TAG, "listStudent: " + listStudent.toString());
                Course course = new Course(courseJson.get("clid").toString(), courseJson.get("coursename").toString(), courseJson.get("classname").toString(), courseJson.get("starttime").toString());
                for (int s = 0; s<listStudent.length(); s++) {
                    JSONObject studentJson = (JSONObject) listStudent.get(s);
                    String presentTime = (String) studentJson.get("intime");
                    String leftTime = (String) studentJson.get("outtime");

                    if (presentTime.length() > 10) {
                        presentTime = presentTime.substring(10);
//                        Log.d(TAG, "present: " + presentTime);
                    }
                    if (leftTime.length() > 10) {
                        leftTime = leftTime.substring(10);
//                        Log.d(TAG, "left: " + leftTime);
                    }

                    Student student = new Student(studentJson.get("stdid").toString(), studentJson.get("stdname").toString(), presentTime, leftTime);
                    course.AddOneStudent(student);
                }
                DataProvider.addOneCourseById(course.getCourseId(),course);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(teacherName + "的教师后台");
        setSupportActionBar(toolbar);


        CourseListAdapter adapter = new CourseListAdapter(
                this, R.layout.list_course, DataProvider.courseList);
        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(getClass().toString(), "onItemClick: " + position);
                Intent intent = new Intent(MyCourseActivity.this, ClassRoomActivity.class);

                Course course = DataProvider.courseList.get(position);
                intent.putExtra(COURSE_ID, course.getCourseId());

                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_quit:
                showQuitDialog();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void showQuitDialog() {
//        创建退出对话框
        AlertDialog isExit = new AlertDialog.Builder(this).create();
        // 设置对话框标题
//        isExit.setTitle("系统提示");
        // 设置对话框消息
        isExit.setMessage("您确定要退出程序吗?");
//         添加选择按钮并注册监听
        isExit.setButton(DialogInterface.BUTTON_POSITIVE,"确定", listener);
        isExit.setButton(DialogInterface.BUTTON_NEGATIVE,"取消", listener);
//         显示对话框
        isExit.show();

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        Log.d(TAG, "onKeyDown: " + keyCode);

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Log.d(TAG, "onKeyDown: equal");
//             创建退出对话框
            AlertDialog isExit = new AlertDialog.Builder(this).create();
            // 设置对话框标题
//            isExit.setTitle("系统提示");
            // 设置对话框消息
            isExit.setMessage("您确定要退出程序吗?");
            // 添加选择按钮并注册监听
            isExit.setButton(DialogInterface.BUTTON_POSITIVE,"确定", listener);
            isExit.setButton(DialogInterface.BUTTON_NEGATIVE,"取消", listener);
            // 显示对话框
            isExit.show();
        }

        return false;
    }

    /**监听对话框里面的button点击事件*/
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

        public void onClick(DialogInterface dialog, int which) {

            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    finish();
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };
}
