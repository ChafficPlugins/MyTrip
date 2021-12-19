package de.chaffic.mytrip.drugs.commands.commands;

import de.chaffic.mytrip.drugs.objects.Addiction;
import de.chaffic.mytrip.drugs.objects.DrugPlayer;
import de.chaffic.mytrip.drugs.objects.MyDrug;
import io.github.chafficui.CrucialAPI.Utils.customItems.CrucialItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static de.chaffic.mytrip.utils.ConfigStrings.PREFIX;

public class AddictionCommands {
    public static void performCommand(Player player, String[] args) {
        //TODO: clear, add, list
        if(args.length >= 2) {
            Player p = player;
            if(args.length > 2) {
                p = Bukkit.getServer().getPlayer(args[2]);
                if(p == null) {
                    player.sendMessage(PREFIX + "§cPlayer not found!"); //TODO: Localization
                    return;
                }
            }

            DrugPlayer drugPlayer = DrugPlayer.getPlayer(p.getUniqueId());
            if(drugPlayer != null) {
                switch (args[1].toLowerCase()) {
                    case "clear":
                        clearAddiction(player, drugPlayer);
                        return;
                    case "add":
                        if(args.length == 3) {
                            addAddiction(player, drugPlayer, args[2]);
                            return;
                        }
                    case "list":
                        listAddictions(player, drugPlayer);
                }
            }
        }
        player.sendMessage(PREFIX + "§cUsage: /drugs addiction <clear/add/list> (<player>) (<drug>)"); //TODO: Localization
    }

    public static void listAddictions(Player player, DrugPlayer drugPlayer) {
        if(drugPlayer != null) {
            player.sendMessage(PREFIX + "§aAddictions:"); //TODO: Localization
            for(Addiction addiction : drugPlayer.getAddictions()) {
                CrucialItem drug = MyDrug.getById(addiction.getDrugId());
                if(drug instanceof MyDrug) {
                    player.sendMessage(PREFIX + "§7- " + drug.getName()); //TODO: Localization
                }
            }
        }
    }

    public static void addAddiction(Player player, DrugPlayer drugPlayer, String drug) {
        MyDrug drugObj = MyDrug.getByName(drug);
        if(drugObj != null) {
            drugPlayer.addAddiction(drugObj);
        } else {
            player.sendMessage(PREFIX + "§cDrug not found!"); //TODO: Localization
        }
    }

    public static void clearAddiction(Player player, DrugPlayer drugPlayer) {
        if(drugPlayer != null) {
            drugPlayer.clear();
            player.sendMessage(PREFIX + "§aAddictions were cleared!"); //TODO: Localization
        }
    }
}
