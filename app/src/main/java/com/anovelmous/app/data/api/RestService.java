package com.anovelmous.app.data.api;

import com.anovelmous.app.data.api.resource.Chapter;
import com.anovelmous.app.data.api.resource.FormattedNovelToken;
import com.anovelmous.app.data.api.resource.Novel;
import com.anovelmous.app.data.api.resource.Token;
import com.anovelmous.app.data.api.resource.Vote;

import java.util.List;

import rx.Observable;

/**
 * Created by Greg Ziegan on 6/10/15.
 */
public interface RestService {
    Observable<List<Novel>> getAllNovels();
    Observable<List<Chapter>> getNovelChapters(long novelId);
    Observable<List<FormattedNovelToken>> getChapterText(long chapterId);
    Observable<List<Token>> getAllTokens();
    Observable<Token> getTokenFromContent(String content);
    Observable<Vote> castVote(Vote vote);
    Observable<List<String>> getGrammarFilteredStrings();
}
