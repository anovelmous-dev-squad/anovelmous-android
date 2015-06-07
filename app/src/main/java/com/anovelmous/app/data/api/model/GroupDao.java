package com.anovelmous.app.data.api.model;

import com.anovelmous.app.data.api.resource.Group;

/**
 * Created by IntelliJ
 * Author: Greg Ziegan on 6/7/15.
 */
public class GroupDao extends BaseDao {
    private String name;

    public GroupDao() {

    }

    public GroupDao(Group group) {
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
}
