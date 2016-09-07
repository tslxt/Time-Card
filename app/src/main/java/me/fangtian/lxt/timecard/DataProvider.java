package me.fangtian.lxt.timecard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lxt on 16/8/29.
 */
public final class DataProvider {

    public static List<Course> courseList = new ArrayList<>();
    public static Map<String, Course> courseMap = new HashMap<>();


    public static void addCourse(String courseId, String courseName, String className,
                                   String timeCourse) {
        Course course = new Course(courseId, courseName, className, timeCourse);
        courseList.add(course);
        courseMap.put(courseId, course);
    }

    public static void addOneCourseById(String courseId, Course course) {
        courseList.add(course);
        courseMap.put(courseId, course);
    }

    public static List<String> getCourseNames() {
        List<String> list = new ArrayList<>();
        for (Course course : courseList) {
            list.add(course.getCourseName());
        }
        return list;
    }

    public static List<Course> getFilteredList(String searchString) {

        List<Course> filteredList = new ArrayList<>();
        for (Course course : courseList) {
            if (course.getCourseId().contains(searchString)) {
                filteredList.add(course);
            }
        }

        return filteredList;

    }

    public static void clearData() {
        courseList.clear();
        courseMap.clear();
    }


}
