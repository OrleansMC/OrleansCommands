package com.orleansmc.commands.settings;

public record CooldownData(
        String group,
        GroupCooldownData data
) {
}