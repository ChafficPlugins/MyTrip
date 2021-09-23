package de.chaffic.MyTrip.Inventories;

import de.chaffic.MyTrip.API.GUIs.ClickableItem;
import de.chaffic.MyTrip.API.GUIs.SmartInventory;
import de.chaffic.MyTrip.API.GUIs.content.InventoryContents;
import de.chaffic.MyTrip.API.GUIs.content.InventoryProvider;
import de.chaffic.MyTrip.API.Objects.MyDrug;
import de.chaffic.MyTrip.Main;
import io.github.chafficui.CrucialAPI.Utils.customItems.CrucialItem;
import io.github.chafficui.CrucialAPI.Utils.customItems.Stack;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class DrugEditor implements InventoryProvider {

    private final Main PLUGIN = Main.getPlugin(Main.class);
    private MyDrug myDrug;

    public SmartInventory getDrugEditor(MyDrug drug){
        return SmartInventory.builder()
                .id("DrugEditor")
                .provider(new DrugEditor())
                .size(6, 9)
                .title(drug.getName() + " - Editor")
                .closeable(false)
                .manager(PLUGIN.GUIAPI)
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        myDrug = MyDrug.getByName(contents.inventory().getTitle().split(" - ")[0]);

        contents.fill(ClickableItem.empty(Stack.getStack(Material.GRAY_STAINED_GLASS_PANE, "")));


        //navigation
        contents.set(5, 8, ClickableItem.of(Stack.getStack(Material.GREEN_STAINED_GLASS_PANE, "CONTINUE"), e -> {
            if (e.isLeftClick()) {
                Player p = (Player) e.getWhoClicked();
                p.playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 29);
                PLUGIN.fc.saveItems();
                contents.inventory().close(player);
                List.drugsInv.open(player);
            }
        }));

        //overdose
        contents.set(0, 2, ClickableItem.empty(Stack.getStack(Material.WITHER_SKELETON_SKULL, "Overdose", Arrays.asList("Amount: " + myDrug.getOverdose(), "Defines how many drugs of this type", "a player must take for an overdose."))));
        contents.set(0, 5, ClickableItem.of(Stack.getStack("saidus2", "-1", null), e -> {
            Player p = (Player) e.getWhoClicked();
            ((MyDrug) CrucialItem.getByKey(myDrug.getKey())).alterOverdose(-1);
            p.playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 29);
        }));
        contents.set(0, 6, ClickableItem.of(Stack.getStack("natatos", "+1", null), e -> {
            Player p = (Player) e.getWhoClicked();
            ((MyDrug) CrucialItem.getByKey(myDrug.getKey())).alterOverdose(1);
            p.playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 29);
        }));
        //delay
        contents.set(1, 2, ClickableItem.empty(Stack.getStack(Material.SAND, "Delay", Arrays.asList("Seconds: " + myDrug.getEffectDelay(), "Defines how much time elapses between", "ingestion of the drug and onset of action."))));
        contents.set(1, 5, ClickableItem.of(Stack.getStack("saidus2", "-1", null), e -> {
            Player p = (Player) e.getWhoClicked();
            ((MyDrug) CrucialItem.getByKey(myDrug.getKey())).alterEffectDelay(-1);
            p.playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 29);
        }));
        contents.set(1, 6, ClickableItem.of(Stack.getStack("natatos", "+1", null), e -> {
            Player p = (Player) e.getWhoClicked();
            ((MyDrug) CrucialItem.getByKey(myDrug.getKey())).alterEffectDelay(1);
            p.playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 29);
        }));
        //duration
        contents.set(2, 2, ClickableItem.empty(Stack.getStack(Material.CLOCK, "Duration", Arrays.asList("Seconds: " + myDrug.getDuration(), "Defines how long the effects should", "work."))));
        contents.set(2, 5, ClickableItem.of(Stack.getStack("saidus2", "-1", null), e -> {
            Player p = (Player) e.getWhoClicked();
            ((MyDrug) CrucialItem.getByKey(myDrug.getKey())).alterDuration(-1);
            p.playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 29);
        }));
        contents.set(2, 6, ClickableItem.of(Stack.getStack("natatos", "+1", null), e -> {
            Player p = (Player) e.getWhoClicked();
            ((MyDrug) CrucialItem.getByKey(myDrug.getKey())).alterDuration(1);
            p.playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 29);
        }));
        //addiction
        contents.set(3, 2, ClickableItem.empty(Stack.getStack(Material.ARROW, "Addiction", Arrays.asList("Probability: " + myDrug.getAddict(), "Determines the likelihood that a player", "will become addicted."))));
        contents.set(3, 5, ClickableItem.of(Stack.getStack("saidus2", "-1", null), e -> {
            Player p = (Player) e.getWhoClicked();
            ((MyDrug) CrucialItem.getByKey(myDrug.getKey())).alterAddict(-1);
            p.playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 29);
        }));
        contents.set(3, 6, ClickableItem.of(Stack.getStack("natatos", "+1", null), e -> {
            Player p = (Player) e.getWhoClicked();
            ((MyDrug) CrucialItem.getByKey(myDrug.getKey())).alterAddict(1);
            p.playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 29);
        }));
        //isBloody
        contents.set(4, 2, ClickableItem.empty(Stack.getStack(Material.WITHER_SKELETON_SKULL, "Bloody", Arrays.asList("State: " + myDrug.isBloody(), "Determines whether the drug triggers a blood", "effect."))));
        contents.set(4, 5, ClickableItem.of(Stack.getStack(Material.RED_STAINED_GLASS_PANE, "false"), e -> {
            Player p = (Player) e.getWhoClicked();
            ((MyDrug) CrucialItem.getByKey(myDrug.getKey())).alterAddict(-1);
            p.playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 29);
        }));
        contents.set(4, 6, ClickableItem.of(Stack.getStack(Material.GREEN_STAINED_GLASS_PANE, "true"), e -> {
            Player p = (Player) e.getWhoClicked();
            ((MyDrug) CrucialItem.getByKey(myDrug.getKey())).alterAddict(1);
            p.playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 29);
        }));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        int state = contents.property("state", 0);
        contents.setProperty("state", state + 1);

        if (state % 5 != 0)
            return;

        contents.set(0, 2, ClickableItem.empty(Stack.getStack(Material.WITHER_SKELETON_SKULL, "Overdose", Arrays.asList("Amount: " + myDrug.getOverdose(), "Defines how many drugs of this type", "a player must take for an overdose."))));
        contents.set(1, 2, ClickableItem.empty(Stack.getStack(Material.SAND, "Delay", Arrays.asList("Seconds: " + myDrug.getEffectDelay(), "Defines how much time elapses between", "ingestion of the drug and onset of action."))));
        contents.set(2, 2, ClickableItem.empty(Stack.getStack(Material.CLOCK, "Duration", Arrays.asList("Seconds: " + myDrug.getDuration(), "Defines how long the effects should", "work."))));
        contents.set(3, 2, ClickableItem.empty(Stack.getStack(Material.ARROW, "Addiction", Arrays.asList("Probability: " + myDrug.getAddict(), "Determines the likelihood that a player", "will become addicted."))));
        contents.set(4, 2, ClickableItem.empty(Stack.getStack(Material.WITHER_SKELETON_SKULL, "Bloody", Arrays.asList("State: " + myDrug.isBloody(), "Determines whether the drug triggers a blood", "effect."))));
    }
}
