package com.yhf.dao;

import com.yhf.domain.ProcessedLink;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yaohengfeng
 * @version 1.0
 * @date 2020/4/30 10:08
 */
public interface IProcessedLinkDao {
    void insertProcessedLink(@Param("url") String url);

    List<String> selcteProcessedLink();
}
