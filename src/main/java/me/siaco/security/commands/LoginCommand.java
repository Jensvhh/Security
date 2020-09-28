package me.siaco.security.commands;

import lombok.RequiredArgsConstructor;
import me.siaco.security.Security;
import me.siaco.security.utils.framework.Command;
import me.siaco.security.utils.framework.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by Siaco
 */
@RequiredArgsConstructor
public class LoginCommand {

    private final Security plugin;

    private static final String CHECK_PERMISSION = "security.check";

    @Command(name = "login", description = "Command used by staff to login with their password", permission = CHECK_PERMISSION, inGameOnly = true)
    public void onCommand(CommandArgs args){
        Player sender = args.getPlayer();
        if (args.length() == 0){
            sender.sendMessage(ChatColor.RED + "Usage: /login <password>");
            sender.sendMessage(ChatColor.RED + "For parameter 'password': String was expected but not found.");
            return;
        }
        if (args.length() == 1){
            String password = args.getArgs(0);
            if (plugin.getProfileManager().getUser(sender.getUniqueId()).getPassword() == null){
                sender.sendMessage(ChatColor.RED + "You do not have a password set please use /setpassword <password>");
                return;
            }
            if (password.equalsIgnoreCase(plugin.getProfileManager().getUser(sender.getUniqueId()).getPassword())){
                plugin.getProfileManager().getUser(sender.getUniqueId()).setAuthorized(true);
                plugin.getFreezeManager().unFreeze(sender);
            } else {
                sender.sendMessage(ChatColor.RED + "Incorrect password! Remember password are CaSe-SeNsItIvE.");
            }
        }
    }
}
