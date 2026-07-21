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
        if (args.length == 0) {
            commandSender.sendMessage("§cUsage: /inv <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            commandSender.sendMessage("§cThe player is not online!");
            return true;
        }

        Player p = (Player) commandSender;
        p.openInventory(target.getInventory());
        return true;
    }
}
