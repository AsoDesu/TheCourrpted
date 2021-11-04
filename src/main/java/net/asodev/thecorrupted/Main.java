package net.asodev.thecorrupted;

import net.asodev.thecorrupted.commands.EndSession;
import net.asodev.thecorrupted.commands.GiveLife;
import net.asodev.thecorrupted.commands.StartSession;
import net.asodev.thecorrupted.listener.Events;
import net.asodev.thecorrupted.manager.LivesManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private LivesManager livesManager;

    @Override
    public void onEnable() {
        livesManager = new LivesManager(this);

        // Commands
        new StartSession(this);
        new GiveLife(this);
        new EndSession(this);

        // Events
        new Events(this);
    }

    public LivesManager getLivesManager() {
        return livesManager;
    }
}
