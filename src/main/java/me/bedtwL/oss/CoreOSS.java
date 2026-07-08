package me.bedtwL.oss;

import me.bedtwL.oss.listeners.player.ChatFormatter;
import me.bedtwL.oss.utils.LuckpermsUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class CoreOSS extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        if (Bukkit.getPluginManager().getPlugin("LuckPerms")!=null)
            LuckpermsUtils.init();
        regListener(new ChatFormatter());
    }
    public void regListener(Listener... listener)
    {
        for (Listener l:listener)
            getServer().getPluginManager().registerEvents(l,this);
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
