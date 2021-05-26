package pl.taskownia.model;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
public class AccountConfirmationToken {
    @Transient
    private final int expirationTime = 1 * 24 * 60; //TODO remove from entity

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    @Temporal(value= TemporalType.TIMESTAMP)
    private Date expiry;

    public AccountConfirmationToken(User user, String token) {
        this.user = user;
        this.token = token;

        Calendar c = Calendar.getInstance();
        c.setTime(new Date(System.currentTimeMillis()));
        c.add(Calendar.MINUTE, expirationTime);

        this.expiry = c.getTime();
    }

    public AccountConfirmationToken() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getExpiry() {
        return expiry;
    }

    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }
}
