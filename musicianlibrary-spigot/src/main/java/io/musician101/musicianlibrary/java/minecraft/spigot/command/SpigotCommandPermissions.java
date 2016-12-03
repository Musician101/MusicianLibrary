package io.musician101.musicianlibrary.java.minecraft.spigot.command;

import io.musician101.musicianlibrary.java.minecraft.command.AbstractCommandPermissions;

public class SpigotCommandPermissions extends AbstractCommandPermissions<String> {
    public SpigotCommandPermissions(String permissionNode, boolean isPlayerOnly, String noPermission, String playerOnly) {
        super(permissionNode, isPlayerOnly, noPermission, playerOnly);
    }
}
