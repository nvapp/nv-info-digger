/**
 * NextVisual
 */
/**
 * NextVisual
 */
package com.nvapp.crawler;

import java.io.File;
import java.io.FileWriter;
import java.util.UUID;

/**
 * 
 * 
 * @version 1.0
 * @author <a herf="lexloo@gmail.com">lexloo</a>
 * @since 销售宝 2.0
 * 
 *        <pre>
 * 历史：
 *      建立: 2015-1-13 lexloo
 * </pre>
 */
public class FileUtils {
    public static final void saveToFile(String content) {
        File file = new File("e:/capture/" + UUID.randomUUID().toString() + ".txt");
        try (FileWriter fr = new FileWriter(file)) {
            fr.write(content);
            fr.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
