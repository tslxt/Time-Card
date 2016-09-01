package me.fangtian.lxt.timecard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by lxt on 16/8/29.
 */
public class CourseListAdapter extends ArrayAdapter<Course> {

    private List<Course> courses;

    public CourseListAdapter(Context context, int resource, List<Course> objects) {
        super(context, resource, objects);
        courses = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_course, parent, false);
        }

        Course course = courses.get(position);

        TextView titleText = (TextView) convertView.findViewById(R.id.titleText);
        titleText.setText(course.getTitleCoruse());

        TextView timeText = (TextView) convertView.findViewById(R.id.timeCourse);
        timeText.setText(course.getTimeCourse());

        return convertView;
    }
}
