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

import java.util.Random;

// From me.bedtwL.bedtwLServerCore.listener.ChatFormatterListener
public class ChatFormatter implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();
        String message = event.getMessage();

        // Check if the message contains @(player)
        message = ParseChat(message, player.hasPermission("bedtwl.emote"));
        LuckPerms api = LuckPermsProvider.get();
        User user = api.getPlayerAdapter(Player.class).getUser(player);
        String surfix = user.getCachedData().getMetaData().getSuffix();
        ChatColor MessageColor = ChatColor.WHITE;
        if (surfix == null) surfix = "";
        else surfix = " " + surfix;
        if (user.getCachedData().getMetaData().getPrefix().equals("§7")) {
            MessageColor = ChatColor.GRAY;
        }
        /* April fool
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
        LocalDate today = LocalDate.now();
        String formattedDate = today.format(formatter);
        if (formattedDate.equals("4/1")) {
            message= replaceRandomCharsWithSpace(message, message.length()/3);
            MessageColor=ChatColor.YELLOW;
        }*/
        event.setMessage(MessageColor + message);
        String prefix=user.getCachedData().getMetaData().getPrefix();
        if (prefix!=null)
            event.setFormat(prefix + " " + player.getDisplayName() + surfix + MessageColor + ": " + message);
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
    public static String ParseChat(String message, boolean emote) {
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

        return ParseEmote(modifiedMessage.substring(1).replace("%", "%%"), emote);
    }

    public static String ParseEmote(String message, boolean emote) {
        String[] words = message.split(" ");
        String modifiedMessage = "";

        for (String word : words) {
            switch (word.toLowerCase()) {
                case "fuck":
                case "dick":
                case "sexy":
                case "nmsl":
                case "shit":
                    word = "****";
                    break;
                case "fucking":
                    word = "*******";
                    break;
                case "nigger":
                    word = "******";
                    break;
                case "bitch":
                    word = "*****";
                    break;
                case "fk":
                    word = "**";
                    break;
                case "sex":
                    word = "***";
                    break;
            }
            //Emote
            if (emote) {
                switch (word.toLowerCase()) {
                    case "<3":
                        word = ChatColor.RED + "❤" + ChatColor.WHITE;
                        break;
                    case "gg":
                        word = ChatColor.GOLD + word + ChatColor.WHITE;
                        break;
                    case "o/":
                        word = ChatColor.LIGHT_PURPLE + "( ﾟ◡ﾟ)/" + ChatColor.WHITE;
                        break;
                    case ":yes:":
                        word = ChatColor.GREEN + "✔" + ChatColor.WHITE;
                        break;
                    case ":no:":
                        word = ChatColor.RED + "✖" + ChatColor.WHITE;
                        break;
                    case ":star:":
                        word = ChatColor.YELLOW + "✮" + ChatColor.WHITE;
                        break;
                    case ":java:":
                        word = "☕";
                        break;
                    case ":arrow:":
                        word = ChatColor.YELLOW + "➜" + ChatColor.WHITE;
                        break;
                    case ":shrug:":
                        word = ChatColor.YELLOW + "¯\\_(ツ)_/¯" + ChatColor.WHITE;
                        break;
                    case ":tableflip:":
                        word = ChatColor.RED + "(╯°□°）╯" + ChatColor.WHITE;
                        break;
                    case ":gimme:":
                        word = ChatColor.AQUA + "༼つ ◕_◕ ༽つ" + ChatColor.WHITE;
                        break;
                    case ":oof:":
                        word = ChatColor.RED + "" + ChatColor.BOLD + "OOF" + ChatColor.WHITE;
                        break;
                    case ":wei:":
                        word = ChatColor.RED + "" + ChatColor.BOLD + "危" + ChatColor.WHITE;
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
