/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kodcu.web.document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author hakdogan
 */

public class Article {
    
    private Long id;
    private String title;
    private String content;
    private Date postDate;
    private String author;
    private String[] tags;

    public Article() {
    }

    public Article(Long id, String title, String content, Date postDate, String author, String[] tags) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.postDate = postDate;
        this.author = author;
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }
}
