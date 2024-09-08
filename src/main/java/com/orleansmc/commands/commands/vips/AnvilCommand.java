package com.orleansmc.commands.commands.vips;

import com.orleansmc.commands.OrleansCommands;
import me.lucko.helper.Commands;
import org.bukkit.Sound;

public class AnvilCommand {
    public static void setup(OrleansCommands plugin) {
        Commands.create()
                .assertPlayer()
                .assertPermission("orleansmc.commands.anvil")
                .handler(c -> {
                    c.sender().openAnvil(null, true);
                    c.sender().playSound(
                            c.sender().getLocation(),
                            Sound.BLOCK_ANVIL_PLACE,
                            1,
                            1
                    );
                })
                .registerAndBind(plugin, "anvil", "örs");
    }
}
