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
    private final String permPrefix = ChatColor.DARK_GRAY + "[PERM] " + ChatColor.GRAY;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("tp")) {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                if(player.hasPermission("overseer.admin.tp")) {
                    if (args.length >= 2) {
                        if (args.length == 2) {
                            UUID userUUIDsrc = null;
                            try {
                                userUUIDsrc = UUIDFetcher.getUUIDOf(args[0]);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Player src = mainClass.getServer().getPlayer(userUUIDsrc);
                            if (src != null) {
                                UUID userUUIDdst = null;
                                try {
                                    userUUIDdst = UUIDFetcher.getUUIDOf(args[1]);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Player dst = mainClass.getServer().getPlayer(userUUIDdst);
                                if (dst != null) {
                                    Location dstLocation = dst.getLocation();
                                    src.teleport(dstLocation);
                                    mainClass.dbCtrl.logAdminCommand(player.getDisplayName(), "tp " + args[0] + " " + args[1]);
                                    src.sendMessage(adminPrefix + "You have been teleported to " + ChatColor.GOLD + args[1]);
                                    player.sendMessage(adminPrefix + "You have teleported " + ChatColor.GOLD + args[0] +
                                            ChatColor.GRAY + " to " + ChatColor.GOLD + args[1]);
                                    for (Player plr : mainClass.getServer().getOnlinePlayers()) {
                                        if (plr.hasPermission("overseer.admin.adminchannel")) {
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
                    player.sendMessage(permPrefix + "You don't have permission to use this command!");
                    return true;
                }
            } else {
                sender.sendMessage(adminPrefix + ChatColor.WHITE + "This command can only be executed by players!");
                return true;
            }
        } else if(cmd.getName().equalsIgnoreCase("freeze")) {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                if(player.hasPermission("overseer.admin.freeze")) {
                    if(args.length == 1) {
                        UUID userUUID = null;
                        try {
                            userUUID = UUIDFetcher.getUUIDOf(args[0]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Player plr = mainClass.getServer().getPlayer(userUUID);
                        if (plr != null) {
                            if (!mainClass.frozenPlayers.containsKey(plr)) {
                                mainClass.frozenPlayers.put(plr, plr.getLocation());
                                plr.sendMessage(adminPrefix + "You have been frozen by " + ChatColor.GOLD + player.getDisplayName());
                                player.sendMessage(adminPrefix + "You have frozen " + ChatColor.GOLD + args[0]);
                                for (Player p : mainClass.getServer().getOnlinePlayers()) {
                                    if (p.hasPermission("overseer.admin.adminchannel")) {
                                        p.sendMessage(ChatColor.DARK_RED + "[OP] " + adminPrefix + ChatColor.GOLD +
                                                player.getDisplayName() + ChatColor.DARK_RED + " has frozen " + args[0]);
                                    }
                                }
                                mainClass.dbCtrl.logAdminCommand(player.getDisplayName(), "freeze " + args[0]);
                                return true;
                            } else {
                                player.sendMessage(adminPrefix + ChatColor.GOLD + args[0] + ChatColor.GRAY + " is already frozen");
                                return true;
                            }
                        } else {
                            player.sendMessage(adminPrefix + ChatColor.GOLD + args[0] + ChatColor.GRAY + " is not online");
                            return true;
                        }
                    } else {
                        player.sendMessage(permPrefix + "You don't have permission to use this command!");
                        return true;
                    }
                } else {
                    player.sendMessage(adminPrefix + ChatColor.RED + "SYNTAX: /freeze <player>");
                    return true;
                }
            } else {
                sender.sendMessage(adminPrefix + ChatColor.WHITE + "This command can only be executed by players!");
                return true;
            }
        } else if(cmd.getName().equalsIgnoreCase("unfreeze")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if(player.hasPermission("overseer.admin.unfreeze")) {
                    if (args.length == 1) {
                        UUID userUUID = null;
                        try {
                            userUUID = UUIDFetcher.getUUIDOf(args[0]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Player plr = mainClass.getServer().getPlayer(userUUID);
                        if (plr != null) {
                            if(plr == player && !player.hasPermission("overseer.admin.unfreeze.self")) {
                                player.sendMessage(permPrefix + "You don't have permission to unfreeze yourself!");
                                return true;
                            }
                            if (mainClass.frozenPlayers.containsKey(plr)) {
                                mainClass.frozenPlayers.remove(plr);
                                plr.sendMessage(adminPrefix + "You have been unfrozen by " + ChatColor.GOLD + player.getDisplayName());
                                player.sendMessage(adminPrefix + "You have unfrozen " + ChatColor.GOLD + args[0]);
                                for (Player p : mainClass.getServer().getOnlinePlayers()) {
                                    if (plr.hasPermission("overseer.admin.adminchannel")) {
                                        p.sendMessage(ChatColor.DARK_RED + "[OP] " + adminPrefix + ChatColor.GOLD +
                                                player.getDisplayName() + ChatColor.DARK_RED + " has unfrozen " + args[0]);
                                    }
                                }
                                mainClass.dbCtrl.logAdminCommand(player.getDisplayName(), "unfreeze " + args[0]);
                                return true;
                            } else {
                                player.sendMessage(adminPrefix + ChatColor.GOLD + args[0] + ChatColor.GRAY + " is not frozen");
                                return true;
                            }
                        } else {
                            player.sendMessage(adminPrefix + ChatColor.GOLD + args[0] + ChatColor.GRAY + " is not online");
                            return true;
                        }
                    } else {
                        player.sendMessage(adminPrefix + ChatColor.RED + "SYNTAX: /unfreeze <player>");
                        return true;
                    }
                } else {
                    player.sendMessage(permPrefix + "You don't have permission to use this command!");
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
