package com.yhf.dao;

import com.yhf.domain.News;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author yaohengfeng
 * @version 1.0
 * @date 2020/2/28 16:10
 */
@Mapper
public interface INewsDao {

    void insertNews(News news);

    List<News> selectNews();
}

