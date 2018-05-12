package com.kodcu.dao;
/*
 * Created by hakdogan on 01/12/2017
 */

import com.google.gson.Gson;
import com.kodcu.entity.Document;
import com.kodcu.prop.ConfigProps;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class QueryDAO {

    private final RestHighLevelClient client;
    private final SearchSourceBuilder sourceBuilder;
    private final ConfigProps props;
    private final Gson gson;

    @Autowired
    public QueryDAO(RestHighLevelClient client, SearchSourceBuilder sourceBuilder,
                    ConfigProps props, Gson gson){
        this.client = client;
        this.sourceBuilder = sourceBuilder;
        this.props = props;
        this.gson = gson;
    }

    /**
     *
     * @param document
     * @return
     */
    public String createIndex(Document document){

        try {
            IndexRequest request = new IndexRequest(props.getIndex().getName(), props.getIndex().getType());
            request.source(gson.toJson(document), XContentType.JSON);
            IndexResponse response = client.index(request);
            return response.getId();
        } catch (Exception ex) {
            log.error("The exception was thrown in createIndex method. {} ", ex);
        }

        return null;
    }

    /**
     *
     * @param document
     * @return
     */
    public String updateDocument(Document document){

        try {
            UpdateRequest request = new UpdateRequest(props.getIndex().getName(),
                    props.getIndex().getType(), document.getId())
                    .doc(gson.toJson(document), XContentType.JSON);

            UpdateResponse response = client.update(request);
            return response.getId();
        } catch (Exception ex){
            log.error("The exception was thrown in updateDocument method. {} ", ex);
        }

        return null;
    }

    /**
     *
     * @return
     */
    public List<Document> matchAllQuery() {

        List<Document> result = new ArrayList<>();

        try {
            flush();
            result = getDocuments(QueryBuilders.matchAllQuery());
        } catch (Exception ex){
            log.error("The exception was thrown in matchAllQuery method. {} ", ex);
        }

        return result;
    }

    /**
     *
     * @param query
     * @return
     */
    public List<Document> wildcardQuery(String query){

        List<Document> result = new ArrayList<>();

        try {
            result = getDocuments(QueryBuilders.queryStringQuery("*" + query.toLowerCase() + "*"));
        } catch (Exception ex){
            log.error("The exception was thrown in wildcardQuery method. {} ", ex);
        }

        return result;
    }

    /**
     *
     * @param id
     * @throws IOException
     */
    public void deleteDocument(String id){
        try {
            DeleteRequest deleteRequest = new DeleteRequest(props.getIndex().getName(), props.getIndex().getType(), id);
            client.delete(deleteRequest);
        } catch (Exception ex){
            log.error("The exception was thrown in deleteDocument method. {} ", ex);
        }
    }

    /**
     *
     * @return
     */
    private SearchRequest getSearchRequest(){
        SearchRequest searchRequest = new SearchRequest(props.getIndex().getName());
        searchRequest.source(sourceBuilder);
        return searchRequest;
    }

    /**
     *
     * @param builder
     * @return
     * @throws IOException
     */
    private List<Document> getDocuments(AbstractQueryBuilder builder) throws IOException {
        List<Document> result = new ArrayList<>();

        sourceBuilder.query(builder);
        SearchRequest searchRequest = getSearchRequest();

        SearchResponse searchResponse = client.search(searchRequest);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            Document doc = gson.fromJson(hit.getSourceAsString(), Document.class);
            doc.setId(hit.getId());
            result.add(doc);
        }

        return result;
    }

    public void flush() throws IOException {
        String endPoint = String.join("/", props.getIndex().getName(), "_flush");
        client.getLowLevelClient().performRequest("POST", endPoint);
    }
}
