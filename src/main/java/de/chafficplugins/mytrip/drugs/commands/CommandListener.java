package de.chafficplugins.mytrip.drugs.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static de.chafficplugins.mytrip.drugs.commands.Commands.CommandCategory.*;
import static de.chafficplugins.mytrip.drugs.commands.Commands.callCommand;
import static de.chafficplugins.mytrip.utils.ConfigStrings.PREFIX;

public class CommandListener implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("mytrip")) {
            if(!(sender instanceof Player)) {
                sender.sendMessage(PREFIX + "§cOnly players can perform MyTrip commands!");
                return true;
            }
            Player player = (Player) sender;
            if(args.length == 0) {
                sender.sendMessage(PREFIX + "/mytrip help");
                return true;
            } else if(args.length == 1) {
                switch (args[0].toLowerCase()) {
                    case "help" -> callCommand(player, HELP);
                    case "info" -> callCommand(player, INFO);
                    case "recover" -> callCommand(player, RECOVER);
                    case "drugs", "list", "ls", "show" -> callCommand(player, LIST);
                    default -> sender.sendMessage(PREFIX + "Unknown command: " + args[0]);
                }
            }else if(args.length == 2) {
                switch (args[0].toLowerCase()) {
                    case "create" -> callCommand(player, CREATE, args[1]);
                    case "recover" -> callCommand(player, RECOVER, null, args[1]);
                    case "give" -> callCommand(player, GIVE, args[1]);
                    case "addictions" -> callCommand(player, ADDICTIONS, args[1]);
                    default -> sender.sendMessage(PREFIX + "Unknown command: " + args[0]);
                }
            } else if(args.length == 3) {
                switch (args[0].toLowerCase()) {
                    case "give" -> callCommand(player, GIVE, args[1], args[2]);
                    case "addictions" -> callCommand(player, ADDICTIONS, args[1], args[2]);
                    default -> sender.sendMessage(PREFIX + "Unknown command: " + args[0]);
                }
            } else if(args.length == 4) {
                if ("addictions".equalsIgnoreCase(args[0])) {
                    callCommand(player, ADDICTIONS, args[1], args[2], args[3]);
                } else {
                    sender.sendMessage(PREFIX + "Unknown command: " + args[0]);
                }
            }
        }
        return true;
    }
}
