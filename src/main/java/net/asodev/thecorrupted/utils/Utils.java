package net.asodev.thecorrupted.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Utils {

    public static String t(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static void titleToAll(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        Bukkit.getOnlinePlayers().forEach(p -> {
            p.sendTitle(Utils.t(title), Utils.t(subtitle), fadeIn, stay, fadeOut);
        });
    }

}
