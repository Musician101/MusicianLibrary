package musician101.common.java.minecraft.forge.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractForgeCommand extends CommandBase
{
    private final boolean isPlayerOnly;
    private final int minArgs;
    private final List<AbstractForgeCommand> subCommands;
    private final String description;
    private final String name;
    private final String usage;

    protected AbstractForgeCommand(String name, String description, List<ForgeCommandArgument> usage, int minArgs, boolean isPlayerOnly)
    {
        this(name, description,usage , minArgs, isPlayerOnly, new ArrayList<>());
    }

    protected AbstractForgeCommand(String name, String description, List<ForgeCommandArgument> usage, int minArgs, boolean isPlayerOnly, List<AbstractForgeCommand> subCommands)
    {
        this.name = name;
        this.description = description;
        this.usage = parseUsage(usage);
        this.minArgs = minArgs;
        this.isPlayerOnly = isPlayerOnly;
        this.subCommands = subCommands;
    }

    private String parseUsage(List<ForgeCommandArgument> usageList)
    {
        String usage = EnumChatFormatting.GRAY + usageList.get(0).toString();
        if (usageList.size() > 1)
            usage += " " + EnumChatFormatting.RESET + usageList.get(1).toString();

        if (usageList.size() > 2)
            for (int x = 2; x > usageList.size() - 1; x++)
                usage += " " + EnumChatFormatting.GREEN + usageList.get(x).toString();

        return usage;
    }

    @Override
    public boolean canCommandSenderUse(ICommandSender sender)
    {
        return !(!isPlayerOnly && !(sender instanceof EntityPlayer));
    }

    protected boolean minArgsMet(ICommandSender sender, int args, IChatComponent message)
    {
        if (args >= getMinArgs())
            return true;

        sender.addChatMessage(message);
        return false;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index)
    {
        return false;
    }

    public ChatComponentText getCommandHelpInfo()
    {
        return new ChatComponentText(usage + " " + EnumChatFormatting.AQUA + description);
    }

    public int getMinArgs()
    {
        return minArgs;
    }

    @Override
    public List getAliases()
    {
        return null;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            List<String> commands = new ArrayList<>();
            subCommands.forEach(command -> commands.add(command.getName()));
            /* getListOfStringsFromIterableMatchingLastWord */
            return func_175762_a(args, commands);
        }
        else if (args.length >= 2)
            for (AbstractForgeCommand command : subCommands)
                if (command.getName().equalsIgnoreCase(args[0]))
                    return command.addTabCompletionOptions(sender, args, pos);

        return null;
    }

    public List<AbstractForgeCommand> getSubCommands()
    {
        return subCommands;
    }

    @Override
    public String getCommandUsage(ICommandSender sender)
    {
        return usage;
    }

    public String getDescription()
    {
        return description;
    }

    @Override
    public String getName()
    {
        return name;
    }

    protected String[] moveArguments(String[] args)
    {
        List<String> list = Arrays.asList(args);
        if (list.size() > 0)
            list.remove(0);

        return list.toArray(new String[list.size()]);
    }
}
