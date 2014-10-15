package musician101.luc.bukkit.command;

import java.util.List;

public interface ICommandManager
{
	List<ICommand> getCommandList();
	
	void addCommand(ICommand command);
	
	void removeCommand(ICommand command);
	
	List<String> getHelp(int page);
}
