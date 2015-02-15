package com.katgamestudios.andyandreih.overseer.admin;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
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

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if(mainClass.frozenPlayers.containsKey(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityToEntityDamage(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity damaged = event.getEntity();
        if(damaged instanceof Player) {
            Player damagedPlayer = (Player) damaged;
            if(mainClass.frozenPlayers.containsKey(damagedPlayer)) {
                event.setCancelled(true);
            }
        }
        if(damager instanceof Player) {
            Player damagerPlayer = (Player) damager;
            if(mainClass.frozenPlayers.containsKey(damagerPlayer)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity damaged = event.getEntity();
        if(damaged instanceof Player) {
            Player damagedPlayer = (Player) damaged;
            if(mainClass.frozenPlayers.containsKey(damagedPlayer)) {
                event.setCancelled(true);
            }
        }
    }
}
