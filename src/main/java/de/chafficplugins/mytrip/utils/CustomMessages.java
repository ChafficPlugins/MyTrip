package de.chafficplugins.mytrip.utils;

import de.chafficplugins.mytrip.MyTrip;
import io.github.chafficui.CrucialAPI.Utils.localization.LocalizedFromYaml;
import io.github.chafficui.CrucialAPI.Utils.localization.Localizer;

import java.io.IOException;

import static de.chafficplugins.mytrip.utils.ConfigStrings.LOCALIZED_IDENTIFIER;

public class CustomMessages extends LocalizedFromYaml {
    private static final MyTrip plugin = MyTrip.getPlugin(MyTrip.class);

    public CustomMessages() throws IOException {
        super(LOCALIZED_IDENTIFIER, plugin.getDataFolder(), "messages.yml");
    }


    public static String getLocalized(String key, String... values) {
        return Localizer.getLocalizedString(LOCALIZED_IDENTIFIER + "_" + key, values);
    }
}
