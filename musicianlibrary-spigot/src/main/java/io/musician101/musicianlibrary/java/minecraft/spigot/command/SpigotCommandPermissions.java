package io.musician101.musicianlibrary.java.minecraft.spigot.command;

import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpigotCommandPermissions {

    private boolean isPlayerOnly;
    private String noPermission;
    private String permissionNode;
    private String playerOnly;

    private SpigotCommandPermissions() {

    }

    public static SpigotCommandPermissions blank() {
        return builder().isPlayerOnly(false).permissionNode("").noPermissionMessage("").noPermissionMessage("").build();
    }

    public static SpigotCommandPermissionsBuilder builder() {
        return new SpigotCommandPermissionsBuilder();
    }

    public String getNoPermissionMessage() {
        return noPermission;
    }

    protected void setNoPermissionMessage(String noPermission) {
        this.noPermission = noPermission;
    }

    public String getPermissionNode() {
        return permissionNode;
    }

    protected void setPermissionNode(String permissionNode) {
        this.permissionNode = permissionNode;
    }

    public String getPlayerOnlyMessage() {
        return playerOnly;
    }

    protected void setPlayerOnlyMessage(String playerOnly) {
        this.playerOnly = playerOnly;
    }

    public boolean isPlayerOnly() {
        return isPlayerOnly;
    }

    protected void setIsPlayerOnly(boolean isPlayerOnly) {
        this.isPlayerOnly = isPlayerOnly;
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

    public static class SpigotCommandPermissionsBuilder {

        private boolean isPlayerOnly = false;
        private String noPermission;
        private String permissionNode;
        private String playerOnly;

        SpigotCommandPermissionsBuilder() {

        }

        @Nonnull
        public SpigotCommandPermissions build() throws IllegalStateException {
            SpigotCommandPermissions scp = new SpigotCommandPermissions();
            if (noPermission == null) {
                throw new IllegalStateException("NoPermission has not been set.");
            }

            if (permissionNode == null) {
                throw new IllegalStateException("PermissionNode has not been set.");
            }

            if (playerOnly == null) {
                throw new IllegalStateException("PlayerOnly has not been set.");
            }

            scp.setIsPlayerOnly(isPlayerOnly);
            scp.setNoPermissionMessage(noPermission);
            scp.setPermissionNode(permissionNode);
            scp.setPlayerOnlyMessage(playerOnly);
            return scp;
        }

        @Nonnull
        public SpigotCommandPermissionsBuilder isPlayerOnly(boolean isPlayerOnly) {
            this.isPlayerOnly = isPlayerOnly;
            return this;
        }

        @Nonnull
        public SpigotCommandPermissionsBuilder noPermissionMessage(String noPermission) {
            this.noPermission = noPermission;
            return this;
        }

        @Nonnull
        public SpigotCommandPermissionsBuilder permissionNode(@Nonnull String permissionNode) {
            this.permissionNode = permissionNode;
            return this;
        }

        @Nonnull
        public SpigotCommandPermissionsBuilder playerOnlyMessage(@Nonnull String playerOnly) {
            this.playerOnly = playerOnly;
            return this;
        }

        @Nonnull
        public SpigotCommandPermissionsBuilder reset() {
            isPlayerOnly = false;
            noPermission = null;
            permissionNode = null;
            playerOnly = null;
            return this;
        }
    }
}
