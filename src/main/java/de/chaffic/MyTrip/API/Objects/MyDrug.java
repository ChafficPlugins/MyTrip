package de.chaffic.MyTrip.API.Objects;

import io.github.chafficui.CrucialAPI.Utils.customItems.CrucialItem;
import io.github.chafficui.CrucialAPI.exceptions.CrucialException;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MyDrug extends CrucialItem {
    private static final Set<MyDrug> unregisteredDrugs = new HashSet<>();

    public MyDrug(String name, String head) {
        super("drug");
        this.isHead = true;
        this.name = name;
        this.material = head;
        unregisteredDrugs.add(this);
    }

    public MyDrug(String name, Material material) {
        super("drug");
        this.isHead = false;
        this.name = name;
        this.material = material.name();
        unregisteredDrugs.add(this);
    }

    private ArrayList<String[]> effects = new ArrayList<>();
    private ArrayList<String> commands = new ArrayList<>();
    private int duration;
    private int effectDelay;
    private int overdose;
    private boolean isBloody;
    private String particle;
    private int addict;

    public static MyDrug getUnregisteredDrugByKey(String key) {
        for (MyDrug drug : unregisteredDrugs) {
            if (drug.getKey().equals(key)) {
                return drug;
            }
        }
        return null;
    }

    public static void clearUnregisteredDrugs() {
        unregisteredDrugs.clear();
    }

    public static MyDrug getByName(String name) {
        for (CrucialItem item : CrucialItem.CRUCIAL_ITEMS) {
            if (item instanceof MyDrug && item.getName().equals(name)) {
                return (MyDrug) item;
            }
        }
        return null;
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack stack = super.getItemStack();
        ItemMeta meta = stack.getItemMeta();
        if (isRegistered()) {
            meta.setDisplayName(ChatColor.WHITE + meta.getDisplayName());
        } else {
            meta.setDisplayName(ChatColor.RED + meta.getDisplayName());
        }
        stack.setItemMeta(meta);
        return stack;
    }

    public ArrayList<String> getCommands() {
        return commands;
    }

    public void setCommands(ArrayList<String> commands) {
        this.commands = commands;
    }

    public void addCommand(String command){
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void alterDuration(int alter){
        if(duration+alter > 0){
            this.duration += alter;
        }
    }

    public int getEffectDelay() {
        return effectDelay;
    }

    public void setEffectDelay(int effectDelay) {
        this.effectDelay = effectDelay;
    }

    public void alterEffectDelay(int alter){
        if(effectDelay+alter > 0){
            this.effectDelay += alter;
        }
    }

    public int getOverdose() {
        return overdose;
    }

    public void setOverdose(int overdose) {
        this.overdose = overdose;
    }

    public void alterOverdose(int alter){
        if(overdose+alter < 99 && overdose+alter > 0){
            this.overdose += alter;
        }
    }

    public boolean isBloody() {
        return isBloody;
    }

    public void setBloody(boolean bloody) {
        isBloody = bloody;
    }

    public String getParticle() {
        return particle;
    }

    public void setParticle(String particle) {
        this.particle = particle;
    }

    public int getAddict() {
        return addict;
    }

    public void setAddict(int addict) {
        this.addict = addict;
    }

    public void alterAddict(int alter) {
        if (addict + alter < 99 && addict + alter > 0) {
            this.addict += alter;
        }
    }

    @Override
    public void register() throws CrucialException {
        super.register();
        unregisteredDrugs.remove(this);
    }

    @Override
    public void delete() {
        super.delete();
    }
}
