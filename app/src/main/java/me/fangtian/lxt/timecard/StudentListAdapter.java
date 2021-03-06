package me.fangtian.lxt.timecard;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by lxt on 16/8/30.
 */
public class StudentListAdapter extends ArrayAdapter<Student> {

    private List<Student> students;

    public StudentListAdapter(Context context, int resource, List<Student> objects) {
        super(context, resource, objects);
        students = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_student, parent, false);
        }

        Student student = students.get(position);

        LinearLayout bg = (LinearLayout) convertView.findViewById(R.id.item_student);
        bg.setBackgroundColor(student.getSelected() == true? Color.LTGRAY:Color.TRANSPARENT);

        TextView idText = (TextView) convertView.findViewById(R.id.studentId);
        idText.setText(student.getStudentId());

        TextView nameText = (TextView) convertView.findViewById(R.id.studentName);
        nameText.setText(student.getStudentName());

        TextView presentText = (TextView) convertView.findViewById(R.id.timePresent);
        presentText.setText(student.getTimePresent());

        TextView leftText = (TextView) convertView.findViewById(R.id.timeLeft);
        leftText.setText(student.getTimeLeft());

        return convertView;
    }

//    @Override
//    public void add(Student object) {
//        students.add(new Student(object.getStudentId(),object.getStudentName(),object.getTimePresent(),object.getTimeLeft()));
//    }
}
