// From me.bedtwL.bedtwLServerCore.commands.admin.troll.inv
package me.bedtwL.oss.listeners.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InventoryCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!commandSender.hasPermission("bedtwL.cmd.inv")) {
            commandSender.sendMessage("§cYou dont have permission for this!");
            return true;
        }
        if (args.length > 0) {
            Player p = (Player) commandSender;
            p.openInventory(Bukkit.getPlayer(args[0]).getInventory());
        } else {
            commandSender.sendMessage("§cUsage: /inv <player>");
        }
        return true;
    }
}
