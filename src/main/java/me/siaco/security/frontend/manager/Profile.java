package me.siaco.security.frontend.manager;

import com.google.common.collect.Lists;
import com.google.common.net.InetAddresses;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;


import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Siaco
 */
@Getter
@Setter
public class Profile implements ConfigurationSerializable {

    private UUID uuid;
    private final List<String> authorizedIPs = Lists.newArrayList();
    private String name;
    private boolean authorized;
    private String password;

    public Profile(UUID uuid) {
        this.uuid = uuid;
    }

    public void addAuthenticatedIP(String address) {
        com.google.common.base.Preconditions.checkNotNull(address, "Cannot log null address");
        if(!authorizedIPs.contains(address)) {
            com.google.common.base.Preconditions.checkArgument(InetAddresses.isInetAddress(address), "Not an Inet address");
            authorizedIPs.add(address);
        }
    }

    @Override
    public Map<String, Object> serialize() {
        return null;
    }
}
