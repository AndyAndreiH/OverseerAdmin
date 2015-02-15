package com.katgamestudios.andyandreih.overseer.admin;

import com.katgamestudios.andyandreih.overseer.main.UUIDFetcher;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LocalCommandListener implements CommandExecutor {
    OverseerAdmin mainClass;

    private final String adminPrefix = ChatColor.DARK_GRAY + "[ADMIN] " + ChatColor.GRAY;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("tp")) {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                if(args.length >= 2) {
                    if(args.length == 2) {
                        UUID userUUIDsrc = null;
                        try {
                            userUUIDsrc = UUIDFetcher.getUUIDOf(args[0]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Player src = mainClass.getServer().getPlayer(userUUIDsrc);
                        if(src.isOnline()) {
                            UUID userUUIDdst = null;
                            try {
                                userUUIDdst = UUIDFetcher.getUUIDOf(args[1]);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Player dst = mainClass.getServer().getPlayer(userUUIDdst);
                            if(dst.isOnline()) {
                                Location dstLocation = dst.getLocation();
                                src.teleport(dstLocation);
                                mainClass.dbCtrl.logAdminCommand(player.getDisplayName(), "tp " + args[0] + " " + args[1]);
                                src.sendMessage(adminPrefix + "You have been teleported to " + ChatColor.GOLD + args[1]);
                                player.sendMessage(adminPrefix + "You have teleported " + ChatColor.GOLD + args[0] +
                                    ChatColor.GRAY + " to " + ChatColor.GOLD + args[1]);
                                for(Player plr : mainClass.getServer().getOnlinePlayers()) {
                                    if(plr.isOp()) {
                                        plr.sendMessage(ChatColor.DARK_RED + "[OP] " + adminPrefix +
                                            ChatColor.GOLD + player.getDisplayName() + ChatColor.DARK_RED + " teleported " +
                                            args[0] + " to " + args[1]);
                                    }
                                }
                                return true;
                            } else {
                                player.sendMessage(adminPrefix + ChatColor.GOLD + args[1] + ChatColor.GRAY + " is not online");
                                return true;
                            }
                        } else {
                            player.sendMessage(adminPrefix + ChatColor.GOLD + args[0] + ChatColor.GRAY + " is not online");
                            return true;
                        }
                    }
                } else {
                    player.sendMessage(adminPrefix + ChatColor.RED + "SYNTAX: /tp <srcPlayer> <dstPlayer/dstLocation>");
                    return true;
                }
            } else {
                sender.sendMessage(adminPrefix + ChatColor.WHITE + "This command can only be executed by players!");
                return true;
            }
        }
        return false;
    }
}
