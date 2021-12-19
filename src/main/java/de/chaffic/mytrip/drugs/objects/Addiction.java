package de.chaffic.mytrip.drugs.objects;

import de.chaffic.mytrip.MyTrip;
import de.chaffic.mytrip.utils.MathUtils;
import io.github.chafficui.CrucialAPI.Utils.customItems.CrucialItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class Addiction {
    private static final MyTrip plugin = MyTrip.getPlugin(MyTrip.class);

    final UUID drugId;
    private int intensity;
    final UUID playerId;

    public Addiction(UUID drugId, int intensity, UUID playerId) {
        this.drugId = drugId;
        this.intensity = intensity;
        this.playerId = playerId;
        loop();
    }

    public void consumed() {
        if (MathUtils.randomInt(0, 100) <= ((MyDrug) CrucialItem.getById(drugId)).getAddictionProbability()) {
            alterIntensity(1);
        }
    }

    public void alterIntensity(int alter) {
        if (intensity + alter < 9) {
            intensity += alter;
        }
    }

    public UUID getDrugId() {
        return drugId;
    }

    public void loop() {
        new BukkitRunnable() {

            @Override
            public void run() {
                Player player = Bukkit.getPlayer(playerId);
                DrugPlayer drugPlayer = DrugPlayer.getPlayer(playerId);
                if (player != null && drugPlayer != null && drugPlayer.getAddicted(drugId) != null) {
                    if (intensity > 0) {
                        //TODO: Add addiction effect

                        player.sendTitle("Addiction", "You are addicted to " + CrucialItem.getById(drugId).getName() + "!", 10, 40, 10);
                        loop();
                    }
                }
                this.cancel();
            }
        }.runTaskTimer(plugin, 16000 / intensity, 16000 / intensity);
    }

    //TODO: Effects
}
