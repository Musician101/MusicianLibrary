package io.musician101.musicianlibrary.java.minecraft.spigot.command;

import io.musician101.musicianlibrary.java.minecraft.util.Builder;
import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

public class SpigotCommandPermissionsBuilder implements Builder<SpigotCommandPermissionsBuilder, SpigotCommandPermissions> {

    private boolean isPlayerOnly = false;
    private String noPermission = "";
    private String permissionNode = "";
    private String playerOnly = "";

    SpigotCommandPermissionsBuilder() {

    }

    @Nonnull
    @Override
    public SpigotCommandPermissions build() throws IllegalStateException {
        checkNotNull(noPermission, "\"No permission\" message can not be null");
        checkNotNull(permissionNode, "Permission node can not be null");
        checkNotNull(playerOnly, "\"Player Only\" message can not be null");
        return new SpigotCommandPermissions(isPlayerOnly, noPermission, permissionNode, playerOnly);
    }

    @Nonnull
    public SpigotCommandPermissionsBuilder isPlayerOnly(boolean isPlayerOnly) {
        this.isPlayerOnly = isPlayerOnly;
        return this;
    }

    @Nonnull
    public SpigotCommandPermissionsBuilder noPermissionMessage(@Nonnull String noPermission) {
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
    @Override
    public SpigotCommandPermissionsBuilder reset() {
        isPlayerOnly = false;
        noPermission = null;
        permissionNode = null;
        playerOnly = null;
        return this;
    }
}
