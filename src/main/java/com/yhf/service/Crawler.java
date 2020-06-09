package com.yhf.service;

import com.yhf.dao.INewsDao;
import com.yhf.domain.News;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yaohengfeng
 * @version 1.0
 * @date 2020/2/28 19:06
 */
public class Crawler {

    private InputStream inputStream;

    private SqlSession sqlSession;

    private INewsDao newsDao;

    private void init() throws IOException {
        inputStream = Resources.getResourceAsStream("MybatisConfig.xml");

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        sqlSession = sqlSessionFactory.openSession();

        newsDao = sqlSession.getMapper(INewsDao.class);
    }

    private void destroy() throws IOException {
        sqlSession.commit();
        sqlSession.close();
        inputStream.close();
    }


    public static void main(String[] args) throws IOException {
        List<String> linkpool = new ArrayList<>();
        Set<String> processedLinks = new HashSet<>();
        linkpool.add("https://sina.cn/");
        Crawler crawler = new Crawler();
        while (true) {
            if (linkpool.isEmpty()) {
                break;
            }
            String link = linkpool.remove(linkpool.size() - 1);
            if (processedLinks.contains(link)) {
                continue;
            }

            if (isInterestingLink(link)) {
                Document doc = httpGetAndParseHtml(link);
                doc.select("a").stream().map(aTags -> aTags.attr("href")).forEach(linkpool::add);
                crawler.init();
                crawler.storeIntoDatabaseIfItIsNewPage(doc, link);
                crawler.destroy();
                processedLinks.add(link);
            }
        }

    }

    private void storeIntoDatabaseIfItIsNewPage(Document doc, String link) {
        Elements articles = doc.select("article");
        if (!articles.isEmpty()) {
            for (Element article : articles) {
                String title = articles.get(0).child(0).text();
                String context = articles.select("p").stream().map(Element::text).collect(Collectors.joining("\n"));
                System.out.println(link);
                System.out.println(title);
                System.out.println(context);
                newsDao.insertNews(new News(title, context, link));
            }
        }
    }

    private static Document httpGetAndParseHtml(String link) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        if (link.startsWith("//")) {
            link = "https:" + link;
        }
//        System.out.println(link);
        HttpGet httpGet = new HttpGet(link);
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36");
        try (CloseableHttpResponse response1 = httpclient.execute(httpGet)) {
//            System.out.println(response1.getStatusLine());
            HttpEntity entity1 = response1.getEntity();
            String html = EntityUtils.toString(entity1);
            return Jsoup.parse(html);
        }
    }

    private static boolean isInterestingLink(String link) {
        return isNotLoginPage(link) && (isNewsPage(link) || isIndexPage(link));
    }

    private static boolean isIndexPage(String link) {
        return "https://sina.cn/".equals(link);
    }

    private static boolean isNewsPage(String link) {
        return link.contains("news.sina.cn");
    }

    private static boolean isNotLoginPage(String link) {
        return !link.contains("passport.sina.cn");
    }

}
