package com.mzh.flink.submit;

public class SubmitTest {
    public static void main(String[] args)  throws Exception{
        String sqls="-- source\n" +
                "create table user_log (\n" +
                "mid_id VARCHAR,\n" +
                "uid VARCHAR,\n" +
                "version_code VARCHAR,\n" +
                "version_name VARCHAR,\n" +
                "lang VARCHAR,\n" +
                "source VARCHAR,\n" +
                "os VARCHAR,\n" +
                "area VARCHAR,\n" +
                "model VARCHAR,\n" +
                "brand VARCHAR,\n" +
                "sdk_version VARCHAR,\n" +
                "gmail VARCHAR,\n" +
                "height_width VARCHAR,\n" +
                "app_time VARCHAR,\n" +
                "network VARCHAR,\n" +
                "lng VARCHAR,\n" +
                "lat VARCHAR,\n" +
                "entry VARCHAR,\n" +
                "open_ad_type VARCHAR,\n" +
                "action VARCHAR,\n" +
                "t bigint,\n" +
                "detail VARCHAR,\n" +
                "extend1 VARCHAR\n" +
                ") WITH (\n" +
                "    'connector.type' = 'kafka',\n" +
                "    'connector.version' = 'universal',\n" +
                "    'connector.topic' = 'topic_start',\n" +
                "    'connector.startup-mode' = 'earliest-offset',\n" +
                "    'connector.properties.zookeeper.connect' = 'hdp1:2181,hdp1:2182,hdp3:2181',\n" +
                "    'connector.properties.bootstrap.servers' = 'hdp1:9092,hdp2:9092,hdp3:9092', \n" +
                "    'update-mode' = 'append',\n" +
                "    'format.type' = 'json',\n" +
                "    'format.derive-schema' = 'true'\n" +
                ");\n" +
                "\n" +
                "-- sink\n" +
                "CREATE TABLE pvuv_sink (\n" +
                "                           dt VARCHAR,\n" +
                "                           pv BIGINT,\n" +
                "                           uv BIGINT\n" +
                ") WITH (\n" +
                "    'connector.type' = 'jdbc',\n" +
                "    'connector.url' = 'jdbc:mysql://hdp3:3306/test',\n" +
                "    'connector.table' = 'pvuv_sink',\n" +
                "    'connector.username' = 'root',\n" +
                "    'connector.password' = 'root',\n" +
                "    'connector.write.flush.max-rows' = '1'\n" +
                ");\n" +
                "\n" +
                "\n" +
                "INSERT INTO pvuv_sink\n" +
                "SELECT\n" +
                "    FROM_UNIXTIME(t/1000, 'yyyy-MM-dd') dt,\n" +
                "    COUNT(*) AS pv,\n" +
                "    COUNT(DISTINCT uid) AS uv\n" +
                "FROM user_log\n" +
                "GROUP BY FROM_UNIXTIME(t/1000, 'yyyy-MM-dd');";


        Submit submit = new Submit("job1",sqls);
        submit.run();
    }
}
