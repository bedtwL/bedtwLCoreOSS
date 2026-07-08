package me.bedtwL.oss.utils;

import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class LuckpermsUtils {
    @Getter
    private static LuckPerms luckPerms;
    public static void init() {
        luckPerms = LuckPermsProvider.get();
    }
    private static CompletableFuture<User> getUser(UUID uuid) {
        return luckPerms.getUserManager().loadUser(uuid);
    }
    public static ChatColor getPrefixColor(Player player) {
        User user = getLuckPerms().getUserManager().getUser(player.getUniqueId());
        if (user == null) {
            return ChatColor.GRAY;
        }

        String prefix=user.getCachedData().getMetaData().getPrefix();
        if (prefix==null)
            return ChatColor.GRAY;
        return extractColorFromPrefix(prefix);
    }
    public static String getSuffix(Player player) {
        User user = getLuckPerms().getUserManager().getUser(player.getUniqueId());
        if (user == null) {
            return "";
        }
        return user.getCachedData().getMetaData().getSuffix();
    }
    private static ChatColor extractColorFromPrefix(String prefix) {
        if (prefix.length() >= 2 && prefix.charAt(0) == '§') {
            char colorCode = prefix.charAt(1);
            return ChatColor.getByChar(colorCode);
        }
        return ChatColor.GRAY;
    }
}
