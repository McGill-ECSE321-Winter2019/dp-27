package ca.mcgill.cooperator.dto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class NotificationDto {
    private int id;
    private String title;
    private String body;
    private Boolean seen;
    private String timeStamp;

    private StudentDto student;
    private AdminDto sender;

    public NotificationDto(
            int id,
            String title,
            String body,
            StudentDto student,
            AdminDto sender,
            Boolean seen,
            long timeStamp) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.seen = seen;
        DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm");
        this.timeStamp = simple.format(timeStamp);
        this.student = student;
        this.sender = sender;
    }

    /*--- Getters and Setters ---*/

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public StudentDto getStudent() {
        return this.student;
    }

    public void setStudent(StudentDto student) {
        this.student = student;
    }

    public AdminDto getSender() {
        return this.sender;
    }

    public void setSender(AdminDto sender) {
        this.sender = sender;
    }

    public Boolean getSeen() {
        return this.seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public String getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm");
        this.timeStamp = simple.format(timeStamp);
    }
}
