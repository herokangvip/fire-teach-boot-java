package com.example.demo.hbase;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.*;


/**
 * 请参考hbase-demo
 */
public class HBaseUtils {
    /*public static void main(String[] args) throws Exception {
*//*        HashMap<String, String> map = new HashMap<>();
        map.put("country","中国");
        map.put("email","xxxx@163.com");
        insterRow("kang","10001","col",map);
        HBaseUtils.getData("kang","10001","col","");*//*
        scanData("kang", "col", "10001", "10009", "10006", 3);
        //HBaseUtils.getData("kang","10009","col","");
        //HBaseUtils.insterRow("kang","10003","col","tel","1xxxxx");
//        HBaseUtils.insterRow("kang","10004","col","tel","1xxxxx");
//        HBaseUtils.insterRow("kang","10005","col","tel","1xxxxx");
//        HBaseUtils.insterRow("kang","10006","col","tel","1xxxxx");
//        HBaseUtils.insterRow("kang","10007","col","tel","1xxxxx");
//        HBaseUtils.insterRow("kang","10008","col","tel","1xxxxx");
//        HBaseUtils.insterRow("kang","10009","col","tel","1xxxxx");
    }

    private static Connection connection;
    private static Admin admin;

    static {
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.property.clientPort", "2181");
        configuration.set("hbase.zookeeper.quorum", "server01");
        configuration.set("hbase.master", "server01:60000");
        try {
            connection = ConnectionFactory.createConnection(configuration);
            admin = connection.getAdmin();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 关闭连接
    public static void close() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 建表
    public static void createTable(String tableNmae, String[] cols) throws IOException {
        TableName tableName = TableName.valueOf(tableNmae);

        if (admin.tableExists(tableName)) {
            System.out.println("talbe is exists!");
        } else {
            HTableDescriptor hTableDescriptor = new HTableDescriptor(tableName);
            for (String col : cols) {
                HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(col);
                hTableDescriptor.addFamily(hColumnDescriptor);
            }
            admin.createTable(hTableDescriptor);
        }
        close();
    }

    // 删表
    public static void deleteTable(String tableName) throws IOException {

        TableName tn = TableName.valueOf(tableName);
        if (admin.tableExists(tn)) {
            admin.disableTable(tn);
            admin.deleteTable(tn);
        }
        close();
    }

    // 查看已有表
    public static void listTables() throws IOException {

        HTableDescriptor hTableDescriptors[] = admin.listTables();
        for (HTableDescriptor hTableDescriptor : hTableDescriptors) {
            System.out.println(hTableDescriptor.getNameAsString());
        }
        close();
    }

    // 插入数据
    public static void insterRow(String tableName, String rowkey, String colFamily, String col, String val)
            throws IOException {

        Table table = connection.getTable(TableName.valueOf(tableName));
        Put put = new Put(Bytes.toBytes(rowkey));
        put.addColumn(Bytes.toBytes(colFamily), Bytes.toBytes(col), Bytes.toBytes(val));
        table.put(put);

        // 批量插入
        *//*
         * List<Put> putList = new ArrayList<Put>(); puts.add(put);
         * table.put(putList);
         *//*
        table.close();
        close();
    }

    // 插入数据
    public static void insterRow(String tableName, String rowkey, String colFamily, Map<String, String> map)
            throws IOException {

        Table table = connection.getTable(TableName.valueOf(tableName));
        Put put = new Put(Bytes.toBytes(rowkey));
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            put.addColumn(Bytes.toBytes(colFamily), Bytes.toBytes(key), Bytes.toBytes(value));
        }
        table.put(put);

        // 批量插入
        *//*
         * List<Put> putList = new ArrayList<Put>(); puts.add(put);
         * table.put(putList);
         *//*
        table.close();
        close();
    }

    // 删除数据
    public static void deleRow(String tableName, String rowkey, String colFamily, String col) throws IOException {

        Table table = connection.getTable(TableName.valueOf(tableName));
        Delete delete = new Delete(Bytes.toBytes(rowkey));
        // 删除指定列族
        // delete.addFamily(Bytes.toBytes(colFamily));
        // 删除指定列
        // delete.addColumn(Bytes.toBytes(colFamily),Bytes.toBytes(col));
        table.delete(delete);
        // 批量删除
        *//*
         * List<Delete> deleteList = new ArrayList<Delete>();
         * deleteList.add(delete); table.delete(deleteList);
         *//*
        table.close();
        close();
    }

    // 根据rowkey查找数据
    public static void getData(String tableName, String rowkey, String colFamily, String col) throws IOException {

        Table table = connection.getTable(TableName.valueOf(tableName));
        Get get = new Get(Bytes.toBytes(rowkey));
        // 获取指定列族数据
        get.addFamily(Bytes.toBytes(colFamily));
        // 获取指定列数据
        // get.addColumn(Bytes.toBytes(colFamily),Bytes.toBytes(col));
        Result result = table.get(get);

        showCell(result);
        table.close();
        close();
    }

    // 格式化输出
    public static void showCell(Result result) {
        Cell[] cells = result.rawCells();
        HashMap<String, Map<String, String>> resMap = new HashMap<>();
        for (Cell cell : cells) {
            String rowKey = new String(CellUtil.cloneRow(cell));
            String name = new String(CellUtil.cloneQualifier(cell));
            String value = new String(CellUtil.cloneValue(cell));
            if (resMap.containsKey(rowKey)) {
                resMap.get(rowKey).put(name, value);
            } else {
                HashMap<String, String> map = new HashMap<>();
                map.put(name, value);
                resMap.put(rowKey, map);
            }
        }
        System.out.println(resMap);
    }

    // 批量查找数据
    public static void scanData(String tableName, String colFamily, String startRow, String stopRow, String lastRowKey, int pageSize) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        scan.addFamily(colFamily.getBytes());
        if (StringUtils.isNotBlank(lastRowKey)) {
            scan.setStartRow(Bytes.toBytes(lastRowKey));
            pageSize++;
        } else {
            scan.setStartRow(Bytes.toBytes(startRow));
        }
        scan.setStopRow(Bytes.toBytes(stopRow));
        //MUST_PASS_ALL过滤器必须全符合
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        //分页过滤器
        PageFilter pageFilter = new PageFilter(pageSize);
        filterList.addFilter(pageFilter);
        scan.setFilter(filterList);
        ResultScanner resultScanner = table.getScanner(scan);
        for (Result result : resultScanner.next(pageSize)) {
            String rowKey = new String(CellUtil.cloneRow(result.rawCells()[0]));
            if (lastRowKey.equals(rowKey)) {
                continue;
            }
            showCell(result);
        }
        table.close();
        close();
        //Scan类常用方法说明
        //指定需要的family或column ，如果没有调用任何addFamily或Column，会返回所有的columns；
        // scan.addFamily();
        // scan.addColumn();
        // scan.setMaxVersions(); //指定最大的版本个数。如果不带任何参数调用setMaxVersions，表示取所有的版本。如果不掉用setMaxVersions，只会取到最新的版本.
        // scan.setTimeRange(); //指定最大的时间戳和最小的时间戳，只有在此范围内的cell才能被获取.
        // scan.setTimeStamp(); //指定时间戳
        // scan.setFilter(); //指定Filter来过滤掉不需要的信息
        // scan.setStartRow(); //指定开始的行。如果不调用，则从表头开始；
        // scan.setStopRow(); //指定结束的行（不含此行）；
        // scan.setBatch(); //指定最多返回的Cell数目。用于防止一行中有过多的数据，导致OutofMemory错误。

        //过滤器
        //1、FilterList代表一个过滤器列表
        //FilterList.Operator.MUST_PASS_ALL -->and
        //FilterList.Operator.MUST_PASS_ONE -->or
        //eg、FilterList list = new FilterList(FilterList.Operator.MUST_PASS_ONE);
        //2、SingleColumnValueFilter
        //3、ColumnPrefixFilter用于指定列名前缀值相等
        //4、MultipleColumnPrefixFilter和ColumnPrefixFilter行为差不多，但可以指定多个前缀。
        //5、QualifierFilter是基于列名的过滤器。
        //6、RowFilter
        //7、RegexStringComparator是支持正则表达式的比较器。
        //8、SubstringComparator用于检测一个子串是否存在于值中，大小写不敏感。

        //设置想取的数据的版本号，不设置则取最新值
        //scan.setMaxVersions();
        //指定最多返回的Cell数目。用于防止一行中有过多的数据，导致OutofMemory错误。
        //scan.setBatch(1000);

        //scan.setTimeStamp(NumberUtils.toLong("1370336286283"));
        //scan.setTimeRange(NumberUtils.toLong("1370336286283"), NumberUtils.toLong("1370336337163"));
        //scan.setStartRow(Bytes.toBytes("quanzhou"));
        //scan.setStopRow(Bytes.toBytes("xiamen"));
        //scan.addFamily(Bytes.toBytes("info"));
        //scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("id"));

        //查询列镞为info，列id值为1的记录
        //方法一(单个查询)
        // Filter filter = new SingleColumnValueFilter(
        //         Bytes.toBytes("info"), Bytes.toBytes("id"), CompareOp.EQUAL, Bytes.toBytes("1"));
        // scan.setFilter(filter);

        //方法二(组合查询)
        //FilterList filterList=new FilterList();
        //Filter filter = new SingleColumnValueFilter(
        //    Bytes.toBytes("info"), Bytes.toBytes("id"), CompareOp.EQUAL, Bytes.toBytes("1"));
        //filterList.addFilter(filter);
        //scan.setFilter(filterList);
    }*/

}
