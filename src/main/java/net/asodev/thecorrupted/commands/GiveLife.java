package net.asodev.thecorrupted.commands;

import net.asodev.thecorrupted.Main;
import net.asodev.thecorrupted.manager.Participant;
import net.asodev.thecorrupted.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveLife implements CommandExecutor {

    Main plugin;
    public GiveLife(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("givelife").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;

        if (args.length == 0) {
            p.sendMessage(Utils.t("&cYou must provide a player."));
            return true;
        }

        Participant part = plugin.getLivesManager().getParticipant(p);
        if (part == null) {
            p.sendMessage(Utils.t("&cYou can't do that, because you aren't in the series!"));
            return true;
        }

        Player targetP = Bukkit.getPlayer(args[0]);
        if (targetP == null) {
            p.sendMessage(Utils.t("&cThat player wasn't found."));
            return true;
        }
        Participant targetPart = plugin.getLivesManager().getParticipant(targetP);

        part.takeLife();
        targetPart.addLife();

        p.playEffect(EntityEffect.TOTEM_RESURRECT);
        p.playSound(p.getLocation(), Sound.ITEM_TOTEM_USE, SoundCategory.PLAYERS, 0.3f, 1);

        targetP.sendTitle(Utils.t("&r"), Utils.t("&aYou recvied a life from " + targetP.getName()), 10, 40, 10);
        return true;
    }

}
