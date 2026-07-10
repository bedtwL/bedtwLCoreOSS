// From me.bedtwL.bedtwLServerCore.utils.BungeeUtils
package me.bedtwL.oss.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.bedtwL.oss.CoreOSS;
import org.bukkit.entity.Player;

public class BungeeUtils {
    public static void sendPlayerToServer(Player p, String server)
    {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        p.sendPluginMessage(CoreOSS.getInstance(), "BungeeCord", out.toByteArray());
    }
}
