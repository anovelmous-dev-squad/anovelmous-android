package com.anovelmous.app.data.api;

import com.anovelmous.app.data.api.resource.Chapter;
import com.anovelmous.app.data.api.resource.Contributor;
import com.anovelmous.app.data.api.resource.FormattedNovelToken;
import com.anovelmous.app.data.api.resource.Novel;
import com.anovelmous.app.data.api.resource.NovelToken;
import com.anovelmous.app.data.api.resource.Token;
import com.anovelmous.app.data.api.resource.Vote;

import java.util.List;

import rx.Observable;

/**
 * Created by Greg Ziegan on 6/10/15.
 */
public interface RestService {
    Observable<List<Novel>> getAllNovels();
    Observable<Chapter> getChapter(String chapterId);
    Observable<List<Chapter>> getNovelChapters(String novelId);
    Observable<List<FormattedNovelToken>> getChapterText(String chapterId);
    Observable<List<Token>> getAllTokens();
    Observable<Token> getTokenFromContent(String content);
    Observable<Vote> castVote(Vote vote);
    Observable<List<String>> getGrammarFilteredStrings();
    Observable<Contributor> getContributorByFbToken(String accessToken);
    Observable<Contributor> getContributor(String contributorId);
    Observable<Contributor> createContributor(Contributor contributor);
    Observable<NovelToken> getMostRecentNovelToken(String chapterId);
}
