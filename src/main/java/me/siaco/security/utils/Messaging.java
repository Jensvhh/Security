package me.siaco.security.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * Created by Siaco
 */
@UtilityClass
public class Messaging {

    public void sendDebug(String msg){
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        Bukkit.getOnlinePlayers().stream().filter(o -> o.hasPermission("mint.debug")).forEach(o -> o.sendMessage(ChatColor.translateAlternateColorCodes('&', msg)));
    }

    public void sendConsoleMessage(String message){
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
}
