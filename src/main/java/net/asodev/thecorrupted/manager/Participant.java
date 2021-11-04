package net.asodev.thecorrupted.manager;

import net.asodev.thecorrupted.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Participant {

    private UUID uuid;
    private Integer lives;
    private Boolean corrupted;

    private Main plugin;
    private LivesManager livesManager;

    public Participant(UUID uuid, Integer lives, Main plugin, LivesManager livesManager) {
        this.uuid = uuid;
        this.lives = lives;
        corrupted = false;
        this.plugin = plugin;
        this.livesManager = livesManager;

        saveToConfig();
    }

    public UUID getUuid() {
        return uuid;
    }
    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public Boolean isCorrupted() {
        return corrupted;
    }
    public Integer getLives() {
        return lives;
    }

    public void setCorrupted(Boolean corrupted) {
        this.corrupted = corrupted;
    }

    public void addLife() {
        this.lives++;
        saveToConfig();
    }
    public void takeLife() {
        this.lives--;
        saveToConfig();
    }
    public void setLives(Integer lives) {
        this.lives = lives;
        saveToConfig();
    }

    public void saveToConfig() {
        getPlayer().setPlayerListName(livesManager.getColour(getLives()) + getPlayer().getName());
        plugin.getConfig().set("lives." + uuid.toString() + ".lives", lives);
        plugin.saveConfig();
    }

    public void removeFromConfig() {
        plugin.getConfig().set("lives." + uuid.toString(), null);
        plugin.saveConfig();
    }
}
