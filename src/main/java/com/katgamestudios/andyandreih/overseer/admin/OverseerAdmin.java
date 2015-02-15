package com.katgamestudios.andyandreih.overseer.admin;

import com.katgamestudios.andyandreih.overseer.main.DatabaseController;
import com.katgamestudios.andyandreih.overseer.main.OverseerMain;
import org.bukkit.plugin.java.JavaPlugin;

public class OverseerAdmin extends JavaPlugin {
    LocalCommandListener cmdExec = new LocalCommandListener();
    DatabaseController dbCtrl = new DatabaseController();

    @Override
    public void onEnable() {
        cmdExec.mainClass = this;

        getCommand("tp").setExecutor(cmdExec);

        dbCtrl.initDb(OverseerMain.dataFolder);
        dbCtrl.openDb();
        getLogger().info("Connected to database.");
    }
}
