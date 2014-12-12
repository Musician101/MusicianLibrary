package musician101.luc.bukkit.command;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSubCommandManager implements ISubCommandManager
{
	List<ISubCommand> commandList = new ArrayList<ISubCommand>();
	
	@Override
	public List<ISubCommand> getCommandList()
	{
		return commandList;
	}
	
	@Override
	public void addCommand(ISubCommand command)
	{
		commandList.add(command);
	}
	
	@Override
	public void removeCommand(ISubCommand command)
	{
		commandList.remove(command);
	}
}
