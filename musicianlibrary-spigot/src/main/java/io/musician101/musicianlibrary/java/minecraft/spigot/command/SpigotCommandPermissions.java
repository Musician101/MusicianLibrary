package io.musician101.musicianlibrary.java.minecraft.spigot.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpigotCommandPermissions {

    private final boolean isPlayerOnly;
    private final String noPermission;
    private final String permissionNode;
    private final String playerOnly;

    SpigotCommandPermissions(boolean isPlayerOnly, String noPermission, String permissionNode, String playerOnly) {
        this.isPlayerOnly = isPlayerOnly;
        this.noPermission = noPermission;
        this.permissionNode = permissionNode;
        this.playerOnly = playerOnly;
    }

    public static SpigotCommandPermissions blank() {
        return builder().build();
    }

    public static SpigotCommandPermissionsBuilder builder() {
        return new SpigotCommandPermissionsBuilder();
    }

    public String getNoPermissionMessage() {
        return noPermission;
    }

    public String getPermissionNode() {
        return permissionNode;
    }

    public String getPlayerOnlyMessage() {
        return playerOnly;
    }

    public boolean isPlayerOnly() {
        return isPlayerOnly;
    }

    public boolean testPermissions(CommandSender sender) {
        if (isPlayerOnly() && !(sender instanceof Player)) {
            sender.sendMessage(getPlayerOnlyMessage());
            return false;
        }

        if (!sender.hasPermission(getPermissionNode())) {
            sender.sendMessage(getNoPermissionMessage());
            return false;
        }

        return true;
    }
}
