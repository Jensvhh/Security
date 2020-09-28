package me.siaco.security.frontend.manager;

import com.google.common.collect.Sets;
import lombok.Getter;
import me.siaco.security.Security;
import me.siaco.security.utils.ItemBuilder;
import me.siaco.security.utils.Menu;
import me.siaco.security.utils.Messages;
import me.siaco.security.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;

import java.util.HashSet;
import java.util.UUID;

/**
 * Created by Siaco
 */
public class FreezeManager implements Listener {

    private final Security plugin;

    @Getter
    public HashSet<UUID> frozenPlayers = Sets.newHashSet();

    public FreezeManager(Security plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public boolean isFrozen(Player player) {
        return frozenPlayers.contains(player.getUniqueId());
    }

    /**
     * Freezes a player.
     *
     * @param player Player that needs to be frozen.
     * @param IP     Is the freeze requiring IP auth.
     */
    public void freezePlayer(Player player, boolean IP) {
        if (frozenPlayers.contains(player.getUniqueId())) {
        } else {
            frozenPlayers.add(player.getUniqueId());
            sendMessage(player);
            if (IP) {
                sendGUI(player);
            }
        }
    }

    /**
     * Unfreezes a player.
     *
     * @param player The player to unfreeze
     */
    public void unFreeze(Player player) {
        if (!frozenPlayers.contains(player.getUniqueId())) {
        } else {
            frozenPlayers.remove(player.getUniqueId());
            if (player.getOpenInventory() != null) {
                player.closeInventory();
            }
            player.sendMessage(ChatColor.GREEN + "You have been successfully authenticated!");
        }
    }

    /**
     * Sends a repeating message to the the player that is frozen.
     *
     * @param player The frozen player.
     */
    private void sendMessage(Player player) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            if (frozenPlayers.contains(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + Messages.STRAIGHT_LINE_DEFAULT);
                player.sendMessage(ChatColor.DARK_GREEN+ "You are currently frozen awaiting authentication.");
                player.sendMessage(ChatColor.DARK_GREEN + "Please use your password to login. /login <password>");
                player.sendMessage(ChatColor.RED + Messages.STRAIGHT_LINE_DEFAULT);
            }
        }, 0L, 200L);
    }

    /**
     * Sends a IP Auth gui to the player that is needing ip auth.
     *
     * @param player The player whom shall get the GUI.
     */
    private void sendGUI(Player player) {
        Menu menu = new Menu(ChatColor.RED + "Awaiting IP Auth", 1);
        menu.runWhenEmpty(false);
        menu.setItem(4, new ItemBuilder(Material.ANVIL)
                .setName(ChatColor.DARK_RED + "Awaiting Authentication")
                .addLoreLine(ChatColor.GRAY + Messages.LORE_LINE_DEFAULT)
                .addLoreLine(ChatColor.DARK_GREEN + "You are frozen awaiting " + ChatColor.RED + "IP Authentication" + ChatColor.DARK_GREEN + '.')
                .addLoreLine(ChatColor.DARK_GREEN + "You may exit the server by clicking the red wool.")
                .addLoreLine(ChatColor.GRAY + Messages.LORE_LINE_DEFAULT)
                .toItemStack());
        menu.setItem(8, new ItemBuilder(Material.WOOL).setDyeColor(DyeColor.RED).setName(ChatColor.RED + "Click to logout").toItemStack());
        menu.setGlobalAction((player1, inv, item, slot, action) -> {
            if (slot == 8) {
                player1.closeInventory();
                Utils.kickFromNetwork(player1, ChatColor.RED + "Unsuccessful Authentication (IP)", Bukkit.getConsoleSender(), plugin);
            }
        });
        menu.showMenu(player);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        if (frozenPlayers.contains(p.getUniqueId()) && e.getInventory().getTitle().equalsIgnoreCase(ChatColor.RED + "Awaiting IP Auth")) {
            //Resend 2 ticks later because you cannot open and close on the same tick. 2 is the magic number.
            Bukkit.getScheduler().runTaskLater(plugin, () -> sendGUI(p), 2L);
        }
    }

    @EventHandler
    public void onPlayerLogin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (frozenPlayers.contains(p.getUniqueId())) {
            //We can start sending the message immediately.
            sendMessage(p);
            //Have to send 6 ticks later to allow for all other login tasks to complete.
            Bukkit.getScheduler().runTaskLater(plugin, () -> sendGUI(p), 6L);
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        String command = e.getMessage();
        if (isFrozen(e.getPlayer())) {
            if (command.toLowerCase().startsWith("/login") || command.toLowerCase().startsWith("/setpassword") || command.toLowerCase().startsWith("/testgui")) {
            } else {
                e.getPlayer().sendMessage(ChatColor.RED + "You may not use commands whilst awaiting authentication. Please use /login <password>");
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (frozenPlayers.contains(e.getPlayer().getUniqueId())) {
            e.setTo(e.getFrom());
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        if (frozenPlayers.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockInteract(BlockBreakEvent e) {
        if (frozenPlayers.contains(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        if (frozenPlayers.contains(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        }
    }
}
