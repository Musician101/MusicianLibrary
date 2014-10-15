package musician101.luc.bukkit.command;

import java.util.List;

import org.bukkit.entity.Player;

public interface ICommand
{
	String getName();
	
	String getDescription();
	
	String getUsage();
	
	String getPermission();
	
	List<String> getAliases();
	
	void execute(Player player, List<String> args) throws Exception;
}
