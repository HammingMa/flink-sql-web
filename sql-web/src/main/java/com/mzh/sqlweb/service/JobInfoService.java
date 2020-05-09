package com.mzh.sqlweb.service;

public interface JobInfoService {
    void addJobInfo(String jobName,String sqls);
    void runJob(String jobName);
}
