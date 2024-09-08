package com.orleansmc.commands.commands.vips;

import com.orleansmc.commands.OrleansCommands;
import me.lucko.helper.Commands;

public class EnchantingTableCommand {
    public static void setup(OrleansCommands plugin) {
        Commands.create()
                .assertPlayer()
                .assertPermission("orleansmc.commands.enchantingtable")
                .handler(c -> {
                    c.sender().openEnchanting(null, true);
                })
                .registerAndBind(plugin, "enchanting", "büyü-masası");
    }
}
