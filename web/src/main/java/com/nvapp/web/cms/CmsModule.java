package com.nvapp.web.cms;

import org.nutz.mvc.annotation.Encoding;
import org.nutz.mvc.annotation.Modules;
import org.nutz.mvc.annotation.Views;

import com.nvapp.controller.FreemarkerViewMaker;


/**
 * 销售宝系统内容管理界面
 * 
 * @author <a herf="lexloo@gmail.com">lexloo</a>
 * @version 1.0
 * @since 销售宝 2.0
 *        <p/>
 *        <p/>
 *        <p/>
 *        <p/>
 *        <p/>
 * 
 *        <pre>
 * 历史：
 *      建立: 2013-8-13 lexloo
 * </pre>
 */
@Modules(value = {}, scanPackage = true)
@Views({FreemarkerViewMaker.class})
@Encoding(input = "UTF-8", output = "UTF-8")
public class CmsModule {}
