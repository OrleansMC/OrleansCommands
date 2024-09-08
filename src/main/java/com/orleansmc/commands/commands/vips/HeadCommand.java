package com.orleansmc.commands.commands.vips;

import com.orleansmc.commands.managers.CooldownManager;
import com.orleansmc.commands.OrleansCommands;
import com.orleansmc.commands.managers.LuckPermsManager;
import com.orleansmc.commands.settings.Settings;
import com.orleansmc.commands.utils.Util;
import me.lucko.helper.Commands;
import net.skinsrestorer.api.exception.DataRequestException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HeadCommand {
    public static void setup(OrleansCommands plugin) {
        Commands.create()
                .assertPlayer()
                .assertPermission("orleansmc.commands.head")
                .handler(c -> {
                    Player player = c.sender();

                    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                        // Cooldown kontrolü
                        if (CooldownManager.isOnCooldown(player.getName(), "head")) {
                            long remaining = CooldownManager.getRemainingCooldown(player.getName(), "head");
                            Bukkit.getScheduler().runTask(plugin, () ->
                                    player.sendMessage(
                                            Util.getExclamation() +
                                                    "Bu komutu tekrar kullanabilmek için " +
                                                    Util.formatTime(remaining * 1000) + " saniye beklemelisin.")
                            );
                            return;
                        }

                        // Oyuncunun kafası veriliyor
                        try {
                            ItemStack head = Util.createPlayerHead(player.getName());
                            player.getInventory().addItem(head);
                        } catch (DataRequestException e) {
                            throw new RuntimeException(e);
                        }

                        int cooldown = 0;
                        for (String group : LuckPermsManager.getPlayerGroups(player)) {
                            if (Settings.cooldowns.containsKey(group)) {
                                cooldown = Settings.cooldowns.get(group).data().head();
                                break;
                            }
                        }
                        CooldownManager.setCooldown(player.getName(), "head", cooldown); // Örnek olarak 300 saniye cooldown
                    });
                })
                .registerAndBind(plugin, "head", "kafa");
    }
}
