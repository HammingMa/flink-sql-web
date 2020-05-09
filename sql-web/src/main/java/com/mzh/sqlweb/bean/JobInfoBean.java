package com.mzh.sqlweb.bean;

public class JobInfoBean {
    private int id;
    private String jobName;
    private String sqls;

    public JobInfoBean(int id, String jobName, String sqls) {
        this.id = id;
        this.jobName = jobName;
        this.sqls = sqls;
    }

    public JobInfoBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getSqls() {
        return sqls;
    }

    public void setSqls(String sqls) {
        this.sqls = sqls;
    }
}
