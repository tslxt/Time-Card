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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        DataProvider.clearData();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_course);

        for (int i = 0; i < ConfigApp.courses.size(); i++){
            DataProvider.addOneCourseById(ConfigApp.courses.get(i).getCourseId(), ConfigApp.courses.get(i));
        }

//        DataProvider.addOneCourseById(course.getCourseId(),course);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(ConfigApp.teacherName + "的教师后台");
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
////             创建退出对话框
//            AlertDialog isExit = new AlertDialog.Builder(this).create();
//            // 设置对话框标题
////            isExit.setTitle("系统提示");
//            // 设置对话框消息
//            isExit.setMessage("您确定要退出程序吗?");
//            // 添加选择按钮并注册监听
//            isExit.setButton(DialogInterface.BUTTON_POSITIVE,"确定", listener);
//            isExit.setButton(DialogInterface.BUTTON_NEGATIVE,"取消", listener);
//            // 显示对话框
//            isExit.show();
            finish();
            Intent intent = new Intent(MyCourseActivity.this, HomeActivity.class);
            startActivity(intent);

        }

        return false;
    }

    /**监听对话框里面的button点击事件*/
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

        public void onClick(DialogInterface dialog, int which) {

            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    ConfigApp.alreaday = false;
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
