package com.superware.config;

public class UserByToken implements HasRole{
    public static final String USER_ID = "userId";
    public static final String ROLE = "role";
    public static final String PASSWORD = "password";
    public static final String AUTHOR_ID = "authorId";
    public static final String PUBLISHER_ID = "publisherId";
    public static final String DATE = "date";
    public static final String IS_ACTIVE = "isActive";

    private int id;
    private String login;
    private String password;
    private RoleEntity role;
    private boolean isActive;
    private int authorId;
    private int publisherId;

    public UserByToken(){}

    public RoleEntity getRole() {
        return role;
    }

    @Override
    public RoleEntity getRoleEntity() {
        return role;
    }

    public void setRole(RoleEntity role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(int publisherId) {
        this.publisherId = publisherId;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", isActive=" + isActive +
                ", authorId=" + authorId +
                ", publisherId=" + publisherId +
                '}';
    }
}
