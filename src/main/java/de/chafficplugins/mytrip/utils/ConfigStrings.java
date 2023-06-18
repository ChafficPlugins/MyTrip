package de.chafficplugins.mytrip.utils;

import org.bukkit.ChatColor;

import java.util.UUID;

public class ConfigStrings {
    public final static int BSTATS_ID = 7038;
    public final static String CRUCIAL_API_VERSION = "2.2.0";
    public final static UUID DRUG_SET_UUID = UUID.fromString("2e116c45-8bd6-4297-a8c1-98041c08d39c");
    public final static UUID DRUG_TEST_UUID = UUID.fromString("764d1358-32d9-4f8b-af6c-c5d64de2bfd0");
    public final static UUID ANTITOXIN_UUID = UUID.fromString("8000f544-c0db-4af2-aea5-80fa8bc53aaa");
    public final static String PREFIX = ChatColor.GRAY + "§7MyTrip §8» " + ChatColor.RESET;
    public final static String DOWNLOAD_URL = "https://drive.google.com/uc?export=download&id=";
    public final static String DRUGS_JSON = "12KyNqYN5EeZ8DX5-OK6BwTX4DbAthFW2";
    public final static String TOOLS_JSON = "1cEON5EPORJDY9e8YONLROcM-iNnq689_";
    public final static String LOCALIZED_IDENTIFIER = "mytrip";

    //Messages
    public final static String ONLY_PLAYERS_CMD = "only_players_cmd"; //Only players can use this command
    public final static String DISABLE_DRUG_SET = "disable_drug_set"; //Only players can use this command
    public final static String ADDICTION_EFFECTS = "addiction_effects";
    public final static String OVERDOSE_EFFECTS = "overdose_effects";
    public final static String UNKNOWN_CMD = "unknown_cmd"; //Unknown command: {0}
    public final static String PLAYER_NOT_FOUND = "player_not_found"; //The player {0} was not found
    public final static String NO_PERMISSION = "no_permission"; //You don't have the permission to use this command.
    public final static String RECOVERED = "recoverd"; //You have been recovered!
    public final static String PLAYER_DIDNT_CONSUME = "player_didnt_consume"; //The player {0} didn't consume any drug
    public final static String RECOVERED_PLAYER = "recoverd_player"; //You have recovered {0}!
    public final static String DRUG_NOT_EXIST = "drug_not_exist"; //The drug {0} does not exist
    public final static String GIVEN_DRUG = "given_drug"; //You have been given {0}!
    public final static String GAVE_DRUG = "gave_drug"; //You gave {0} {1}!
    public final static String DRUG_ALREADY_EXISTS = "drug_already_exists"; //The drug {0} already exists
    public final static String UNKNOWN_SUB_COMMAND = "unknown_sub_command"; //Unknown sub command: {0}
    public final static String ADDICTIONS_GOT_CLEARED = "addictions_got_cleared"; //Your addictions got cleared!
    public final static String PLAYER_HAS_NO_ADDICTIONS = "player_has_no_addictions"; //The player {0} has no addictions!
    public final static String CLEARED_ADDICTIONS_OF_PLAYER = "cleared_addictions_of_player"; //You cleared the addictions of {0}!
    public final static String ADDICTIONS_OF = "addictions_of"; //Addictions of {0}:
    public final static String ADDICTED_TO = "addicted_to"; //You are addicted to {0}!
    public final static String UNKNOWN_PLAYER_OR_ALREADY_ADDICTED = "unknown_player_or_already_addicted"; //The player {0} was not found or he is already addicted to {0}!
    public final static String ADDED_ADDICTION = "added_addiction"; //You added {0} to {1}'s addictions!
    public final static String NO_PERMS_TO_DO_THIS = "no_perms_to_do_this"; //You don't have the permission to do this!
    public final static String IS_HIGH = "is_high"; //{0} is high!
    public final static String IS_NOT_HIGH = "is_not_high"; //{0} is not high!
    public static final String COULDNT_SAVE_DRUG = "couldnt_save_drug"; //Couldn't save drug {0}!
    public static final String ADDICTION = "addiction"; //Addiction
    public static final String ADDICTION_KICKS = "addiction_kicks"; //Your addiction to {0} kicks in!
    public static final String EFFECTS_START_IN = "effects_start_in"; //The drugs effects will start in {0} seconds!
    public static final String WAS_DELETED = "was_deleted"; //{0} was deleted!

    //Permissions
    public final static String PERM_ADMIN = "mytrip.*";
    public final static String PERM_USE_ANY = "mytrip.use.*";
    public final static String PERM_USE_DRUG_TEST = "mytrip.use.";
    public final static String PERM_USE_ANTITOXIN = "mytrip.use.";
    public final static String PERM_USE_DRUG_SET = "mytrip.use.";
    public final static String PERM_USE_ = "mytrip.use.";
    public final static String PERM_CRAFT_ANY = "mytrip.craft.*";
    public final static String PERM_CRAFT_ = "mytrip.craft.";
    public final static String PERM_CMD_HELP = "mytrip.help";
    public final static String PERM_CMD_LIST = "mytrip.list";
    public final static String PERM_CMD_RECOVER = "mytrip.recover";
    public final static String PERM_CMD_RECOVER_SELF = "mytrip.recover.self";
    public final static String PERM_CMD_RECOVER_OTHERS = "mytrip.recover.other";
    public final static String PERM_CMD_CREATE = "mytrip.create";
    public final static String PERM_CMD_GIVE = "mytrip.give";
    public final static String PERM_CMD_GIVE_SELF = "mytrip.give.self";
    public final static String PERM_CMD_GIVE_OTHERS = "mytrip.give.others";
    public final static String PERM_CMD_ADDICTIONS = "mytrip.addictions";
    public final static String PERM_CMD_ADDICTIONS_CLEAR = "mytrip.addictions.clear";
    public final static String PERM_CMD_ADDICTIONS_CLEAR_SELF = "mytrip.addictions.clear.self";
    public final static String PERM_CMD_ADDICTIONS_CLEAR_OTHERS = "mytrip.addictions.clear.others";
    public final static String PERM_CMD_ADDICTIONS_LIST = "mytrip.addictions.list";
    public final static String PERM_CMD_ADDICTIONS_LIST_SELF = "mytrip.addictions.list.self";
    public final static String PERM_CMD_ADDICTIONS_LIST_OTHERS = "mytrip.addictions.list.others";
    public final static String PERM_CMD_ADDICTIONS_ADD = "mytrip.addictions.add";
    public final static String PERM_CMD_ADDICTIONS_ADD_SELF = "mytrip.addictions.add.self";
    public final static String PERM_CMD_ADDICTIONS_ADD_OTHERS = "mytrip.addictions.add.others";

    //Config
    public final static String FEATURE_HEAL_ON_DEATH = "features.heal_on_death";
    public final static String SETTING_PERMISSIONS = "settings.permissions";
    public final static String SETTING_ALERTS = "settings.update-alerts";
}
