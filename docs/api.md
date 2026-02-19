# Developer API

MyTrip provides an event-based API that other plugins can use to hook into drug-related actions.

## Maven Dependency

MyTrip is available via [JitPack](https://jitpack.io/). Add the following to your `pom.xml`:

### Repository

```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

### Dependency

```xml
<dependency>
    <groupId>com.github.ChafficPlugins</groupId>
    <artifactId>MyTrip</artifactId>
    <version>VERSION</version>
    <scope>provided</scope>
</dependency>
```

Replace `VERSION` with the desired release tag (e.g., `0.7.6`). Check [JitPack](https://jitpack.io/#ChafficPlugins/MyTrip) for available versions.

> **Note:** MyTrip depends on [CrucialLib](https://github.com/ChafficPlugins/CrucialLib) v3.0.0+. If your plugin interacts with MyTrip's custom item types, you may need CrucialLib as a dependency as well:
>
> ```xml
> <dependency>
>     <groupId>com.github.ChafficPlugins</groupId>
>     <artifactId>CrucialLib</artifactId>
>     <version>v3.0.0</version>
>     <scope>provided</scope>
> </dependency>
> ```

---

## API Events

MyTrip uses a custom event system. All event classes are in the `de.chafficplugins.mytrip.api` package. To listen for events, extend one of the event classes and register your listener with `APICaller.registerEvent()`.

### Event Classes

#### DrugAPIEvents

Hook into drug consumption lifecycle events.

```java
import de.chafficplugins.mytrip.api.DrugAPIEvents;
import de.chafficplugins.mytrip.drugs.objects.MyDrug;
import org.bukkit.entity.Player;

public class MyDrugListener extends DrugAPIEvents {
    @Override
    public boolean onDrugStart(Player player, MyDrug drug) {
        // Called when a player starts consuming a drug.
        // Return true to cancel the drug consumption.
        return false;
    }

    @Override
    public void onDrugEnd(Player player, MyDrug drug) {
        // Called when a drug's effects wear off on a player.
    }
}
```

#### DrugToolAPIEvents

Hook into drug tool interactions (drug tests, anti-toxin, drug set crafting).

```java
import de.chafficplugins.mytrip.api.DrugToolAPIEvents;
import de.chafficplugins.mytrip.drugs.objects.DrugPlayer;
import io.github.chafficui.CrucialLib.Utils.customItems.CrucialItem;
import org.bukkit.block.BlockState;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;

public class MyToolListener extends DrugToolAPIEvents {
    @Override
    public boolean onDrugTest(Player tester, DrugPlayer tested, boolean isPositive) {
        // Called when a drug test is performed.
        // Return true to cancel the drug test.
        return false;
    }

    @Override
    public boolean onAntiToxin(Player player, Collection<PotionEffect> activePotionEffects) {
        // Called when a player uses anti-toxin.
        // Return true to cancel the anti-toxin usage.
        return false;
    }

    @Override
    public boolean onDrugCraftPrepare(LivingEntity entity, BlockState drugSetBlock,
                                       CrucialItem result, CraftingInventory drugSetInventory) {
        // Called when a drug is being crafted in a Drug Set.
        // Return true to cancel the crafting.
        return false;
    }
}
```

#### DrugSetAPIEvents

Hook into Drug Set block interactions.

```java
import de.chafficplugins.mytrip.api.DrugSetAPIEvents;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

public class MyDrugSetListener extends DrugSetAPIEvents {
    @Override
    public boolean onDrugSetOpen(Player player, BlockState state) {
        // Called when a player opens a Drug Set crafting station.
        // Return true to cancel opening the Drug Set.
        return false;
    }
}
```

### Registering Events

Use `APICaller.registerEvent()` to register your event listener, and `APICaller.unregisterEvent()` to remove it:

```java
import de.chafficplugins.mytrip.api.APICaller;
import org.bukkit.plugin.java.JavaPlugin;

public class MyPlugin extends JavaPlugin {
    private MyDrugListener drugListener;

    @Override
    public void onEnable() {
        drugListener = new MyDrugListener();
        APICaller.registerEvent(drugListener);
        APICaller.registerEvent(new MyToolListener());
        APICaller.registerEvent(new MyDrugSetListener());
    }

    @Override
    public void onDisable() {
        APICaller.unregisterEvent(drugListener);
    }
}
```

> **Important:** Make sure MyTrip is listed as a `depend` or `softdepend` in your plugin's `plugin.yml` so it loads before your plugin.

---

## plugin.yml Example

```yaml
name: MyAddon
version: 1.0
main: com.example.myaddon.MyPlugin
depend: [MyTrip]
```

---

## Source Code

For more details on available classes and methods, browse the [source code on GitHub](https://github.com/ChafficPlugins/MyTrip/tree/master/src/main/java/de/chafficplugins/mytrip/api).
