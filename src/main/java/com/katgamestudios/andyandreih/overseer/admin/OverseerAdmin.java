package com.katgamestudios.andyandreih.overseer.admin;

import com.katgamestudios.andyandreih.overseer.main.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class OverseerAdmin extends JavaPlugin {
    LocalCommandListener cmdExec = new LocalCommandListener();
    DatabaseController dbCtrl = new DatabaseController();
    LocalEventListener eventListen = new LocalEventListener();

    public static HashMap<Player, Location> frozenPlayers = new HashMap<Player, Location>();

    @Override
    public void onEnable() {
        cmdExec.mainClass = this;
        eventListen.mainClass = this;

        getServer().getPluginManager().registerEvents(eventListen, this);

        getCommand("tp").setExecutor(cmdExec);

        dbCtrl.initDb(OverseerMain.dataFolder);
        dbCtrl.openDb();
        getLogger().info("Connected to database.");
    }
}
