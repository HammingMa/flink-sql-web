package com.mzh.flink.submit;

public class SQLCommand {
    private SQLType sqlType;
    private String sql;

    public SQLCommand(SQLType sqlType, String sql) {
        this.sqlType = sqlType;
        this.sql = sql;
    }

    public SQLType getSqlType() {
        return sqlType;
    }

    public void setSqlType(SQLType sqlType) {
        this.sqlType = sqlType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
