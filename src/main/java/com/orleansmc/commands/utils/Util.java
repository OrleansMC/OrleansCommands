package com.orleansmc.commands.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import dev.lone.itemsadder.api.CustomStack;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.skinsrestorer.api.SkinsRestorer;
import net.skinsrestorer.api.SkinsRestorerProvider;
import net.skinsrestorer.api.exception.DataRequestException;
import net.skinsrestorer.api.property.MojangSkinDataResult;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    public static Component getComponent(String text) {
        final MiniMessage mm = MiniMessage.miniMessage();
        return mm.deserialize(text);
    }

    public static String getString(Component component) {
        final MiniMessage mm = MiniMessage.miniMessage();
        return mm.serialize(component);
    }

    public static String upperFirstChar(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }


    public static ItemStack getCustomSkull(String url) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD); // Kafa itemi oluştur

        if (url == null || url.isEmpty())
            return skull;

        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), "custom_skull");

        String texture = "{\"textures\":{\"SKIN\":{\"url\":\"" + url + "\"}}}";
        profile.getProperties().put("textures", new Property("textures", Base64.getEncoder().encodeToString(texture.getBytes())));

        try {
            Field profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, profile);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        skull.setItemMeta(skullMeta);
        return skull;
    }

    public static String getExclamation() {
        return PlaceholderAPI.setPlaceholders(null, "%img_exclamation%") + " ";
    }

    public static String formatTime(long ms) {
        long secNum = ms / 1000;
        long days = secNum / 86400;
        long hours = (secNum - (days * 86400)) / 3600;
        long minutes = (secNum - (days * 86400) - (hours * 3600)) / 60;
        long seconds = secNum - (days * 86400) - (hours * 3600) - (minutes * 60);

        StringBuilder result = new StringBuilder();
        if (days > 0) result.append(days).append(" Gün");
        if (hours > 0) result.append((!result.isEmpty() ? ", " : "")).append(hours).append(" Saat");
        if (minutes > 0 && (days == 0 || hours == 0))
            result.append((!result.isEmpty() ? ", " : "")).append(minutes).append(" Dakika");
        if (seconds > 0 && (hours == 0 || minutes == 0))
            result.append((!result.isEmpty() ? ", " : "")).append(seconds).append(" Saniye");

        return result.toString();
    }

    public static ItemStack getItemFromString(String item) {
        ItemStack itemStack;
        String[] parts = item.split(":");
        String group = parts[0];
        String nameAndData = Arrays.stream(parts).toList().subList(1, parts.length).stream().reduce((a, b) -> a + ":" + b).orElse("");
        String name;
        String data;
        if (nameAndData.contains("{")) {
            String[] nameAndDataParts = nameAndData.split("\\{");
            name = nameAndDataParts[0];
            data = "{" + Arrays.stream(nameAndDataParts).toList().subList(1, nameAndDataParts.length).stream().reduce((a, b) -> a + "{" + b).orElse("");
        } else {
            data = "";
            name = nameAndData;
        }

        if (item.startsWith("minecraft:")) {
            Material material = Material.getMaterial(name.toUpperCase());
            if (material == null) {
                throw new RuntimeException("Invalid material: " + name.toUpperCase());
            }
            itemStack = new ItemStack(material);
        } else {
            CustomStack customStack = CustomStack.getInstance(group + ":" + name);
            if (customStack == null) {
                throw new RuntimeException("Invalid item: " + item);
            }
            itemStack = customStack.getItemStack();
        }
        if (!data.isEmpty()) {
            ReadWriteNBT nbt = NBT.parseNBT(data);
            int count = 1;

            Pattern pattern = Pattern.compile("count:(\\d+)");
            Matcher matcher = pattern.matcher(data);
            if (matcher.find()) count = Integer.parseInt(matcher.group(1));

            itemStack.setAmount(count);
            nbt.mergeCompound(NBT.itemStackToNBT(itemStack));
            return NBT.itemStackFromNBT(NBT.parseNBT(nbt.toString()));
        }
        return itemStack;
    }

    public static String prettyNumber(long number) {
        // Milyon gruplamak için kalıp (pattern)
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.'); // Gruplama ayıracı olarak nokta kullan

        DecimalFormat df = new DecimalFormat("#,###,###", symbols);

        // Sonucu döndür
        return df.format(number);
    }

    public static String stripColor(String input) {
        return input.replaceAll("§[0-9a-fk-or-x]", "");
    }

    public static Location getLocationFromString(String configString) {
        String spawnWorld = configString.split(":")[0];
        String[] spawnCoordinates = configString.split(":")[1].split(",");

        return new Location(Bukkit.getWorld(spawnWorld), Double.parseDouble(spawnCoordinates[0]), Double.parseDouble(spawnCoordinates[1]), Double.parseDouble(spawnCoordinates[2]), Float.parseFloat(spawnCoordinates[3]), Float.parseFloat(spawnCoordinates[4]));
    }

    public static String getWorldNameFromLocationString(String locationString) {
        return locationString.split(":")[0];
    }

    public static String stripMiniMessage(String miniMessageText) {
        Component component = MiniMessage.miniMessage().deserialize(miniMessageText);
        String legacyText = LegacyComponentSerializer.legacySection().serialize(component);
        return stripColor(legacyText);
    }

    public static ItemStack createPlayerHead(String playerName) throws DataRequestException {
        String base64Texture = getPlayerHeadTexture(playerName);
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD); // Kafa itemi oluştur
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

        if (base64Texture != null && !base64Texture.isEmpty()) {
            GameProfile profile = new GameProfile(UUID.randomUUID(), playerName);
            profile.getProperties().put("textures", new Property("textures", base64Texture));

            try {
                Field profileField = skullMeta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(skullMeta, profile);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

            skull.setItemMeta(skullMeta);
        }

        return skull;
    }

    public static String getPlayerHeadTexture(String playerName) throws DataRequestException {
        SkinsRestorer skinRestorer = SkinsRestorerProvider.get();
        MojangSkinDataResult skinData = skinRestorer.getSkinStorage().getPlayerSkin(playerName, true).orElse(null);

        if (skinData != null) {
            return skinData.getSkinProperty().getValue(); // Base64 encoded texture
        } else {
            return null; // Eğer skin verisi bulunamazsa null döner
        }
    }
}
