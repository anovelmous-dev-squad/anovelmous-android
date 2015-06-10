package com.anovelmous.app.data.api.model;

import com.anovelmous.app.data.api.resource.User;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by IntelliJ
 * Author: Greg Ziegan on 6/7/15.
 */
public class RealmUser extends RealmObject {
    @PrimaryKey private long id;
    private String url;
    private String username;
    private String email;
    private RealmList<RealmGroup> groups;
    private Date dateJoined;

    public RealmUser() {

    }

    public RealmUser(User user, Realm realm) {
        id = user.id;
        url = user.url;
        username = user.username;
        email = user.email;

        for (String groupId : user.groups) {
            groups.add(realm.where(RealmGroup.class)
                            .equalTo("url", groupId)
                            .findFirst()
            );
        }

        dateJoined = user.dateJoined.toDate();
    }

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

    public RealmList<RealmGroup> getGroups() {
        return groups;
    }

    public void setGroups(RealmList<RealmGroup> groups) {
        this.groups = groups;
    }

    public Date getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(Date dateJoined) {
        this.dateJoined = dateJoined;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
