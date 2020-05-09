package com.mzh.sqlweb.service.impl;

import com.mzh.sqlweb.mapper.JobInfoMapper;
import com.mzh.sqlweb.service.JobInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.InputStreamReader;
import java.io.LineNumberReader;


@Service(value = "jobInfoService")
public class JobInfoServiceImpl implements JobInfoService {
    @Autowired
    private JobInfoMapper jobInfoMapper;

    @Override
    public void addJobInfo(String jobName, String sqls) {
        jobInfoMapper.addJobInfo(jobName, sqls);
    }

    @Override
    public void runJob(String jobName) {
        String finkHome = "/opt/flink-1.10.0";

        String cmd = finkHome + "/bin/flink run -c com.mzh.flink.submit.Submit -jar " + finkHome + "/flink-sql-web/sql-submit-1.0-SNAPSHOT.jar  --job-name " + jobName;

        try {
            Process exec = Runtime.getRuntime().exec(cmd);
            System.out.println(cmd);
            int code = exec.waitFor();
            System.out.println(code);


            InputStreamReader ir = new InputStreamReader(exec.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line;
            exec.waitFor();

            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
