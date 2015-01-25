/**
 * NextVisual
 */
/**
 * NextVisual
 */
package com.nvapp.web.cms;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.nvapp.indexer.Reader;

/**
 * 查询视图
 * 
 * @version 1.0
 * @author <a herf="lexloo@gmail.com">lexloo</a>
 * @since 销售宝 2.0
 * 
 *        <pre>
 * 历史：
 *      建立: 2015-1-25 lexloo
 * </pre>
 */
public class SearchViewController {
    /**
     * 查询首页
     * 
     */
    @At("/index")
    @Ok("fm:/WEB-INF/html/index.html")
    public void showIndex() {}

    /**
     * 查询结果
     * 
     * @param value 查询关键字
     * 
     */
    @At("/search/query")
    @Ok("json")
    public Object query(@Param("value") String value) {
        return new Reader().searcherDocs("content", value);
    }

    /**
     * 显示文件内容
     * 
     * @param fileName 文件名
     * 
     */
    @At("/search/showText")
    @Ok("raw:text/plain")
    public String showText(@Param("fileName") String fileName) {
        System.out.println(fileName);
        try {
            InputStream inputStream = new FileInputStream(fileName);
            InputStreamReader fr = new InputStreamReader(inputStream, "gbk");
            BufferedReader br = new BufferedReader(fr);
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
