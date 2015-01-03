package com.nvapp.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.nutz.mvc.View;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * Nutz MVC freemarker支持
 * 
 * @author <a herf="lexloo@gmail.com">lexloo</a>
 * @version 1.0
 * @since 销售宝 2.0
 *        <p/>
 * 
 *        y
 * 
 *        <pre>
 *                             历史：
 *                                  建立: 2013-8-13 lexloo
 * </pre>
 */
public class FreemarkerView implements View {
    /**
     * freemark配置
     */
    private static final String CONFIG_SERVLET_CONTEXT_KEY = "freemarker.Configuration";
    /**
     * ?
     */
    private static final String VALUE = "value";
    /**
     * ?
     */
    private static final String REQUEST = "request";
    /**
     * request请求参数
     */
    private static final String REQUEST_MAP = "parametersMap";
    /**
     * ?
     */
    private static final String RESPONSE = "response";
    /**
     * ?
     */
    private static final String SESSION = "session";
    /**
     * ?
     */
    private static final String APPLICATION = "application";
    /**
     * 上下文背景
     */
    private static final String CONTEXT = "context";
    /**
     * ?
     */
    private String path;
    /**
     * ?
     */
    private Configuration cfg;

    /**
     * 使用的主题
     */
    // private String theme = Kernel.getInstance().getConfigurator().getSystemConfig("THEME");

    /**
     * 构造函数
     * 
     * @param path 模板路径
     */
    public FreemarkerView(String path) {
        this.path = path;
    }

    /**
     * @param request 请求init
     * @param response 反应
     * @param value 对象
     * @throws Exception 异常
     */
    @SuppressWarnings({"rawtypes"})
    public void render(HttpServletRequest request, HttpServletResponse response, Object value) throws Exception {
        HttpSession session = request.getSession();
        ServletContext sc = session.getServletContext();
        cfg = getConfiguration(sc);
        SimpleHash root = new SimpleHash(ObjectWrapper.BEANS_WRAPPER);

        // root.put("theme", theme);
        root.put(VALUE, value);
        root.put(REQUEST, request);
        root.put(REQUEST_MAP, this.getRefererMap(request));

        // 地址和参数
        session.setAttribute("referer", request.getRequestURI());
        session.setAttribute("refererMap", getRefererMap(request));

        root.put(CONTEXT, request.getContextPath());
        root.put(RESPONSE, response);
        root.put(SESSION, session);
        root.put(APPLICATION, sc);

        Enumeration reqs = request.getAttributeNames();
        while (reqs.hasMoreElements()) {
            String strKey = (String) reqs.nextElement();
            root.put(strKey, request.getAttribute(strKey));
        }

        Template t = cfg.getTemplate(path);
        response.setContentType("text/html; charset=utf-8");
        t.process(root, response.getWriter());
    }

    /**
     * ParameterMap转换成Map 直接在session中存入request.getParameterMap(),在jetty中可以获取，在Tomcat不能获取，不知道什么原因.
     * 
     * @param request 请求
     * @return Map对象
     */
    private Map<String, String> getRefererMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        for (Object item : request.getParameterMap().entrySet()) {
            @SuppressWarnings("rawtypes")
            Map.Entry entry = (Map.Entry) item;
            String[] vArr = (String[]) entry.getValue();
            if (vArr != null && vArr.length > 0) {
                paramMap.put((String) entry.getKey(), vArr[0]);
            }
        }

        return paramMap;
    }

    /**
     * 获取配置
     * 
     * @param servletContext 上下文
     * @return 配置
     * @throws TemplateException 异常
     */
    public final synchronized Configuration getConfiguration(ServletContext servletContext) throws TemplateException {
        Configuration config = (Configuration) servletContext.getAttribute(CONFIG_SERVLET_CONTEXT_KEY);
        if (config == null) {
            config = new Configuration();
            config.setServletContextForTemplateLoading(servletContext, "/");
            config.setDefaultEncoding("UTF-8");
            config.setEncoding(Locale.CHINA, "UTF-8");
            config.setWhitespaceStripping(true);
            config.setTemplateExceptionHandler(new CustomizedTemplateExceptionHandler());

            loadSettings(servletContext, config);

            servletContext.setAttribute(CONFIG_SERVLET_CONTEXT_KEY, config);
        }
        config.setWhitespaceStripping(true);
        return config;
    }

    /**
     * 加载配置
     * 
     * @param servletContext 上下文
     * @param config 配置
     */
    private void loadSettings(ServletContext servletContext, Configuration config) {
        try {
            config.setSettings(FreemarkerView.class.getClassLoader()
                                                   .getResourceAsStream("templates/freemarker.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 自定义的模板异常处理器
     * <p>
     * 由于{@link TemplateExceptionHandler#HTML_DEBUG_HANDLER}渲染出来的html页面背景色是黄色， 看得人眼睛都花了。所以，这里自定义一个异常处理器， 跟
     * {@link TemplateExceptionHandler#HTML_DEBUG_HANDLER}渲染效果一样。 区别就是背景色是白色，而不是黄色。文字也是黑色，而不是红色。
     * </p>
     * 
     * @author <a href="wangbo@jccatech.com">wangbo</a>
     */
    private class CustomizedTemplateExceptionHandler implements TemplateExceptionHandler {

        @Override
        public void handleTemplateException(TemplateException te, Environment env, Writer out) throws TemplateException {
            PrintWriter pw = (out instanceof PrintWriter) ? (PrintWriter) out : new PrintWriter(out);
            pw.println("<html><div>--模板解析出错--</div><div>");
            te.printStackTrace(pw);
            pw.println("</div></html>");
            pw.flush();
            throw te;
        }
    }
}
