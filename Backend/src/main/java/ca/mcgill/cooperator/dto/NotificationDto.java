package ca.mcgill.cooperator.dto;

public class NotificationDto {
    private Integer id;
    private String title;
    private String body;
    private Boolean seen;
    private Long timeStamp;

    private StudentDto student;
    private AdminDto sender;
    
    public NotificationDto() {};

    public NotificationDto(
            Integer id,
            String title,
            String body,
            StudentDto student,
            AdminDto sender,
            Boolean seen,
            Long timeStamp) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.seen = seen;
        this.timeStamp = timeStamp;
        this.student = student;
        this.sender = sender;
    }

    /*--- Getters and Setters ---*/

    public Integer getId() {
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

    public Long getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
