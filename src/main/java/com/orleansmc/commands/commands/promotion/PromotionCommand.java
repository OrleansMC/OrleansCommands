package com.orleansmc.commands.commands.promotion;

import com.orleansmc.bukkit.players.PlayersProvider;
import com.orleansmc.bukkit.players.models.PlayerModel;
import com.orleansmc.commands.OrleansCommands;
import com.orleansmc.commands.utils.Util;
import com.orleansmc.common.webhooks.DiscordWebhook;
import com.orleansmc.common.webhooks.WebhookProvider;
import me.clip.placeholderapi.PlaceholderAPI;
import me.lucko.helper.Commands;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PromotionCommand {
    public static PlayersProvider playersProvider;
    public static WebhookProvider webhookProvider;

    public static void setup(OrleansCommands plugin) {
        playersProvider = plugin.getService(PlayersProvider.class);
        webhookProvider = plugin.getService(WebhookProvider.class);

        Commands.create()
                .assertPlayer()
                .tabHandler(c -> {
                    if (c.args().size() == 1 && c.sender().hasPermission("orleansmc.commands.promotion")) {
                        return List.of("KOD");
                    }
                    return null;
                })
                .handler(c -> {
                    Player player = c.sender();
                    if (!player.hasPermission("orleansmc.commands.promotion")) {
                        player.sendMessage(Util.getComponent(
                                "<color:#ff0000>Bu komutu kullanmak için bir diyara sahip olmalısın.</color>"
                        ));
                        return;
                    }
                    String code = c.args().getFirst();
                    if (code.length() < 3) {
                        player.sendMessage(Util.getComponent(
                                "<color:#ff0000>Geçerli bir promosyon kodu belirtmelisiniz.</color>"
                        ));
                        return;
                    }
                    OfflinePlayer offlinePlayer = Arrays.stream(plugin.getServer().getOfflinePlayers()).filter(
                            p -> p.getName() != null && p.getName().equalsIgnoreCase(code)
                    ).findFirst().orElse(null);
                    if (offlinePlayer == null) {
                        player.sendMessage(Util.getComponent(
                                "<color:#ff0000>Geçerli bir promosyon kodu belirtmelisiniz.</color>"
                        ));
                        return;
                    }

                    if (Objects.equals(offlinePlayer.getName(), player.getName())) {
                        player.sendMessage(Util.getComponent(
                                "<color:#ff0000>Kendi promosyon kodunuzu kullanamazsınız.</color>"
                        ));
                        return;
                    }

                    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                        PlayerModel userModel = playersProvider.fetchPlayer(player.getName());
                        PlayerModel targetModel = playersProvider.fetchPlayer(offlinePlayer.getName());

                        if (userModel.invites.invitedBy != null) {
                            player.sendMessage(Util.getComponent(
                                    "<color:#ff0000>Davet hediyenizi zaten almışsınız.</color>"
                            ));
                            return;
                        }
                        userModel.invites.invitedBy = targetModel.name;
                        playersProvider.savePlayer(userModel);
                        targetModel.invites.invitedPlayers.add(userModel.name);
                        playersProvider.savePlayer(targetModel);

                        plugin.getCreditCurrency().depositPlayer(targetModel.name, 250);
                        plugin.getGemCurrency().depositPlayer(player.getName(), 500);

                        String gemIcon = PlaceholderAPI.setPlaceholders(player, "%img_gem%");
                        String creditIcon = PlaceholderAPI.setPlaceholders(player, "%img_credit%");
                        Bukkit.getScheduler().runTask(plugin, () -> {
                            Bukkit.getServer()
                                    .dispatchCommand(
                                            Bukkit.getConsoleSender(),
                                            "sendmessage " + player.getName() + " <color:#AE89EB>500</color>" + gemIcon + "<green> hediye olarak hesabınıza yüklendi.</green>"
                                    );
                            Bukkit.getServer()
                                    .dispatchCommand(
                                            Bukkit.getConsoleSender(),
                                            "sendmessage " + targetModel.name + " <color:#00e5ff>" + player.getName() + "</color> <green>adlı oyuncuyu davet ettiğiniz için</green> <color:#EEC427>250</color>" + " \uF801\uF801" + creditIcon + "<green> hediye olarak hesabınıza yüklendi.</green>"
                                    );
                        });

                        DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/1288918780910506138/-GpLujk2Ju98BsfgyLmyju2s-a1iMoWTuCT8MvaC9ArV0jK8oWayJ2WbxTqxUZn6uyDD");
                        webhook.setAvatarUrl("https://mc-heads.net/avatar/" + player.getName() + "/64.png");
                        webhook.setUsername(player.getName());
                        webhook.addEmbed(
                                new DiscordWebhook.EmbedObject()
                                        .setTitle("Promosyon Kodu Kullanıldı")
                                        .addField("Kod", code, true)
                                        .addField("Kullanıcı", player.getName(), true)
                                        .addField("Hediyeler", "500 Mücevher, 250 Kredi", false)
                                        .setColor(Color.GREEN)
                        );
                        webhookProvider.sendWebhook(webhook);
                    });
                })
                .registerAndBind(plugin, "promotion", "promo", "promosyon", "kod-kullan");
    }
}
