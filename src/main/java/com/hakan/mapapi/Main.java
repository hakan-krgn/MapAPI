package com.hakan.mapapi;

import com.hakan.mapapi.utils.nms.SetupNMS;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Plugin instance;

    public static Plugin getInstance() {
        return instance;
    }

    public static void setInstance(Plugin plugin) {
        instance = plugin;
        if (instance != null) {
            Bukkit.getLogger().warning("MapAPI already registered.");
            return;
        }
        new SetupNMS().setup();
    }

    @Override
    public void onEnable() {
        setInstance(this);
    }
}