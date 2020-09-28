package me.siaco.security.frontend.listeners;

import lombok.RequiredArgsConstructor;
import me.siaco.security.Security;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by Siaco
 */

@RequiredArgsConstructor
public class Listeners implements Listener {

    private final Security plugin;

    private static final String CHECK_PERMISSION = "security.check";

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        if (p.hasPermission(CHECK_PERMISSION) || p.isOp()){
            if (plugin.getProfileManager().getUser(p.getUniqueId()).getPassword() == null){
                p.sendMessage(StringUtils.repeat(ChatColor.RED + ChatColor.BOLD.toString() + "Please set your password. /setpassword <password>", 5));
                plugin.getFreezeManager().freezePlayer(p, false);
                return;
            }
            if (!plugin.getProfileManager().getUser(p.getUniqueId()).getAuthorizedIPs().contains(p.getAddress().getHostName().replace("/", ""))){
                plugin.getFreezeManager().freezePlayer(p, true);
            } else {
                plugin.getFreezeManager().freezePlayer(p, false);
            }
        }
    }
}
