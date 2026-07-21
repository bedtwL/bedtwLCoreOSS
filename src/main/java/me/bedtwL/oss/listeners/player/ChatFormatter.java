package me.bedtwL.oss.listeners.player;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

// From me.bedtwL.bedtwLServerCore.listener.ChatFormatterListener
public class ChatFormatter implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();
        String message = event.getMessage();

        // Check if the message contains @(player)
        message = parseChat(message, player.hasPermission("bedtwl.emote"));
        LuckPerms api = LuckPermsProvider.get();
        User user = api.getPlayerAdapter(Player.class).getUser(player);
        String surfix = user.getCachedData().getMetaData().getSuffix();
        if (surfix == null) surfix = "";
        else surfix = " " + surfix;/*
        if (user.getCachedData().getMetaData().getPrefix().equals("§7")) {
            MessageColor = ChatColor.GRAY;
        }*/
        /* April fool
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
        LocalDate today = LocalDate.now();
        String formattedDate = today.format(formatter);
        if (formattedDate.equals("4/1")) {
            message= replaceRandomCharsWithSpace(message, message.length()/3);
            MessageColor=ChatColor.YELLOW;
        }*/
        event.setMessage("§f"+message);
        String prefix = user.getCachedData().getMetaData().getPrefix();
        if (!prefix.equals("§e"))
            event.setFormat(prefix + " " + player.getDisplayName() + surfix + "§f: " + message);
        else
            event.setFormat("§e" + player.getDisplayName() + surfix + "§f: " + message);
    }

    // Used in April fool before
    private static String replaceRandomCharsWithSpace(String input, int replacements) {
        Random random = new Random();
        char[] chars = input.toCharArray();
        int maxReplacements = Math.min(replacements, chars.length);
        for (int i = 0; i < maxReplacements; i++) {
            int index;
            do {
                index = random.nextInt(chars.length);
            } while (chars[index] == ' ');
            chars[index] = ' ';
        }
        return new String(chars);
    }

    // From me.bedtwL.bedtwLServerCore.utils.StringsUtils
    public static String parseChat(String message, boolean emote) {
        String[] words = message.split(" ");
        String modifiedMessage = "";

        for (String word : words) {
            // Check if the word starts with @(player)
            if (word.startsWith("@") && word.length() > 1) {
                String playerName = word.substring(1); // Extract player name
                // TODO: Nick Feature hooking
                // Player mentionedPlayer = Bukkit.getPlayer(bedtwLServerCoreAPI.getDatabaseUtils().getRealName(playerName));
                Player mentionedPlayer = Bukkit.getPlayer(playerName);
                // Check if the mentioned player is online
                if (mentionedPlayer != null) {
                    // TODO: Vanish Feature hooking
                    // if (!RuntimeDatabase.vanish.getOrDefault(mentionedPlayer.getUniqueId(), false))
                    word = ChatColor.AQUA + "@" + mentionedPlayer.getDisplayName() + ChatColor.WHITE;
                }
            }
            modifiedMessage = modifiedMessage + " " + word;
        }

        return parseEmote(modifiedMessage.substring(1).replace("%", "%%"), emote);
    }

    public static String parseEmote(String message, boolean emote) {
        String[] words = message.split(" ");
        String modifiedMessage = "";

        List<String> wordsToFilter = Arrays.asList("fuck", "dick", "sexy", "nmsl", "shit", "fucking", "nigger", "bitch", "fk", "sex");
        for (String word : words) {
            if(wordsToFilter.contains(word)) {
                StringBuilder sb = new StringBuilder(word.length());
                for (int i = 0; i < word.length(); i++) {
                    sb.append("*");
                }
                word = sb.toString();
            }
            //Emote
            if (emote) {
                switch (word.toLowerCase()) {
                    case "<3":
                        word = "§c❤§f";
                        break;
                    case "gg":
                        word = "§6"+word + "§f";
                        break;
                    case "o/":
                        word = "§d( ﾟ◡ﾟ)/§f";
                        break;
                    case ":yes:":
                        word = "§a✔§f";
                        break;
                    case ":no:":
                        word = "§c✖§f";
                        break;
                    case ":star:":
                        word = "§e✮§f";
                        break;
                    case ":java:":
                        word = "☕";
                        break;
                    case ":arrow:":
                        word = "§e➜§f";
                        break;
                    case ":shrug:":
                        word = "§e¯\\_(ツ)_/¯§f";
                        break;
                    case ":tableflip:":
                        word = "§c(╯°□°）╯§f";
                        break;
                    case ":gimme:":
                        word = "§b༼つ ◕_◕ ༽つ§f";
                        break;
                    case ":oof:":
                        word = "§c§lOOF§f";
                        break;
                    case ":wei:":
                        word = "§c§l危§f";
                        break;
                    case ":ciallo:":
                        word="§bCiallo～§e(∠・ω< )⌒§6☆§f";
                        break;
                    case ":skull:":
                        word = "\uD83D\uDC80";
                        break;
                }
            }
            modifiedMessage = modifiedMessage + " " + word;
        }
        return modifiedMessage.substring(1);
    }
}
