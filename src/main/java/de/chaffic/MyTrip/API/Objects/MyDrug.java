package de.chaffic.MyTrip.API.Objects;

import io.github.chafficui.CrucialAPI.Interfaces.CrucialItem;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MyDrug extends CrucialItem {

public static final class Builder {
        private static String name = "undefined";
        private static String material = "DIRT";
        private static List<String> lore = new ArrayList<>();
        private static String[] crafting = {"AIR", "AIR", "AIR",
                "AIR", "DIAMOND", "AIR",
                "AIR", "AIR", "AIR"};
        private static ArrayList<String[]> effects = new ArrayList<>();
        private static String type = "DRUG";
        private static int duration = 100;
        private static int effectDelay = 2;
        private static int overdose = 5;
        private static boolean isBloody = false;
        private static String particle;
        private static int addict = 5;

        private Builder(){

        }

        public Builder name(String name){
            Builder.name = name;
            return this;
        }

        public Builder material(String material){
            Builder.material = material;
            return this;
        }

        public Builder lore(List<String> lore){
            Builder.lore = lore;
            return this;
        }

        public Builder crafting(String[] crafting){
            Builder.crafting = crafting;
            return this;
        }

        public Builder effects(ArrayList<String[]> effects){
            Builder.effects = effects;
            return this;
        }

        public Builder type(String type){
            Builder.type = type;
            return this;
        }

        public Builder duration(int duration){
            Builder.duration = duration;
            return this;
        }

        public Builder effectDelay(int effectDelay){
            Builder.effectDelay = effectDelay;
            return this;
        }

        public Builder overdose(int overdose){
            Builder.overdose = overdose;
            return this;
        }

        public Builder isBloody(boolean isBloody){
            Builder.isBloody = isBloody;
            return this;
        }

        public Builder particle(String particle){
            Builder.particle = particle;
            return this;
        }

        public Builder addict(int addict){
            Builder.addict = addict;
            return this;
        }

        public MyDrug build(){
            MyDrug myDrug = new MyDrug();
            myDrug.name = Builder.name;
            myDrug.material = Builder.material;
            myDrug.lore = Builder.lore;
            myDrug.crafting = Builder.crafting;
            myDrug.effects = Builder.effects;
            myDrug.type = Builder.type;
            myDrug.duration = Builder.duration;
            myDrug.effectDelay = Builder.effectDelay;
            myDrug.overdose = Builder.overdose;
            myDrug.isBloody = Builder.isBloody;
            myDrug.particle = Builder.particle;
            myDrug.addict = Builder.addict;
            String[] m = myDrug.material.split(":");
            if(m[0].equals("HEAD")){
                myDrug.isHead = true;
                myDrug.material = m[1];
            }
            return myDrug;
        }
    }

    @Override
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

    private ArrayList<String[]> effects;
    private int duration;
    private int effectDelay;
    private int overdose;
    private boolean isBloody;
    private String particle;
    private int addict;

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

    public static Builder drugBuilder(){
        return new Builder();
    }
}
