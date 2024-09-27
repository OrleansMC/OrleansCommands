package com.orleansmc.commands.commands;

import com.orleansmc.commands.OrleansCommands;
import com.orleansmc.commands.commands.promotion.PromotionCommand;
import com.orleansmc.commands.commands.vips.*;

public class CommandLoader {
    public static void load(OrleansCommands plugin) {
        CraftingTableCommand.setup(plugin);
        EnderChestCommand.setup(plugin);
        EnchantingTableCommand.setup(plugin);
        RepairCommand.setup(plugin);
        SkinCommand.setup(plugin);
        AnvilCommand.setup(plugin);
        FeedCommand.setup(plugin);
        HeadCommand.setup(plugin);
        PromotionCommand.setup(plugin);
    }
}
