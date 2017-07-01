package me.fangtian.lxt.timecard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "TSLXT";
//    private String teacherName = "";

    private TextView welcome;
    private TextView takeLesson;
    private TextView takeAnnotation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        welcome = (TextView) findViewById(R.id.welcome);
        takeLesson = (TextView) findViewById(R.id.takeLesson);
        takeAnnotation = (TextView) findViewById(R.id.takeAnnotation);

        Log.d(TAG, "course " + ConfigApp.courses.size());
        Log.d(TAG, "exercise " + ConfigApp.exercises.size());

        if (ConfigApp.alreaday == false) {
            try {
                ConfigApp.teacherName = ConfigApp.data.get("name").toString();

                ConfigApp.showQstUrl = ConfigApp.data.get("qstshowurl").toString();

                JSONArray list = (JSONArray) ConfigApp.data.get("list");

                /**
                 * 打卡课程列表
                 * */

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

                    ConfigApp.courses.add(course);
                }

                /**
                 * 天天习题数组
                 * */
                JSONArray dailywork = (JSONArray) ConfigApp.data.get("dailywork");
                Log.d(TAG, "onCreate: " + dailywork.toString());
                Log.d(TAG, "onCreate: " + dailywork.length());
                for (int i = 0; i < dailywork.length(); i++){
                    JSONObject courseJson = (JSONObject) dailywork.get(i);
                    JSONArray listExercises = (JSONArray) courseJson.get("list");
                    Log.d(TAG, "onCreate: " + listExercises.length());
                    for ( int s = 0; s < listExercises.length(); s++) {
                        Log.d(TAG, "onCreate: " + listExercises.get(s));
                        JSONObject tempObject = (JSONObject) listExercises.get(s);
                        Exercise tempEx = new Exercise();
                        tempEx.setClassid(tempObject.get("classid").toString());
                        tempEx.setClassname(tempObject.get("classname").toString());
                        tempEx.setStdid(tempObject.get("stdid").toString());
                        tempEx.setStdname(tempObject.get("stdname").toString());
                        tempEx.setAnswerpic(ConfigApp.PICSERVER + tempObject.get("answerpic").toString());
                        tempEx.setQid(tempObject.get("qid").toString());
                        tempEx.setClid(tempObject.get("clid").toString());
                        tempEx.setWorktime(tempObject.get("worktime").toString());
                        ConfigApp.exercises.add(tempEx);

//                        Log.d(TAG, "onCreate: " + tempEx.getAnswerpic());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ConfigApp.alreaday = true;






        setTitle(ConfigApp.teacherName + "老师的方田后台");

        Date date = new Date();
        DateFormat fullFormat = DateFormat.getDateInstance(DateFormat.FULL);

        String now = fullFormat.format(date);

        welcome.setText("亲爱的" + ConfigApp.teacherName + "，现在是" + now + "。欢迎您的到来！");

        if (ConfigApp.courses.size() == 0) {
            takeLesson.setText("您今天没有课！");
        }else {
            takeLesson.setText("您今天有" +ConfigApp.courses.size()+ "课要上。");
        }

        if (ConfigApp.exercises.size() == 0) {
            takeAnnotation.setText("您没有作业要批改。");
        }else {
            takeAnnotation.setText("您最近一段时间有" +ConfigApp.exercises.size()+ "份作业没有批改，要加油哦。");
        }


    }

    public void takeLessonHandler(View view) {
        Toast.makeText(this, "去打卡签到", Toast.LENGTH_LONG).show();
        finish();
        Intent intent = new Intent(HomeActivity.this, MyCourseActivity.class);
        startActivity(intent);
    }

    public void takeAnnotationHandler(View view) {
        Toast.makeText(this, "去判作业", Toast.LENGTH_LONG).show();
        finish();
        Intent intent = new Intent(HomeActivity.this, AnnotationActivity.class);
        startActivity(intent);
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
