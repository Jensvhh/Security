package me.siaco.security.utils;

import com.google.common.collect.Lists;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.minecraft.util.com.google.common.collect.Iterables;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.List;

/**
 * Created by Siaco
 */
public class Utils {

    private static final String BUNGEE_CHANNEL_NAME = "BungeeCord";

    public static void kickFromNetwork(Player player, String reason, CommandSender sender, JavaPlugin plugin) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("KickPlayer");
        out.writeUTF(player.getName());
        out.writeUTF(reason);
        player.kickPlayer("Could not authenticate.");
        Player messenger = sender instanceof Player ? ((Player) sender) : Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        if (messenger != null) messenger.sendPluginMessage(plugin, BUNGEE_CHANNEL_NAME, out.toByteArray());
    }

    public static <E> List<E> createList(Object object, Class<E> type) {
        List<E> output = Lists.newArrayList();
        if(object != null && object instanceof List) {
            List<?> input = (List)object;

            for (Object value : input) {
                if (value != null && value.getClass() != null) {
                    if (!type.isAssignableFrom(value.getClass())) {
                        String simpleName = type.getSimpleName();
                        throw new AssertionError("Cannot cast to list! Key " + value + " is not a " + simpleName);
                    }

                    E e = type.cast(value);
                    output.add(e);
                }
            }
        }

        return output;
    }
}
