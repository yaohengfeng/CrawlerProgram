package com.yhf.dao.impl;

import com.yhf.dao.ICrawlerDao;
import com.yhf.domain.News;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

/**
 * @author yaohengfeng
 * @version 1.0
 * @date 2020/4/30 14:43
 */
public class CrawlerDaoImpl implements ICrawlerDao {

    private SqlSessionFactory sqlSessionFactory;

//    private INewsDao newsDao;
//
//    private ILinkpoolDao linkpoolDao;
//
//    private IProcessedLinkDao processedLinkDao;

    public CrawlerDaoImpl() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("MybatisConfig.xml");

        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);


//        newsDao =sqlSession.getMapper(INewsDao.class);
    }


    @Override
    public synchronized String  getNextLinkThenDelete() throws IOException {
        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            String url = sqlSession.selectOne("com.yhf.dao.ILinkpoolDao.selectLink");
            if (url != null) {
                sqlSession.delete("com.yhf.dao.ILinkpoolDao.deleteLink", url);
            }
            return url;
        }
    }

    @Override
    public void insertNewsIntoDatabase(String url, String title, String content) throws SQLException, IOException {
//        newsDao = sqlSession.getMapper(INewsDao.class);
//        newsDao.insertNews(new News(title, content, url));
//        sqlSession.commit();
//        destroy();
        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            sqlSession.insert("com.yhf.dao.INewsDao.insertNews", new News(title, content, url));
        }
    }

    @Override
    public boolean isLinkProcessed(String link) throws IOException {
//        processedLinkDao = sqlSession.getMapper(IProcessedLinkDao.class);
//        List<String> list = processedLinkDao.selcteProcessedLink();
//        sqlSession.commit();
        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            List<String> list = sqlSession.selectList("com.yhf.dao.IProcessedLinkDao.selcteProcessedLink");
            return list.contains(link);
        }
    }

    @Override
    public void insertProcessedLink(String url) throws IOException {
//        processedLinkDao = sqlSession.getMapper(IProcessedLinkDao.class);
//        processedLinkDao.insertProcessedLink(url);
//        sqlSession.commit();
        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            sqlSession.insert("com.yhf.dao.IProcessedLinkDao.insertProcessedLink", url);
        }
    }

    @Override
    public void insertLinkToBeProcessed(String url) {
//        linkpoolDao = sqlSession.getMapper(ILinkpoolDao.class);
//        linkpoolDao.insertLink(url);
//        sqlSession.commit();
        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            sqlSession.insert("com.yhf.dao.ILinkpoolDao.insertLink", url);
        }
    }
}
