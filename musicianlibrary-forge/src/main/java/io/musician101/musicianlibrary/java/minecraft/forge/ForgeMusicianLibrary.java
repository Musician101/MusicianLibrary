package io.musician101.musicianlibrary.java.minecraft.forge;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = ForgeMusicianLibrary.MOD_ID, name = ForgeMusicianLibrary.MOD_NAME, version = ForgeMusicianLibrary.VERSION)
public class ForgeMusicianLibrary {

    public static final String MOD_ID = "forgemusicianlibrary";
    public static final String MOD_NAME = "forgemusicianlibrary";
    public static final String VERSION = "3.0-SNAPSHOT";
    private Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        //TODO need message to alert of api loading, need TextComponent utils
        logger = event.getModLog();
        logger.info("MusicianLibrary " + VERSION + " for Forge");
    }
}
