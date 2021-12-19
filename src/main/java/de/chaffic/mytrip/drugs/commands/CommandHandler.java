package de.chaffic.mytrip.drugs.commands;

import de.chaffic.mytrip.drugs.commands.commands.AddictionCommands;
import de.chaffic.mytrip.drugs.commands.commands.DrugCommands;
import de.chaffic.mytrip.drugs.commands.commands.PluginCommands;
import de.chaffic.mytrip.drugs.inventories.DrugList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class CommandHandler implements CommandExecutor {
    //TODO: Permission checks
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("mytrip")) {
            if (args.length <= 0) {
                sender.sendMessage("§c/mytrip help");
                return true;
            }
            if (sender instanceof Player) {
                Player player = (Player) sender;
                switch (args[0].toLowerCase()) {
                    case "drugs", "list", "ls", "show" -> Objects.requireNonNullElseGet(DrugList.drugList, () -> new DrugList()).open(player);
                    case "addiction" -> {
                        AddictionCommands.performCommand(player, args);
                        return true;
                    }
                    case "drug" -> {
                        DrugCommands.performCommand(player, args);
                        return true;
                    }
                    case "get" -> {
                        DrugCommands.getDrug(player, args);
                        return true;
                    }
                    case "give" -> {
                        DrugCommands.giveDrug(player, args);
                        return true;
                    }
                }
            }
            if ("recover".equals(args[0])) {
                DrugCommands.recoverDrug(sender, args);
            } else {
                PluginCommands.performCommand(sender, args[0]);
            }
            return true;
        }
        return false;
    }
}
