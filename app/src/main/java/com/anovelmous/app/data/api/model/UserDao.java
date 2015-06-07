package com.anovelmous.app.data.api.model;

import com.anovelmous.app.data.api.resource.User;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by IntelliJ
 * Author: Greg Ziegan on 6/7/15.
 */
public class UserDao extends BaseDao {
    private String username;
    private String email;
    private RealmList<GroupDao> groups;
    private Date dateJoined;

    public UserDao() {

    }

    public UserDao(User user, Realm realm) {
        id = user.id;
        url = user.url;
        username = user.username;
        email = user.email;

        for (String groupId : user.groups) {
            groups.add(realm.where(GroupDao.class)
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

    public RealmList<GroupDao> getGroups() {
        return groups;
    }

    public void setGroups(RealmList<GroupDao> groups) {
        this.groups = groups;
    }

    public Date getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(Date dateJoined) {
        this.dateJoined = dateJoined;
    }
}
