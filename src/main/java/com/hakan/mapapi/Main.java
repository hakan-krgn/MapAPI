package com.hakan.mapapi;

import com.hakan.mapapi.utils.nms.SetupNMS;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Plugin instance;

    public static Plugin getInstance() {
        return instance;
    }

    public static void setInstance(Plugin plugin) {
        new SetupNMS().setup();
        instance = plugin;
    }

    @Override
    public void onEnable() {
        setInstance(this);
    }
}