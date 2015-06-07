package com.anovelmous.app.data.api.transforms;

import com.anovelmous.app.data.api.resource.Novel;
import com.anovelmous.app.data.api.resource.NovelsResponse;

import java.util.Collections;
import java.util.List;

import rx.functions.Func1;

public final class SearchResultToNovelList implements Func1<NovelsResponse, List<Novel>> {
  private static volatile SearchResultToNovelList instance;

  public static SearchResultToNovelList instance() {
    if (instance == null) {
      instance = new SearchResultToNovelList();
    }
    return instance;
  }

  @Override public List<Novel> call(NovelsResponse novelsResponse) {
    return novelsResponse.items == null //
        ? Collections.<Novel>emptyList() //
        : novelsResponse.items;
  }
}
