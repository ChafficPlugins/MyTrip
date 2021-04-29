package de.chaffic.MyTrip.API.Objects;

import io.github.chafficui.CrucialAPI.Interfaces.CrucialItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class MyDrug extends CrucialItem {
    private ArrayList<String[]> effects;
    private ArrayList<String> commands;
    private int duration;
    private int effectDelay;
    private int overdose;
    private boolean isBloody;
    private String particle;
    private int addict;

    public MyDrug(String name, String head, String type) {
        super(name, head, type);
    }

    public MyDrug(String name, Material material, String type) {
        super(name, material, type);
    }

    public static MyDrug getByName(String name){
        for (CrucialItem item:CrucialItem.getCrucialItems()){
            if(item instanceof MyDrug && item.getName().equals(name)){
                return (MyDrug) item;
            }
        }
        return null;
    }

    public ItemStack get() {
        ItemStack stack = super.get();
        ItemMeta meta = stack.getItemMeta();
        if(isRegistered){
            meta.setDisplayName(ChatColor.WHITE + meta.getDisplayName());
        } else {
            meta.setDisplayName(ChatColor.RED + meta.getDisplayName());
        }
        stack.setItemMeta(meta);
        return stack;
    }

    public void setMaterial(Material material){
        this.material = material.name();
        this.isHead = false;
    }

    public void setMaterial(String head){
        this.material = head;
        this.isHead = true;
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

    public void alterAddict(int alter){
        if(addict+alter < 99 && addict+alter > 0){
            this.addict += alter;
        }
    }

    @Override
    public void delete(){
        super.delete();
    }
}
