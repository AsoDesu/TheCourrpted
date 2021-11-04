package net.asodev.thecorrupted.listener;

import net.asodev.thecorrupted.Main;
import net.asodev.thecorrupted.manager.Participant;
import net.asodev.thecorrupted.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Events implements Listener {

    Main plugin;
    public Events(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void Death(PlayerDeathEvent e) {
        Participant participant = plugin.getLivesManager().getParticipant(e.getEntity());
        participant.takeLife();
        String deathMsg;
        if (participant.getLives() != 0) {
            deathMsg = e.getDeathMessage() + Utils.t(String.format("&a [Lives: %d]", participant.getLives()));
        } else {
            deathMsg = e.getDeathMessage() + Utils.t(String.format("&c [Lives: %d]", participant.getLives()));
            plugin.getLivesManager().removePlayer(e.getEntity());
        }
        e.setDeathMessage(deathMsg);

        if (e.getEntity().getKiller() != null) {
            Participant killer = plugin.getLivesManager().getParticipant(e.getEntity().getKiller());
            if (killer.isCorrupted()) {
                killer.setCorrupted(false);
                killer.getPlayer().sendMessage(Utils.t("&dYou've been cured!"));
            }
        }
    }

    @EventHandler
    public void Chat(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        Integer lives;
        Participant part = plugin.getLivesManager().getParticipant(e.getPlayer());
        if (part == null) lives = 0;
        else lives = part.getLives();
        Bukkit.getOnlinePlayers().forEach(p -> {
            String msg = Utils.t(e.getMessage().replace(p.getName(), ChatColor.of("#a7dcf2") + p.getName()));
            p.sendMessage(Utils.t(String.format("%s%s&r: %s", plugin.getLivesManager().getColour(lives), e.getPlayer().getName(), msg)));
        });
    }

}
