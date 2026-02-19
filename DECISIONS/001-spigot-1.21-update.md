# Decision 001: Update to Spigot 1.21 + Java 21

## Status
Accepted

## Context
MyTrip was built against Spigot 1.20.1 API with Java 14/16 as the compiler target. The plugin checked compatibility for server versions 1.15–1.20. As Minecraft 1.21 has become the standard, the plugin needed to be updated to support the latest server version and take advantage of modern Java features.

CrucialLib (formerly CrucialAPI) also released v3.0.0 requiring Java 21 and Spigot 1.21+, making this update necessary to stay compatible with the dependency.

## Decision
- Update Spigot API dependency from `1.20.1-R0.1-SNAPSHOT` to `1.21.4-R0.1-SNAPSHOT`
- Update Java compiler source/target from 14 to 21
- Update `java.version` property from 16 to 21
- Update CrucialAPI dependency to CrucialLib v3.0.0 (`com.github.ChafficPlugins:CrucialLib:v3.0.0`)
- Update `api-version` in plugin.yml from `1.15` to `1.21`
- Add PaperMC repository alongside existing Spigot repository
- Update the Spigot repository URL to the public group URL

## Specific API Changes Made

### CrucialAPI → CrucialLib Migration
- All imports changed from `io.github.chafficui.CrucialAPI` to `io.github.chafficui.CrucialLib`
- `CRUCIAL_API_VERSION` constant renamed to `CRUCIAL_LIB_VERSION` and updated to `"3.0.0"`
- `Crucial.java` updated: all references to "CrucialAPI" in log messages changed to "CrucialLib", download URLs updated to GitHub
- `plugin.yml` softdepend kept as `CrucialAPI` (CrucialLib registers as CrucialAPI for backward compatibility)

### Spigot 1.21 API Changes
- `PotionEffectType.DAMAGE_RESISTANCE` → `PotionEffectType.RESISTANCE` (in `DrugToolEvents.java`)
- `PotionEffectType.getByName()` calls retained for dynamic effect resolution from config/stored data (deprecated but functional in 1.21, maintains backward compatibility with existing drug configs)
- Server compatibility check in `MyTrip.onEnable()` updated to include `"1.21"`

### Build Configuration
- `maven-compiler-plugin` source/target updated from 14 to 21
- Added `maven-surefire-plugin` v3.2.5 for test execution
- Added PaperMC Maven repository
- Updated Spigot repository URL

## Consequences

### Positive
- Plugin now runs on Minecraft 1.21.x servers
- Compatible with CrucialLib v3.0.0 and its features
- Can use Java 21 language features (records, sealed classes, pattern matching, etc.)
- Better alignment with the current Minecraft server ecosystem

### Negative
- Minimum Java version is now 21 (server operators must upgrade their Java runtime)
- Server operators must update to CrucialLib v3.0.0 (old CrucialAPI versions are incompatible)
- `PotionEffectType.getByName()` is deprecated and may be removed in a future Spigot version — existing drug configs using old effect names (CONFUSION, SLOW, DAMAGE_RESISTANCE, etc.) will need migration when this method is removed

### Risks
- Existing drug configs stored in JSON may reference old PotionEffectType names — these still resolve via `getByName()` but a future Spigot update could break them
- The Google Drive download URLs for default drug/tool data may become stale
