package pl.taskownia.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sun.istack.NotNull;
import pl.taskownia.serializer.UserSerializer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@JsonSerialize(using = UserSerializer.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long id;
//    @Column
//    private AppRole appRole;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @ElementCollection(fetch = FetchType.EAGER) //Required by Spring Security
    List<Role> roles;
    @Column(nullable = false)
    private Status status;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "personal_data_id", referencedColumnName = "id_pers_data")
    @NotNull
    private UserPersonalData personalData = new UserPersonalData();
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id_address")
    @NotNull
    private UserAddress address = new UserAddress();
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id_image")
    @NotNull
    private UserImage image = new UserImage();
    @OneToMany(mappedBy = "user")
    private List<Chat> chat = new ArrayList<>();
    @OneToMany(mappedBy = "author")
    private List<Project> projectsAuthor = new ArrayList<>();
    @OneToMany(mappedBy = "maker")
    private List<Project> projectsMaker = new ArrayList<>();
    @Column(nullable = false)
    @Temporal(value= TemporalType.TIMESTAMP)
    private Date created_at;
    @Column(nullable = false)
    @Temporal(value= TemporalType.TIMESTAMP)
    private Date updated_at;

//    private enum AppRole {
//        AUTHOR, MAKER
//    }

    public enum Status { //TODO: add new states - for Zbychu
        STATE1, STATE2, STATE3
    }

    public User(){}

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

//    public AppRole getAppRole() {
//        return appRole;
//    }
//
//    public void setAppRole(AppRole role) {
//        this.appRole = role;
//    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public UserPersonalData getPersonalData() {
        return personalData;
    }

    public void setPersonalData(UserPersonalData personalData) {
        this.personalData = personalData;
    }

    public UserAddress getAddress() {
        return address;
    }

    public void setAddress(UserAddress address) {
        this.address = address;
    }

    public UserImage getImage() {
        return image;
    }

    public void setImage(UserImage image) {
        this.image = image;
    }

    public List<Chat> getPub_msg() {
        return chat;
    }

    public void setPub_msg(List<Chat> pub_msg) {
        this.chat = chat;
    }

    public List<Project> getProjectsAuthor() {
        return projectsAuthor;
    }

    public void setProjectsAuthor(List<Project> projectsAuthor) {
        this.projectsAuthor = projectsAuthor;
    }

    public List<Project> getProjectsMaker() {
        return projectsMaker;
    }

    public void setProjectsMaker(List<Project> projectsMaker) {
        this.projectsMaker = projectsMaker;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }
}
