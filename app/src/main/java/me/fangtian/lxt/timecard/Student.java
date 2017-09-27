package me.fangtian.lxt.timecard;

/**
 * Created by lxt on 16/8/30.
 */
public class Student {

    private String studentId;
    private String studentName;

    private String timePresent;
    private String timeLeft;

    private boolean selected = false;

    public Student(String studentId, String studentName, String timePresent, String timeLeft) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.timePresent = verifyString(timePresent);
        this.timeLeft = verifyString(timeLeft);
    }

    public String getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public String getTimePresent() { return timePresent; }
    public String getTimeLeft() { return timeLeft; }
    public boolean getSelected() { return selected; }

    public void setTimePresent(String timePresent) {
        this.timePresent = verifyString(timePresent);
    }
    public void setTimeLeft(String timeLeft) {
        this.timeLeft = verifyString(timeLeft);
    }
    public void setSelected(Boolean selected) { this.selected = selected; }

    private String verifyString(String date) {
        if (date.length() > 11) {
            return date.substring(11);
        } else {
            return date;
        }
    }

}