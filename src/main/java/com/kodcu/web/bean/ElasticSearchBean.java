/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kodcu.web.bean;

import com.kodcu.service.ClientProvider;
import com.kodcu.web.document.Article;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.indices.IndexMissingException;
import org.elasticsearch.search.SearchHit;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

import java.util.Collections;
/**
 *
 * @author hakdogan
 */

@ManagedBean
@ViewScoped
public class ElasticSearchBean {
    
    private String tag;
    private Article selectArticle;
    private Article article            = new Article();
    private List<Article>  articleList = new ArrayList<Article>();

    private static final String INDEX_NAME = "kodcucom";
    private static final String TYPE_NAME  = "article";

    private String wildCardQuery;

    public ElasticSearchBean() {
        prepareDocumentList();
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Article getSelectArticle() {
        return selectArticle;
    }

    public void setSelectArticle(Article selectArticle) {
        this.selectArticle = selectArticle;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }

    public String getWildCardQuery() {
        return wildCardQuery;
    }

    public void setWildCardQuery(String wildCardQuery) {
        this.wildCardQuery = wildCardQuery;
    }

    public void articleSelect(){

        article               = selectArticle;
        String[] documentTags = selectArticle.getTags();
        tag                   = "";

        for(int i=0; i<documentTags.length; i++){
            documentTags[i] = documentTags[i].replace("[", "");
            documentTags[i] = documentTags[i].replace("]", "");
            tag += documentTags[i] + ",";
        }

        tag = tag.substring(0, tag.length()-1);
    }

    public void clearWildCardQuery(){
        wildCardQuery = "";
    }

    public void collectionSort(){

        Collections.sort(articleList, new Comparator<Article>(){

            @Override
            public int compare(Article o1, Article o2) {
                return o2.getId().compareTo(o1.getId());
            }
        });
    }

    public void prepareDocumentList(){

        wildCardQuery = "";
        ClientProvider.instance().getClient()
                .admin().indices().prepareRefresh().execute().actionGet();

        try {

            SearchResponse response = ClientProvider.instance().getClient()
                    .prepareSearch(INDEX_NAME)
                    .setTypes(TYPE_NAME)
                    .setQuery(matchAllQuery())
                    .execute()
                    .actionGet();

            articleList.clear();

            Article temporary = null;
            String[] tags     = null;

            if (response != null) {
                for (SearchHit hit : response.getHits()) {

                    try {

                        tags = hit.getSource().get("tags").toString().split(",");
                        temporary = new Article(Long.parseLong(hit.getSource().get("id").toString()),
                                hit.getSource().get("title").toString(),
                                hit.getSource().get("content").toString(), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(hit.getSource().get("postDate").toString()),
                                hit.getSource().get("author").toString(), tags);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    articleList.add(temporary);
                }
            }

            collectionSort();

        } catch (IndexMissingException ex){
            System.out.println("IndexMissingException: " + ex.toString());
        }
    }

    public void fullTextSearch(){

        articleList.clear();
        Article temporary = null;
        String[] tags     = null;

        try {
            QueryBuilder queryBuilder = QueryBuilders.queryString("*"+wildCardQuery+"*");
            SearchRequestBuilder searchRequestBuilder = ClientProvider.instance().getClient().prepareSearch(INDEX_NAME);
            searchRequestBuilder.setTypes(TYPE_NAME);
            searchRequestBuilder.setSearchType(SearchType.DEFAULT);
            searchRequestBuilder.setQuery(queryBuilder);
            searchRequestBuilder.setFrom(0).setSize(60).setExplain(true);

            SearchResponse response = searchRequestBuilder.execute().actionGet();

            if (response != null) {

                for (SearchHit hit : response.getHits()) {

                    try {

                        tags = hit.getSource().get("tags").toString().split(",");
                        temporary = new Article(Long.parseLong(hit.getSource().get("id").toString()),
                                hit.getSource().get("title").toString(),
                                hit.getSource().get("content").toString(), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(hit.getSource().get("postDate").toString()),
                                hit.getSource().get("author").toString(), tags);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    articleList.add(temporary);
                }
            }

            collectionSort();

        } catch (IndexMissingException ex){
            System.out.println("IndexMissingException: " + ex.toString());
        }
    }

    public static Map<String, Object> putJsonDocument(Long ID, String title, String content, Date postDate,
                                                      String[] tags, String author){

        Map<String, Object> jsonDocument = new HashMap<String, Object>();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            String formattedDate = sdf.format(postDate);

            jsonDocument.put("id", ID);
            jsonDocument.put("title", title);
            jsonDocument.put("content", content);
            jsonDocument.put("postDate", formattedDate);
            jsonDocument.put("tags", tags);
            jsonDocument.put("author", author);
        } catch (Exception ex){
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sorry, an error has occurred", ex.toString()));
        }

        return jsonDocument;
    }

    public void saveArticle() {

        Long ID = 1l;
        try {

            CountResponse countResponse= ClientProvider.instance().getClient()
                    .prepareCount(INDEX_NAME)
                    .setQuery(termQuery("_type", TYPE_NAME))
                    .execute().actionGet();
            ID += countResponse.getCount();

        } catch (IndexMissingException ex){
            System.out.println("IndexMissingException: " + ex.toString());
        }

        String[] postTags = tag.split(",");

        try {

            if(null == selectArticle)
                ClientProvider.instance().getClient().prepareIndex(INDEX_NAME, TYPE_NAME, ID.toString())
                        .setSource(putJsonDocument(ID, article.getTitle(), article.getContent(),
                                article.getPostDate(), postTags,
                                article.getAuthor())).execute().actionGet();
            else {

                Map<String, Object> updateObject = new HashMap<String, Object>();

                updateObject.put("title", selectArticle.getTitle());
                updateObject.put("content", selectArticle.getContent());
                updateObject.put("postDate", selectArticle.getPostDate());
                updateObject.put("author", selectArticle.getAuthor());
                updateObject.put("tags", postTags);


                ClientProvider.instance().getClient().prepareUpdate(INDEX_NAME, TYPE_NAME, selectArticle.getId().toString())
                        .setScript("ctx._source.title=title; ctx._source.content=content; "
                                + "ctx._source.postDate=postDate; ctx._source.author=author; "
                                + "ctx._source.tags=tags")
                        .setScriptParams(updateObject).execute().actionGet();
            }

        } catch (Exception ex){
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sorry, an error has occurred", ex.toString()));
        }

        prepareDocumentList();
        initArticle();

    }

    public void removeArticle(){

        try {
            ClientProvider.instance().getClient().prepareDelete(INDEX_NAME, TYPE_NAME, selectArticle.getId().toString()).execute().actionGet();
        } catch (Exception ex){
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sorry, an error has occurred", ex.toString()));
        }

        prepareDocumentList();
        initArticle();
    }

    public void initArticle() {

        tag           = "";
        wildCardQuery = "";
        selectArticle = null;
        article       = new Article();

    }

}
