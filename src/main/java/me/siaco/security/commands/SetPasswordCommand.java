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
public class SetPasswordCommand {

    private final Security plugin;

    private static final String CHECK_PERMISSION = "security.check";

    @Command(name = "setpassword", description = "Command used by staff to set their password", permission = CHECK_PERMISSION, usage = "/setpassword <password>", inGameOnly = true)
    public void onCommand(CommandArgs args){
        Player sender = args.getPlayer();
        if (args.length() == 0){
            sender.sendMessage(ChatColor.RED + "Usage: /setpassword <password>");
            sender.sendMessage(ChatColor.RED + "For parameter 'password': String was expected but not found.");
            return;
        }
        if (args.length() == 1){
            String newPassword = args.getArgs(0);
            plugin.getProfileManager().getUser(sender.getUniqueId()).setPassword(newPassword);
            sender.sendMessage(ChatColor.GREEN + "You have successfully set your password.");
        }
    }
}
