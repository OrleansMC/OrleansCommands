package com.orleansmc.commands.commands.vips;

import com.orleansmc.commands.managers.CooldownManager;
import com.orleansmc.commands.OrleansCommands;
import com.orleansmc.commands.managers.LuckPermsManager;
import com.orleansmc.commands.settings.Settings;
import com.orleansmc.commands.utils.Util;
import me.lucko.helper.Commands;
import net.skinsrestorer.api.exception.DataRequestException;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
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

                        String playerName = c.args().isEmpty() ? null : c.args().get(0);
                        if (playerName == null) {
                            Bukkit.getScheduler().runTask(plugin, () ->
                                    player.sendMessage(Util.getExclamation() + "Bir oyuncu adı belirtmelisiniz.")
                            );
                            return;
                        }


                        if (playerName.equalsIgnoreCase("alpho320")) {
                            Bukkit.getScheduler().runTask(plugin, () ->
                                    player.sendMessage(
                                            Util.getComponent(
                                                    "<color:#ff0000>Bu kafa bulunamadı.</color>"
                                            )
                                    ));
                            return;
                        }
                        // Oyuncunun kafası veriliyor
                        try {
                            ItemStack head = Util.createPlayerHead(playerName);
                            player.getInventory().addItem(head);
                        } catch (DataRequestException e) {
                            throw new RuntimeException(e);
                        }

                        Bukkit.getScheduler().runTask(plugin, () -> {
                            player.sendMessage(Util.getComponent(
                                    "<color:#00ff00>Oyuncunun kafası başarıyla verildi.</color>"
                            ));
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                        });

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
