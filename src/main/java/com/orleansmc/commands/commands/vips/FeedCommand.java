package com.orleansmc.commands.commands.vips;

import com.orleansmc.commands.managers.CooldownManager;
import com.orleansmc.commands.OrleansCommands;
import com.orleansmc.commands.managers.LuckPermsManager;
import com.orleansmc.commands.settings.Settings;
import com.orleansmc.commands.utils.Util;
import me.lucko.helper.Commands;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class FeedCommand {
    public static void setup(OrleansCommands plugin) {
        Commands.create()
                .assertPlayer()
                .assertPermission("orleansmc.commands.feed")
                .handler(c -> {
                    Player player = c.sender();

                    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                        // Cooldown kontrolü
                        if (CooldownManager.isOnCooldown(player.getName(), "feed")) {
                            long remaining = CooldownManager.getRemainingCooldown(player.getName(), "feed");
                            Bukkit.getScheduler().runTask(plugin, () ->
                                    player.sendMessage(
                                            Util.getExclamation() +
                                                    "Bu komutu tekrar kullanabilmek için " +
                                                    Util.formatTime(remaining * 1000) + " saniye beklemelisin.")
                            );
                            return;
                        }

                        // Oyuncunun açlık seviyesi dolduruluyor
                        player.setFoodLevel(20);

                        int cooldown = 0;
                        for (String group : LuckPermsManager.getPlayerGroups(player)) {
                            if (Settings.cooldowns.containsKey(group)) {
                                cooldown = Settings.cooldowns.get(group).data().feed();
                                break;
                            }
                        }
                        CooldownManager.setCooldown(player.getName(), "feed", cooldown); // Örnek olarak 300 saniye cooldown
                    });
                })
                .registerAndBind(plugin, "feed", "acıktım");
    }
}
