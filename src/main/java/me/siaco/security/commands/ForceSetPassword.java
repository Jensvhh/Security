package me.siaco.security.commands;

import lombok.RequiredArgsConstructor;
import me.siaco.security.Security;
import me.siaco.security.utils.framework.Command;
import me.siaco.security.utils.framework.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Siaco
 */
@RequiredArgsConstructor
public class ForceSetPassword {

    private final Security plugin;

    @Command(name = "forcesetpassword", consoleOnly = true)
    public void onCommand(CommandArgs args) {
        CommandSender sender = args.getSender();
        if (args.length() == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /forcesetpassword <user> <password>");
            sender.sendMessage(ChatColor.RED + "For parameter 'password': String was expected but not found.");
            return;
        }
        Player target = Bukkit.getPlayer(args.getArgs(0));
        if (target == null) {
            sender.sendMessage(ChatColor.RED + args.getArgs(0) + " is not a valid player.");
            return;
        }
        if (args.length() == 2) {
            String newPassword = args.getArgs(1);
            plugin.getProfileManager().getUser(target.getUniqueId()).setPassword(newPassword);
            sender.sendMessage("Successfully set the password of " + target.getName() + '.');
        }
    }
}
