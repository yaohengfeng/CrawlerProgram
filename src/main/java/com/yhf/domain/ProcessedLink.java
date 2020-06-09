package com.yhf.domain;

import java.util.Date;

/**
 * @author yaohengfeng
 * @version 1.0
 * @date 2020/4/30 9:41
 */
public class ProcessedLink {

    private Integer id;

    private String url;

    private Date createAt;

    private Date updateAt;


    public ProcessedLink() {
    }

    public ProcessedLink(String url, Date createAt, Date updateAt) {
        this.url = url;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }
}
