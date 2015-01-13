/**
 * NextVisual
 */
/**
 * NextVisual
 */
package com.nvapp.crawler;

import java.io.IOException;
import java.util.Iterator;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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
        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String html = htmlParseData.getHtml();

            Document doc = Jsoup.parse(html);
            Iterator<Element> it = doc.select("div span").iterator();
            while (it.hasNext()) {
                Element el = it.next();

                if (el.hasText()) {
                    String content = el.text();

                    if (content.contains("地震") || content.contains("厦门")) {
                        FileUtils.saveToFile(content);
                    }
                }
            }
        }
    }
}
