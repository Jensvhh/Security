package me.siaco.security.commands;

import lombok.RequiredArgsConstructor;
import me.siaco.security.Security;
import me.siaco.security.utils.framework.Command;
import me.siaco.security.utils.framework.CommandArgs;

/**
 * Created by Siaco
 */
@RequiredArgsConstructor
public class TestGUICommand {

    private final Security plugin;

    @Command(name = "testgui", inGameOnly = true, isOperatorOnly = true)
    public void onCommand(CommandArgs args) {
        if (plugin.getFreezeManager().isFrozen(args.getPlayer())) {
            plugin.getFreezeManager().unFreeze(args.getPlayer());
        } else {
            plugin.getFreezeManager().freezePlayer(args.getPlayer(), true);
        }
    }
}
