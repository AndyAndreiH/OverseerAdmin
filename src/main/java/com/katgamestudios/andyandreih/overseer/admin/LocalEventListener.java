package com.katgamestudios.andyandreih.overseer.admin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class LocalEventListener implements Listener {
    OverseerAdmin mainClass;

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if(mainClass.frozenPlayers.containsKey(player)) {
            player.teleport(mainClass.frozenPlayers.get(player));
        }
    }
}
