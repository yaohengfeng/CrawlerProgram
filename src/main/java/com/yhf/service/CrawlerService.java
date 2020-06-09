package com.yhf.service;

import com.yhf.dao.ICrawlerDao;
import com.yhf.dao.impl.CrawlerDaoImpl;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.Collectors;

/**
 * @author yaohengfeng
 * @version 1.0
 * @date 2020/4/30 11:03
 */
public class CrawlerService extends Thread {

    private ICrawlerDao crawlerDao;

    public CrawlerService(ICrawlerDao crawlerDao) {
        this.crawlerDao = crawlerDao;
    }

    @Override
    public void run() {
        try {
            String link;
            while ((link = crawlerDao.getNextLinkThenDelete()) != null) {

                if (crawlerDao.isLinkProcessed(link)) {
                    continue;
                }
                if (isInterestingLink(link)) {
                    Document doc = httpGetAndParseHtml(link);
                    parseUrlsFromPageAndStoreIntoDatabase(doc);
                    storeIntoDatabaseIfItIsNewPage(doc, link);
                    crawlerDao.insertProcessedLink(link);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void parseUrlsFromPageAndStoreIntoDatabase(Document doc) {
        for (Element aTag : doc.select("a")) {
            String href = aTag.attr("href");

            if (href.startsWith("//")) {
                href = "https:" + href;
            }

            if (!href.toLowerCase().startsWith("javascript")) {
                crawlerDao.insertLinkToBeProcessed(href);
            }
        }
    }

    private void storeIntoDatabaseIfItIsNewPage(Document doc, String link) throws IOException, SQLException {
        Elements articles = doc.select("article");
        if (!articles.isEmpty()) {
            for (Element article : articles) {
                String title = articles.get(0).child(0).text();
                String context = articles.select("p").stream().map(Element::text).collect(Collectors.joining("\n"));
                crawlerDao.insertNewsIntoDatabase(link, title, context);
                System.out.println(title);
            }
        }
    }

    private static Document httpGetAndParseHtml(String link) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        if (link.startsWith("//")) {
            link = "https:" + link;
        }
        System.out.println(link);
        HttpGet httpGet = new HttpGet(link);
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36");
        try (CloseableHttpResponse response1 = httpclient.execute(httpGet)) {
            System.out.println(response1.getStatusLine());
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
