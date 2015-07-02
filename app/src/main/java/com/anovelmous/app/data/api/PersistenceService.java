package com.anovelmous.app.data.api;

import com.anovelmous.app.data.api.resource.Chapter;
import com.anovelmous.app.data.api.resource.Contributor;
import com.anovelmous.app.data.api.resource.FormattedNovelToken;
import com.anovelmous.app.data.api.resource.Novel;
import com.anovelmous.app.data.api.resource.ResourceCount;
import com.anovelmous.app.data.api.resource.Token;
import com.anovelmous.app.data.api.resource.Vote;

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

    Observable<Chapter> chapter(String chapterId);
    Observable<List<Chapter>> chapters(String novelId);
    Observable<ResourceCount> chaptersCount();
    Observable<Chapter> saveChapter(Chapter chapter);

    Observable<List<FormattedNovelToken>> chapterText(String chapterId);
    Observable<ResourceCount> chapterTextTokenCount(String chapterId);
    Observable<FormattedNovelToken> saveFormattedNovelToken(FormattedNovelToken formattedNovelToken);

    Observable<List<Token>> tokens();
    Observable<ResourceCount> tokensCount();
    Observable<Token> lookupTokenByContent(String content);
    Observable<List<Token>> saveTokens(List<Token> tokens);
    Observable<Token> saveToken(Token token);

    Observable<Vote> saveVote(Vote vote);

    Observable<Contributor> getContributorByFbToken(String accessToken);
    Observable<Contributor> getContributor(String contributorId);
    Observable<Contributor> createContributor(Contributor contributor);
    Observable<Contributor> updateContributor(Contributor contributor);
}
