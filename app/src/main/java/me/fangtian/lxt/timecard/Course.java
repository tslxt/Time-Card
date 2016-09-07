package me.fangtian.lxt.timecard;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by lxt on 16/8/29.
 */
public class Course {

    private String courseId;
    private String courseName;
    private String className;
    private String timeCourseStart;
//    private String timeCourseEnd;
    private ArrayList<Student> students = new ArrayList<Student>();


    public Course(String courseId, String courseName, String className, String timeCourseStart) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.className = className;
        this.timeCourseStart = timeCourseStart;
//        this.timeCourseEnd = timeCourseEnd;
    }

    public String getCourseId() { return courseId; }
    public String getCourseName() { return courseName; }
    public String getClassName() { return className; }
    public String getTimeCourseStart() { return timeCourseStart; }
//    public String getTimeCourseEnd() { return timeCourseEnd; }
    public ArrayList<Student> getStudents() { return students; }

    public void AddOneStudent(Student student) {
        students.add(student);
    }
}
