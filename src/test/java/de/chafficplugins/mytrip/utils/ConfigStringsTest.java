package de.chafficplugins.mytrip.utils;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ConfigStringsTest {

    @Test
    void crucialLibVersion_is3_0_0() {
        assertEquals("3.0.0", ConfigStrings.CRUCIAL_LIB_VERSION);
    }

    @Test
    void drugSetUuid_isValid() {
        assertNotNull(ConfigStrings.DRUG_SET_UUID);
        assertEquals(UUID.fromString("2e116c45-8bd6-4297-a8c1-98041c08d39c"), ConfigStrings.DRUG_SET_UUID);
    }

    @Test
    void drugTestUuid_isValid() {
        assertNotNull(ConfigStrings.DRUG_TEST_UUID);
        assertEquals(UUID.fromString("764d1358-32d9-4f8b-af6c-c5d64de2bfd0"), ConfigStrings.DRUG_TEST_UUID);
    }

    @Test
    void antitoxinUuid_isValid() {
        assertNotNull(ConfigStrings.ANTITOXIN_UUID);
        assertEquals(UUID.fromString("8000f544-c0db-4af2-aea5-80fa8bc53aaa"), ConfigStrings.ANTITOXIN_UUID);
    }

    @Test
    void bstatsId_isSet() {
        assertEquals(7038, ConfigStrings.BSTATS_ID);
    }

    @Test
    void permissionStrings_areNotNull() {
        assertNotNull(ConfigStrings.PERM_ADMIN);
        assertNotNull(ConfigStrings.PERM_USE_ANY);
        assertNotNull(ConfigStrings.PERM_CRAFT_ANY);
        assertNotNull(ConfigStrings.PERM_CMD_HELP);
        assertNotNull(ConfigStrings.PERM_CMD_LIST);
        assertNotNull(ConfigStrings.PERM_CMD_RECOVER);
        assertNotNull(ConfigStrings.PERM_CMD_CREATE);
        assertNotNull(ConfigStrings.PERM_CMD_GIVE);
        assertNotNull(ConfigStrings.PERM_CMD_ADDICTIONS);
    }

    @Test
    void configKeys_areNotNull() {
        assertNotNull(ConfigStrings.FEATURE_HEAL_ON_DEATH);
        assertNotNull(ConfigStrings.SETTING_PERMISSIONS);
        assertNotNull(ConfigStrings.SETTING_ALERTS);
        assertNotNull(ConfigStrings.DISABLE_DRUG_SET);
        assertNotNull(ConfigStrings.ADDICTION_EFFECTS);
        assertNotNull(ConfigStrings.OVERDOSE_EFFECTS);
    }

    @Test
    void messageKeys_areNotNull() {
        assertNotNull(ConfigStrings.ONLY_PLAYERS_CMD);
        assertNotNull(ConfigStrings.UNKNOWN_CMD);
        assertNotNull(ConfigStrings.PLAYER_NOT_FOUND);
        assertNotNull(ConfigStrings.NO_PERMISSION);
        assertNotNull(ConfigStrings.RECOVERED);
        assertNotNull(ConfigStrings.DRUG_NOT_EXIST);
        assertNotNull(ConfigStrings.GIVEN_DRUG);
        assertNotNull(ConfigStrings.GAVE_DRUG);
        assertNotNull(ConfigStrings.DRUG_ALREADY_EXISTS);
        assertNotNull(ConfigStrings.ADDICTIONS_GOT_CLEARED);
        assertNotNull(ConfigStrings.IS_HIGH);
        assertNotNull(ConfigStrings.IS_NOT_HIGH);
        assertNotNull(ConfigStrings.WAS_DELETED);
    }

    @Test
    void adminPermission_isWildcard() {
        assertEquals("mytrip.*", ConfigStrings.PERM_ADMIN);
    }

    @Test
    void permissionPrefixes_areCorrect() {
        assertTrue(ConfigStrings.PERM_USE_ANY.startsWith("mytrip.use."));
        assertTrue(ConfigStrings.PERM_CRAFT_ANY.startsWith("mytrip.craft."));
        assertTrue(ConfigStrings.PERM_CRAFT_.startsWith("mytrip.craft."));
        assertTrue(ConfigStrings.PERM_USE_.startsWith("mytrip.use."));
    }
}
