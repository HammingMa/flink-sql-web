package com.mzh.sqlweb.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface JobInfoMapper {
     void addJobInfo(String jobName,String sqls);
}
