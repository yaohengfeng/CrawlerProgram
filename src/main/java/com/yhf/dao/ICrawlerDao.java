package com.yhf.dao;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @author yaohengfeng
 * @version 1.0
 * @date 2020/4/30 14:43
 */
public interface ICrawlerDao {
    String getNextLinkThenDelete() throws SQLException, IOException;

    void insertNewsIntoDatabase(String url, String title, String content) throws SQLException, IOException;

    boolean isLinkProcessed(String link) throws SQLException, IOException;

    void insertProcessedLink(String link) throws IOException;

    void insertLinkToBeProcessed(String href);

}
