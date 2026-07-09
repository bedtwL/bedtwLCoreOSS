// From me.bedtwL.bedtwLServerCore.commands.world.WorldCommand

package me.bedtwL.oss.listeners.commands;

import me.bedtwL.oss.CoreOSS;
import me.bedtwL.oss.utils.bedtwLCommand;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WorldCommand implements bedtwLCommand {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!commandSender.hasPermission("bedtwl.cmd.world")) {
            commandSender.sendMessage(ChatColor.RED + "You don't have permission!");
            return true;
        }

        if (args.length == 0) {
            commandSender.sendMessage(ChatColor.RED + "Usage: /world <go|unload> [world-name] [environment] [generator]");
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "go":
                handleGoCommand(commandSender, args);
                break;

            case "unload":
                handleUnloadCommand(commandSender, args);
                break;

            default:
                commandSender.sendMessage(ChatColor.RED + "Invalid subcommand! Use: /world <go|unload>");
                break;
        }

        return true;
    }

    private void handleGoCommand(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /world go <world-name> [environment] [generator]");
            return;
        }

        String worldName = args[1];
        World.Environment environment;
        String generator;

        if (args.length > 2) {
            switch (args[2].toLowerCase()) {
                case "overworld":
                    environment = World.Environment.NORMAL;
                    break;
                case "nether":
                    environment = World.Environment.NETHER;
                    break;
                case "the_end":
                    environment = World.Environment.THE_END;
                    break;
                default:
                    sender.sendMessage(ChatColor.RED + "Invalid environment type! Use: overworld, nether, or the_end.");
                    return;
            }
        } else {
            environment = World.Environment.NORMAL;
        }

        if (args.length > 3) {
            generator = args[3].toLowerCase();
            if (!generator.equals("default") && !generator.equals("void")) {
                sender.sendMessage(ChatColor.RED + "Invalid generator type! Use: default or void.");
                return;
            }
        } else {
            generator = "default";
        }

        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            sender.sendMessage("§eTeleporting you to §6" + worldName);
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.teleport(world.getSpawnLocation());
            } else {
                sender.sendMessage(ChatColor.RED + "Only players can be teleported!");
            }
            return;
        }

        sender.sendMessage("§eCreating world §6" + worldName + " §easynchronously...");
        Bukkit.getScheduler().runTask(CoreOSS.getInstance(), () -> {
            try {
                WorldCreator worldCreator = new WorldCreator(worldName).environment(environment);

                if ("void".equalsIgnoreCase(generator)) worldCreator.generator(new ChunkGenerator() {
                    @Override
                    public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
                        return createChunkData(world);
                    }
                });
                World createdWorld = Bukkit.createWorld(worldCreator);
                if (createdWorld == null) {
                    sender.sendMessage(ChatColor.RED + "Failed to create the world!");
                    return;
                }
                Bukkit.getScheduler().runTask(CoreOSS.getInstance(), () -> {
                    sender.sendMessage("§eTeleporting you to the newly created world §6" + worldName);
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        player.teleport(createdWorld.getSpawnLocation());
                    }
                    addWorldToAutoLoad(worldName);
                });

            } catch (Exception ex) {
                sender.sendMessage(ChatColor.RED + "Error while creating the world: " + ex.getMessage());
            }
        });
    }
    private void handleUnloadCommand(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /world unload <world-name>");
            return;
        }
        String worldName = args[1];
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            sender.sendMessage(ChatColor.RED + "World '" + worldName + "' does not exist or is not loaded.");
            return;
        }
        if (Bukkit.unloadWorld(world, true)) {
            sender.sendMessage(ChatColor.GREEN + "World '" + worldName + "' has been unloaded successfully.");
            removeWorldFromAutoLoad(worldName);
        } else {
            sender.sendMessage(ChatColor.RED + "Failed to unload world '" + worldName + "'.");
        }
    }
    private void addWorldToAutoLoad(String worldName) {
        List<String> autoLoadWorlds = CoreOSS.getInstance().getConfig().getStringList("worlds.auto-load");
        if (!autoLoadWorlds.contains(worldName)) {
            autoLoadWorlds.add(worldName);
            CoreOSS.getInstance().getConfig().set("worlds.auto-load", autoLoadWorlds);
            CoreOSS.getInstance().saveConfig();
        }
    }

    private void removeWorldFromAutoLoad(String worldName) {
        List<String> autoLoadWorlds = CoreOSS.getInstance().getConfig().getStringList("worlds.auto-load");
        if (autoLoadWorlds.contains(worldName)) {
            autoLoadWorlds.remove(worldName);
            CoreOSS.getInstance().getConfig().set("worlds.auto-load", autoLoadWorlds);
            CoreOSS.getInstance().saveConfig();
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            suggestions.addAll(Arrays.asList("go", "unload"));
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("go") || args[0].equalsIgnoreCase("unload")) {
                for (World world : Bukkit.getWorlds()) {
                    suggestions.add(world.getName());
                }
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("go")) {
            suggestions.addAll(Arrays.asList("overworld", "nether", "the_end"));
        } else if (args.length == 4 && args[0].equalsIgnoreCase("go")) {
            suggestions.addAll(Arrays.asList("default", "void"));
        }

        return suggestions;
    }
}
