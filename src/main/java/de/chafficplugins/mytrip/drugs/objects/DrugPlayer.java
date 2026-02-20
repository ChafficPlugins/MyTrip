package de.chafficplugins.mytrip.drugs.objects;

import com.google.gson.reflect.TypeToken;
import de.chafficplugins.mytrip.MyTrip;
import de.chafficplugins.mytrip.utils.MathUtils;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class DrugPlayer {
    private final UUID uuid;
    private final ArrayList<Addiction> addictions = new ArrayList<>();
    private double dose;
    private static final MyTrip plugin = MyTrip.getInstance();

    public DrugPlayer(Player player) {
        this.uuid = player.getUniqueId();
    }

    public boolean consume(MyDrug drug) {
        Addiction addiction = getAddicted(drug.getId());
        if (addiction != null) {
            addiction.consumed();
        } else {
            if (MathUtils.randomInt(0, 100) <= drug.getAddictionProbability()) {
                addictions.add(new Addiction(drug.getId(), 1, uuid));
            }
        }
        dose += (1d / (double) drug.getOverdose());
        return dose >= 1;
    }

    public void addAddiction(MyDrug drug) {
        if(getAddicted(drug.getId()) == null) {
            addictions.add(new Addiction(drug.getId(), 1, uuid));
        }
    }

    public Addiction getAddicted(UUID drugId) {
        for (Addiction addiction : addictions) {
            if (addiction.drugId.equals(drugId)) {
                return addiction;
            }
        }
        return null;
    }

    public ArrayList<Addiction> getAddictions() {
        return addictions;
    }

    public void subDose(MyDrug drug) {
        dose -= 1d / ((double) drug.getOverdose());
    }

    public void setDose(double dose) {
        this.dose = dose;
    }

    public void remove(MyDrug drug) {
        addictions.removeIf(addiction -> addiction.drugId.equals(drug.getId()));
    }

    public void clear() {
        addictions.clear();
    }

    public UUID getUuid() {
        return uuid;
    }

    public double getDose() {
        return dose;
    }

    public void joined() {
        for (Addiction addiction : addictions) {
            addiction.loop();
        }
    }

    //static
    public static ArrayList<DrugPlayer> playerData = new ArrayList<>();

    public static DrugPlayer getPlayer(UUID uuid) {
        for (DrugPlayer playerData : playerData) {
            if (playerData.getUuid().equals(uuid)) {
                return playerData;
            }
        }
        return null;
    }

    public static void addPlayer(DrugPlayer player) {
        playerData.add(player);
    }

    public static void saveAll() throws IOException {
        plugin.fileManager.saveToJson("playerdata.json", playerData);
    }

    public static void loadAll() throws IOException {
        playerData = plugin.fileManager.loadFromJson("playerdata.json", new TypeToken<ArrayList<DrugPlayer>>() {
        }.getType());
        if (playerData == null) {
            playerData = new ArrayList<>();
        }
    }
}
