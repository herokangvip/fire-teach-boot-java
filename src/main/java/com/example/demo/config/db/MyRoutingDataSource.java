package com.example.demo.config.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.lang.Nullable;

/**
 * server
 *
 * @author sandykang
 */
public class MyRoutingDataSource extends AbstractRoutingDataSource {

    /**
     * determineCurrentLookupKey
     * @return obj
     */
    @Nullable
    @Override
    protected Object determineCurrentLookupKey() {
        DBTypeEnum dbTypeEnum = DBContextHolder.get();
        if (dbTypeEnum != null && dbTypeEnum.equals(DBTypeEnum.SLAVE)) {
            return dbTypeEnum;
        } else {
            return DBTypeEnum.MASTER;
        }
    }

}