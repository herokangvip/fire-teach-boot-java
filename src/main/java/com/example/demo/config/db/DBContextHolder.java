package com.example.demo.config.db;


/**
 * server
 *
 * @author sandykang
 */
public class DBContextHolder {
    private static final ThreadLocal<DBTypeEnum> contextHolder = new ThreadLocal<>();


    /**
     * set
     * @param dbType dbType
     */
    public static void set(DBTypeEnum dbType) {
        contextHolder.set(dbType);
    }

    /**
     * get
     * @return DBTypeEnum
     */
    public static DBTypeEnum get() {
        return contextHolder.get();
    }

    public static void remove() {
        contextHolder.remove();
    }

    /**
     * master
     */
    public static void master() {
        set(DBTypeEnum.MASTER);
    }

    /**
     * slave
     */
    public static void slave() {
        set(DBTypeEnum.SLAVE);
    }

}
