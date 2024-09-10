package com.orleansmc.commands.managers;

import com.orleansmc.commands.OrleansCommands;
import com.orleansmc.commands.utils.Util;
import dev.unnm3d.rediseconomy.api.RedisEconomyAPI;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;

public class PlayTimeMoneyManager {
    RedisEconomyAPI redisEconomyAPI;

    public PlayTimeMoneyManager(OrleansCommands plugin) {
        redisEconomyAPI = RedisEconomyAPI.getAPI();
        String gemIcon = PlaceholderAPI.setPlaceholders(null, "%img_gem%");

        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            plugin.getLogger().info("Checking online players for money reward");
            Bukkit.getOnlinePlayers().forEach(player -> {
                redisEconomyAPI.getDefaultCurrency().depositPlayer(
                        player.getName(),
                        6
                );
                player.sendMessage(
                        Util.getComponent(
                                "<color:#9199ff>Oynadığınız için <color:#AE89EB>6</color></color>" + gemIcon + " <color:#9199ff>kazandınız!</color>"
                        )
                );
            });
        }, 20 * 60 * 10, 20 * 60 * 10);
    }
}
