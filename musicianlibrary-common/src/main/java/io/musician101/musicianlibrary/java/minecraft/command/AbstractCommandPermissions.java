package io.musician101.musicianlibrary.java.minecraft.command;


public class AbstractCommandPermissions<M>
{
    private final boolean isPlayerOnly;
    private final M noPermission;
    private final String permissionNode;
    private final M playerOnly;

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
