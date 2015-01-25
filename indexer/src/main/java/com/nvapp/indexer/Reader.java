/**
 * NVAPP (2014)
 */
package com.nvapp.indexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
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
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

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
public class Reader {
    /**
     * 构造器
     */
    public Reader() {

    }

    /**
     * 查找文档
     * 
     * @param key 键
     * @param value 值
     */
    public JSONArray searcherDocs(String key, String value) {
        JSONArray rtn = new JSONArray();

        if (value == null) {
            return rtn;
        }

        try {
            Directory directory = new SimpleFSDirectory(new File("e:/luc_dir"));
            Analyzer analyzer = new IKAnalyzer(true);

            BooleanQuery query = new BooleanQuery();
            String[] vs = value.split(" ");
            for (String v : vs) {
                System.out.println(v.trim());

                Term term = new Term(key, v.trim());
                query.add(new TermQuery(term), BooleanClause.Occur.MUST);
            }

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
                JSONObject item = new JSONObject();
                String fileName = document.get("fileName");
                InputStream inputStream = new FileInputStream(fileName);
                InputStreamReader fr = new InputStreamReader(inputStream, "gbk");
                BufferedReader br = new BufferedReader(fr);
                String line = null;
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                br.close();

                String v = sb.toString();
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

            analyzer.close();
            searcher.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rtn;
    }
}
