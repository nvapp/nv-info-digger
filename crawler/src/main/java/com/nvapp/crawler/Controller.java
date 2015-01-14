/**
 * NextVisual
 */
/**
 * NextVisual
 */
package com.nvapp.crawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

/**
 * 
 * 
 * @version 1.0
 * @author <a herf="lexloo@gmail.com">lexloo</a>
 * @since 销售宝 2.0
 * 
 *        <pre>
 * 历史：
 *      建立: 2015-1-11 lexloo
 * </pre>
 */
public class Controller {
    public static void main(String[] args) throws Exception {
        String crawlStorageFolder = "c:/data/crawl/root";
        int numberOfCrawlers = 7;

        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);

        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        /*
         * For each crawl, you need to add some seed urls. These are the first URLs that are fetched and then the
         * crawler starts following links which are found in these pages
         */
        controller.addSeed("http://www.cea.gov.cn/");
        controller.addSeed("http://www.iem.net.cn/");
        controller.addSeed("http://www.hnea.gov.cn/");

        /*
         * Start the crawl. This is a blocking operation, meaning that your code will reach the line after this only
         * when crawling is finished.
         */
        controller.start(TextCrawler.class, numberOfCrawlers);
    }
}
