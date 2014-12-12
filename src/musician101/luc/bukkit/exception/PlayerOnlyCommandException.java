package musician101.luc.bukkit.exception;

@SuppressWarnings("serial")
public class PlayerOnlyCommandException extends Exception
{
	public PlayerOnlyCommandException(String message)
	{
		super(message);
	}
}
