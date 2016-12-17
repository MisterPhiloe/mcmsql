package com.morecommunityminecraft.mcmsql;


import com.sun.istack.internal.Nullable;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class MySQL {

    private HikariDataSource ds = null;

    public MySQL(String host, String port, String username, String password, String database) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        config.setUsername(username);
        config.setPassword(password);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        this.ds = new HikariDataSource(config);
    }

    private Connection getConnection() {
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void closeConnection() {
        try {
            if (getConnection() != null)
                getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void createTable(String tableName, String[] cN, String[] cT, @Nullable String addition) {
        if (cN.length != cT.length) return;
        PreparedStatement ps = null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cN.length; i++) {
            sb.append(cN[i]).append(" " + cT[i]).append(", ");
        }
        String query = "CREATE TABLE IF NOT EXISTS " + tableName + "(" + sb.toString() + addition + ")";
        //Main.getInstance().getLogger().info(query);
        try {
            if (getConnection() != null) {
                ps = getConnection().prepareStatement(query);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void insertObject(String tableName, String[] fields, String[] values, @Nullable String addition) {
        if (fields.length != values.length) return;
        if (addition == null) {
            addition = "";
        }
        PreparedStatement ps = null;
        StringBuilder sb1 = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            sb1.append(fields[i]);
            if (i + 1 < fields.length)
                sb1.append(", ");
        }

        StringBuilder sb2 = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            sb2.append("'" + values[i] + "'");
            if (i + 1 < values.length)
                sb2.append(", ");
        }
        String query = "INSERT IGNORE INTO " + tableName + " ( " + sb1.toString() + ") VALUES ( " + sb2.toString() + " ) " + addition;
        //Main.getInstance().getLogger().info(query);
        try {
            if (getConnection() != null) {
                ps = getConnection().prepareStatement(query);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateObject(String tableName, String[] fields, String[] values, String[] where) {
        if (fields.length != values.length) return;
        PreparedStatement ps = null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            sb.append(fields[i]).append(" = ").append(values[i]);
            if (i + 1 < fields.length)
                sb.append(", ");
        }
        String query = "UPDATE " + tableName + " SET " + sb.toString() + " WHERE " + where[0] + "=" + where[1];
        try {
            if (getConnection() != null) {
                ps = getConnection().prepareStatement(query);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public String[] getObject(String tableName, String[] fields, @Nullable String[] where) {
        String addition;
        PreparedStatement ps = null;
        ResultSet rs = null;
        if (where == null) {
            addition = "";
        }

        StringBuilder wheresb = new StringBuilder();
        if (where != null) {
            for (int i = 0; i < (where.length / 2); i += 2) {
                wheresb.append("WHERE " + where[i]).append("=" + where[i + 1]);
                if (i >= 2) {
                    wheresb.append(", ");
                }
            }
        }

        StringBuilder columns = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            columns.append(fields[i]);
            if (i + 1 < fields.length)
                columns.append(", ");
        }
        String query = "SELECT " + columns.toString() + " FROM " + tableName + " " + wheresb.toString();
        String[] stray = new String[fields.length];
        int count = 0;
        try {
            if (getConnection() != null) {
                ps = getConnection().prepareStatement(query);
                rs = ps.executeQuery();
                while (rs.next()) {
                    stray[count] = rs.getString(fields[count]);
                    count++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Arrays.toString(stray));
        return stray;
    }

    public void removeObject(String tableName, String[] fields, String[] values) {
        if (fields.length != values.length) return;
        PreparedStatement ps = null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            sb.append(fields[i]).append("=").append(values[i]);
            if (i + 1 < fields.length)
                sb.append(", ");
        }

        String query = "DELETE FROM " + tableName + " WHERE " + sb.toString();
        try {
            if (getConnection() != null) {
                ps = getConnection().prepareStatement(query);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
