package dao;

import com.alibaba.fastjson.JSONObject;
import entity.BlogCore;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.elasticsearch.client.Response;
import org.junit.Test;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import sun.rmi.transport.Transport;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collections;
import java.util.Date;


/**
 * @author ${user}
 * @version V1.0
 * @ClassName: ${file_name}
 * @Description: ${todo}(用一句话描述该文件做什么)
 * @Date ${date} ${time}
 */
public class ClientBasicTest {
    private RestClient restClient;
    private Response response;
    private Header header;

    @Before
    public void testBefore() {
        restClient = RestClient.builder(new HttpHost("127.0.0.1", 9200, "http")).build();
        header = new BasicHeader("Content-Type", "application/json");
    }

//    @Test
//    public class ClassLoaderInfoT {
//            ClassLoader loader = Thread.currentThread().getContextClassLoader();
//            System.out.println("当前类加载器：" + loader);
//            System.out.println("当前类的父亲加载器（根加载器）：" + loader.getParent());
//            System.out.println("当前类父亲的父亲加载器（无）：" + loader.getParent().getParent());
//    }
    @Test
    public void testIndexWithId() throws IOException {
        BlogCore blog = new BlogCore();
        blog.setBlogId("2");
        blog.setBlogTitle("达摩院超越业界龙头");
        blog.setBlogContent("达摩院一定也必须要超越英特尔，" +
                "必须超越微软，必须超越IBM，因为我们生于二十一世纪，我们是有机会后发优势的。");
        blog.setCreateTime(new Date());

        String json = JSONObject.toJSONString(blog);
        HttpEntity entity = new StringEntity(json,
                "utf-8");
        response = restClient.performRequest("PUT", "/test/blog/" + blog.getBlogId(), Collections.<String, String>emptyMap(), entity, header);
        System.out.println(EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testIndexWithAutoId() throws IOException {
        BlogCore blog = new BlogCore();
        blog.setBlogId("5");
        blog.setBlogTitle("达摩院成立");
        blog.setBlogContent("达摩院一定也必须要超越英特尔，必须超越微软，必须超越IBM，因为我们生于二十一世纪，我们是有机会后发优势的。");
        blog.setCreateTime(new Date());

        String json = JSONObject.toJSONString(blog);
        HttpEntity entity = new StringEntity(json, "utf-8");
        response = restClient.performRequest("POST", "/test/blog/", Collections.<String, String>emptyMap(), entity, header);
        System.out.println(EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testNewCreate() throws Exception {
        InetSocketTransportAddress node0 = new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300);
       // InetSocketTransportAddress node1 = new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9301);
       // InetSocketTransportAddress node2 = new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9302);
       // InetSocketTransportAddress node3 = new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9303);
        Settings setting = Settings.builder().put("cluster.name", "elasticsearch").build();
        TransportClient TsC = new PreBuiltTransportClient(setting);
        TsC.addTransportAddress(node0);
       // TsC.addTransportAddress(node1);
       // TsC.addTransportAddress(node2);
       // TsC.addTransportAddress(node3);
//        return TsC;
        String tets = "123";
    }

    /**
     * 删除
     *
     * @Test public void testDelete() throws IOException {
     * response = restClient.performRequest("DELETE", "/test/blog/2",  Collections.<String,String>emptyMap(), header);
     * System.out.println(EntityUtils.toString(response.getEntity()));
     * }
     */

    @After
    public void testAfter() {
        try {
            restClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
