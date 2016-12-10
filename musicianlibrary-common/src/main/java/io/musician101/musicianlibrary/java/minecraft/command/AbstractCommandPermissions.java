package io.musician101.musicianlibrary.java.minecraft.command;


import io.musician101.musicianlibrary.java.minecraft.MLResettableBuilder;
import javax.annotation.Nonnull;

public abstract class AbstractCommandPermissions<M, S> {

    private boolean isPlayerOnly;
    private M noPermission;
    private String permissionNode;
    private M playerOnly;

    public M getNoPermissionMessage() {
        return noPermission;
    }

    protected void setNoPermissionMessage(M noPermission) {
        this.noPermission = noPermission;
    }

    public String getPermissionNode() {
        return permissionNode;
    }

    protected void setPermissionNode(String permissionNode) {
        this.permissionNode = permissionNode;
    }

    public M getPlayerOnlyMessage() {
        return playerOnly;
    }

    protected void setPlayerOnlyMessage(M playerOnly) {
        this.playerOnly = playerOnly;
    }

    public boolean isPlayerOnly() {
        return isPlayerOnly;
    }

    protected void setIsPlayerOnly(boolean isPlayerOnly) {
        this.isPlayerOnly = isPlayerOnly;
    }

    public abstract boolean testPermissions(S sender);

    protected static abstract class AbstractCommandPermissionsBuilder<B extends AbstractCommandPermissionsBuilder<B, M, S, T>, M, S, T> implements MLResettableBuilder<T, B> {

        protected boolean isPlayerOnly = false;
        protected M noPermission;
        protected String permissionNode;
        protected M playerOnly;

        @Nonnull
        public abstract B isPlayerOnly(boolean isPlayerOnly);

        @Nonnull
        public abstract B noPermissionMessage(@Nonnull M noPermission);

        @Nonnull
        public abstract B permissionNode(@Nonnull String permissionNode);

        @Nonnull
        public abstract B playerOnlyMessage(@Nonnull M playerOnly);
    }
}
