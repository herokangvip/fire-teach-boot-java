package com.example.demo.config.es;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesResponse;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.GetAliasesResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EsClient {

    @Autowired(required = false)
    private RestHighLevelClient esClient;

    private static String skuMapping;

    static {
        InputStream stream = EsClient.class.getResourceAsStream("/es/sku.json");
        skuMapping = IoUtil.readUtf8(stream);
    }

    public String getSkuMapping() {
        return skuMapping;
    }

    public boolean existsIndex(String index) {
        TimeInterval timer = DateUtil.timer();
        IndicesClient indicesClient = esClient.indices();
        GetIndexRequest request = new GetIndexRequest().indices(index);
        try {
            boolean exists = indicesClient.exists(request, RequestOptions.DEFAULT);
            return exists;
        } catch (IOException e) {
            throw new RemoteAccessException("es index exists error. index:" + index + ", costTime:"
                    + timer.interval(), e);
        }
    }

    public boolean createIndex(String index, Integer shards, Integer replicas,
                               String type, String mapping) throws Exception {
        TimeInterval timer = DateUtil.timer();
        if (existsIndex(index)) {
            return true;
        }
        CreateIndexRequest request = new CreateIndexRequest(index);
        Map<String, Object> map = new HashMap<>();
        map.put("number_of_shards", shards);
        map.put("number_of_replicas", replicas);
        request.settings(map);

        request.mapping(type, mapping, XContentType.JSON);
        try {
            CreateIndexResponse response = esClient.indices().create(request, RequestOptions.DEFAULT);
            String indexRes = response.index();
            System.out.println("=====" + indexRes);
            return true;
        } catch (IOException e) {
            throw new RemoteAccessException("es create index error. index:" + index + ", costTime:"
                    + timer.interval(), e);
        }
    }

    public AcknowledgedResponse putMapping(String index, String type, String mapping) {
        TimeInterval timer = DateUtil.timer();
        PutMappingRequest putMappingRequest = Requests.putMappingRequest(index).type(type)
                .source(mapping, XContentType.JSON);
        try {
            AcknowledgedResponse response = esClient.indices().putMapping(putMappingRequest, RequestOptions.DEFAULT);
            return response;
        } catch (IOException e) {
            throw new RemoteAccessException("es put mapping error. index:" + index + ", type:" + type + ", costTime:"
                    + timer.interval(), e);
        }
    }

    public GetMappingsResponse getMapping(String index, String type) {
        TimeInterval timer = DateUtil.timer();
        GetMappingsRequest getMappingRequest = new GetMappingsRequest();
        getMappingRequest.indices(index);
        getMappingRequest.types(type);
        try {
            GetMappingsResponse mapping = esClient.indices().getMapping(getMappingRequest, RequestOptions.DEFAULT);
            return mapping;
        } catch (IOException e) {
            throw new RemoteAccessException("es put mapping error. index:" + index + ", costTime:"
                    + timer.interval(), e);
        }
    }

    public IndicesAliasesResponse setAlias(String[] indices, String alias) {
        IndicesAliasesRequest indicesAliasesRequest = new IndicesAliasesRequest();
        IndicesAliasesRequest.AliasActions add = IndicesAliasesRequest.AliasActions.add();
        add.indices(indices);
        add.alias(alias);
        indicesAliasesRequest.addAliasAction(add);
        try {
            return esClient.indices().updateAliases(indicesAliasesRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RemoteAccessException("es setAlias error. alias:" + alias, e);
        }
    }

    public List<String> getAlias(String index) {
        GetAliasesRequest getAliasesRequest = new GetAliasesRequest();
        getAliasesRequest.indices(index);
        try {
            GetAliasesResponse alias = esClient.indices().getAlias(getAliasesRequest, RequestOptions.DEFAULT);
            Set<AliasMetaData> aliasMetaData = alias.getAliases().get(index);
            List<String> collect = aliasMetaData.stream().map(AliasMetaData::getAlias).collect(Collectors.toList());
            return collect;
        } catch (IOException e) {
            throw new RemoteAccessException("es getAlias error. index:" + index, e);
        }
    }

    public void upsert(String index, String type, String id, Object obj) {
        TimeInterval timer = DateUtil.timer();
        String json = JSONObject.toJSONString(obj);
        UpdateRequest updateRequest = new UpdateRequest(index, type, id)
                .routing(id)
                .doc(json, XContentType.JSON).setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE)
                .upsert(json, XContentType.JSON);
        UpdateResponse response;
        try {
            response = esClient.update(updateRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RemoteAccessException("es upsert error. index:" + index + ", type:" + type
                    + ", docId:" + id + ", costTime:" + timer.interval(), e);
        }
    }

    public <T> void batchUpsert(String index, String type, String idKey, List<T> list) {
        BulkRequest bulkRequest = new BulkRequest();
        for (T obj : list) {
            String json = JSONObject.toJSONString(obj);
            JSONObject jsonObject = JSONObject.parseObject(json);
            Object object = jsonObject.get(idKey);
            String id = String.valueOf(object);
            UpdateRequest updateRequest = new UpdateRequest(index, type, id)
                    .routing(id)
                    .doc(json, XContentType.JSON)
                    .upsert(json, XContentType.JSON);
            bulkRequest.add(updateRequest);
        }
        bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        try {
            BulkResponse bulk = esClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RemoteAccessException("es batchUpsert error. index:" + index + ", type:" + type, e);
        }
    }

    public void delete(String index, String type, String id) {
        DeleteRequest deleteRequest = new DeleteRequest(index, type, id);
        deleteRequest.routing(id).setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        try {
            DeleteResponse response = esClient.delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RemoteAccessException("es delete error. index:" + index + ", type:" + type + ", docId:" + id, e);
        }
    }


    public <T> List<T> searchAll(String index, String type, TypeReference<T> typeReference) {
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);
        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource().query(matchAllQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = null;
        try {
            response = esClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RemoteAccessException("es searchAll error. index:" + index + ", type:" + type, e);
        }
        if (response == null || response.getHits() == null || response.getHits().getTotalHits() <= 0) {
            return new ArrayList<>();
        }
        SearchHits searchHits = response.getHits();
        List<T> list = Lists.newArrayListWithExpectedSize(searchHits.getHits().length);
        for (SearchHit searchHit : searchHits.getHits()) {
            //System.out.println(searchHit.getSourceAsString());
            list.add(JSONObject.parseObject(searchHit.getSourceAsString(), typeReference));
        }
        return list;
    }

    /**
     * search
     */
    public <T> Page<T> search(String index, String type, QueryBuilder queryBuilder, String sortField,
                              SortOrder sortOrder, long timeout, int from, int size, TypeReference<T> typeReference) {
        SearchResponse response = null;
        try {
            SearchRequest searchRequest = new SearchRequest(index); //客户端请求
            searchRequest.types(type);
            //创建搜索的builder
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            //构建query
            searchSourceBuilder
                    .query(queryBuilder)
                    .from(from)
                    .size(size)
                    .timeout(TimeValue.timeValueMillis(timeout))
                    .sort(sortField, sortOrder);
            searchRequest.source(searchSourceBuilder);
            response = esClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RemoteAccessException("es search error. index:" + index + ", type:" + type, e);
        }
        Page<T> page = new Page<>();
        if (response == null || response.getHits() == null || response.getHits().getTotalHits() <= 0L) {
            page.setList(new ArrayList<>());
            return page;
        }
        SearchHits searchHits = response.getHits();
        long totalHits = searchHits.getTotalHits();//7.x高版本不是返回long而是一个对象，所以一定要注意es客户端和服务端的版本号
        page.setTotalRecord(totalHits);
        List<T> list = Lists.newArrayListWithExpectedSize(searchHits.getHits().length);
        for (SearchHit searchHit : searchHits.getHits()) {
            list.add(JSONObject.parseObject(searchHit.getSourceAsString(), typeReference));
        }
        page.setList(list);
        return page;
    }


    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Page<T> {
        private long totalRecord = 0L;
        private List<T> list;
    }

}
