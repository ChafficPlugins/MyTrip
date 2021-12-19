package de.chaffic.mytrip.utils;

import org.bukkit.ChatColor;

import java.util.UUID;

public class ConfigStrings {
    public final static int SPIGOT_ID = 76816;
    public final static int BSTATS_ID = 7038;
    public final static String CRUCIAL_API_VERSION = "2.1.0";
    public final static UUID DRUG_SET_UUID = UUID.fromString("2e116c45-8bd6-4297-a8c1-98041c08d39c");
    public final static UUID DRUG_TEST_UUID = UUID.fromString("764d1358-32d9-4f8b-af6c-c5d64de2bfd0");
    public final static UUID ANTITOXIN_UUID = UUID.fromString("8000f544-c0db-4af2-aea5-80fa8bc53aaa");
    public final static String PREFIX = ChatColor.GRAY + "[" + ChatColor.WHITE + "MyTrip" + ChatColor.GRAY + "] " + ChatColor.RESET;
    public final static String DOWNLOAD_URL = "https://drive.google.com/uc?export=download&id=";
    public final static String DRUGS_JSON = "12KyNqYN5EeZ8DX5-OK6BwTX4DbAthFW2";
    public final static String TOOLS_JSON = "1cEON5EPORJDY9e8YONLROcM-iNnq689_";

    //Permissions
    public final static String PERM_ADMIN = "mytrip.*";
    public final static String PERM_USE_ANY = "mytrip.use.*";
    public final static String PERM_USE_DRUG_TEST = "mytrip.use.";
    public final static String PERM_USE_ANTITOXIN = "mytrip.use.";
    public final static String PERM_USE_DRUG_SET = "mytrip.use.";
    public final static String PERM_USE_ = "mytrip.use.";
    public final static String PERM_CRAFT_ANY = "mytrip.craft.*";
    public final static String PERM_CRAFT_ = "mytrip.craft.";

    //Config
    public final static String FEATURE_HEAL_ON_DEATH = "features.heal_on_death";
    public final static String SETTING_PERMISSIONS = "settings.permissions";
    public final static String SETTING_ALERTS = "settings.update-alerts";
}
