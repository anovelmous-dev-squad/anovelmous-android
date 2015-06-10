package com.anovelmous.app.data.api.model;

import com.anovelmous.app.data.api.resource.Group;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by IntelliJ
 * Author: Greg Ziegan on 6/7/15.
 */
public class RealmGroup extends RealmObject {
    @PrimaryKey private long id;
    private String url;
    private String name;

    public RealmGroup() {

    }

    public RealmGroup(Group group) {
        id = group.id;
        url = group.url;
        name = group.name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
