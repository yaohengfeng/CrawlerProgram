package com.yhf;

import com.yhf.dao.ICrawlerDao;
import com.yhf.dao.impl.CrawlerDaoImpl;
import com.yhf.service.CrawlerService;

import java.io.IOException;

/**
 * @author yaohengfeng
 * @version 1.0
 * @date 2020/4/30 16:30
 */
public class Main {
    public static void main(String[] args) throws IOException {
        CrawlerDaoImpl crawlerDao = new CrawlerDaoImpl();
        for (int i = 0; i < 8; i++) {
            new CrawlerService(crawlerDao).start();
        }
    }
}
