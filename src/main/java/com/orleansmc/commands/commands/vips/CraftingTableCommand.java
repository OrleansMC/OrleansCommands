package com.orleansmc.commands.commands.vips;

import com.orleansmc.commands.OrleansCommands;
import me.lucko.helper.Commands;

public class CraftingTableCommand {
    public static void setup(OrleansCommands plugin) {
        Commands.create()
                .assertPlayer()
                .assertPermission("orleansmc.commands.craftingtable")
                .handler(c -> {
                    c.sender().openWorkbench(null, true);
                })
                .registerAndBind(plugin, "crafting", "çalışma-masası");
    }
}
