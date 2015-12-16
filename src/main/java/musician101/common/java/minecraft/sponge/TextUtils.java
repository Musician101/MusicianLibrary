package musician101.common.java.minecraft.sponge;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

public class TextUtils
{
    public static Text greenText(String content)
    {
        return Texts.builder(content).color(TextColors.GREEN).build();
    }

    public static Text goldText(String content)
    {
        return Texts.builder(content).color(TextColors.GOLD).build();
    }

    public static Text redText(String content)
    {
        return Texts.builder(content).color(TextColors.RED).build();
    }
}
