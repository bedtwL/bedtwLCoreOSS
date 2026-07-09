// From me.bedtwL.bedtwLServerCore.utils.command.bedtwLCommand
package me.bedtwL.oss.utils;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;

public interface bedtwLCommand extends CommandExecutor, TabExecutor {
    default void register(PluginCommand command) {
        command.setExecutor(this);
        command.setTabCompleter(this);
    }
}
