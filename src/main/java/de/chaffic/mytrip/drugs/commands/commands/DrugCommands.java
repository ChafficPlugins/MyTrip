package de.chaffic.mytrip.drugs.commands.commands;

import de.chaffic.mytrip.drugs.inventories.DrugList;
import de.chaffic.mytrip.drugs.objects.MyDrug;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.Objects;

import static de.chaffic.mytrip.utils.ConfigStrings.PREFIX;

public class DrugCommands {
    public static void performCommand(Player player, String[] args) {
        //TODO: create, delete, list, get
        if(args.length >= 2) {
            if(args[1].equalsIgnoreCase("list")) {
                Objects.requireNonNullElseGet(DrugList.drugList, () -> new DrugList()).open(player);
            } else if(args.length >= 3) {
                MyDrug drug = MyDrug.getByName(args[2]);
                if(drug != null) {
                    switch (args[1].toLowerCase()) {
                        case "create" -> {
                            player.sendMessage(PREFIX + "This command is not available in this version."); //TODO: Localization
                            return;
                        }
                        case "delete" -> {
                            deleteDrug(player, drug);
                            return;
                        }
                        case "get" -> {
                            giveDrug(player, drug);
                            return;
                        }
                        case "give" -> {
                            giveDrug(player, args);
                            return;
                        }
                    }
                } else {
                    player.sendMessage(PREFIX + "Drug not found."); //TODO: Localization
                    return;
                }
            }
        }
        player.sendMessage(PREFIX + "§cUsage: /drugs drug <create/delete/list/get> (<drug>) (<player>) "); //TODO: Localization
    }

    private static void giveDrug(Player player, MyDrug drug) {
        player.getInventory().addItem(drug.getItemStack());
        player.sendMessage(PREFIX + "You reveived the drug " + drug.getName()); //TODO: Localization
    }

    public static void getDrug(Player player, String[] args) {
        if(args.length >= 2) {
            MyDrug drug = MyDrug.getByName(args[1]);
            if(drug != null) {
                DrugCommands.giveDrug(player, drug);
            } else {
                player.sendMessage(PREFIX + "Drug not found."); //TODO: Localization
            }
        } else {
            player.sendMessage(PREFIX + "Usage: /mytrip get <drug>"); //TODO: Localization
        }
    }

    public static void giveDrug(Player player, String[] args) {
        if(args.length >= 4) {
            MyDrug drug = MyDrug.getByName(args[2]);
            if(drug != null) {
                Player p = Bukkit.getPlayer(args[3]);
                if(p != null) {
                    giveDrug(p, drug);
                    player.sendMessage(PREFIX + "You gave " + p.getName() + " the drug " + drug.getName()); //TODO: Localization
                } else {
                    player.sendMessage(PREFIX + "Player not found."); //TODO: Localization
                }
            } else {
                player.sendMessage(PREFIX + "Drug not found."); //TODO: Localization
            }
        } else {
            player.sendMessage(PREFIX + "Usage: /mytrip give <drug> (<player>)"); //TODO: Localization
        }
    }

    public static void recoverDrug(CommandSender sender, String[] args) {
        Player p;
        if(args.length >= 2) {
            p = Bukkit.getPlayer(args[1]);
            if(p == null) {
                sender.sendMessage(PREFIX + "Player not found."); //TODO: Localization
                return;
            }
        } else if(sender instanceof Player) {
            p = (Player) sender;
        } else {
            sender.sendMessage(PREFIX + "Usage: /mytrip recover <player>"); //TODO: Localization
            return;
        }

        if(args.length >= 1) {
            for (PotionEffect effect : p.getActivePotionEffects()) {
                p.removePotionEffect(effect.getType());
            }
            p.sendMessage(PREFIX + "You have been cured."); //TODO: Localization
            sender.sendMessage(PREFIX + "You have cured " + p.getName() + "."); //TODO: Localization
        } else {
            sender.sendMessage(PREFIX + "Usage: /mytrip recover <player>"); //TODO: Localization
        }
    }

    private static void deleteDrug(Player player, MyDrug drug) {
        drug.delete();
        player.sendMessage(PREFIX + "You deleted the drug " + drug.getName()); //TODO: Localization
    }
}
