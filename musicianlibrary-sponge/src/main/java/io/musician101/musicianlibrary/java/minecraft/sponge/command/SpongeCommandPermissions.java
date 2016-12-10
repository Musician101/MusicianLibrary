package io.musician101.musicianlibrary.java.minecraft.sponge.command;

import io.musician101.musicianlibrary.java.minecraft.command.AbstractCommandPermissions;
import javax.annotation.Nonnull;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class SpongeCommandPermissions extends AbstractCommandPermissions<Text, CommandSource> {

    SpongeCommandPermissions() {

    }

    @Override
    public boolean testPermissions(CommandSource sender) {
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

    public static class SpongeCommandPermissionsBuilder extends AbstractCommandPermissionsBuilder<SpongeCommandPermissionsBuilder, Text, CommandSource, SpongeCommandPermissions> {

        SpongeCommandPermissionsBuilder() {

        }

        @Nonnull
        @Override
        public SpongeCommandPermissions build() throws IllegalStateException {
            SpongeCommandPermissions scp = new SpongeCommandPermissions();
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
        public SpongeCommandPermissionsBuilder reset() {
            isPlayerOnly = false;
            noPermission = null;
            permissionNode = null;
            playerOnly = null;
            return this;
        }

        @Nonnull
        @Override
        public SpongeCommandPermissionsBuilder setIsPlayerOnly(boolean isPlayerOnly) {
            this.isPlayerOnly = isPlayerOnly;
            return this;
        }

        @Nonnull
        @Override
        public SpongeCommandPermissionsBuilder setNoPermissionMessage(Text noPermission) {
            this.noPermission = noPermission;
            return this;
        }

        @Nonnull
        @Override
        public SpongeCommandPermissionsBuilder setPermissionNode(String permissionNode) {
            this.permissionNode = permissionNode;
            return this;
        }

        @Nonnull
        @Override
        public SpongeCommandPermissionsBuilder setPlayerOnlyMessage(Text playerOnly) {
            this.playerOnly = playerOnly;
            return this;
        }
    }
}
