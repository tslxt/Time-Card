package me.fangtian.lxt.timecard;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by lxt on 16/8/29.
 */
public class Course {

    private String courseId;
    private String titleCoruse;
    private String timeCourse;
    private ArrayList<Student> students = new ArrayList<Student>();


    public Course(String courseId, String titleCoruse, String timeCourse) {
        this.courseId = courseId;
        this.titleCoruse = titleCoruse;
        this.timeCourse = timeCourse;
    }

    public String getCourseId() { return courseId; }
    public String getTitleCoruse() { return titleCoruse; }
    public String getTimeCourse() { return timeCourse; }
    public ArrayList<Student> getStudents() { return students; }

    public void AddOneStudent(Student student) {
        students.add(student);
    }
}
