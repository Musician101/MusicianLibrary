package io.musician101.musicianlibrary.java.minecraft.sponge.command;

import io.musician101.musicianlibrary.java.minecraft.command.AbstractCommandPermissions;
import org.spongepowered.api.text.Text;

public class SpongeCommandPermissions extends AbstractCommandPermissions<Text> {
    public SpongeCommandPermissions(String permissionNode, boolean isPlayerOnly, Text noPermission, Text playerOnly) {
        super(permissionNode, isPlayerOnly, noPermission, playerOnly);
    }
}
