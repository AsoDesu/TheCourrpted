package net.asodev.thecorrupted.commands;

import net.asodev.thecorrupted.Main;
import net.asodev.thecorrupted.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StartSession implements CommandExecutor {

    Main plugin;
    public StartSession(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("startsession").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin.getLivesManager().startSession(sender);
        sender.sendMessage(Utils.t("&aStarted Session."));
        return true;
    }

}
