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

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ClassRoomActivity extends AppCompatActivity {

    public static final String ACTION = "ACTION";
    private static final String ACTION_PRESENT = "签到";
    private static final int PRESENT_REQEUST = 1;

    private static final String ACTION_LEFT = "签退";
    private static final int LEFT_REQEUST = 0;

    public static final String RETURN_BARCODE = "RETURN_BARCODE";

    private ArrayList<Student> students = getStudents();
    private StudentListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_room);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);



        String courseId = getIntent().getStringExtra(MyCourseActivity.COURSE_ID);
        Course course = DataProvider.courseMap.get(courseId);

        toolbar.setTitle(course.getTitleCoruse());
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private ArrayList<Student> getStudents() {
        ArrayList<Student> listStu = new ArrayList<Student>();
        Student stu1 = new Student("12323","庄韪菱", "", "");
        listStu.add(stu1);
        Student stu2 = new Student("2899750","吴兢","","");
        listStu.add(stu2);
        Student stu3 = new Student("395","张鸿飞","","");
        listStu.add(stu3);
        return listStu;
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
                    Date date = new Date();
                    SimpleDateFormat dateformatter = new SimpleDateFormat("HH:mm:ss");
                    adapter.getItem(position).setTimePresent(dateformatter.format(date));
                    adapter.notifyDataSetChanged();
                    Snackbar.make(findViewById(R.id.listStudentView), adapter.getItem(position).getStudentName() + " 签到成功", Snackbar.LENGTH_LONG)
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
                    Date date = new Date();
                    SimpleDateFormat dateformatter = new SimpleDateFormat("HH:mm:ss");
                    adapter.getItem(position).setTimeLeft(dateformatter.format(date));
                    adapter.notifyDataSetChanged();
                    Snackbar.make(findViewById(R.id.listStudentView), adapter.getItem(position).getStudentName() + " 签退成功", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        }
    }
}
