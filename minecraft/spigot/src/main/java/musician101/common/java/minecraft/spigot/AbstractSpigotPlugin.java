package musician101.common.java.minecraft.spigot;

import musician101.common.java.minecraft.spigot.command.AbstractSpigotCommand;
import net.gravitydevelopment.updater.Updater;
import net.gravitydevelopment.updater.Updater.UpdateResult;
import net.gravitydevelopment.updater.Updater.UpdateType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class AbstractSpigotPlugin<Plugin extends AbstractSpigotPlugin<Plugin, Config>, Config extends AbstractSpigotConfig<Plugin>> extends JavaPlugin
{
    protected Config config;
    protected List<AbstractSpigotCommand<Plugin>> commands;

    protected void versionCheck(int pluginNumber)
    {
        if (config.isUpdateCheckEnabled())
        {
            Updater updater = new Updater(this, pluginNumber, this.getFile(), UpdateType.NO_DOWNLOAD, true);
            if (updater.getResult() == UpdateResult.NO_UPDATE)
                getLogger().info("There are no new versions available.");
            else if (updater.getResult() == UpdateResult.FAIL_DBO)
                getLogger().info("Failed to connect to CurseAPI.");
            else if (updater.getResult() == UpdateResult.UPDATE_AVAILABLE)
            {
                getLogger().info("An update is available!");
                getLogger().info(updater.getLatestName() + " " + updater.getLatestType() + " " + updater.getLatestGameVersion() + " " + updater.getLatestFileLink());
            }
            else
                getLogger().info("An error occurred while attempting to check for updates");
        }
        else
            getLogger().info("Update is disabled");
    }

    public Config getPluginConfig()
    {
        return config;
    }

    public List<AbstractSpigotCommand<Plugin>> getCommands()
    {
        return commands;
    }
}
