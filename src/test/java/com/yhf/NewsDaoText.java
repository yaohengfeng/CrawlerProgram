package com.yhf;

import com.yhf.dao.INewsDao;
import com.yhf.domain.News;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author yaohengfeng
 * @version 1.0
 * @date 2020/4/29 16:53
 */
public class NewsDaoText {
    private InputStream inputStream;
    private SqlSession sqlSession;
    private INewsDao INewsDao;
    @Before
    public void init() throws IOException {
        inputStream= Resources.getResourceAsStream("MybatisConfig.xml");

        SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);

        sqlSession = sqlSessionFactory.openSession();

        INewsDao =sqlSession.getMapper(INewsDao.class);
    }

    @After
    public void destroy() throws IOException {
        sqlSession.commit();
        sqlSession.close();
        inputStream.close();
    }

    @Test
    public void insertNews(){
        News news=new News();
        news.setTitle("duai");
        news.setContext("hauhdwhahjkncjk");
        news.setUrl("ausidbbcjz");
        INewsDao.insertNews(news);
//        List<News> news = INewsDao.selectNews();
//        System.out.println(news);
    }

}
