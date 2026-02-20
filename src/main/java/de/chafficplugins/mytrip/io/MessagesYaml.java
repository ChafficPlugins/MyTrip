package de.chafficplugins.mytrip.io;

import de.chafficplugins.mytrip.MyTrip;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import static de.chafficplugins.mytrip.utils.ConfigStrings.*;

public class MessagesYaml {
    private static final MyTrip plugin = MyTrip.getInstance();

    public static void create() throws IOException {
        File messageFile = new File(plugin.getDataFolder(), "messages.yml");
        if(!messageFile.exists()) {
            if(!messageFile.getParentFile().mkdirs()) plugin.warning("Couldn't create messages.yml");
            plugin.saveResource("messages.yml", false);
        }
        YamlConfiguration messages = YamlConfiguration.loadConfiguration(messageFile);

        messages.addDefault(ONLY_PLAYERS_CMD, "Only players can use this command!");
        messages.addDefault(UNKNOWN_CMD, "Unknown command: {0}");
        messages.addDefault(PLAYER_NOT_FOUND, "The player {0} was not found");
        messages.addDefault(NO_PERMISSION, "You don't have the permission to use this command.");
        messages.addDefault(RECOVERED, "You have been recovered!");
        messages.addDefault(PLAYER_DIDNT_CONSUME, "The player {0} didn't consume any drug");
        messages.addDefault(RECOVERED_PLAYER, "You have recovered {0}!");
        messages.addDefault(DRUG_NOT_EXIST, "The drug {0} does not exist");
        messages.addDefault(GIVEN_DRUG, "You have been given {0}!");
        messages.addDefault(GAVE_DRUG, "You gave {0} {1}!");
        messages.addDefault(DRUG_ALREADY_EXISTS, "The drug {0} already exists");
        messages.addDefault(UNKNOWN_SUB_COMMAND, "Unknown sub command: {0}");
        messages.addDefault(ADDICTIONS_GOT_CLEARED, "Your addictions got cleared!");
        messages.addDefault(PLAYER_HAS_NO_ADDICTIONS, "The player {0} has no addictions!");
        messages.addDefault(CLEARED_ADDICTIONS_OF_PLAYER, "You cleared the addictions of {0}!");
        messages.addDefault(ADDICTIONS_OF, "Addictions of {0}: ");
        messages.addDefault(ADDICTED_TO, "You are addicted to {0}!");
        messages.addDefault(UNKNOWN_PLAYER_OR_ALREADY_ADDICTED, "The player {0} was not found or he is already addicted to {0}!");
        messages.addDefault(ADDED_ADDICTION, "You added {0} to {1}'s addictions!");
        messages.addDefault(NO_PERMS_TO_DO_THIS, "You don't have the permission to do this!");
        messages.addDefault(IS_HIGH, "{0} is high!");
        messages.addDefault(IS_NOT_HIGH, "{0} is not high!");
        messages.addDefault(COULDNT_SAVE_DRUG, "Couldn't save drug {0}!");
        messages.addDefault(ADDICTION, "Addiction");
        messages.addDefault(ADDICTION_KICKS, "Your addiction to {0} kicks in!");
        messages.addDefault(EFFECTS_START_IN, "The drugs effects will start in {0} seconds!");
        messages.addDefault(WAS_DELETED, "{0} was deleted!");

        messages.options().copyDefaults(true);
        messages.save(messageFile);
    }
}
