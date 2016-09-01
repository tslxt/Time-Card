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

    static {

        addCourse("101",
                "六年级期末复习（上）",
                "上课时间： 2016-09-11 9:00:00");
        addCourse("102",
                "六年级期末复习（中）",
                "上课时间： 2016-09-11 14:00:00");
        addCourse("103",
                "六年级期末复习（下）",
                "上课时间： 2016-09-11 18:30:00");

    }

    private static void addCourse(String courseId, String titleCourse,
                                   String timeCourse) {
        Course course = new Course(courseId, titleCourse, timeCourse);
        courseList.add(course);
        courseMap.put(courseId, course);
    }

    public static List<String> getCourseNames() {
        List<String> list = new ArrayList<>();
        for (Course course : courseList) {
            list.add(course.getTitleCoruse());
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


}
