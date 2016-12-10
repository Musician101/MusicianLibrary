package io.musician101.musicianlibrary.java.minecraft.spigot.command;

import io.musician101.musicianlibrary.java.minecraft.command.AbstractCommandPermissions;
import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpigotCommandPermissions extends AbstractCommandPermissions<String, CommandSender> {

    SpigotCommandPermissions() {

    }

    public static SpigotCommandPermissions blank() {
        return builder().setIsPlayerOnly(false).setPermissionNode("").setNoPermissionMessage("").setNoPermissionMessage("").build();
    }

    public static SpigotCommandPermissionsBuilder builder() {
        return new SpigotCommandPermissionsBuilder();
    }

    @Override
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

    public static class SpigotCommandPermissionsBuilder extends AbstractCommandPermissionsBuilder<SpigotCommandPermissionsBuilder, String, CommandSender, SpigotCommandPermissions> {

        SpigotCommandPermissionsBuilder() {

        }

        @Nonnull
        @Override
        public SpigotCommandPermissions build() throws IllegalStateException {
            SpigotCommandPermissions scp = new SpigotCommandPermissions();
            if (noPermission == null)
                throw new IllegalStateException("NoPermission has not been set.");

            if (permissionNode == null)
                throw new IllegalStateException("PermissionNode has not been set.");

            if (playerOnly == null)
                throw new IllegalStateException("PlayerOnly has not been set.");

            scp.setIsPlayerOnly(isPlayerOnly);
            scp.setNoPermissionMessage(noPermission);
            scp.setPermissionNode(permissionNode);
            scp.setPlayerOnlyMessage(playerOnly);
            return scp;
        }

        @Nonnull
        @Override
        public SpigotCommandPermissionsBuilder reset() {
            isPlayerOnly = false;
            noPermission = null;
            permissionNode = null;
            playerOnly = null;
            return this;
        }

        @Nonnull
        @Override
        public SpigotCommandPermissionsBuilder setIsPlayerOnly(boolean isPlayerOnly) {
            this.isPlayerOnly = isPlayerOnly;
            return this;
        }

        @Nonnull
        @Override
        public SpigotCommandPermissionsBuilder setNoPermissionMessage(String noPermission) {
            this.noPermission = noPermission;
            return this;
        }

        @Nonnull
        @Override
        public SpigotCommandPermissionsBuilder setPermissionNode(String permissionNode) {
            this.permissionNode = permissionNode;
            return this;
        }

        @Nonnull
        @Override
        public SpigotCommandPermissionsBuilder setPlayerOnlyMessage(String playerOnly) {
            this.playerOnly = playerOnly;
            return this;
        }
    }
}
