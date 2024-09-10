package com.orleansmc.commands;

import com.orleansmc.commands.commands.CommandLoader;
import com.orleansmc.commands.managers.PlayTimeMoneyManager;
import com.orleansmc.commands.settings.Settings;
import com.orleansmc.commands.managers.CooldownManager;
import me.lucko.helper.plugin.ExtendedJavaPlugin;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public final class OrleansCommands extends ExtendedJavaPlugin {
    PlayTimeMoneyManager playTimeMoneyManager;
    @Override
    protected void enable() {
        CooldownManager.init(this);
        CommandLoader.load(this);
        Settings.load(this.loadConfig("config.yml"));

        this.playTimeMoneyManager = new PlayTimeMoneyManager(this);
    }

    @Override
    protected void disable() {
        // Plugin shutdown logic
    }
}
