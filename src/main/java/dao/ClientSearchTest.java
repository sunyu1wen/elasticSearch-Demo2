package dao;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.RestClient;
import org.apache.http.Header;
import org.elasticsearch.client.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

/**
 * @author ${user}
 * @version V1.0
 * @ClassName: ${file_name}
 * @Description: ${todo}(用一句话描述该文件做什么)
 * @Date ${date} ${time}
 */
public class ClientSearchTest {
    private RestClient restClient;
    private Response response;
    private Header header;

    @Before
    public void testBefore() {
        restClient = RestClient.builder(
                new HttpHost("127.0.0.1", 9200, "http")).build();
        header = new BasicHeader("Content-Type", "application/json");
    }


    /**
     * 轻量搜索1
     */
    @Test
    public void testSearch1() throws IOException {
        response = restClient.performRequest("GET", "/test/blog/_search?pretty", header);
        System.out.println(EntityUtils.toString(response.getEntity()));
    }

    /**
     * 轻量搜索2query string search
     */
    @Test
    public void testSearch2() throws IOException {
        response = restClient.performRequest("GET", "/test/blog/_search?q=blogTitle:达摩&pretty", header);
        System.out.println(EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testSearch() throws IOException {
        response = restClient.performRequest("GET", "/shoes/product/2", header);
        System.out.println(EntityUtils.toString(response.getEntity()));
    }


    /**
     * 使用查询表达式搜索query DSL
     */
    @Test
    public void testSearchWithMatch() throws IOException {
        String json = "{" +
                "    \"query\" : {" +
                "        \"match\" : {" +
                "            \"blogTitle\" : \"达摩阿里巴巴\"" +
                "        }" +
                "    }" +
                "}";
        HttpEntity entity = new StringEntity(json, "utf-8");
        response = restClient.performRequest("GET", "/test/blog/_search?pretty", Collections.<String, String>emptyMap(), entity, header);
        System.out.println(EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void queryMatchAll() throws IOException {
        String jsonQ = "{\n" +
                "  \"query\": {\n" +
                "    \"match_all\": {}\n" +
                "  }\n" +
                "}";
        HttpEntity entity = new StringEntity(jsonQ, "utf-8");
        response = restClient.performRequest("GEt", "/shoes/product/_search?pretty", Collections.<String, String>emptyMap(), entity, header);
        System.out.println(EntityUtils.toString(response.getEntity()));
    }

    /**
     * 使用查询表达式搜索
     */
    @Test
    public void testSearchWithMatchAndFilter() throws IOException {
        String json = "{\n    \"query\" : {\n" +
                "        \"bool\": {\n" +
                "            \"must\": {\n" +
                "                \"match\" : {\n" +
                "                    \"blogTitle\" : \"达摩\" \n" +
                "                }\n" +
                "            },\n" +
                "            \"filter\": {\n" +
                "                \"range\" : {\n" +
                "                    \"blogId\" : { \"gt\" : 1 } \n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";
        HttpEntity entity = new StringEntity(json, "utf-8");
        response = restClient.performRequest("GET", "/test/blog/_search?pretty", Collections.<String, String>emptyMap(), entity, header);
        System.out.println(EntityUtils.toString(response.getEntity()));
    }

    /**
     * 短语搜索  （单词之间紧挨着）
     *
     * @throws IOException
     */
    @Test
    public void testSearchWithMatchPhrase() throws IOException {
        String json = "{\n" +
                "    \"query\" : {\n" +
                "        \"match_phrase\" : {\n" +
                "            \"blogContent\" : \"阿里巴巴达摩院\"\n" +
                "        }\n" +
                "    }\n" +
                "}";
        HttpEntity entity = new StringEntity(json, "utf-8");
        response = restClient.performRequest("GET", "/test/blog/_search?pretty", Collections.<String, String>emptyMap(), entity, header);
        System.out.println(EntityUtils.toString(response.getEntity()));
    }


    /**
     * 高亮
     *
     * @throws IOException
     */
    @Test
    public void testSearchWithighlight() throws IOException {
        String json = "{\n" +
                "    \"query\" : {\n" +
                "        \"match\" : {\n" +
                "            \"blogContent\" : \"阿里巴巴全球研究院\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"highlight\": {\n" +
                "        \"fields\" : {\n" +
                "            \"blogContent\" : {}\n" +
                "        }\n" +
                "    }\n" +
                "}";
        HttpEntity entity = new StringEntity(json, "utf-8");
        response = restClient.performRequest("GET", "/test/blog/_search?pretty", Collections.<String, String>emptyMap(), entity, header);
        System.out.println(EntityUtils.toString(response.getEntity()));
    }


    @After
    public void testAfter() {
        try {
            restClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
