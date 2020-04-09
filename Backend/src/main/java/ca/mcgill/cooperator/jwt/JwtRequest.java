package ca.mcgill.cooperator.jwt;

public class JwtRequest {

    private String user;
    private String password;
    private String role;
    private String token;

    public JwtRequest() {}

    public JwtRequest(String user, String password, String role, String token) {
        this.user = user;
        this.password = password;
        this.role = role;
        this.token = token;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
