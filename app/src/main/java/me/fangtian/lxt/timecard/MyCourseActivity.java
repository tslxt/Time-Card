package me.fangtian.lxt.timecard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

public class MyCourseActivity extends AppCompatActivity {

    public static final String COURSE_ID = "COURSE_ID";
    private List<Course> courses = DataProvider.courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_course);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        CourseListAdapter adapter = new CourseListAdapter(
                this, R.layout.list_course, courses);
        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(getClass().toString(), "onItemClick: " + position);
                Intent intent = new Intent(MyCourseActivity.this, ClassRoomActivity.class);

                Course course = courses.get(position);
                intent.putExtra(COURSE_ID, course.getCourseId());

                startActivity(intent);
            }
        });

    }

}
