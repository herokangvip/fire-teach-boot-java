package com.example.demo.dao;

import com.example.demo.domain.Sku;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SkuEsDao extends ElasticsearchRepository<Sku,Long> {
    Sku findByNameAndAndCategoryAndId(String name,String category,Long id);
}
