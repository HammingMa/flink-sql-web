package com.mzh.flink.submit;

import java.util.regex.Pattern;

public enum SQLType {

    INSERT_INTO("(insert\\s+into.*)"),
    CREATE_TABLE("(create\\s+table.*)"),
    CREATE_VIEW("(create\\s+view.*)"),
    SET("set(\\s+(\\S+)\\s*=(.*))?");

    public final Pattern pattern;

    SQLType(String pattern) {
        this.pattern = Pattern.compile(pattern,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    }


}
