package com.morecommunityminecraft.mcmsql;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class Main extends JavaPlugin {

    private static Main main;
    private String[] dbKeys = new String[5];
    private MySQL sql = null;

    public static Main getMain() {
        return main;
    }

    @Override
    public void onEnable() {
        main = this;
        setupConfig();
        this.sql = new MySQL(dbKeys[0], dbKeys[1], dbKeys[2], dbKeys[3], dbKeys[4]);
    }

    @Override
    public void onDisable() {
        getMySQL().closeConnection();
        main = null;
    }

    private void setupConfig() {
        String[] stray = {"hostname", "port", "username", "password", "database"};
        List<String> l = new ArrayList<>();
        File file = new File(getDataFolder(), "database.yml");
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        for (int i = 0; i < stray.length; i++) {
            String s = stray[i];
            if (yaml.getString(s) == null || yaml.getString(s).equalsIgnoreCase("")) {
                yaml.set(s, s);
            } else {
                dbKeys[i] = yaml.getString(s);
            }
        }
        try {
            yaml.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MySQL getMySQL() {
        return this.sql;
    }
}
