package com.orleansmc.commands.commands.vips;

import com.orleansmc.commands.OrleansCommands;
import me.lucko.helper.Commands;

public class AnvilCommand {
    public static void setup(OrleansCommands plugin) {
        Commands.create()
                .assertPlayer()
                .assertPermission("orleansmc.commands.anvil")
                .handler(c -> {
                    c.sender().openAnvil(null, true);
                })
                .registerAndBind(plugin, "anvil", "örs");
    }
}
