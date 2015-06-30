package com.anovelmous.app.data.api.model;

import com.anovelmous.app.data.api.resource.Token;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by IntelliJ
 * Author: Greg Ziegan on 6/7/15.
 */
public class RealmToken extends RealmObject {
    @PrimaryKey private String id;
    private String url;
    private String content;
    private boolean isValid;
    private boolean isPunctuation;
    private Date createdAt;

    private String restVerb;
    private boolean lastRequestFinished;

    public RealmToken() {

    }

    public RealmToken(Token token) {
        id = (token.id == null) ? UUID.randomUUID().toString() : token.id;
        url = token.url;
        content = token.content;
        isValid = token.isValid;
        isPunctuation = token.isPunctuation;
        createdAt = token.createdAt.toDate();
        restVerb = token.restVerb.toString();
        lastRequestFinished = token.restVerb.equals(RestVerb.GET); // Does not matter if a GET finishes
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean isValid() {
        return isValid;
    }

    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }

    public boolean isPunctuation() {
        return isPunctuation;
    }

    public void setIsPunctuation(boolean isPunctuation) {
        this.isPunctuation = isPunctuation;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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
