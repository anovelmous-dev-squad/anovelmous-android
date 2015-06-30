package com.anovelmous.app.data.api.model;

import com.anovelmous.app.data.api.resource.Contributor;

import java.util.Date;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by IntelliJ
 * Author: Greg Ziegan on 6/7/15.
 */
public class RealmContributor extends RealmObject {
    @PrimaryKey private String id;
    private String url;
    private String username;
    @Index private String email;
    private RealmList<RealmGuild> groups;
    private Date dateJoined;
    private String authToken;
    private String fbAccessToken;

    private String restVerb;
    private boolean lastRequestFinished;

    public RealmContributor() {

    }

    public RealmContributor(RealmContributor realmContributor, Contributor contributor, Realm realm) {
        this(contributor, realm);
        if (!realmContributor.isLastRequestFinished() &&
                RestVerb.getValueForString(realmContributor.getRestVerb()) != RestVerb.GET)
            this.lastRequestFinished = true;

        if (realmContributor.getFbAccessToken() != null && contributor.fbAccessToken == null)
            this.fbAccessToken = realmContributor.getFbAccessToken();
        else
            this.fbAccessToken = contributor.fbAccessToken;
    }

    public RealmContributor(Contributor contributor, Realm realm) {
        id = (contributor.id == null) ? UUID.randomUUID().toString() : contributor.id;
        url = contributor.url;
        username = contributor.username;
        email = contributor.email;

        groups = new RealmList<>();
        for (String groupUrl : contributor.groups) {
            groups.add(realm.where(RealmGuild.class)
                            .equalTo("url", groupUrl)
                            .findFirst());
        }

        dateJoined = contributor.dateJoined.toDate();
        restVerb = contributor.restVerb.toString();
        lastRequestFinished = contributor.restVerb.equals(RestVerb.GET);

        fbAccessToken = contributor.fbAccessToken;
    }

    public RealmContributor(Contributor contributor, Realm realm, String authToken) {
        this(contributor, realm);
        this.authToken = authToken;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public RealmList<RealmGuild> getGroups() {
        return groups;
    }

    public void setGroups(RealmList<RealmGuild> groups) {
        this.groups = groups;
    }

    public Date getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(Date dateJoined) {
        this.dateJoined = dateJoined;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
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

    public String getFbAccessToken() {
        return fbAccessToken;
    }

    public void setFbAccessToken(String fbAccessToken) {
        this.fbAccessToken = fbAccessToken;
    }
}
