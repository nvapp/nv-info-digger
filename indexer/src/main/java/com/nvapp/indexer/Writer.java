/**
 * NVAPP (2014)
 */
package com.nvapp.indexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.nvapp.data.FileObject;

/**
 * Index Writer
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
public class Writer {
    /**
     * 目录
     */
    private Directory directory;
    /**
     * 配置信息
     */
    private IndexWriterConfig config;
    /**
     * Index
     */
    private IndexWriter indexWriter;

    private Analyzer analyzer;

    /**
     * 构造器
     */
    public Writer() {
        try {
            directory = new SimpleFSDirectory(new File("e:/luc_dir"));
            analyzer = new IKAnalyzer(true);
            config = new IndexWriterConfig(Version.LUCENE_36, analyzer);
            indexWriter = new IndexWriter(directory, config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入数据
     * 
     * @param fo
     */
    public void writeData(FileObject fo) {
        try {
            Document doc = new Document();
            doc.add(new Field("content", fo.getContent(), Store.YES, Index.ANALYZED));
            doc.add(new Field("fileName", fo.getFileName(), Store.YES, Index.NO));

            indexWriter.addDocument(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 索引文件
     * 
     * @param root 文件目录
     */
    public void indexFiles(File root) {
        try {
            System.out.println("开始索引");
            File[] files = root.listFiles();
            for (File file : files) {
                System.out.println("索引文件：" + file.getAbsolutePath());
                InputStream inputStream = new FileInputStream(file);
                InputStreamReader fr = new InputStreamReader(inputStream, "gbk");
                BufferedReader br = new BufferedReader(fr);

                String line = null;
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                br.close();

                Document doc = new Document();
                doc.add(new Field("content", sb.toString(), Store.NO, Index.ANALYZED));
                doc.add(new Field("fileName", file.getAbsolutePath(), Store.YES, Index.NO));

                indexWriter.addDocument(doc);
            }
            System.out.println("结束索引");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 开始写
     */
    public void startWrite() {
        try {
            indexWriter.deleteAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 结束写
     */
    public void endWrite() {
        try {
            indexWriter.forceMerge(100);
            indexWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Writer writer = new Writer();
        writer.indexFiles(new File("E:/huangqi/capture"));
        writer.endWrite();
    }
}
