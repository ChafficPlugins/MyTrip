package de.chafficplugins.mytrip.drugs.objects;

import com.google.gson.reflect.TypeToken;
import de.chafficplugins.mytrip.MyTrip;
import io.github.chafficui.CrucialLib.Utils.customItems.CrucialItem;
import io.github.chafficui.CrucialLib.exceptions.CrucialException;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.IOException;
import java.util.*;

import static de.chafficplugins.mytrip.utils.ConfigStrings.*;
import static de.chafficplugins.mytrip.utils.CustomMessages.getLocalized;

public class MyDrug extends CrucialItem {
    private ArrayList<String[]> effects = new ArrayList<>();
    private ArrayList<String> commands = new ArrayList<>();
    private long duration;
    private long effectDelay;
    private int overdose;
    private String particle;
    private int addict;

    public MyDrug(String name, String head) {
        super("drug");
        this.name = name;
        this.material = head;
        unregisteredDrugs.add(this);
    }

    public MyDrug(String name, Material material) {
        super("drug");
        this.name = name;
        this.material = material.name();
        unregisteredDrugs.add(this);
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack stack = super.getItemStack();
        ItemMeta meta = stack.getItemMeta();
        if (meta != null) {
            if (isRegistered) {
                meta.setDisplayName(ChatColor.WHITE + meta.getDisplayName());
            } else {
                meta.setDisplayName(ChatColor.RED + meta.getDisplayName());
            }
        }
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public void register() throws CrucialException {
        super.register();
        unregisteredDrugs.remove(this);
    }

    public void setMaterial(ItemStack stack) {
        if(stack != null) {
            this.material = stack.getType().name();
        } else {
            this.material = "AIR";
        }
    }

    public ArrayList<String> getCommands() {
        return commands;
    }

    public void setCommands(ArrayList<String> commands) {
        this.commands = commands;
    }

    public void addCommand(String command) {
        commands.add(command);
    }

    public ArrayList<String[]> getEffects() {
        return effects;
    }

    public void addEffect(String[] effect) {
        this.effects.add(effect);
    }

    public void setEffects(ArrayList<String[]> effects) {
        this.effects = effects;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        if(duration < 0) return;
        this.duration = duration;
    }

    public long getEffectDelay() {
        return effectDelay;
    }

    public void setEffectDelay(long effectDelay) {
        if(effectDelay < 0) return;
        this.effectDelay = effectDelay;
    }

    public int getOverdose() {
        return overdose;
    }

    public void setOverdose(int overdose) {
        if(overdose < 1 || overdose > 99) return;
        this.overdose = overdose;
    }

    public String getParticle() {
        return particle;
    }

    public void setParticle(String particle) {
        this.particle = particle;
    }

    public void setRecipe(ItemStack[] itemStacks) {
        for (int i = 0; i < 9; i++) {
            if(itemStacks[i] != null) {
                recipe[i] = itemStacks[i].getType().name();
            } else {
                recipe[i] = "AIR";
            }
        }
    }

    public int getAddictionProbability() {
        return addict;
    }

    public void setAddictionProbability(int addict) {
        if(addict < 0 || addict > 100) return;
        this.addict = addict;
    }

    // Static
    private static final Set<MyDrug> unregisteredDrugs = new HashSet<>();
    private static final MyTrip plugin = MyTrip.getInstance();

    public static MyDrug getUnregisteredDrugById(UUID id) {
        for (MyDrug drug : unregisteredDrugs) {
            if (drug.id.equals(id)) {
                return drug;
            }
        }
        return null;
    }

    public static MyDrug getByName(String name) {
        for (CrucialItem item : CRUCIAL_ITEMS) {
            if (item instanceof MyDrug && item.getName().equalsIgnoreCase(name)) {
                return (MyDrug) item;
            }
        }
        return null;
    }

    public static void clearUnregisteredDrugs() {
        unregisteredDrugs.clear();
    }

    public static void doDrug(Player p, ItemStack stack) {
        MyDrug drug = (MyDrug) getByStack(stack);
        long duration = drug.duration * 20;
        long delay = drug.effectDelay * 20;

        //instant visuals
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GRAY +  getLocalized(EFFECTS_START_IN, ChatColor.GREEN + String.valueOf(drug.effectDelay) + ChatColor.GRAY)));
        int amount = stack.getAmount();
        if (amount >= 1) {
            stack.setAmount(amount - 1);
            if(p.getInventory().getItemInMainHand() == stack) {
                p.getInventory().setItemInMainHand(stack);
            } else if(p.getInventory().getItemInOffHand() == stack) {
                p.getInventory().setItemInOffHand(stack);
            }
        }
        p.playSound(p.getLocation(), Sound.ITEM_HONEY_BOTTLE_DRINK, 10, 29);
        DrugPlayer dp = DrugPlayer.getPlayer(p.getUniqueId());
        if(dp == null) {
            dp = new DrugPlayer(p);
            DrugPlayer.addPlayer(dp);
        }
        if(dp.consume(drug)) {
            for (String effect : plugin.getConfigStringList(OVERDOSE_EFFECTS)) {
                String[] split = effect.split(":");
                p.addPotionEffect(new PotionEffect(Objects.requireNonNull(PotionEffectType.getByName(split[0])), 100*20, Integer.parseInt(split[1])));
            }
        }
        doEffects(p, drug, duration, delay);
    }

    public static void deleteDrug(MyDrug drug, CommandSender sender) {

        if (CrucialItem.getByStack(drug.getItemStack()) != null) {
            drug.delete();
            sender.sendMessage(PREFIX + getLocalized(WAS_DELETED, drug.getName()));
            return;
        }
        sender.sendMessage(PREFIX + getLocalized(DRUG_NOT_EXIST, drug.getName()));
    }

    private static void doEffects(Player p, MyDrug drug, long duration, long delay) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            p.playSound(p.getLocation(), Sound.AMBIENT_CAVE, 10, 29);
            long currentDuration;

            //effects
            for (String[] effect : drug.effects) {
                try {
                    PotionEffectType type = PotionEffectType.getByName(effect[0]);
                    int strength = Integer.parseInt(effect[1]);

                    if (p.hasPotionEffect(Objects.requireNonNull(type))) {
                        currentDuration = Objects.requireNonNull(p.getPotionEffect(type)).getDuration();
                    } else {
                        currentDuration = 0L;
                    }
                    p.removePotionEffect(type);
                    p.addPotionEffect(new PotionEffect(type, (int) (currentDuration + duration), strength - 1));
                } catch (Exception ex) {
                    plugin.error("Error 012: Tryied to run drug " + drug.name +
                            " but failed. Is PotionEffect " + effect[0] + " legal?");
                }
            }
            Bukkit.getScheduler().runTaskLater(plugin, () -> Objects.requireNonNull(DrugPlayer.getPlayer(p.getUniqueId())).subDose(drug), duration);
        }, delay);
    }

    public static void saveAll() throws IOException {
        ArrayList<MyDrug> drugs = new ArrayList<>();
        for (CrucialItem item : CRUCIAL_ITEMS) {
            if (item instanceof MyDrug) {
                drugs.add((MyDrug) item);
            }
        }
        plugin.fileManager.saveToJson("drugs.json", drugs);
    }

    public static void loadAll() throws IOException, CrucialException {
        for (Object drug : plugin.fileManager.loadFromJson("drugs.json", new TypeToken<ArrayList<MyDrug>>(){}.getType())) {
            if (drug instanceof MyDrug) {
                // namespacedKey is not persisted in JSON, so after deserialization
                // isRegistered=true but namespacedKey=null — reset to avoid
                // Bukkit.getRecipe(null) crash in unregister()
                ((MyDrug) drug).isRegistered = false;
                ((MyDrug) drug).register();
            }
        }
    }
}