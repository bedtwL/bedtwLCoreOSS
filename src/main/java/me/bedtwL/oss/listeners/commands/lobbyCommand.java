// From me.bedtwL.bedtwLServerCore.commands.bungee.lobby
package me.bedtwL.oss.listeners.commands;

import me.bedtwL.oss.utils.BungeeUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class lobbyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player p = ((Player) commandSender).getPlayer();
            if (Bukkit.getPluginManager().getPlugin("PureFFA")!=null)
                Bukkit.dispatchCommand(p, "ffa quit");
            else
                BungeeUtils.sendPlayerToServer(p, "main");
        }
        return true;
    }
}
