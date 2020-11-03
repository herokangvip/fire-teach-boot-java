package com.example.demo.es;

import com.example.demo.utils.json.JsonUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EsTest {

    private static RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(new HttpHost("localhost", 9200, "http")));


    @After
    public void after() throws IOException {
        client.close();
    }

    /**
     * 按_id查询
     */
    @Test
    public void getById() {
        GetRequest request = new GetRequest(
                "test_index",
                "test_type",
                "1");
        //禁用_source，一般不用
        //request.fetchSourceContext(FetchSourceContext.DO_NOT_FETCH_SOURCE);
        //手动设置routing，按id默认routing就是_id
        //request.routing("routing");
        try {
            GetResponse response = client.get(request, RequestOptions.DEFAULT);
            Map<String, Object> sourceAsMap = response.getSourceAsMap();
            System.out.println("结果:" + JsonUtils.toJsonString(sourceAsMap));
        } catch (ElasticsearchException e) {
            if (e.status() == RestStatus.NOT_FOUND) {
                System.out.println("索引不存在");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 按_id查询，只返回_source的部分字段，当属性很多并且只想使用少数字段时使用。
     */
    @Test
    public void getById2() {
        GetRequest request = new GetRequest(
                "test_index",
                "test_type",
                "1");
        String[] includes = new String[]{"name"};
        String[] excludes = Strings.EMPTY_ARRAY;
        FetchSourceContext fetchSourceContext = new FetchSourceContext(true, includes, excludes);
        request.fetchSourceContext(fetchSourceContext);
        try {
            GetResponse response = client.get(request, RequestOptions.DEFAULT);
            Map<String, Object> sourceAsMap = response.getSourceAsMap();
            System.out.println("结果:" + JsonUtils.toJsonString(sourceAsMap));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 按_id查询，查询store内容。
     */
    @Test
    public void getById3() {
        GetRequest request = new GetRequest(
                "test_index",
                "test_type",
                "1");
        request.storedFields("name");

        try {
            GetResponse getResponse = client.get(request, RequestOptions.DEFAULT);
            DocumentField field = getResponse.getField("name");
            if (field == null) {
                System.out.println("该字段没有store");
            } else {
                String message = getResponse.getField("name").getValue();
                System.out.println("结果:" + JsonUtils.toJsonString(message));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 异步查询，按_id查询
     */
    @Test
    public void getById4() {
        GetRequest request = new GetRequest(
                "test_index",
                "test_type",
                "1");

        client.getAsync(request, RequestOptions.DEFAULT, new ActionListener<GetResponse>() {
            @Override
            public void onResponse(GetResponse response) {
                Map<String, Object> sourceAsMap = response.getSourceAsMap();
                System.out.println("结果:" + JsonUtils.toJsonString(sourceAsMap));
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("查询异常");
            }
        });

    }

    /**
     * 指定版本号查询，版本号不一致抛出异常
     */
    @Test
    public void getById5() {
        try {
            GetRequest request = new GetRequest("test_index", "test_type", "1").version(4);
            GetResponse getResponse = client.get(request, RequestOptions.DEFAULT);
        } catch (ElasticsearchException exception) {
            if (exception.status() == RestStatus.CONFLICT) {
                System.out.println("版本号不一致");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void updateById() {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("name", "我是谁");
        UpdateRequest request = new UpdateRequest("test_index", "test_type", "100")
                .doc(jsonMap);


        try {
            UpdateResponse updateResponse = client.update(request, RequestOptions.DEFAULT);
            ReplicationResponse.ShardInfo shardInfo = updateResponse.getShardInfo();
            if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
                System.out.println("部分shard更新失败");
            }
            if (shardInfo.getFailed() > 0) {
                for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                    String reason = failure.reason();
                }
            }
            System.out.println("结果:" + JsonUtils.toJsonString(updateResponse.getResult().getLowercase()));
        } catch (ElasticsearchException e) {
            if (e.status() == RestStatus.NOT_FOUND) {
                System.out.println("文档不存在");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 有则更新，没有则insert
     */
    @Test
    public void upsertById() {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("name", "我是谁更");
        jsonMap.put("age", 101);
        UpdateRequest request = new UpdateRequest("test_index", "test_type", "1")
                .doc(jsonMap);

        String json = "{\"name\":\"upsert\"}";
        request.upsert(json, XContentType.JSON);


        try {
            UpdateResponse updateResponse = client.update(request, RequestOptions.DEFAULT);
            ReplicationResponse.ShardInfo shardInfo = updateResponse.getShardInfo();
            System.out.println("结果:" + JsonUtils.toJsonString(updateResponse.getResult().getLowercase()));
        } catch (ElasticsearchException e) {
            if (e.status() == RestStatus.NOT_FOUND) {
                System.out.println("文档不存在");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
