package com.anovelmous.app.data.api.model;

import com.anovelmous.app.data.api.resource.Token;

/**
 * Created by IntelliJ
 * Author: Greg Ziegan on 6/7/15.
 */
public class TokenDao extends TimeStampedDao {
    private String content;
    private boolean isValid;
    private boolean isPunctuation;

    public TokenDao() {

    }

    public TokenDao(Token token) {
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
}
