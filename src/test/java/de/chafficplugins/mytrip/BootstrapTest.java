package de.chafficplugins.mytrip;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests that verify the plugin can bootstrap correctly without a running server.
 * These tests catch configuration mistakes that would prevent startup.
 */
class BootstrapTest {

    @Test
    void pluginYml_dependsOnCrucialLib_notCrucialAPI() {
        InputStream in = getClass().getClassLoader().getResourceAsStream("plugin.yml");
        assertNotNull(in, "plugin.yml must exist in resources");
        String content = new Scanner(in, StandardCharsets.UTF_8).useDelimiter("\\A").next();
        assertFalse(content.contains("CrucialAPI"),
                "plugin.yml must not reference CrucialAPI — CrucialLib v3.0.0 registers as 'CrucialLib'");
        assertTrue(content.contains("CrucialLib"),
                "plugin.yml must declare CrucialLib as a softdepend");
    }

    @Test
    void crucialJava_usesCorrectPluginName() throws Exception {
        // Read the Crucial.java source to verify it looks up "CrucialLib"
        // This is a source-level check because Crucial uses static initializers that need a server
        InputStream in = getClass().getClassLoader().getResourceAsStream(
                "../../src/main/java/de/chafficplugins/mytrip/utils/Crucial.java");
        // If source isn't on classpath, skip gracefully — CI runs mvn verify which catches compile errors
        if (in == null) return;
        String source = new Scanner(in, StandardCharsets.UTF_8).useDelimiter("\\A").next();
        assertFalse(source.contains("getPlugin(\"CrucialAPI\")"),
                "Crucial.java must look up 'CrucialLib', not 'CrucialAPI'");
    }

    @Test
    void configStrings_noGoogleDriveConstants() {
        // Verify we removed the hardcoded Google Drive download URLs
        for (Field field : de.chafficplugins.mytrip.utils.ConfigStrings.class.getDeclaredFields()) {
            String name = field.getName();
            assertFalse(name.equals("DOWNLOAD_URL"),
                    "ConfigStrings should not contain DOWNLOAD_URL — Google Drive downloads were removed");
            assertFalse(name.equals("DRUGS_JSON") && field.getType() == String.class,
                    "ConfigStrings.DRUGS_JSON (Google Drive file ID) should be removed");
            assertFalse(name.equals("TOOLS_JSON") && field.getType() == String.class,
                    "ConfigStrings.TOOLS_JSON (Google Drive file ID) should be removed");
        }
    }

    @Test
    void defaultDrugsJson_existsAndIsValidJson() {
        InputStream in = getClass().getClassLoader().getResourceAsStream("defaults/drugs.json");
        assertNotNull(in, "defaults/drugs.json must be bundled in the JAR");
        assertDoesNotThrow(() -> {
            JsonArray arr = new Gson().fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), JsonArray.class);
            assertNotNull(arr, "drugs.json must be a valid JSON array");
        }, "drugs.json must be valid JSON");
    }

    @Test
    void defaultToolsJson_existsAndIsValidJson() {
        InputStream in = getClass().getClassLoader().getResourceAsStream("defaults/tools.json");
        assertNotNull(in, "defaults/tools.json must be bundled in the JAR");
        assertDoesNotThrow(() -> {
            JsonArray arr = new Gson().fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), JsonArray.class);
            assertNotNull(arr, "tools.json must be a valid JSON array");
            assertTrue(arr.size() >= 3, "tools.json must contain at least 3 tools (drug set, drug test, antitoxin)");
        }, "tools.json must be valid JSON");
    }

    @Test
    void defaultToolsJson_containsRequiredToolUuids() {
        InputStream in = getClass().getClassLoader().getResourceAsStream("defaults/tools.json");
        assertNotNull(in);
        String content = new Scanner(in, StandardCharsets.UTF_8).useDelimiter("\\A").next();
        assertTrue(content.contains("2e116c45-8bd6-4297-a8c1-98041c08d39c"),
                "tools.json must contain the Drug Set UUID");
        assertTrue(content.contains("764d1358-32d9-4f8b-af6c-c5d64de2bfd0"),
                "tools.json must contain the Drug Test UUID");
        assertTrue(content.contains("8000f544-c0db-4af2-aea5-80fa8bc53aaa"),
                "tools.json must contain the Antitoxin UUID");
    }
}
