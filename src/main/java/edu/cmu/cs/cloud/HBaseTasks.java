package edu.cmu.cs.cloud;

import java.io.IOException;
import java.lang.Throwable;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.coprocessor.AggregationClient;
import org.apache.hadoop.hbase.client.coprocessor.LongColumnInterpreter;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class HBaseTasks {

    /**
     * The private IP address of HBase master node.
     */
    private static String zkAddr = "172.31.90.129";
    /**
     * The name of your HBase table.
     */
    private static TableName tableName = TableName.valueOf("business");
    /**
     * HTable handler.
     */
    private static Table bizTable;
    /**
     * HBase connection.
     */
    private static Connection conn;
    /**
     * Byte representation of column family.
     */
    private static byte[] bColFamily = Bytes.toBytes("data");
    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getRootLogger();

    /**
     * Initialize HBase connection.
     *
     * @throws IOException if a network exception occurs.
     */
    private static void initializeConnection() throws IOException {
        // Remember to set correct log level to avoid unnecessary output.
        LOGGER.setLevel(Level.OFF);
        if (!zkAddr.matches("\\d+.\\d+.\\d+.\\d+")) {
            System.out.print("Malformed HBase IP address");
            System.exit(-1);
        }
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", zkAddr);
        conf.set("hbase.zookeeper.property.clientport", "2181");
        conn = ConnectionFactory.createConnection(conf);
        bizTable = conn.getTable(tableName);
    }

    /**
     * Clean up resources.
     *
     * @throws IOException
     * Throw IOEXception
     */
    private static void cleanup() throws IOException {
        if (bizTable != null) {
            bizTable.close();
        }
        if (conn != null) {
            conn.close();
        }
    }

    /**
     * You should complete the missing parts in the following method.
     * Feel free to add helper functions if necessary.
     *
     * For all questions, output your answer in ONE single line,
     * i.e. use System.out.print().
     *
     * @param args The arguments for main method.
     * @throws IOException if a remote or network exception occurs.
     */
    public static void main(String[] args) throws IOException {
        initializeConnection();
        switch (args[0]) {
            case "demo":
                demo();
                break;
            case "q11":
                q11();
                break;
            case "q12":
                q12();
                break;
            case "q13":
                q13();
                break;
            case "q14":
                q14();
                break;
            default:
                break;
        }
        cleanup();
    }

    /**
     * This is a demo of how to use HBase Java API.
     * It will print the number of businesses in "Pittsburgh".
     *
     * @throws IOException if a remote or network exception occurs.
     */
    private static void demo() throws IOException {
        Scan scan = new Scan();
        byte[] bCol = Bytes.toBytes("city");
        scan.addColumn(bColFamily, bCol);
        SubstringComparator comp = new SubstringComparator("Pittsburgh");
        Filter filter = new SingleColumnValueFilter(
                bColFamily, bCol, CompareFilter.CompareOp.EQUAL, comp);
        scan.setFilter(filter);
        ResultScanner rs = bizTable.getScanner(scan);
        int count = 0;
        for (Result r = rs.next(); r != null; r = rs.next()) {
            count++;
            // System.out.println(Bytes.toString(r.getValue(bColFamily, bCol)));
        }
        System.out.println("Scan finished. " + count + " match(es) found.");
        rs.close();
    }

    /**
     * Question 11.
     *
     * Scenario:
     * What's that new "Asian Fusion" place in "Shadyside" with free wifi and
     * bike parking?
     *
     * Print each name of the business on a single line.
     * If there are multiple answers, print all of them.
     *
     * Note:
     * 1. The "neighborhood" column should contain "Shadyside" as a substring.
     * 2. The "categories" column should contain "Asian Fusion" as a substring.
     * 3. The "WiFi" and "BikeParking" information can be found in the
     * "attributes" column. Please be careful about the format of the data.
     *
     * You are allowed to make changes such as modifying method name, parameter
     * list and/or return type.
     */
    private static void q11() throws IOException {
        Scan scan = new Scan();
        byte[] neighborhoodCol = Bytes.toBytes("neighborhood");
        byte[] categoriesCol = Bytes.toBytes("categories");
        byte[] attributesCol = Bytes.toBytes("attributes");
        byte[] nameCol = Bytes.toBytes("name");
        scan.addColumn(bColFamily, neighborhoodCol);
        scan.addColumn(bColFamily, categoriesCol);
        scan.addColumn(bColFamily, attributesCol);
        scan.addColumn(bColFamily, nameCol);
        SubstringComparator neighborhoodComp = new SubstringComparator("Shadyside");
        SubstringComparator categoriesComp = new SubstringComparator("Asian Fusion");
        SubstringComparator wifiComp = new SubstringComparator("'WiFi': 'free'");
        SubstringComparator bikeParkingComp = new SubstringComparator("'BikeParking': True");
        Filter neighborhoodFilter = new SingleColumnValueFilter(
                bColFamily, neighborhoodCol, CompareFilter.CompareOp.EQUAL, neighborhoodComp);
        Filter categoriesFilter = new SingleColumnValueFilter(
                bColFamily, categoriesCol, CompareFilter.CompareOp.EQUAL, categoriesComp);
        Filter wifiFilter = new SingleColumnValueFilter(
                bColFamily, attributesCol, CompareFilter.CompareOp.EQUAL, wifiComp);
        Filter bikeParkingFilter = new SingleColumnValueFilter(
                bColFamily, attributesCol, CompareFilter.CompareOp.EQUAL, bikeParkingComp);
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        filterList.addFilter(neighborhoodFilter);
        filterList.addFilter(categoriesFilter);
        filterList.addFilter(wifiFilter);
        filterList.addFilter(bikeParkingFilter);
        scan.setFilter(filterList);

        ResultScanner rs = bizTable.getScanner(scan);
        for (Result r = rs.next(); r != null; r = rs.next()) {
            System.out.println(Bytes.toString(r.getValue(bColFamily, nameCol)));
        }
        rs.close();
    }

    /**
     * Question 12.
     *
     * Scenario:
     * I'm looking for some Indian food to eat in Downtown or Oakland of Pittsburgh
     * that start serving on Fridays at 5pm, but still deliver in case I'm too lazy
     * to drive there.
     *
     * Print each name of the business on a single line.
     * If there are multiple answers, print all of them.
     *
     * Note:
     * 1. The "name" column should contain "India" as a substring.
     * 2. The "neighborhood" column should contain "Downtown" or "Oakland"
     * as a substring.
     * 3. The "city" column should contain "Pittsburgh" as a substring.
     * 4. The "hours" column shows the hours when businesses start serving.
     * 5. The "RestaurantsDelivery" information can be found in the
     * "attributes" column.
     *
     * Hint:
     * You may consider using other comparators in the filter.
     *
     * You are allowed to make changes such as modifying method name, parameter
     * list and/or return type.
     */
    private static void q12() throws IOException {
        Scan scan = new Scan();
        byte[] nameCol = Bytes.toBytes("name");
        byte[] neighborhoodCol = Bytes.toBytes("neighborhood");
        byte[] cityCol = Bytes.toBytes("city");
        byte[] hoursCol = Bytes.toBytes("hours");
        byte[] attributesCol = Bytes.toBytes("attributes");
        scan.addColumn(bColFamily, nameCol);
        scan.addColumn(bColFamily, neighborhoodCol);
        scan.addColumn(bColFamily, cityCol);
        scan.addColumn(bColFamily, hoursCol);
        scan.addColumn(bColFamily, attributesCol);

        SubstringComparator nameComp = new SubstringComparator("India");
        RegexStringComparator neighborhoodComp = new RegexStringComparator(
                ".*(Downtown|Oakland).*");
        SubstringComparator cityComp = new SubstringComparator("Pittsburgh");
        SubstringComparator hoursComp = new SubstringComparator("'Friday': '17:00");
        SubstringComparator deliveryComp = new SubstringComparator("'RestaurantsDelivery': True");

        Filter nameFilter = new SingleColumnValueFilter(
                bColFamily, nameCol, CompareFilter.CompareOp.EQUAL, nameComp);
        Filter neighborhoodFilter = new SingleColumnValueFilter(
                bColFamily, neighborhoodCol, CompareFilter.CompareOp.EQUAL, neighborhoodComp);
        Filter cityFilter = new SingleColumnValueFilter(
                bColFamily, cityCol, CompareFilter.CompareOp.EQUAL, cityComp);
        Filter hoursFilter = new SingleColumnValueFilter(
                bColFamily, hoursCol, CompareFilter.CompareOp.EQUAL, hoursComp);
        Filter deliveryFilter = new SingleColumnValueFilter(
                bColFamily, attributesCol, CompareFilter.CompareOp.EQUAL, deliveryComp);
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        filterList.addFilter(nameFilter);
        filterList.addFilter(neighborhoodFilter);
        filterList.addFilter(cityFilter);
        filterList.addFilter(hoursFilter);
        filterList.addFilter(deliveryFilter);
        scan.setFilter(filterList);

        ResultScanner rs = bizTable.getScanner(scan);
        for (Result r = rs.next(); r != null; r = rs.next()) {
            System.out.println(Bytes.toString(r.getValue(bColFamily, nameCol)));
        }
        rs.close();
    }

    /**
     * Question 13.
     *
     * Write HBase query to do the equivalent of the SQL query:
     * SELECT name FROM businesses where business_id = "I1vE5o98Wy5pCULJoEclqw"
     *
     * Hint:
     * You may consider using other HBase operations which are used to search
     * and retrieve one single row by the rowkey.
     *
     * You are allowed to make changes such as modifying method name, parameter
     * list and/or return type.
     */
    private static void q13() throws IOException {
        Scan scan = new Scan();
        byte[] nameCol = Bytes.toBytes("name");
        scan.addColumn(bColFamily, nameCol);
        ResultScanner rs = bizTable.getScanner(scan);
        for (Result r = rs.next(); r != null; r = rs.next()) {
            if (Bytes.toString(r.getRow()).equals("I1vE5o98Wy5pCULJoEclqw")) {
                System.out.println(Bytes.toString(r.getValue(bColFamily, nameCol)));
                break;
            }
        }
        rs.close();
    }

    /**
     * Question 14.
     *
     * Write HBase query to do the equivalent of the SQL query:
     * SELECT COUNT(*) FROM businesses
     *
     * Print the number on a single line.
     *
     * Note:
     * 1. HBase uses Coprocessor to perform data aggregation across multiple
     * region servers, you need to enable Coprocessors inside HBase shell
     * before writing Java code.
     *
     *   Step 1. disable the table
     *   hbase> disable 'mytable'
     *
     *   Step 2. add the coprocessor
     *   hbase> alter 'mytable', METHOD =>
     *     'table_att','coprocessor'=>
     *     '|org.apache.hadoop.hbase.coprocessor.AggregateImplementation||'
     *
     *   Step 3. re-enable the table
     *   hbase> enable 'mytable'
     *
     * 2. You may want to look at the AggregationClient Class in HBase APIs.
     *
     * You are allowed to make changes such as modifying method name, parameter
     * list and/or return type.
     */
    private static void q14() {
        try {
            Configuration conf = HBaseConfiguration.create();
            // conf.setInt("hbase.client.retries.number", 1);
            // conf.setInt("ipc.client.connect.max.retries", 1);
            conf.set("hbase.zookeeper.quorum", zkAddr);
            conf.set("hbase.zookeeper.property.clientport", "2181");
            AggregationClient aggregationClient = new AggregationClient(conf);
            Scan scan = new Scan();
            scan.addFamily(bColFamily);
            long rowCount = aggregationClient.rowCount(
                    tableName, new LongColumnInterpreter(), scan);
            System.out.println(rowCount);
        } catch (Throwable e) {
            System.err.println(e);
        }
    }

}