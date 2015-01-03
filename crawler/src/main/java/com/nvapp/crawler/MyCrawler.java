package com.nvapp.crawler;

import java.util.Set;

public class MyCrawler {
    private void initCrawlerWithSeeds(String[] seeds) {
        for (int i = 0; i < seeds.length; i++)
            LinkQueue.addUnvisitedUrl(seeds[i]);
    }

    public void crawling(String[] seeds) {
        LinkFilter filter = new LinkFilter() {
            public boolean accept(String url) {
                if (url.startsWith("http://"))
                    return true;
                else
                    return false;
            }
        };
        initCrawlerWithSeeds(seeds);
        while (!LinkQueue.unVisitedUrlsEmpty() && LinkQueue.getVisitedUrlNum() <= 1000) {
            try {
                String visitUrl = (String) LinkQueue.unVisitedUrlDeQueue();
                if (visitUrl == null)
                    continue;
                DownLoadFile downLoader = new DownLoadFile();

                LinkQueue.addVisitedUrl(visitUrl);
                downLoader.downloadFile(visitUrl);

                
                Set<String> links = HtmlParserTool.extracLinks(visitUrl, filter);
                System.out.println(links);
                for (String link : links) {
                    LinkQueue.addUnvisitedUrl(link);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        MyCrawler crawler = new MyCrawler();
        crawler.crawling(new String[]{"http://www.csi.ac.cn"});
    }

}
