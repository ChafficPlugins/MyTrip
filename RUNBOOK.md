# Runbook

## Release Process

1. **Version bump**: Update `<version>` in `pom.xml`
2. **Build**: `mvn clean verify` — ensure all tests pass
3. **Tag**: `git tag v<version>` and push: `git push origin v<version>`
4. **GitHub Release**: Create a release on GitHub with the tag, attach the JAR from `target/MyTrip-v<version>.jar`
5. **SpigotMC**: Upload the new JAR to the [SpigotMC resource page](https://www.spigotmc.org/resources/)
6. **Wiki**: Update version references if needed

## Adding a New Drug Type

### Via In-Game GUI (Recommended)
1. Run `/mt create <drugname>` as an OP player
2. Place ingredient items in the 3x3 crafting grid
3. Place the result item in the output slot
4. Click "Continue" to proceed to effect selection
5. Click effects to add them to the drug
6. Click "CREATE" to register the drug
7. Edit properties (duration, delay, overdose, addiction) via `/mt list` → select drug → "EDIT"

### Via Code
1. Create a `MyDrug` instance:
   ```java
   MyDrug drug = new MyDrug("DrugName", Material.SUGAR);
   drug.setDuration(100);       // seconds
   drug.setEffectDelay(2);      // seconds before effects start
   drug.setOverdose(5);         // doses before overdose (0-99)
   drug.setAddictionProbability(10); // percent chance per use (0-100)
   drug.addEffect(new String[]{"SPEED", "1"});
   drug.addEffect(new String[]{"JUMP", "2"});
   drug.setRecipe(new ItemStack[]{...}); // 9 slots
   drug.register();
   ```

2. The drug will be auto-saved to `drugs.json` on server shutdown

### Via JSON (Advanced)
1. Edit `plugins/MyTrip/do not edit/drugs.json` while the server is stopped
2. Follow the existing drug format in the JSON array
3. Effect names use Bukkit PotionEffectType names (SPEED, BLINDNESS, etc.)
4. Start the server — drugs will be loaded and registered

## Common Issues

### Plugin fails to enable with "CrucialLib not found"
- Ensure CrucialLib v3.0.0+ JAR is in the `plugins/` folder
- Check that CrucialLib loads before MyTrip (it's listed as a softdepend)
- Verify CrucialLib version matches: MyTrip requires 3.0.x

### "Error 26: Wrong version of CrucialLib"
- Download CrucialLib v3.0.0 or higher from [GitHub](https://github.com/ChafficPlugins/CrucialLib)
- Remove old CrucialAPI JAR if present

### Drug set disabled automatically
- This happens when the server is in offline mode (`online-mode=false` in server.properties)
- Drug set requires online mode for UUID-based player identification
- Workaround: Drugs can still be crafted in regular crafting tables when drug set is disabled

### Drugs appear red in /mt list
- Red name = unregistered drug
- Check console for registration errors
- Common cause: invalid material name in drug data
- Fix: edit `drugs.json` or delete and recreate the drug

### Players can't craft drugs
- Check if `settings.permissions` is `true` in config.yml
- If yes, players need `mytrip.craft.<drugname>` or `mytrip.craft.*` permission
- If drug set is enabled, players need to hold the drug set item while crafting

### Milk bucket doesn't clear effects
- This is intentional: milk consumption is blocked while the player has an active drug dose
- Use antitoxin item to clear effects instead

## Emergency: Reverting a Bad Release

1. **Immediate**: Replace the plugin JAR with the previous working version and restart
2. **Data safety**: Drug/tool/player data in `plugins/MyTrip/do not edit/` is forward-compatible but check for corruption
3. **Rollback tag**: `git revert <commit>` or `git checkout v<previous-version>`
4. **Re-release**: Build from the reverted commit and re-upload

## Updating CrucialLib Dependency

1. Update the version in `pom.xml`:
   ```xml
   <dependency>
       <groupId>com.github.ChafficPlugins</groupId>
       <artifactId>CrucialLib</artifactId>
       <version>v<new-version></version>
       <scope>provided</scope>
   </dependency>
   ```
2. Update `CRUCIAL_LIB_VERSION` in `ConfigStrings.java` to match
3. Check the [CrucialLib migration docs](https://chafficplugins.github.io/docs/cruciallib/migrations/) for API changes
4. Update import statements if package structure changed
5. Run `mvn clean verify` to ensure compatibility
6. Update `Crucial.java` version check logic if the major version changed
