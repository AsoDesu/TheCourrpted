package net.asodev.thecorrupted.manager;

import net.asodev.thecorrupted.Main;
import net.asodev.thecorrupted.utils.Utils;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class LivesManager {

    Main plugin;
    Map<UUID, Participant> players;

    BukkitTask task;
    Integer mins;

    public LivesManager(Main plugin) {
        this.plugin = plugin;

        if (!plugin.getConfig().contains("lives")) return;
        this.players = new HashMap<>();

        plugin.getConfig().getConfigurationSection("lives").getKeys(false).forEach(k -> {
            Player p = Bukkit.getPlayer(UUID.fromString(k));
            if (p == null) return;

            int lives = plugin.getConfig().getInt("lives." + k + ".lives");
            if (lives == 0) return;
            addPlayer(p, lives);
        });
    }

    public void startSession(CommandSender starter) {
        if (players == null) {
            players = new HashMap<>();
            Bukkit.getOnlinePlayers().forEach(p -> {
                if (p.getGameMode() == GameMode.SPECTATOR) return;
                addPlayer(p, 4);
            });
            starter.sendMessage(Utils.t("&aInitialized " + Bukkit.getOnlinePlayers().size() + " players."));
        }

        mins = 0;
        task = new BukkitRunnable(){
            @Override
            public void run() {
                if (mins == 0) {
                    startPickingSequence();
                    return;
                }
                if (mins < 5) {
                    Bukkit.broadcastMessage(Utils.t("&aThe corrupted will be chosen in " + mins + " minutes!!"));
                }
                mins--;
            }
        }.runTaskTimer(plugin, 0, 1200);
    }

    private void startPickingSequence() {
        Bukkit.broadcastMessage(Utils.t("&cThe corrupted is being chosen."));
        task.cancel();

        Utils.titleToAll("&cYou are....", "", 10, 40, 10);
        task = new BukkitRunnable(){
            @Override
            public void run() {
                pickCorrupted();
            }
        }.runTaskLater(plugin, 40);
    }

    private void pickCorrupted() {
        Random rand = new Random();
        int amount = rand.nextInt(3)+1;
        List<Participant> participants = new ArrayList<>(players.values());

        for (int i = 0; i < amount; i++) {
            if (participants.size() == 0) continue;
            int randI = rand.nextInt(participants.size());
            Participant part = participants.get(randI);
            participants.remove(randI);

            part.setCorrupted(true);
            if (part.getPlayer() == null) continue;
            part.getPlayer().sendTitle(Utils.t("\u0001"), "", 10, 30, 10);
            part.getPlayer().playSound(part.getPlayer().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1, 1);
        }

        for (Participant part : participants) {
            if (part.getPlayer() == null) continue;
            part.getPlayer().sendTitle(Utils.t("\u0002 \u0001"), "", 10, 30, 10);
            part.getPlayer().playSound(part.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, SoundCategory.BLOCKS, 1, 1);
        }
    }

    private void addPlayer(Player p, Integer lives) {
        Participant part = new Participant(p.getUniqueId(), lives, plugin, this);
        players.put(p.getUniqueId(), part);
        p.setPlayerListName(getColour(part.getLives()) + p.getName());
    }

    public ChatColor getColour(Integer lives) {
        switch (lives) {
            case 0:
                return ChatColor.GRAY;
            case 1:
                return ChatColor.RED;
            case 2:
                return ChatColor.YELLOW;
            case 3:
                return ChatColor.GREEN;
            case 4:
                return ChatColor.DARK_GREEN;
            default:
                return ChatColor.GOLD;
        }
    }

    public Participant getParticipant(Player p) {
        return players.get(p.getUniqueId());
    }

    public void removePlayer(Player p) {
        p.setGameMode(GameMode.SPECTATOR);
        getParticipant(p).removeFromConfig();
        players.remove(p.getUniqueId());
    }

    public void endSession() {
        Bukkit.broadcastMessage(Utils.t("&r\n&c&lThe Session has ended, kicking everyone in 30 seconds!\n&r"));
        Bukkit.getOnlinePlayers().forEach(p -> {
            Participant part = getParticipant(p);
            if (part.isCorrupted()) {
                p.sendMessage(Utils.t("&cYou weren't cured, so you've been set to one life."));
                part.setLives(1);
            }
        });

        new BukkitRunnable(){
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(p -> {
                    p.kickPlayer(Utils.t("&aThe session has ended\n&r\nCome back next time :D"));
                });
            }
        }.runTaskLater(plugin, 600);

    }

}
