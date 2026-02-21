package de.chafficplugins.mytrip.drugs.objects;

import de.chafficplugins.mytrip.MyTrip;
import de.chafficplugins.mytrip.utils.MathUtils;
import io.github.chafficui.CrucialLib.Utils.customItems.CrucialItem;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;
import java.util.UUID;

import static de.chafficplugins.mytrip.utils.ConfigStrings.*;
import static de.chafficplugins.mytrip.utils.CustomMessages.getLocalized;

public class Addiction {
    private static final MyTrip plugin = MyTrip.getInstance();

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
        int result = intensity + alter;
        if (result >= 1 && result <= 8) {
            intensity = result;
        }
    }

    public int getIntensity() {
        return intensity;
    }

    public UUID getDrugId() {
        return drugId;
    }

    public void loop() {
        if (intensity <= 0) return;
        new BukkitRunnable() {
            @Override
            public void run() {
                Player player = Bukkit.getPlayer(playerId);
                DrugPlayer drugPlayer = DrugPlayer.getPlayer(playerId);
                if (player != null && drugPlayer != null && drugPlayer.getAddicted(drugId) != null) {
                    if (intensity > 0) {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(getLocalized(ADDICTION_KICKS, ChatColor.YELLOW + MyDrug.getById(drugId).getName() + ChatColor.RESET)));

                        player.damage(intensity);
                        if(intensity > 5) {
                            for(String type: plugin.getConfigStringList(ADDICTION_EFFECTS)) {
                                String[] split = type.split(":");
                                player.addPotionEffect(new PotionEffect(Objects.requireNonNull(PotionEffectType.getByName(split[0])), 60 * intensity, Integer.parseInt(split[1])));
                            }
                        }

                        player.sendTitle(getLocalized(ADDICTION), getLocalized(ADDICTED_TO, CrucialItem.getById(drugId).getName()), 10, 40, 10);
                        loop();
                    }
                }
                this.cancel();
            }
        }.runTaskTimer(plugin, 16000 / intensity, 16000 / intensity);
    }
}
