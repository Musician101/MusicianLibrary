package io.musician101.musicianlibrary.java.minecraft.sponge;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;


public class TextUtils
{
    private TextUtils()
    {

    }

    public static Text aquaText(String content)
    {
        return Text.builder(content).color(TextColors.AQUA).build();
    }

    public static Text blackText(String content)
    {
        return Text.builder(content).color(TextColors.BLACK).build();
    }

    public static Text blueText(String content)
    {
        return Text.builder(content).color(TextColors.BLUE).build();
    }

    public static Text darkAquaText(String content)
    {
        return Text.builder(content).color(TextColors.DARK_AQUA).build();
    }

    public static Text darkBlueText(String content)
    {
        return Text.builder(content).color(TextColors.DARK_BLUE).build();
    }

    public static Text darkGrayText(String content)
    {
        return Text.builder(content).color(TextColors.DARK_GRAY).build();
    }

    public static Text darkGreenText(String content)
    {
        return Text.builder(content).color(TextColors.DARK_GREEN).build();
    }

    public static Text darkPurpleText(String content)
    {
        return Text.builder(content).color(TextColors.DARK_PURPLE).build();
    }

    public static Text darkRedText(String content)
    {
        return Text.builder(content).color(TextColors.DARK_RED).build();
    }

    public static Text goldText(String content)
    {
        return Text.builder(content).color(TextColors.GOLD).build();
    }

    public static Text grayText(String content)
    {
        return Text.builder(content).color(TextColors.GRAY).build();
    }

    public static Text greenText(String content)
    {
        return Text.builder(content).color(TextColors.GREEN).build();
    }

    public static Text lightPurpleText(String content)
    {
        return Text.builder(content).color(TextColors.LIGHT_PURPLE).build();
    }

    public static Text redText(String content)
    {
        return Text.builder(content).color(TextColors.RED).build();
    }

    public static Text whiteText(String content)
    {
        return Text.builder(content).color(TextColors.WHITE).build();
    }

    public static Text yellowText(String content)
    {
        return Text.builder(content).color(TextColors.YELLOW).build();
    }
}
