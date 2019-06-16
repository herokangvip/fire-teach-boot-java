package com.example.demo.es;

import com.example.demo.DemoApplication;
import com.example.demo.dao.SkuEsDao;
import com.example.demo.domain.Sku;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class EsDemoApplicationTests {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private SkuEsDao skuEsDao;

    @Test
    public void addIndex() {
        boolean a = elasticsearchTemplate.indexExists(Sku.class);
        System.out.println("索引是否存在" + a);
        boolean index = elasticsearchTemplate.createIndex(Sku.class);
        System.out.println("创建索引的结果:" + index);
        boolean b = elasticsearchTemplate.indexExists(Sku.class);
        System.out.println("索引是否存在" + b);
    }

    @Test
    public void insert() {
        Sku sku = new Sku(1000L, "苹果手机", "手机数码", 5888.00);
        Sku save = skuEsDao.save(sku);
        System.out.println(save);
    }

    @Test
    public void update(){
        Sku sku = new Sku(1002L, "最厉害的华为手机", "手机数码", 5888.00);
        skuEsDao.save(sku);
    }

    @Test
    public void batchInsert() {
        ArrayList<Sku> skus = new ArrayList<>();
        skus.add(new Sku(1001L, "小米手机", "手机数码", 1888.00));
        skus.add(new Sku(1002L, "华为手机", "手机数码", 2888.00));
        skus.add(new Sku(1003L, "荣耀手机", "手机数码", 3888.00));
        skus.add(new Sku(1004L, "联想手机", "手机数码", 4888.00));
        skus.add(new Sku(1005L, "中兴手机", "手机数码", 6888.00));
        skus.add(new Sku(1006L, "诺基亚手机", "手机数码", 7888.00));
        skus.add(new Sku(1007L, "vivo手机", "手机数码", 8888.00));
        skus.add(new Sku(1008L, "oppo手机", "手机数码", 9888.00));
        skus.add(new Sku(1009L, "oneP手机", "手机数码", 10888.00));
        skus.add(new Sku(1010L, "锤子手机", "手机数码", 888.00));

        Iterable<Sku> iterable = skuEsDao.saveAll(skus);
        System.out.println(iterable);
    }

    @Test
    public void findAll() {
        Iterable<Sku> all = skuEsDao.findAll();
        all.forEach(System.out::println);
    }

    @Test
    public void findBy() {
        Sku sku = skuEsDao.findByNameAndAndCategoryAndId("华为手机", "手机数码", 1002L);
        System.out.println(sku);
    }




    @Test
    public void matchQuery() {
        // 构建查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 添加基本分词查询
        queryBuilder.withQuery(QueryBuilders.matchQuery("name", "厉害"));
        // 搜索，获取结果
        NativeSearchQuery searchQuery = queryBuilder.build();
        Page<Sku> items = skuEsDao.search(searchQuery);
        // 总条数
        long total = items.getTotalElements();
        System.out.println("total = " + total);
        for (Sku item : items) {
            System.out.println(item);
        }
    }




}
