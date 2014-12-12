package musician101.luc.bukkit.command;

import java.util.List;

public abstract class AbstractSubCommand implements ISubCommand
{
	String name;
	String description;
	String usage;
	String permission;
	List<String> aliases;
	
	public AbstractSubCommand(String name, String description, String usage, String permission, List<String> aliases)
	{
		this.name = name;
		this.description = description;
		this.usage = usage;
		this.permission = permission;
		this.aliases = aliases;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public String getDescription()
	{
		return description;
	}
	
	@Override
	public String getUsage()
	{
		return usage;
	}
	
	@Override
	public String getPermission()
	{
		return permission;
	}
	
	@Override
	public List<String> getAliases()
	{
		return aliases;
	}
}
