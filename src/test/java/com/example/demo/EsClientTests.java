package com.example.demo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.demo.config.es.EsClient;
import com.example.demo.domain.Sku;
import com.example.demo.domain.es.MySku;
import com.example.demo.domain.es.SpInfo;
import com.example.demo.utils.json.JsonUtils;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.ConstantScoreQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApplication.class})
public class EsClientTests {

    @Resource
    private EsClient esClient;


    @Test
    public void createIndex() throws Exception {
        String skuMapping = esClient.getSkuMapping();
        boolean response = esClient.createIndex("sku", 5, 1, "_doc", skuMapping);
        System.out.println("===:" + JSONObject.toJSONString(response));
    }

    @Test
    public void putMapping() throws Exception {
        String skuMapping = esClient.getSkuMapping();
        AcknowledgedResponse response = esClient.putMapping("sku", "_doc", skuMapping);
        System.out.println("===:" + JSONObject.toJSONString(response));
    }

    @Test
    public void getMapping() {
        GetMappingsResponse response = esClient.getMapping("sku", "_doc");
        ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> mappings = response.getMappings();
        ImmutableOpenMap<String, MappingMetaData> sku = mappings.get("sku");
        MappingMetaData doc = sku.get("_doc");
        Map<String, Object> stringObjectMap = doc.sourceAsMap();
        System.out.println("===:" + JSONObject.toJSONString(response));
    }

    @Test
    public void insert() {
        String[] arr1 = new String[]{"手机"};
        String[] arr2 = new String[]{"手机", "数码"};
        String[] arr3 = new String[]{"水果"};

        esClient.upsert("sku", "_doc", "1",
                new MySku("1", "iphon14苹果手机最新款", arr1, 1.1d,
                        new SpInfo("11111", "12222")));
        esClient.upsert("sku", "_doc", "2",
                new MySku("2", "小米手机最新款", arr1, 2.2d,
                        new SpInfo("21111", "22222")));
        esClient.upsert("sku", "_doc", "3",
                new MySku("3", "华为手机最新款", arr2, 3.3d,
                        new SpInfo("31111", "32222")));
        esClient.upsert("sku", "_doc", "4",
                new MySku("4", "山东烟台红富士苹果", arr3, 4.4d,
                        new SpInfo("41111", "42222")));
        esClient.upsert("sku", "_doc", "5",
                new MySku("5", "苹果梨新鲜现摘", arr3, 5.5d,
                        new SpInfo("51111", "52222")));
        System.out.println("===:");
    }


    @Test
    public void insert2Arr() {
        String[] arr = new String[]{"手机"};
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        esClient.upsert("sku", "_doc", "10",
                new MySku("10", "果苹手机", arr, 9.9d,
                        new SpInfo("91111", "92222"), sdf.format(new Date())));
        System.out.println("===:");
    }

    @Test
    public void delete() {
        esClient.delete("sku", "_doc", "6");
        System.out.println("===:");
    }

    @Test
    public void batchInsert() {

        String[] arr = new String[]{"水果"};

        List<MySku> list = new ArrayList<>();
        MySku mySku = new MySku("6", "666草莓现摘", arr, 6.6d,
                new SpInfo("61111", "62222"));
        MySku mySku7 = new MySku("7", "777草莓现摘", arr, 7.7d,
                new SpInfo("71111", "72222"));

        list.add(mySku);
        list.add(mySku7);
        esClient.batchUpsert("sku", "_doc", "skuId", list);
        System.out.println("===:");
    }


    @Test
    public void searchAll() {
        List<MySku> mySkus = esClient.searchAll("sku", "_doc", new TypeReference<MySku>() {
        });
        System.out.println(JSONArray.toJSONString(mySkus));
    }


    @Test
    public void search() {
        TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery("category", "手机", "水果");
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("category", "手机");
        List<MySku> list = esClient.search("sku", "_doc", termQueryBuilder,
                "skuId", SortOrder.ASC, 3000, 0, 10, new TypeReference<MySku>() {
                }).getList();
        System.out.println(JSONArray.toJSONString(list));

        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("skuName", "手机");
        List<MySku> list2 = esClient.search("sku", "_doc", matchQueryBuilder,
                "skuId", SortOrder.ASC, 3000, 0, 10, new TypeReference<MySku>() {
                }).getList();
        System.out.println(JSONArray.toJSONString(list2));
    }


    @Test
    public void search2() {
        TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery("category", "手机");
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        //品类是手机，且名字不包含华为的
        //MatchQueryBuilder matchQueryBuilder2 = QueryBuilders.matchQuery("skuName", "华为");
        //boolQueryBuilder.must(termsQueryBuilder).mustNot(matchQueryBuilder2);

        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("category", "数码");
        boolQueryBuilder.must(termsQueryBuilder).mustNot(termQueryBuilder);
        List<MySku> list = esClient.search("sku", "_doc", boolQueryBuilder,
                "skuId", SortOrder.ASC, 3000, 0, 10, new TypeReference<MySku>() {
                }).getList();
        System.out.println(JSONArray.toJSONString(list));
    }


    @Test
    public void filterSearch() {
        TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery("category", "手机");
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.filter(termsQueryBuilder);

        List<MySku> list = esClient.search("sku", "_doc", boolQueryBuilder,
                "skuId", SortOrder.ASC, 3000, 0, 10, new TypeReference<MySku>() {
                }).getList();
        System.out.println(JSONArray.toJSONString(list));
    }

    @Test
    public void constantSearch() {
        TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery("category", "手机");
        ConstantScoreQueryBuilder constantScoreQueryBuilder = QueryBuilders.constantScoreQuery(termsQueryBuilder).boost(1.0f);
        List<MySku> list = esClient.search("sku", "_doc", constantScoreQueryBuilder,
                "skuId", SortOrder.ASC, 3000, 0, 10, new TypeReference<MySku>() {
                }).getList();
        System.out.println(JSONArray.toJSONString(list));
    }

    @Test
    public void searchCount() {
        TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery("category", "手机");
        ConstantScoreQueryBuilder constantScoreQueryBuilder = QueryBuilders.constantScoreQuery(termsQueryBuilder).boost(1.0f);
        EsClient.Page<MySku> page = esClient.search("sku", "_doc", constantScoreQueryBuilder,
                "skuId", SortOrder.ASC, 3000, 0, 10, new TypeReference<MySku>() {
                });

        List<MySku> list = page.getList();
        long totalRecord = page.getTotalRecord();
        System.out.println(JSONArray.toJSONString(list));
    }


}
