/**
 * NVAPP (2014)
 */
package com.nvapp.data;

/**
 * 
 * 
 * @version 1.0
 * @author <a herf="lexloo@gmail.com">lexloo</a>
 * @since 销售宝 2.0
 * 
 *        <pre>
 * 历史：
 *      建立: 2014-12-16 lexloo
 * </pre>
 */
public class FileObject {
    /**
     * 文件内容
     */
    private String content;
    /**
     * 文件名
     */
    private String fileName;

    /**
     * 构造函数
     * 
     * @param content 文件内容
     * @param fileName 文件名
     */
    public FileObject(String content, String fileName) {
        this.content = content;
        this.fileName = fileName;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
