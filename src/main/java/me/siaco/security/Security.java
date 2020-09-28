package me.siaco.security;

import lombok.Getter;
import me.siaco.security.commands.*;
import me.siaco.security.frontend.listeners.Listeners;
import me.siaco.security.frontend.manager.FreezeManager;
import me.siaco.security.frontend.manager.Profile;
import me.siaco.security.frontend.manager.ProfileManager;
import me.siaco.security.utils.framework.CommandFramework;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Siaco
 */
public class Security extends JavaPlugin {

    @Getter
    private static Security instance;

    @Getter
    private CommandFramework framework;

    @Getter
    private FreezeManager freezeManager;

    @Getter
    private ProfileManager profileManager;

    @Override
    public void onEnable() {
        instance = this;
        profileManager = new ProfileManager(this);
        ConfigurationSerialization.registerClass(Profile.class);
        framework = new CommandFramework(this);
        freezeManager = new FreezeManager(this);
        registerCommands();
        registerListeners();
    }

    private void registerListeners() {
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new Listeners(this), this);
    }

    private void registerCommands() {
        framework.registerCommands(new ForceSetPassword(this));
        framework.registerCommands(new LoginCommand(this));
        framework.registerCommands(new SetPasswordCommand(this));
        framework.registerCommands(new TestGUICommand(this));
        framework.registerCommands(new AuthenticateIPCommand(this));
    }

    @Override
    public void onDisable() {
        freezeManager.frozenPlayers.clear();
        //Cannot schedule a new task onDisable.
        profileManager.saveUserData();
        instance = null;
    }
}
