package com.orleansmc.commands.managers;

import com.orleansmc.commands.OrleansCommands;
import com.orleansmc.common.redis.RedisProvider;

public class CooldownManager {
    private static final String COOLDOWN_PREFIX = "cooldown:";
    private static RedisProvider redisProvider;
    private static OrleansCommands plugin;

    public static void init(OrleansCommands plugin) {
        CooldownManager.plugin = plugin;
        redisProvider = plugin.getService(RedisProvider.class);
    }

    public static boolean isOnCooldown(String playerName, String command) {
        String key = COOLDOWN_PREFIX + command + ":" + playerName;
        return redisProvider.exists(key);
    }

    public static long getRemainingCooldown(String playerName, String command) {
        String key = COOLDOWN_PREFIX + command + ":" + playerName;
        return redisProvider.ttl(key); // Kalan süreyi saniye olarak döndürür
    }

    public static void setCooldown(String playerName, String command, long seconds) {
        String key = COOLDOWN_PREFIX + command + ":" + playerName;
        redisProvider.setex(key, (int) seconds, "1");
    }
}
