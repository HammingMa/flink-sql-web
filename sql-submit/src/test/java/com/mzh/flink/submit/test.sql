-- source 
create table user_log (
                mid_id VARCHAR, 
                uid VARCHAR, 
                version_code VARCHAR, 
                version_name VARCHAR, 
                lang VARCHAR, 
                source VARCHAR, 
                os VARCHAR, 
                area VARCHAR, 
                model VARCHAR, 
                brand VARCHAR, 
                sdk_version VARCHAR, 
                gmail VARCHAR, 
                height_width VARCHAR, 
                app_time VARCHAR, 
                network VARCHAR, 
                lng VARCHAR, 
                lat VARCHAR, 
                entry VARCHAR, 
                open_ad_type VARCHAR, 
                action VARCHAR, 
                t bigint, 
                detail VARCHAR, 
                extend1 VARCHAR 
                ) WITH ( 
                    'connector.type' = 'kafka', 
                    'connector.version' = 'universal', 
                    'connector.topic' = 'topic_start', 
                    'connector.startup-mode' = 'earliest-offset', 
                    'connector.properties.zookeeper.connect' = 'hdp1:2181,hdp1:2182,hdp3:2181', 
                    'connector.properties.bootstrap.servers' = 'hdp1:9092,hdp2:9092,hdp3:9092',  
                    'update-mode' = 'append', 
                    'format.type' = 'json', 
                    'format.derive-schema' = 'true' 
                ); 
                 
                -- sink 
                CREATE TABLE pvuv_sink ( 
                                           dt VARCHAR, 
                                           pv BIGINT, 
                                           uv BIGINT 
                ) WITH ( 
                    'connector.type' = 'jdbc', 
                    'connector.url' = 'jdbc:mysql://hdp3:3306/test', 
                    'connector.table' = 'pvuv_sink', 
                    'connector.username' = 'root', 
                    'connector.password' = 'root', 
                    'connector.write.flush.max-rows' = '1' 
                ); 
                 
                 
                INSERT INTO pvuv_sink 
                SELECT 
                    FROM_UNIXTIME(t/1000, 'yyyy-MM-dd') dt, 
                    COUNT(*) AS pv, 
                    COUNT(DISTINCT uid) AS uv 
                FROM user_log 
                GROUP BY FROM_UNIXTIME(t/1000, 'yyyy-MM-dd');