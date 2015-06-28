package com.anovelmous.app.data.api;

import com.anovelmous.app.data.api.model.RestVerb;
import com.anovelmous.app.data.api.resource.Chapter;
import com.anovelmous.app.data.api.resource.FormattedNovelToken;
import com.anovelmous.app.data.api.resource.Novel;
import com.anovelmous.app.data.api.resource.ResourceCount;
import com.anovelmous.app.data.api.resource.Token;
import com.anovelmous.app.data.api.resource.User;
import com.anovelmous.app.data.api.resource.Vote;
import com.facebook.AccessToken;

import java.util.List;

import rx.Observable;

/**
 * Created by IntelliJ
 * Author: Greg Ziegan on 6/8/15.
 */
public interface PersistenceService {
    Observable<List<Novel>> novels();
    Observable<ResourceCount> novelsCount();
    Observable<Novel> saveNovel(Novel novel);

    Observable<Chapter> chapter(long chapterId);
    Observable<List<Chapter>> chapters(long novelId);
    Observable<ResourceCount> chaptersCount();
    Observable<Chapter> saveChapter(Chapter chapter);

    Observable<List<FormattedNovelToken>> chapterText(long chapterId);
    Observable<ResourceCount> chapterTextTokenCount(long chapterId);
    Observable<FormattedNovelToken> saveFormattedNovelToken(FormattedNovelToken formattedNovelToken);

    Observable<List<Token>> tokens();
    Observable<ResourceCount> tokensCount();
    Observable<Token> lookupTokenByContent(String content);
    Observable<List<Token>> saveTokens(List<Token> tokens);
    Observable<Token> saveToken(Token token);

    Observable<Vote> saveVote(Vote vote);

    Observable<User> getUser(AccessToken accessToken);
    Observable<User> getUser(String authToken);
    Observable<User> getUser(long userId);
    Observable<User> createUser(User user);
}
