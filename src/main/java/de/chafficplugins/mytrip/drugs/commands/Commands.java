package de.chafficplugins.mytrip.drugs.commands;

import de.chafficplugins.mytrip.MyTrip;
import de.chafficplugins.mytrip.drugs.inventories.DrugList;
import de.chafficplugins.mytrip.drugs.inventories.drugCreation.DrugCraft;
import de.chafficplugins.mytrip.drugs.objects.Addiction;
import de.chafficplugins.mytrip.drugs.objects.DrugPlayer;
import de.chafficplugins.mytrip.drugs.objects.MyDrug;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import static de.chafficplugins.mytrip.utils.ConfigStrings.*;
import static de.chafficplugins.mytrip.utils.CustomMessages.getLocalized;
import static de.chafficplugins.mytrip.utils.PlayerUtils.hasOnePermissions;

public class Commands {
    private static final MyTrip plugin = MyTrip.getInstance();

    public enum CommandCategory {
        INFO,
        HELP,
        LIST,
        CREATE,
        RECOVER,
        GIVE,
        ADDICTIONS
    }

    public static void callCommand(Player caller, CommandCategory category) {
        performCommand(caller, category, null, null, null);
    }

    public static void callCommand(Player caller, CommandCategory category, String sub) {
        performCommand(caller, category, sub, sub, null);
    }

    public static void callCommand(Player caller, CommandCategory category, String sub, String affected) {
        getAffectedPlayer(caller, category, sub, sub, affected);
    }

    public static void callCommand(Player caller, CommandCategory category, String sub, String drugName, String affected) {
        getAffectedPlayer(caller, category, sub, drugName, affected);
    }

    private static void getAffectedPlayer(Player caller, CommandCategory category, String sub, String drugName, String affected) {
        if(affected == null) return;
        Player affectedPlayer = Bukkit.getPlayer(affected);
        if(affectedPlayer == null) {
            caller.sendMessage(PREFIX + getLocalized(PLAYER_NOT_FOUND, affected));
            return;
        }
        performCommand(caller, category, sub, drugName, affectedPlayer);
    }

    private static void performCommand(Player caller, CommandCategory category, String sub, String drugName, Player affected) {
        switch (category) {
            case INFO -> infoCommand(caller);
            case HELP -> helpCommand(caller);
            case LIST -> listCommand(caller);
            case CREATE -> createCommand(caller, drugName);
            case RECOVER -> recoverCommand(caller, affected);
            case GIVE -> giveCommand(caller, affected, drugName);
            case ADDICTIONS -> addictionsCommand(caller, affected, sub, drugName);
        }
    }

    private static void infoCommand(Player caller) {
        caller.sendMessage(PREFIX + "§7MyTrip Info");
        caller.sendMessage(PREFIX + "§7Version: §e" + plugin.getDescription().getVersion());
        caller.sendMessage(PREFIX + "§7Developer: §e" + plugin.getDescription().getAuthors().get(0));
        caller.sendMessage(PREFIX + "§7Website: §e" + plugin.getDescription().getWebsite());
    }

    private static void helpCommand(Player caller) {
        if(!hasOnePermissions(caller, PERM_CMD_HELP)) {
            caller.sendMessage(PREFIX + getLocalized(NO_PERMISSION));
            return;
        }
        caller.sendMessage("§8§m-----------------------------------------------------");
        caller.sendMessage("§7/mytrip info");
        caller.sendMessage("§7/mytrip help");
        caller.sendMessage("§7/mytrip list");
        caller.sendMessage("§7/mytrip create <drug>");
        caller.sendMessage("§7/mytrip recover <player>");
        caller.sendMessage("§7/mytrip give <drug> {<player>}");
        caller.sendMessage("§7/mytrip addictions clear {<player>}");
        caller.sendMessage("§7/mytrip addictions list {<player>}");
        caller.sendMessage("§7/mytrip addictions add <drug> {<player>}");
        caller.sendMessage("§8§m-----------------------------------------------------");
    }

    private static void recoverCommand(Player caller, Player affected) {
        if(affected == null) {
            if(!hasOnePermissions(caller, PERM_CMD_RECOVER, PERM_CMD_RECOVER_SELF)) {
                caller.sendMessage(PREFIX + getLocalized(NO_PERMISSION));
                return;
            }
            affected = caller;
        } else if(!hasOnePermissions(caller, PERM_CMD_RECOVER, PERM_CMD_RECOVER_OTHERS)) {
            caller.sendMessage(PREFIX + getLocalized(NO_PERMISSION));
            return;
        }
        DrugPlayer drugPlayer = DrugPlayer.getPlayer(affected.getUniqueId());
        if(drugPlayer != null && drugPlayer.getDose() <= 0) {
            drugPlayer.setDose(0);
            for (PotionEffect effect : affected.getActivePotionEffects()) {
                affected.removePotionEffect(effect.getType());
            }
            affected.sendMessage(PREFIX + getLocalized(RECOVERED));
        } else {
            caller.sendMessage(PREFIX + getLocalized(PLAYER_DIDNT_CONSUME, affected.getName()));
        }
        if(affected != caller) caller.sendMessage(PREFIX + getLocalized(RECOVERED_PLAYER, affected.getName()));
    }

    private static void listCommand(Player caller) {
        if(!hasOnePermissions(caller, PERM_CMD_LIST)) {
            caller.sendMessage(PREFIX + getLocalized(NO_PERMISSION));
            return;
        }

        DrugList list = DrugList.drugList;
        if(list == null) {
            list = new DrugList();
        }
        list.open(caller);
    }

    private static void giveCommand(Player caller, Player affected, String drugName) {
        if(affected == null) {
            if(!hasOnePermissions(caller, PERM_CMD_GIVE, PERM_CMD_GIVE_SELF)) {
                caller.sendMessage(PREFIX + getLocalized(NO_PERMISSION));
                return;
            }
            affected = caller;
        } else if(!hasOnePermissions(caller, PERM_CMD_GIVE, PERM_CMD_GIVE_OTHERS)) {
            caller.sendMessage(PREFIX + getLocalized(NO_PERMISSION));
            return;
        }
        MyDrug drug = MyDrug.getByName(drugName);
        if(drug == null) {
            caller.sendMessage(PREFIX + getLocalized(DRUG_NOT_EXIST, drugName));
            return;
        }
        affected.getInventory().addItem(drug.getItemStack());
        affected.sendMessage(PREFIX + getLocalized(GIVEN_DRUG, drug.getName()));
        if(affected != caller) caller.sendMessage(PREFIX + getLocalized(GAVE_DRUG, drug.getName(), affected.getName()));
    }

    private static void createCommand(Player caller, String drugName) {
        if(!hasOnePermissions(caller, PERM_CMD_CREATE)) {
            caller.sendMessage(PREFIX + getLocalized(NO_PERMISSION));
            return;
        }
        MyDrug drug = MyDrug.getByName(drugName);
        if(drug == null) {
            drug = new MyDrug(drugName, Material.BARRIER);
        } else {
            caller.sendMessage(PREFIX + getLocalized(DRUG_ALREADY_EXISTS, drug.getName()));
            return;
        }
        new DrugCraft(drug).open(caller);
        caller.playSound(caller.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 29);
    }

    private static void addictionsCommand(Player caller, Player affected, String sub, String drugName) {
        switch (sub) {
            case "clear" -> clearAddictionsCommand(caller, affected);
            case "list" -> listAddictionsCommand(caller, affected);
            case "add" -> addAddictionCommand(caller, affected, drugName);
            default -> caller.sendMessage(PREFIX + getLocalized(UNKNOWN_SUB_COMMAND, sub));
        }
    }

    private static void clearAddictionsCommand(Player caller, Player affected) {
        if(affected == null) {
            if(!hasOnePermissions(caller, PERM_CMD_ADDICTIONS, PERM_CMD_ADDICTIONS_CLEAR, PERM_CMD_ADDICTIONS_CLEAR_SELF)) {
                caller.sendMessage(PREFIX + getLocalized(NO_PERMISSION));
                return;
            }
            affected = caller;
        } else if(!hasOnePermissions(caller, PERM_CMD_ADDICTIONS, PERM_CMD_ADDICTIONS_CLEAR, PERM_CMD_ADDICTIONS_CLEAR_OTHERS)) {
            caller.sendMessage(PREFIX + getLocalized(NO_PERMISSION));
            return;
        }
        DrugPlayer drugPlayer = DrugPlayer.getPlayer(affected.getUniqueId());
        if(drugPlayer != null && drugPlayer.getAddictions().size() > 0) {
            drugPlayer.clear();
            affected.sendMessage(PREFIX + getLocalized(ADDICTIONS_GOT_CLEARED));
        } else {
            caller.sendMessage(PREFIX + getLocalized(PLAYER_HAS_NO_ADDICTIONS, affected.getName()));
            return;
        }
        if(affected != caller) caller.sendMessage(PREFIX + getLocalized(CLEARED_ADDICTIONS_OF_PLAYER, affected.getName()));
    }

    private static void listAddictionsCommand(Player caller, Player affected) {
        if(affected == null) {
            if(!hasOnePermissions(caller, PERM_CMD_ADDICTIONS, PERM_CMD_ADDICTIONS_LIST, PERM_CMD_ADDICTIONS_LIST_SELF)) {
                caller.sendMessage(PREFIX + getLocalized(NO_PERMISSION));
                return;
            }
            affected = caller;
        } else if(!hasOnePermissions(caller, PERM_CMD_ADDICTIONS, PERM_CMD_ADDICTIONS_LIST, PERM_CMD_ADDICTIONS_LIST_OTHERS)) {
            caller.sendMessage(PREFIX + getLocalized(NO_PERMISSION));
            return;
        }
        DrugPlayer drugPlayer = DrugPlayer.getPlayer(affected.getUniqueId());
        if(drugPlayer != null && drugPlayer.getAddictions().size() > 0) {
            caller.sendMessage(PREFIX + getLocalized(ADDICTIONS_OF, affected.getName()));
            for(Addiction addiction : drugPlayer.getAddictions()) {
                caller.sendMessage("§7- " + MyDrug.getById(addiction.getDrugId()).getName() + " (" + addiction.getIntensity() + ")");
            }
        } else {
            caller.sendMessage(PREFIX + getLocalized(PLAYER_HAS_NO_ADDICTIONS, affected.getName()));
        }
    }

    private static void addAddictionCommand(Player caller, Player affected, String drugName) {
        if(affected == null) {
            if(!hasOnePermissions(caller, PERM_CMD_ADDICTIONS, PERM_CMD_ADDICTIONS_ADD, PERM_CMD_ADDICTIONS_ADD_SELF)) {
                caller.sendMessage(PREFIX + getLocalized(NO_PERMISSION));
                return;
            }
            affected = caller;
        } else if(!hasOnePermissions(caller, PERM_CMD_ADDICTIONS, PERM_CMD_ADDICTIONS_ADD, PERM_CMD_ADDICTIONS_ADD_OTHERS)) {
            caller.sendMessage(PREFIX + getLocalized(NO_PERMISSION));
            return;
        }
        MyDrug drug = MyDrug.getByName(drugName);
        if(drug == null) {
            caller.sendMessage(PREFIX + getLocalized(DRUG_NOT_EXIST, drugName));
            return;
        }
        DrugPlayer drugPlayer = DrugPlayer.getPlayer(affected.getUniqueId());
        if(drugPlayer != null && drugPlayer.getAddicted(drug.getId()) == null) {
            drugPlayer.addAddiction(drug);
            affected.sendMessage(PREFIX + getLocalized(ADDICTED_TO, drug.getName()));
        } else {
            caller.sendMessage(PREFIX + getLocalized(UNKNOWN_PLAYER_OR_ALREADY_ADDICTED, affected.getName(), drug.getName()));
            return;
        }
        if(affected != caller) caller.sendMessage(PREFIX + getLocalized(ADDED_ADDICTION, drug.getName(), affected.getName()));
    }
}
