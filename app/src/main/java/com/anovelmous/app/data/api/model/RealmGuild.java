package com.anovelmous.app.data.api.model;

import com.anovelmous.app.data.api.resource.Guild;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by IntelliJ
 * Author: Greg Ziegan on 6/7/15.
 */
public class RealmGuild extends RealmObject {
    @PrimaryKey private String id;
    private String url;
    private String name;

    private String restVerb;
    private boolean lastRequestFinished;

    public RealmGuild() {

    }

    public RealmGuild(Guild guild) {
        id = (guild.id == null) ? UUID.randomUUID().toString() : guild.id;
        url = guild.url;
        name = guild.name;
        restVerb = guild.restVerb.toString();
        lastRequestFinished = guild.restVerb.equals(RestVerb.GET);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRestVerb() {
        return restVerb;
    }

    public void setRestVerb(String restVerb) {
        this.restVerb = restVerb;
    }

    public boolean isLastRequestFinished() {
        return lastRequestFinished;
    }

    public void setLastRequestFinished(boolean lastRequestFinished) {
        this.lastRequestFinished = lastRequestFinished;
    }
}
