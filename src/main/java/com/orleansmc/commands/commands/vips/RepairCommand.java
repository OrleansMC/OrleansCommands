package com.orleansmc.commands.commands.vips;

import com.orleansmc.commands.managers.CooldownManager;
import com.orleansmc.commands.OrleansCommands;
import com.orleansmc.commands.managers.LuckPermsManager;
import com.orleansmc.commands.settings.Settings;
import com.orleansmc.commands.utils.Util;
import me.lucko.helper.Commands;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RepairCommand {
    public static void setup(OrleansCommands plugin) {
        Commands.create()
                .assertPlayer()
                .assertPermission("orleansmc.commands.repair")
                .handler(c -> {
                    Player player = c.sender();

                    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                        // Cooldown kontrolü
                        if (CooldownManager.isOnCooldown(player.getName(), "repair")) {
                            long remaining = CooldownManager.getRemainingCooldown(player.getName(), "repair");
                            Bukkit.getScheduler().runTask(plugin, () ->
                                    player.sendMessage(
                                            Util.getExclamation() +
                                                    "Bu komutu tekrar kullanabilmek için " +
                                                    Util.formatTime(remaining * 1000) + " saniye beklemelisin.")
                            );
                            return;
                        }

                        // Elindeki eşya onarılıyor
                        ItemStack itemInHand = player.getInventory().getItemInMainHand();
                        if (itemInHand.getType() != Material.AIR) {
                            itemInHand.setDurability((short) 0);
                        } else {
                            Bukkit.getScheduler().runTask(plugin, () ->
                                    player.sendMessage(Util.getExclamation() + "Onarılacak bir eşya tutmalısın.")
                            );
                            return;
                        }

                        Bukkit.getScheduler().runTask(plugin, () -> {
                            player.sendMessage(Util.getComponent(
                                    "<color:#00ff00>Eşyanız başarıyla onarıldı.</color>"
                            ));
                            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1, 1);
                        });

                        int cooldown = 0;
                        for (String group : LuckPermsManager.getPlayerGroups(player)) {
                            if (Settings.cooldowns.containsKey(group)) {
                                cooldown = Settings.cooldowns.get(group).data().repair();
                                break;
                            }
                        }
                        CooldownManager.setCooldown(player.getName(), "repair", cooldown); // Örnek olarak 300 saniye cooldown
                    });
                })
                .registerAndBind(plugin, "repair");
    }
}
