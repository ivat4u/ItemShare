package example.com.itemshare;


import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final  class DBUtils {
    private static final  String TAG = "DBUtils";


    // 查询
    public  final static ResultSet executeQuery(String sql,Statement stmt) {
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public final static boolean login_rep(Statement sql, String logSql) {
        // 获取Sql查询语句

        // 操作DB对象
        try {
            ResultSet rs = sql.executeQuery(logSql);
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //增删查改
    public final static int executeUpdate(String sql,Statement stmt) {
        int ret = 0;
        try {
            ret = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
}
}