package com.erban.imageloader;


public class KLoadContext<Result> {

    private String imageUrl = null;
    private String mSuggestionQueryUrl = null;
    private SearchEngineType mSearchEngineType = null;
    private boolean isFromCache = false;
    private Result result = null;
    private boolean mIgnoreCache = false;


    public KLoadContext(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSuggestionQueryUrl() {
        return mSuggestionQueryUrl;
    }

    public void setSuggestionQueryUrl(String suggestionUrl) {
        this.mSuggestionQueryUrl = suggestionUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public SearchEngineType getSearchEngineType() {
        return mSearchEngineType;
    }

    public void setSearchEngineType(SearchEngineType searchEngineType) {
        this.mSearchEngineType = searchEngineType;
    }

    public boolean isFromCache() {
        return isFromCache;
    }

    public void setFromCache(boolean isFromCache) {
        this.isFromCache = isFromCache;
    }

    public boolean isIgnoreCache() {
        return mIgnoreCache;
    }

    public void setIgnoreCache(boolean ignoreCache) {
        this.mIgnoreCache = ignoreCache;
    }

    public enum SearchEngineType {
        BAIDU, SOKU, TAOBAO, EASOU, BING, GOOGLE
    }

}
