package io.musician101.musicianlibrary.java.minecraft.forge.command;

import io.musician101.musicianlibrary.java.minecraft.forge.TextComponentUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ModContainer;

public class ForgeHelpCommand<M> extends AbstractForgeCommand<M> {
    private final AbstractForgeCommand<M> mainCommand;

    public ForgeHelpCommand(M modInstance, ICommandSender sender, AbstractForgeCommand<M> mainCommand) {
        super(modInstance, "help", "Display help info for " + mainCommand.getUsage(sender), new ForgeCommandUsage(new ForgeCommandArgument(TextFormatting.getTextWithoutFormattingCodes(mainCommand.getUsage(sender))), new ForgeCommandArgument("help")));
        this.mainCommand = mainCommand;
    }

    @Override
    protected void build() {
        setConsumer((server, sender, args) -> {
            ModContainer modContainer = FMLCommonHandler.instance().findContainerFor(modInstance);
            sender.sendMessage(TextComponentUtils.greenText("===== ")
                    .appendSibling(TextComponentUtils.whiteText(modContainer.getName() + " v" + modContainer.getVersion()))
                    .appendSibling(TextComponentUtils.greenText(" =====")));
            sender.sendMessage(new TextComponentString(mainCommand.getCommandHelpInfo(sender)));
            mainCommand.getSubCommands().forEach(command -> sender.sendMessage(new TextComponentString(command.getCommandHelpInfo(sender))));
        });
    }
}
