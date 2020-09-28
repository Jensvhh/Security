/*
 * Copyright Missionary (c) 2017. Unauthorized use of this work can and will result in further action against the unauthorized user(s).
 */
package me.siaco.security.utils;

/**
 * Created by Siaco
 * @author Jens
 */
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.siaco.security.Security;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class Menu {

    private String title = "";
    private int rows = 3;
    private HashMap<Integer, ItemStack> content = new HashMap<>();
    private HashMap<Integer, ItemAction> commands = new HashMap<>();
    private Inventory inventory;
    private ItemAction gaction;
    private boolean runempty = false;

    public Menu(String title, int rows, ItemStack[] contents) {
        this(title, rows);
        setContents(contents);
    }


    public Menu(String title, int rows) {
        if (rows < 1 || rows > 6) throw new IndexOutOfBoundsException("Menu can only have between 1 and 6 rows.");
        this.title = title;
        this.rows = rows;
        setListener(Security.getInstance());
    }

    private void setListener(JavaPlugin pl) {
        pl.getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onInvClick(InventoryClickEvent event) {
                Player player = (Player) event.getWhoClicked();
                Inventory inv = event.getInventory();
                ItemStack item = event.getCurrentItem();
                int slot = event.getRawSlot();
                InventoryAction a = event.getAction();

                if (item == null || item.getType() == Material.AIR) {
                    if (!runempty) return;
                }

                if (inv.getName().equals(title)) {
                    if (inv.equals(inventory)) {
                        if (slot <= (rows * 9) - 1) {
                            event.setCancelled(true);
                            if (hasAction(slot)) commands.get(slot).run(player, inv, item, slot, a);
                            if (gaction != null) gaction.run(player, inv, item, slot, a);
                        }
                    }
                }
            }
        }, pl);
    }

    public void onClose(final Callback<InventoryClose> closeCallback) {
        Security.getInstance().getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onInvClick(InventoryCloseEvent event) {
                Player player = (Player) event.getPlayer();
                Inventory inv = event.getInventory();

                if (inv.getName().equals(title)) {
                    if (inv.equals(inventory)) {
                        closeCallback.run(new InventoryClose(player, inv));
                    }
                }
            }
        }, Security.getInstance());
    }

    @Deprecated
    public boolean hasAction(int slot) {
        return commands.containsKey(slot);
    }

    @Deprecated
    public void setAction(int slot, ItemAction action) {
        commands.put(slot, action);
    }

    public void setGlobalAction(ItemAction action) {
        this.gaction = action;
    }

    public void removeGlobalAction() {
        this.gaction = null;
    }

    @Deprecated
    public void removeAction(int slot) {
        if (commands.containsKey(slot)) {
            commands.remove(slot);
        }
    }

    public void runWhenEmpty(boolean state) {
        this.runempty = state;
    }

    public int nextOpenSlot() {
        int h = 0;
        for (Integer i : content.keySet()) {
            if (i > h) h = i;
        }
        for (int i = 0; i <= h; i++) {
            if (!content.containsKey(i)) return i;
        }
        return h + 1;
    }

    public void addItem(ItemStack item) {
        if (nextOpenSlot() > (rows * 9) - 1) {
            Security.getInstance().getLogger().info("addItem() : Inventory is full.");
            return;
        }
        setItem(nextOpenSlot(), item);
    }

    public void setItem(int slot, ItemStack item) throws IndexOutOfBoundsException {
        if (slot < 0 || slot > (rows * 9) - 1)
            throw new IndexOutOfBoundsException("setItem() : Slot is outside inventory.");
        if (item == null || item.getType() == Material.AIR) {
            removeItem(slot);
            return;
        }
        content.put(slot, item);
    }

    public void fill(ItemStack item) {
        for (int i = 0; i < rows * 9; i++) content.put(i, item);
    }

    public void fillRange(int s, int e, ItemStack item) throws IndexOutOfBoundsException {
        if (e <= s) throw new IndexOutOfBoundsException("fillRange() : Ending index must be less than starting index.");
        if (s < 0 || s > (rows * 9) - 1)
            throw new IndexOutOfBoundsException("fillRange() : Starting index is outside inventory.");
        if (e < 0 || e > (rows * 9) - 1)
            throw new IndexOutOfBoundsException("fillRange() : Ending index is outside inventory.");
        for (int i = s; i <= e; i++) content.put(i, item);
    }

    public void removeItem(int slot) {
        if (content.containsKey(slot)) content.remove(slot);
    }

    public ItemStack getItem(int slot) {
        if (content.containsKey(slot)) return content.get(slot);
        return null;
    }

    public void replaceItem(Integer slot, ItemStack itemStack) {
        inventory.setItem(slot, itemStack);
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int rows() {
        return this.rows;
    }

    public void build() {
        this.inventory = Bukkit.createInventory(null, rows * 9, this.title);
        inventory.clear();
        for (Map.Entry<Integer, ItemStack> entry : content.entrySet())
            inventory.setItem(entry.getKey(), entry.getValue());
    }

    public Inventory getMenu() {
        build();
        return inventory;
    }

    public void showMenu(Player player) {
        player.openInventory(getMenu());
    }

    public ItemStack[] getContents() {
        return getMenu().getContents();
    }

    public void setContents(ItemStack[] contents) throws ArrayIndexOutOfBoundsException {
        if (contents.length > rows * 9)
            throw new ArrayIndexOutOfBoundsException("setContents() : Contents are larger than inventory.");
        content.clear();
        for (int i = 0; i < contents.length; i++) {
            if (contents[i] != null && contents[i].getType() != Material.AIR) content.put(i, contents[i]);
        }
    }

    @Getter
    @RequiredArgsConstructor
    public class InventoryClose {
        private final Player player;
        private final Inventory inventory;
    }

    public interface ItemAction {
        void run(Player player, Inventory inv, ItemStack item, int slot, InventoryAction action);
    }


}
