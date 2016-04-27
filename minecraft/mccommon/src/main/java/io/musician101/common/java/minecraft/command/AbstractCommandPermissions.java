package io.musician101.common.java.minecraft.command;

public class AbstractCommandPermissions<M>
{
    private boolean isPlayerOnly;
    private M noPermission;
    private String permissionNode;
    private M playerOnly;

    protected AbstractCommandPermissions(String permissionNode, boolean isPlayerOnly, M noPermission, M playerOnly)
    {
        this.permissionNode = permissionNode;
        this.isPlayerOnly = isPlayerOnly;
        this.noPermission = noPermission;
        this.playerOnly = playerOnly;
    }

    public boolean isPlayerOnly()
    {
        return isPlayerOnly;
    }

    public M getNoPermissionMessage()
    {
        return noPermission;
    }

    public String getPermissionNode()
    {
        return permissionNode;
    }

    public M getPlayerOnlyMessage()
    {
        return playerOnly;
    }
}
