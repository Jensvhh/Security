package me.siaco.security.frontend.manager;

/**
 * Created by Siaco
 */

import com.doctordark.base.collect.GuavaCompat;
import me.siaco.security.Security;
import me.siaco.security.utils.Config;

import org.bukkit.configuration.MemorySection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.*;

public class ProfileManager implements Listener {
    private final Map<UUID, Profile> users = new HashMap<>();
    private Config userConfig;
    private final Security plugin;

    public ProfileManager(Security plugin) {
        this.plugin = plugin;
        this.reloadUserData();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!event.getPlayer().hasPermission("staff")) return;
        UUID uuid = event.getPlayer().getUniqueId();
        users.putIfAbsent(uuid, new Profile(uuid));
    }

    /**
     * Gets a map of {@link Profile} this manager holds.
     *
     * @return map of user UUID strings to their corresponding {@link Profile}.
     */
    public Map<UUID, Profile> getUsers() {
        return users;
    }

    /**
     * Gets a {@link Profile} by their {@link UUID} asynchronously.
     *
     * @param uuid the {@link UUID} to get from
     * @return the {@link Profile} with the {@link UUID}
     */
    public Profile getUserAsync(UUID uuid) {
        synchronized (users) {
            Profile revert;
            Profile user = users.putIfAbsent(uuid, revert = new Profile(uuid));
            return GuavaCompat.firstNonNull(user, revert);
        }
    }

    /**
     * Gets a {@link Profile} by their {@link UUID}.
     *
     * @param uuid the {@link UUID} to get from
     * @return the {@link Profile} with the {@link UUID}
     */
    public Profile getUser(UUID uuid) {
        Profile revert;
        Profile user = users.putIfAbsent(uuid, revert = new Profile(uuid));
        return GuavaCompat.firstNonNull(user, revert);
    }

    /**
     * Loads the user data from storage.
     */
    public void reloadUserData() {
        userConfig = new Config(plugin, "faction-users");

        Object object = userConfig.get("users");
        if (object instanceof MemorySection) {
            MemorySection section = (MemorySection) object;
            Collection<String> keys = section.getKeys(false);
            for (String id : keys) {
                users.put(UUID.fromString(id), (Profile) userConfig.get(section.getCurrentPath() + '.' + id));
            }
        }
    }

    /**
     * Saves the user data to storage.
     */
    public void saveUserData() {
        Set<Map.Entry<UUID, Profile>> entrySet = users.entrySet();
        Map<String, Profile> saveMap = new LinkedHashMap<>(entrySet.size());
        for (Map.Entry<UUID, Profile> entry : entrySet) {
            saveMap.put(entry.getKey().toString(), entry.getValue());
        }

        userConfig.set("users", saveMap);
        userConfig.save();
    }
}