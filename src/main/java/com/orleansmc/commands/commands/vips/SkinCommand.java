package com.orleansmc.commands.commands.vips;

import com.orleansmc.commands.managers.CooldownManager;
import com.orleansmc.commands.OrleansCommands;
import com.orleansmc.commands.managers.LuckPermsManager;
import com.orleansmc.commands.settings.Settings;
import com.orleansmc.commands.utils.Util;
import me.lucko.helper.Commands;
import net.skinsrestorer.api.SkinsRestorer;
import net.skinsrestorer.api.SkinsRestorerProvider;
import net.skinsrestorer.api.exception.DataRequestException;
import net.skinsrestorer.api.exception.MineSkinException;
import net.skinsrestorer.api.property.InputDataResult;
import net.skinsrestorer.api.storage.PlayerStorage;
import net.skinsrestorer.api.storage.SkinStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class SkinCommand {
    static SkinsRestorer skinsRestorerAPI = SkinsRestorerProvider.get();

    public static void setup(OrleansCommands plugin) {
        Commands.create()
                .assertPlayer()
                .assertPermission("orleansmc.commands.skin")
                .handler(c -> {
                    Player player = c.sender();

                    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                        // Cooldown kontrolü
                        if (CooldownManager.isOnCooldown(player.getName(), "skin")) {
                            long remaining = CooldownManager.getRemainingCooldown(player.getName(), "skin");
                            Bukkit.getScheduler().runTask(plugin, () ->
                                    player.sendMessage(
                                            Util.getExclamation() +
                                                    "Bu komutu tekrar kullanabilmek için " +
                                                    Util.formatTime(remaining * 1000) + " saniye beklemelisin.")
                            );
                            return;
                        }

                        try {
                            String skin = c.args().isEmpty() ? null : c.args().get(0);
                            if (skin == null) {
                                player.sendMessage(Util.getExclamation() + "Bir oyuncu adı belirtmelisiniz.");
                                return;
                            }
                            SkinStorage skinStorage = skinsRestorerAPI.getSkinStorage();
                            Optional<InputDataResult> result = skinStorage.findOrCreateSkinData(skin);

                            if (result.isEmpty()) {
                                player.sendMessage(Util.getExclamation() + "Skin bulunamadı.");
                                return;
                            }

                            PlayerStorage playerStorage = skinsRestorerAPI.getPlayerStorage();

                            // Associate the skin with the player
                            playerStorage.setSkinIdOfPlayer(player.getUniqueId(), result.get().getIdentifier());

                            // Instantly apply skin to the player without requiring the player to rejoin
                            skinsRestorerAPI.getSkinApplier(Player.class).applySkin(player);
                        } catch (DataRequestException | MineSkinException e) {
                            e.printStackTrace();
                        }

                        int cooldown = 0;
                        for (String group : LuckPermsManager.getPlayerGroups(player)) {
                            if (Settings.cooldowns.containsKey(group)) {
                                cooldown = Settings.cooldowns.get(group).data().skin();
                                break;
                            }
                        }
                        CooldownManager.setCooldown(player.getName(), "skin", cooldown); // Örnek olarak 300 saniye cooldown
                    });
                })
                .registerAndBind(plugin, "skin", "kafa");
    }
}
