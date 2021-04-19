package de.chaffic.MyTrip.API.Objects;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class DrugPlayer {
    public UUID player;
    ArrayList<Addiction> addictions = new ArrayList<>();
    public double dose;

    public DrugPlayer(Player player) {
        this.player = player.getUniqueId();
    }

    public Addiction isAddicted(MyDrug drug){
        for (Addiction addiction:addictions) {
            if (addiction.getId().equals(drug.getId())) {
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

    /**
     * @param drug the consumed drug
     * @return isOverdose
     */
    public boolean consume(MyDrug drug){
        if(isAddicted(drug) != null){
            System.out.println("already addicted!");
            isAddicted(drug).consumed();
        } else {
            if((new Random().nextInt(100)) <= drug.getAddict()){
                System.out.println("newly addicted!");
                addictions.add(new Addiction(drug, 1, player));
            }
            System.out.println("not addicted!");
        }
        dose += 1d/((double)drug.getOverdose());
        System.out.println("new dose: " + dose);
        return dose >= 1;
    }

    public void subDose(MyDrug drug){
        dose -= 1d/((double) drug.getOverdose());
    }

    public void remove(MyDrug drug){
        addictions.removeIf(addiction -> addiction.getId().equals(drug.getId()));
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
