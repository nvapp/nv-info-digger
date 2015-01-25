/**
 * NextVisual
 */
/**
 * NextVisual
 */
package com.nvapp.web.cms;

import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;


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
}
