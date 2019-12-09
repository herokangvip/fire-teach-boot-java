package com.example.demo.es;

import com.example.demo.DemoApplication;
import com.example.demo.dao.SkuEsDao;
import com.example.demo.domain.Sku;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ScrolledPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

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
    public void update() {
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
        Sku sku = skuEsDao.findByNameAndCategoryAndId("华为手机", "手机数码", 1002L);
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

    @Test
    public void matchScroll() {
        // 创建查询条件对象
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        // 拼接查询条件
        queryBuilder.must(QueryBuilders.rangeQuery("price").gte(3000).lte(10000)); //假设查询状态为1的

        // 创建查询对象
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices("sku_index")//索引名
                .withTypes("sku_type")//类型名
                .withFilter(queryBuilder)// 不会计算相关度
                //.withQuery(queryBuilder)// 会计算相关度
                .withPageable(PageRequest.of(0, 2))//从0页开始查，每页2个结果
                .build();

        // 滚动查询
        ScrolledPage<Sku> scroll = (ScrolledPage<Sku>) elasticsearchTemplate.startScroll(3000, searchQuery, Sku.class);

        // 判断是否有内容
        while (scroll.hasContent()) {
            List<Sku> content = scroll.getContent();
            content.forEach(a -> System.out.println(a.getPrice()));
            // 业务逻辑省略
            //取下一页，scrollId在es服务器上可能会发生变化，需要用最新的。发起continueScroll请求会重新刷新快照保留时间
            scroll = (ScrolledPage<Sku>) elasticsearchTemplate.continueScroll(scroll.getScrollId(), 3000, Sku.class);
        }

        // 最后释放查询
        elasticsearchTemplate.clearScroll(scroll.getScrollId());
    }


}
