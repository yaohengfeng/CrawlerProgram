package com.yhf.domain;

import java.io.Serializable;

/**
 * @author yaohengfeng
 * @version 1.0
 * @date 2020/2/28 16:13
 */
public class News implements Serializable {
    private Integer id;
    private String title;
    private String context;
    private String url;

    public News() {
    }

    public News(Integer id, String title, String context, String url) {
        this.id = id;
        this.title = title;
        this.context = context;
        this.url = url;
    }

    public News(String title, String context, String url) {
        this.title = title;
        this.context = context;
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", context='" + context + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
