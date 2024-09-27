package com.orleansmc.commands;

import com.orleansmc.commands.commands.CommandLoader;
import com.orleansmc.commands.managers.PlayTimeMoneyManager;
import com.orleansmc.commands.settings.Settings;
import com.orleansmc.commands.managers.CooldownManager;
import dev.unnm3d.rediseconomy.api.RedisEconomyAPI;
import dev.unnm3d.rediseconomy.currency.Currency;
import me.lucko.helper.plugin.ExtendedJavaPlugin;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.Objects;

public final class OrleansCommands extends ExtendedJavaPlugin {
    PlayTimeMoneyManager playTimeMoneyManager;
    RedisEconomyAPI redisEconomyAPI;

    @Override
    protected void enable() {
        CooldownManager.init(this);
        CommandLoader.load(this);
        Settings.load(this.loadConfig("config.yml"));

        this.redisEconomyAPI = RedisEconomyAPI.getAPI();
        this.playTimeMoneyManager = new PlayTimeMoneyManager(this);
    }

    @Override
    protected void disable() {
        // Plugin shutdown logic
    }

    public Currency getCreditCurrency() {
        return Objects.requireNonNull(redisEconomyAPI.getCurrencyByName("CREDIT"));
    }

    public Currency getGemCurrency() {
        return Objects.requireNonNull(redisEconomyAPI.getCurrencyByName("GEM"));
    }
}
