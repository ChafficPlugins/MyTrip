package de.chaffic.MyTrip.Commands;

import de.chaffic.MyTrip.API.DrugAPI;
import de.chaffic.MyTrip.Inventories.List;
import de.chaffic.MyTrip.API.Objects.MyDrug;
import io.github.chafficui.CrucialAPI.API.CItem;
import io.github.chafficui.CrucialAPI.API.Effects;
import io.github.chafficui.CrucialAPI.Interfaces.CrucialItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;

import de.chaffic.MyTrip.Main;

public class MyTripCommands implements CommandExecutor{

    private static final String prefix = ChatColor.WHITE + "[" + ChatColor.DARK_RED + "MyTrip" + ChatColor.WHITE + "] " + ChatColor.RESET;
    private static final Main plugin = Main.getPlugin(Main.class);
    private final String noPermissions = ChatColor.RED + plugin.getWord("no permissions");

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        //mytrip commands
        //basic commands
        if(cmd.getName().equalsIgnoreCase("mytrip")) {
            String master = "mytrip.*";
            if(args.length == 0 || args[0].equalsIgnoreCase("help")) {
                //lists all commands
                if(sender.hasPermission(master)) {
                    sender.sendMessage(ChatColor.RED + plugin.getWord("list commands"));
                    sender.sendMessage(ChatColor.GRAY + "- " + ChatColor.RED + "/" + ChatColor.WHITE + "mytrip list");
                    sender.sendMessage(ChatColor.GRAY + "- " + ChatColor.RED + "/" + ChatColor.WHITE + "mytrip remove");
                    sender.sendMessage(ChatColor.GRAY + "- " + ChatColor.RED + "/" + ChatColor.WHITE + "mytrip recover");
                    sender.sendMessage(ChatColor.GRAY + "- " + ChatColor.RED + "/" + ChatColor.WHITE + "mytrip give");
                    sender.sendMessage(ChatColor.GRAY + "- " + ChatColor.RED + "/" + ChatColor.WHITE + "mytrip create");
                    sender.sendMessage(ChatColor.RED + "There is NO reload command. Please RESTART your server!");
                } else {
                    sender.sendMessage(prefix + noPermissions);
                }
            } else if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("r")) {
                //removes effects from player
                if(sender instanceof Player) {
                    if(sender.hasPermission("mytrip.remove") || sender.hasPermission(master)) {
                        Player p = (Player) sender;
                        if(!p.getActivePotionEffects().isEmpty()) {
                            Effects.removeBlood(p);
                            for(PotionEffect effect : p.getActivePotionEffects()) {
                                p.removePotionEffect(effect.getType());
                            }
                            p.sendMessage(prefix + ChatColor.GREEN + plugin.getWord("sobered up"));
                        } else {
                            p.sendMessage(prefix + ChatColor.YELLOW + plugin.getWord("not high"));
                        }
                    } else {
                        sender.sendMessage(prefix + noPermissions);
                    }
                } else {
                    sender.sendMessage(prefix + ChatColor.RED + plugin.getWord("only players"));
                }
            }
            //drug commands
            else if(args[0].equalsIgnoreCase("give")) {
                //gives a drug with custom quality
                if(sender instanceof Player) {
                    if(sender.hasPermission(master)) {
                        try {
                            if(args.length == 2) {
                                MyDrug item = MyDrug.getByName(args[1].replace("_", " "));
                                if(item != null){
                                    ((Player) sender).getInventory().addItem(item.get());
                                    sender.sendMessage(prefix + ChatColor.RESET + plugin.getWord("You received") + " " + item.getName());
                                    return true;
                                }
                                sender.sendMessage(prefix + ChatColor.RED + args[1] + plugin.getWord("not exist"));
                            } else {
                                sender.sendMessage(prefix + ChatColor.YELLOW + "/mt give <drugname>");
                            }
                        } catch(Exception e) {
                            e.printStackTrace();
                            sender.sendMessage(prefix + ChatColor.YELLOW + "/mt give <drugname>");
                        }
                    } else {
                        sender.sendMessage(prefix + noPermissions);
                    }
                } else {
                    sender.sendMessage(prefix + ChatColor.RED + plugin.getWord("only players"));
                }
            } else if(args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("ls")) {
                //Lists all drugs
                if(sender instanceof Player) {
                    if(sender.hasPermission(master) || sender.hasPermission("mytrip.list")) {
                        Player p = (Player) sender;
                        List.drugsInv.open(p);
                    } else {
                        sender.sendMessage(prefix + noPermissions);
                    }
                } else {
                    sender.sendMessage(prefix + ChatColor.RED + plugin.getWord("only players"));
                }
            } else if(args[0].equalsIgnoreCase("recover")) {
                //removes an addiction
                if(sender.hasPermission(master)) {
                    if(args.length == 2) {
                        try {
                            Player p = Bukkit.getPlayer(args[1]);
                            DrugAPI.getPlayerData(p.getUniqueId()).clear();
                            sender.sendMessage(prefix + ChatColor.GREEN + args[1] + " " + plugin.getWord("no longer addicted"));
                        } catch(Exception e) {
                            sender.sendMessage(prefix + ChatColor.RED + args[1] + " " + plugin.getWord("not on server"));
                        }
                    } else {
                        sender.sendMessage(prefix + ChatColor.YELLOW + "/mt recover <playername>");
                    }
                } else {
                    sender.sendMessage(prefix + noPermissions);
                }
            } else if(args[0].equalsIgnoreCase("create")) {
                //creates new drug
                if(sender.hasPermission(master)) {
                    if(sender instanceof Player) {
                        Player p = (Player) sender;
                        if(args.length == 2) {
                            MyDrug myDrug = MyDrug.getByName(args[1]);
                            if(myDrug == null){
                                new MyDrug(args[1], Material.DIRT, "MYTRIP_DRUG");
                            } else{
                                p.sendMessage(prefix + ChatColor.YELLOW + "There is already a drug with this name");
                                return false;

                            }
                            Inventory inv = Bukkit.createInventory(p, 27, args[1] + " - " + plugin.getWord("InvCreation 1"));
                            p.openInventory(inv);
                            p.playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 29);
                            return true;
                        } else {
                            sender.sendMessage(prefix + ChatColor.YELLOW + "/mt create <drugname>");
                        }
                    } else {
                        //console
                        sender.sendMessage(prefix + ChatColor.RED + plugin.getWord("only players"));
                    }
                } else {
                    sender.sendMessage(prefix + noPermissions);
                }
            } else {
                sender.sendMessage(prefix + ChatColor.YELLOW + "Please use an existing command");
            }
        }
        return true;
    }
}
