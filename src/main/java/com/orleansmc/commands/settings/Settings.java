package com.orleansmc.commands.settings;

import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;

public class Settings {
    public static HashMap<String, CooldownData> cooldowns = new HashMap<>();

    public static void load(YamlConfiguration config) {
        for (String group : config.getConfigurationSection("cooldowns").getKeys(false)) {
            CooldownData data = new CooldownData(group, new GroupCooldownData(
                    config.getInt("cooldowns." + group + ".repair"),
                    config.getInt("cooldowns." + group + ".feed"),
                    config.getInt("cooldowns." + group + ".head")
            ));
            cooldowns.put(group, data);
        }
    }
}
