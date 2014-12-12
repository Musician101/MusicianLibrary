package musician101.luc.bukkit.command;

import java.util.List;

import org.bukkit.command.CommandSender;

public interface ISubCommand
{
	String getName();
	
	String getDescription();
	
	String getUsage();
	
	String getPermission();
	
	List<String> getAliases();
	
	void execute(CommandSender sender, List<String> args) throws Exception;
}
