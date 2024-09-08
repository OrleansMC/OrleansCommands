package com.orleansmc.commands.managers;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

public class LuckPermsManager {
    public static LuckPerms luckPermsAPI = Objects.requireNonNull(Bukkit.getServicesManager().getRegistration(LuckPerms.class)).getProvider();

    public static LuckPerms getLuckPermsAPI() {
        return luckPermsAPI;
    }

    public static List<String> getPlayerGroups(Player player) {
        User user = luckPermsAPI.getPlayerAdapter(Player.class).getUser(player);
        return user.getInheritedGroups(user.getQueryOptions()).stream()
                .sorted((a, b) -> Integer.compare(b.getWeight().orElse(0), a.getWeight().orElse(0)))
                .map(Group::getName).toList();
    }
}