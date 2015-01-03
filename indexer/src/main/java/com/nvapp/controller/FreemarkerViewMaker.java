package com.nvapp.controller;

import org.nutz.ioc.Ioc;
import org.nutz.mvc.View;
import org.nutz.mvc.ViewMaker;

/**
 * Nutz MVC freemarker支持
 * 
 * @version 1.0
 * @author <a herf="lexloo@gmail.com">lexloo</a>
 * @since 销售宝 2.0
 * 
 *        <pre>
 * 历史：
 *      建立: 2013-8-13 lexloo
 * </pre>
 */
public class FreemarkerViewMaker implements ViewMaker {
    /**
     * @param ioc ioc容器
     * @param type 类型
     * @param value 值
     * @return 视图
     */
    public View make(Ioc ioc, String type, String value) {
        if ("fm".equalsIgnoreCase(type)) {
            return new FreemarkerView(value);
        }
        return null;
    }

}
