package com.morecommunityminecraft.mcmsql;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

    /**
     * @return new connection
     */
    public Connection getConnection() {
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void executeQuery(String query) {
        BukkitRunnable br = new BukkitRunnable() {
            @Override
            public void run() {
                Connection conn = getConnection();
                PreparedStatement ps = null;
                try {
                    ps = conn.prepareStatement(query);
                    ps.executeQuery();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (ps != null) {
                            ps.close();
                        }
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        br.runTaskAsynchronously(Main.getMain());
    }

    public void executeUpdate(String query) {
        BukkitRunnable br = new BukkitRunnable() {
            @Override
            public void run() {
                Connection conn = getConnection();
                PreparedStatement ps = null;
                try {
                    ps = conn.prepareStatement(query);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (ps != null) {
                            ps.close();
                        }
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        br.runTaskAsynchronously(Main.getMain());
    }

}
