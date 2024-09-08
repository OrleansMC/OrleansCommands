package com.orleansmc.commands.commands.vips;

import com.orleansmc.commands.OrleansCommands;
import me.lucko.helper.Commands;

public class EnderChestCommand {
    public static void setup(OrleansCommands plugin) {
        Commands.create()
                .assertPlayer()
                .assertPermission("orleansmc.commands.enderchest")
                .handler(c -> {
                    c.sender().openInventory(c.sender().getEnderChest());
                })
                .registerAndBind(plugin, "enderchest");
    }
}
