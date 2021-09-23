package de.chaffic.MyTrip.API.Objects;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class DrugPlayer {
    private final UUID uuid;
    ArrayList<Addiction> addictions = new ArrayList<>();
    public double dose;

    public DrugPlayer(java.util.UUID uuid) {
        this.uuid = uuid;
    }

    public DrugPlayer(Player player) {
        this.uuid = player.getUniqueId();
    }

    public UUID getUUID() {
        return uuid;
    }

    public Addiction isAddicted(MyDrug drug) {
        for (Addiction addiction:addictions) {
            if (addiction.getId().equals(drug.getKey())) {
                return addiction;
            }
        }
        return null;
    }

    public Addiction isAddicted(String drugID){
        for (Addiction addiction:addictions) {
            if (addiction.getId().equals(drugID)) {
                return addiction;
            }
        }
        return null;
    }

    public boolean consume(MyDrug drug){
        if(isAddicted(drug) != null){
            isAddicted(drug).consumed();
        } else {
            if((new Random().nextInt(100)) <= drug.getAddict()){
                addictions.add(new Addiction(drug, 1, uuid));
            }
        }
        dose += 1d/((double)drug.getOverdose());
        System.out.println("new dose: " + dose);
        return dose >= 1;
    }

    public void subDose(MyDrug drug){
        dose -= 1d/((double) drug.getOverdose());
    }

    public void remove(MyDrug drug){
        addictions.removeIf(addiction -> addiction.getId().equals(drug.getKey()));
    }

    public void clear(){
        addictions.clear();
    }

    public void joined(){
        for (Addiction addiction:addictions) {
            addiction.loop();
        }
    }
}
