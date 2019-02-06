package io.pivotal.datatx.lucene;

import org.apache.geode.cache.lucene.LuceneIndex;
import org.apache.geode.cache.lucene.LuceneQueryException;
import org.apache.geode.cache.lucene.LuceneQueryProvider;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.search.Query;


public class LongRangeQueryProvider implements LuceneQueryProvider {
    String fieldName;
    long lowerValue;
    long upperValue;

    private transient Query luceneQuery;

    public LongRangeQueryProvider(String fieldName, long lowerValue, long upperValue) {
        this.fieldName = fieldName;
        this.lowerValue = lowerValue;
        this.upperValue = upperValue;
    }

    @Override
    public Query getQuery(LuceneIndex index) throws LuceneQueryException {
        if (luceneQuery == null) {
            luceneQuery = LongPoint.newRangeQuery(fieldName, lowerValue, upperValue);
        }
        System.out.println("LongRangeQueryProvider, using java serializable");
        return luceneQuery;
    }

}
