package me.fangtian.lxt.timecard;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lxt on 16/9/4.
 */
public class ConfigApp {

//    public static String SERVER = "http://www.fangtian.me/fangtian_ol/admin.php/Index";
    public static String SERVER = "http://182.92.239.180/fangtian_ol_new/admin.php/Index";

    public static String PICSERVER = "http://101.200.124.30";

    public static String LOGIN_API = "/teacherLoginOk.html";

    public static String CHECKIN_API = "/stdCheckin.html";

    public static JSONObject data = new JSONObject();

    public static ArrayList<Exercise> exercises = new ArrayList<Exercise>();

    public static String teacherName = "";

    public static ArrayList<Course> courses = new ArrayList<Course>();

    public static boolean alreaday = false;

    public static String ANNOSERVER = SERVER + "/correctDwByMobile";

    public static String showQstUrl = SERVER + "/Quest/showForMobile/id/";

    public static JSONObject correctionstandard = new JSONObject();

    public static void resetConfig(){
        ConfigApp.alreaday = false;
        ConfigApp.exercises = null;
        ConfigApp.exercises = new ArrayList<Exercise>();
        ConfigApp.data = null;
        ConfigApp.data = new JSONObject();
        ConfigApp.courses = null;
        ConfigApp.courses =  new ArrayList<Course>();
    }

}
