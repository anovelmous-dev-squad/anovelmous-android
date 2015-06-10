package com.anovelmous.app.data.api.model;

import com.anovelmous.app.data.api.resource.Token;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by IntelliJ
 * Author: Greg Ziegan on 6/7/15.
 */
public class RealmToken extends RealmObject {
    @PrimaryKey private long id;
    private String url;
    private String content;
    private boolean isValid;
    private boolean isPunctuation;
    private Date createdAt;

    public RealmToken() {

    }

    public RealmToken(Token token) {
        id = token.id;
        url = token.url;
        content = token.content;
        isValid = token.isValid;
        isPunctuation = token.isPunctuation;
        createdAt = token.createdAt.toDate();
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
