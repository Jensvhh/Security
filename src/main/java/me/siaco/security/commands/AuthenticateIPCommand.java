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
public class AuthenticateIPCommand {

    private final Security plugin;

    @Command(name = "authip", permission = "security.authip", description = "Command used to authenticate IP's into the system.")
    public void onCommand(CommandArgs args){
        if (!args.getSender().isOp() || !args.getSender().hasPermission("security.authip")){
            args.getSender().sendMessage(ChatColor.RED + "No permission.");
            return;
        }
        CommandSender sender = args.getSender();
        if (args.length() == 0 || args.length() > 1){
            sender.sendMessage(ChatColor.RED + "Usage: /authip <playerName>");
            sender.sendMessage(ChatColor.RED + "Too " + (args.length() > 1 ? "many" : "few") + " arguments! Expected 'playerName' but got nothing.");
            return;
        }
        Player target = Bukkit.getPlayer(args.getArgs(0));
        plugin.getProfileManager().getUser(target.getUniqueId()).addAuthenticatedIP(target.getAddress().getAddress().getHostAddress());
        sender.sendMessage(ChatColor.GREEN + "Successfully authenticated the IP of the player " + target.getName() + '.');
        if (plugin.getFreezeManager().isFrozen(target)){
            plugin.getFreezeManager().unFreeze(target);
        }
    }

}
