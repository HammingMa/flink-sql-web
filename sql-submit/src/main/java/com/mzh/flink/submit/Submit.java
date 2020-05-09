package com.mzh.flink.submit;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;


public class Submit {


    public static String getSqls(String jobName) {
        String url = "jdbc:mysql://hdp3:3306/test";
        String userName = "root";
        String password = "root";

        Connection conn = null;
        Statement stmt = null;
        String sqls = null;
        try {

            //1.加载驱动程序
            Class.forName("com.mysql.jdbc.Driver");
            //2. 获得数据库连接
            conn = DriverManager.getConnection(url, userName, password);
            //3.操作数据库，实现增删改查
            stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("select sqls from job_info where job_name='" + jobName + "'");
            //如果有数据，rs.next()返回true

            rs.next();
            sqls = rs.getString("sqls");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return sqls;
    }

    private String sqls;
    private String jobName;

    public Submit(String jobName,String sqls) {
        this.sqls = sqls;
        this.jobName=jobName;
    }

    public void run() throws Exception {

        //获取环境
        EnvironmentSettings settings = EnvironmentSettings.newInstance().
                useBlinkPlanner().
                inStreamingMode().
                build();



        TableEnvironment tEnv = TableEnvironment.create(settings);

        List<SQLCommand> sqlCommands = splitSqls();


        // 执行sql 语句
        for (SQLCommand sqlCommand : sqlCommands) {
            switch (sqlCommand.getSqlType()) {
                case SET:
                    //设置参数
                    String[] sets = sqlCommand.getSql().split("=");
                    String key = sets[0];
                    String value = sets[1];
                    tEnv.getConfig().getConfiguration().setString(key, value);
                    break;
                case CREATE_TABLE:
                    tEnv.sqlUpdate(sqlCommand.getSql());
                    break;
                case CREATE_VIEW:
                    tEnv.sqlUpdate(sqlCommand.getSql());
                    break;
                case INSERT_INTO:
                    tEnv.sqlUpdate(sqlCommand.getSql());
                    break;
            }

        }

        tEnv.execute(jobName);
    }

    // 分割sql语句
    public List<SQLCommand> splitSqls() {
        List<SQLCommand> sqlList = new ArrayList<>();

        String[] strArray = sqls.split("\n");

        StringBuilder sqlBudiler = new StringBuilder();

        for (String s : strArray) {
            String sTrim = s.trim();
            if (!sTrim.isEmpty() && !sTrim.startsWith("--")) {
                sqlBudiler.append(s);
                sqlBudiler.append("\n");
                if (sTrim.endsWith(";")) {
                    SQLCommand sqlCommand = getSQLCommand(sqlBudiler);
                    sqlList.add(sqlCommand);
                    sqlBudiler.setLength(0);

                }
            }


        }


        System.out.println(sqlList.size());
        return sqlList;
    }

    // 创建sqlCommand 对象
    private SQLCommand getSQLCommand(StringBuilder sqlBudiler) {
        String sql = sqlBudiler.substring(0, sqlBudiler.length() - 2).trim();
        // 确定sql 语句的类型
        for (SQLType type : SQLType.values()) {

            Matcher matcher = type.pattern.matcher(sql.toLowerCase());

            if (matcher.matches()) {
                System.out.println(sql.replaceAll(";",""));
                //创建 提价sql对象
                return new SQLCommand(type, sql.replaceAll(";",""));
            }
        }

        return null;
    }

    public static void main(String[] args) throws Exception {

        String jobName = null;
        try {
            ParameterTool paras = ParameterTool.fromArgs(args);
            jobName = paras.get("job-name");
        } catch (Exception e) {
            System.err.println("没有作业名");
            return;
        }

        String sqls = getSqls(jobName);

        //System.out.println(sqls);
        Submit submit = new Submit(jobName,sqls);
        submit.run();

    }
}
