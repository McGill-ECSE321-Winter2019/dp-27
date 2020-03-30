package ca.mcgill.cooperator.dto;

public class AuthUserDto {

    private String user;
    private String password;

    public AuthUserDto(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public String getUser() {
        return this.user;
    }

    public String getPassword() {
        return this.password;
    }
}
