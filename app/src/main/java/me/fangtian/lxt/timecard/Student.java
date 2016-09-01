package me.fangtian.lxt.timecard;

/**
 * Created by lxt on 16/8/30.
 */
public class Student {

    private String studentId;
    private String studentName;

    private String timePresent;
    private String timeLeft;

    public Student (String studentId, String studentName, String timePresent, String timeLeft) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.timePresent = timePresent;
        this.timeLeft = timeLeft;
    }

    public String getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public String getTimePresent() { return timePresent; }
    public String getTimeLeft() { return timeLeft; }

    public void setTimePresent(String timePresent) { this.timePresent = timePresent; }
    public void setTimeLeft(String timeLeft) { this.timeLeft = timeLeft; }

}
