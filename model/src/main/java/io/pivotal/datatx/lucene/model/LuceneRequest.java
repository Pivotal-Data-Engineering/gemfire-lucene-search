package io.pivotal.datatx.lucene.model;

public class LuceneRequest {

    private String region;
    private String query;
    private String index;
    private String defaultField;

    public LuceneRequest(String region, String query, String index, String defaultField) {
        this.region = region;
        this.query = query;
        this.index = index;
        this.defaultField = defaultField;
    }

    public LuceneRequest() {
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getDefaultField() {
        return defaultField;
    }

    public void setDefaultField(String defaultField) {
        this.defaultField = defaultField;
    }
}
