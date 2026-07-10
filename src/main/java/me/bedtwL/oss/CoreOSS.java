package me.bedtwL.oss;

import lombok.Getter;
import me.bedtwL.oss.listeners.commands.WorldCommand;
import me.bedtwL.oss.listeners.commands.lobbyCommand;
import me.bedtwL.oss.listeners.player.ChatFormatter;
import me.bedtwL.oss.utils.LuckpermsUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class CoreOSS extends JavaPlugin {
    @Getter
    private static CoreOSS instance;
    @Override
    public void onEnable() {
        instance=this;
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        if (Bukkit.getPluginManager().getPlugin("LuckPerms")!=null)
            LuckpermsUtils.init();
        regListener(new ChatFormatter());
        new WorldCommand().register(Bukkit.getPluginCommand("world"));
        Bukkit.getPluginCommand("lobby").setExecutor(new lobbyCommand());
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
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
