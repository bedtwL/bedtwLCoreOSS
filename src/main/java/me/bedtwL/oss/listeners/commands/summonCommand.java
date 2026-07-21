// From me.bedtwL.bedtwLServerCore.nms.v1_21_R01.commands.minecraft.summonCommand
package me.bedtwL.oss.listeners.commands;

import me.bedtwL.oss.utils.bedtwLCommand;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class summonCommand implements bedtwLCommand {
    private final Random random = new Random();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("summon")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Only players can use this command!");
                return true;
            }

            Player player = (Player) sender;

            if (!player.hasPermission("bedtwl.cmd.summon")) {
                player.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }

            if (args.length < 1) {
                player.sendMessage(ChatColor.YELLOW + "Usage: /summon <entity> [count] [radius] [x] [y] [z]");
                return true;
            }

            EntityType entityType;
            try {
                entityType = EntityType.valueOf(args[0].toUpperCase());
            } catch (IllegalArgumentException e) {
                player.sendMessage(ChatColor.RED + "Invalid entity type!");
                return true;
            }

            Location baseLocation = player.getLocation();
            int count = 1;
            double radius = 0;
            boolean enableAI = true;

            if (args.length >= 2) {
                try {
                    count = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "Invalid count! Using default of 1.");
                    return true;
                }
            }

            if (args.length >= 3) {
                try {
                    radius = Double.parseDouble(args[2]);
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "Invalid radius! Using default of 0.");
                    return true;
                }
            }


            if (args.length >= 6) {
                try {
                    double x = args[3].equals("~") ? baseLocation.getX() : Double.parseDouble(args[4]);
                    double y = args[4].equals("~") ? baseLocation.getY() : Double.parseDouble(args[5]);
                    double z = args[5].equals("~") ? baseLocation.getZ() : Double.parseDouble(args[6]);
                    baseLocation = new Location(player.getWorld(), x, y, z);
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "Invalid coordinates! Using your current location.");
                }
            }

            for (int i = 0; i < count; i++) {
                Location spawnLocation = getSpawnLocation(baseLocation, radius);
                player.getWorld().spawnEntity(spawnLocation, entityType);
                /*
                if (entity instanceof Mob) {
                    ((Mob) entity).setAI(enableAI);
                }*/
            }
            player.sendMessage(ChatColor.YELLOW + "Summoned " + ChatColor.GOLD + "x" + count + " of " + entityType.name() +
                    ChatColor.YELLOW + " within a radius of " + radius + "!");
            return true;
        }
        return false;
    }

    private Location getSpawnLocation(Location baseLocation, double radius) {
        if (radius == 0) {
            return baseLocation;
        }

        double angle = random.nextDouble() * 2 * Math.PI;
        double r = random.nextDouble() * radius;
        double x = r * Math.cos(angle);
        double z = r * Math.sin(angle);

        return baseLocation.clone().add(x, 0, z);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            String currentInput = args[0].toLowerCase();
            for (EntityType type : EntityType.values()) {
                if (type.isSpawnable() && type.name().toLowerCase().startsWith(currentInput)) {
                    completions.add(type.name().toLowerCase());
                }
            }
        } else if (args.length == 2) {
            completions.add("<count>");
        } else if (args.length == 3) {
            completions.add("<radius>");
        } else if (args.length >= 4 && args.length <= 6) {
            completions.add("~");
        }

        return completions;
    }
}