/**
 * NextVisual
 */
/**
 * NextVisual
 */
package com.nvapp.crawler;

import java.io.IOException;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

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
public class TextCrawler extends WebCrawler {
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g|ico"
                                                           + "|png|tiff?|mid|mp2|mp3|mp4"
                                                           + "|wav|avi|mov|mpeg|ram|m4v|pdf"
                                                           + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

    public TextCrawler() throws IOException {}

    /**
     * You should implement this function to specify whether the given url should be crawled or not (based on your
     * crawling logic).
     */
    @Override
    public boolean shouldVisit(WebURL url) {
        String href = url.getURL().toLowerCase();
        System.out.println(href);
        if (FILTERS.matcher(href).matches()) {
            return false;
        }

        return true;
    }

    /**
     * This function is called when a page is fetched and ready to be processed by your program.
     */
    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        System.out.println("开始处理:" + url);
        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String html = htmlParseData.getHtml();

            System.out.println(html);
            // Document doc = Jsoup.parse(html);
            // String brand = doc.select("div.choose_item").first().text();
            //
            // Elements contents = doc.select("div.list_content");
            //
            // if (contents.size() == 20 && !url.contains("index=")) {
            // return;
            // } else {
            // System.out.println("URL: " + url);
            // }
            //
            // for (Element c : contents) {
            // Element info = c.select(".list_content_carInfo").first();
            // String title = info.select("h1").first().text();
            //
            // Elements prices = info.select(".list_content_price div");
            // String newPrice = prices.get(0).text();
            // String oldPrice = prices.get(1).text();
            //
            // Elements others = info.select(".list_content_other div");
            // String mileage = others.get(0).select("ins").first().text();
            // String age = others.get(1).select("ins").first().text();
            //
            // String stage = "unknown";
            // if (c.select("i.car_tag_zhijian").size() != 0) {
            // stage = c.select("i.car_tag_zhijian").text();
            // } else if (c.select("i.car_tag_yushou").size() != 0) {
            // stage = "presell";
            // }
            // }
        }
    }
}
