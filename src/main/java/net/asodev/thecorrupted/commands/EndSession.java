package net.asodev.thecorrupted.commands;

import net.asodev.thecorrupted.Main;
import net.asodev.thecorrupted.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class EndSession implements CommandExecutor {

    Main plugin;
    public EndSession(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("endsession").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin.getLivesManager().endSession();
        sender.sendMessage(Utils.t("&aYou've ended the session"));
        return true;
    }

}
