/**
 * NVAPP (2014)
 */
package com.nvapp.indexer;

import java.io.File;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
            directory = new SimpleFSDirectory(new File("c:/luc_dir"));
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

    /**
     * 查找文档
     * 
     * @param key 键
     * @param value 值
     */
    public JSONArray searcherDocs(String key, String value) {
        JSONArray rtn = new JSONArray();
        try {
            Term term = new Term(key, value);
            TermQuery query = new TermQuery(term);

            IndexReader reader = IndexReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);
            TopDocs docs = searcher.search(query, 100);

            SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<span class=\"hightlighterCss\">", "</span>");
            /** 创建QueryScorer */
            QueryScorer scorer = new QueryScorer(query);
            /** 创建Fragmenter */
            Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
            Highlighter highlight = new Highlighter(formatter, scorer);
            highlight.setTextFragmenter(fragmenter);
            for (ScoreDoc doc : docs.scoreDocs) {
                Document document = searcher.doc(doc.doc);

                // System.out.println(document.get("fileName"));
                JSONObject item = new JSONObject();

                String v = document.get("content");
                if (v != null) {
                    TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(v));
                    String str1 = highlight.getBestFragment(tokenStream, v);
                    item.put("content", str1);
                } else {
                    item.put("content", "");
                }

                item.put("fileName", document.get("fileName"));
                rtn.add(item);
            }
            searcher.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rtn;
    }

    public static void main(String[] args) {
        try {
            Writer writer = new Writer();
            writer.startWrite();
            writer.writeData(new FileObject("黄奇你好, sex ahow dy an 阿发 sfa fa falf alf alf af alf af af alf a fa afa fa; falf alfalfa jjjjjjjjjjjjjjjjjjd  afa ;fa fla flaf af a al falf alfalf a a falf alfjafalfja lfalf alf alfjalflafjlasfjlafjalfjlafjalfjalfjalf ajflefjal fjalfjalfjelf jalfjlafjalfjalfalfjlajflaef lajfldfjalfjlafjalfjalfjlajelajflajflejlajflajf afla ef alf alf eal falfh aejflafoyfaf elayof alf ale alf alf lafyoef la falf alfyoa flaf lafyoaf af alfyaof elhfaow lflfoe af lafeya fl ad afy",
                                            "cccc"));
            writer.writeData(new FileObject("黄奇厦门地震你好ddddd, sex ddd dy and afy", "cccc"));
            writer.writeData(new FileObject("企管科的啊分页，塞牙缝，的法院搜房啊快递费， 阿发呀", "cccc"));
            writer.writeData(new FileObject("上的发顺丰 声地震局发射 声发射", "cccc"));
            writer.writeData(new FileObject("上的发地震顺丰 声发射 声发射", "cccc"));
            writer.writeData(new FileObject("上的发顺丰地震 声发地震射 声发射", "cccc"));
            writer.endWrite();

//            System.out.println(writer.searcherDocs("content", "你好").toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
