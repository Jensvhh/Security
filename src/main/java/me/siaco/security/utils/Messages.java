package me.siaco.security.utils;

import net.minecraft.util.com.google.common.base.Strings;
import org.bukkit.ChatColor;

/**
 * Created by Siaco
 */
public class Messages {
    private static final String STRAIGHT_LINE_TEMPLATE = ChatColor.STRIKETHROUGH.toString() + Strings.repeat("-", 256);
    public static final String STRAIGHT_LINE_DEFAULT = STRAIGHT_LINE_TEMPLATE.substring(0, 55);
    public static final String LORE_LINE_DEFAULT = ChatColor.STRIKETHROUGH + "------------------------------";

}

