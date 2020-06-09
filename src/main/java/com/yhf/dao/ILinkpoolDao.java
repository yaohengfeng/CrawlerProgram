package com.yhf.dao;

import com.yhf.domain.Linkpool;
import org.apache.ibatis.annotations.Param;

/**
 * @author yaohengfeng
 * @version 1.0
 * @date 2020/4/30 9:43
 */
public interface ILinkpoolDao {
    void insertLink(@Param("url") String href);

    String selectLink();

    void deleteLink(@Param("url") String url);
}
