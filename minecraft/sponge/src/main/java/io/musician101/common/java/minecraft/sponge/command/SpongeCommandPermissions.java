package io.musician101.common.java.minecraft.sponge.command;

import io.musician101.common.java.minecraft.command.AbstractCommandPermissions;
import org.spongepowered.api.text.Text;

@SuppressWarnings("WeakerAccess")
public class SpongeCommandPermissions extends AbstractCommandPermissions<Text>
{
    public SpongeCommandPermissions(String permissionNode, boolean isPlayerOnly, Text noPermission, Text playerOnly)
    {
        super(permissionNode, isPlayerOnly, noPermission, playerOnly);
    }
}
