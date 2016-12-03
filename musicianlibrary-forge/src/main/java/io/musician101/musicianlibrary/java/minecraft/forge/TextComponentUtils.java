package io.musician101.musicianlibrary.java.minecraft.forge;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class TextComponentUtils {
    private TextComponentUtils() {

    }

    public static ITextComponent aquaText(String content) {
        return new TextComponentString(content).setStyle(new Style().setColor(TextFormatting.AQUA));
    }

    public static ITextComponent blackText(String content) {
        return new TextComponentString(content).setStyle(new Style().setColor(TextFormatting.BLACK));
    }

    public static ITextComponent blueText(String content) {
        return new TextComponentString(content).setStyle(new Style().setColor(TextFormatting.BLUE));
    }

    public static ITextComponent darkAquaText(String content) {
        return new TextComponentString(content).setStyle(new Style().setColor(TextFormatting.DARK_AQUA));
    }

    public static ITextComponent darkBlueText(String content) {
        return new TextComponentString(content).setStyle(new Style().setColor(TextFormatting.DARK_BLUE));
    }

    public static ITextComponent darkGrayText(String content) {
        return new TextComponentString(content).setStyle(new Style().setColor(TextFormatting.DARK_GRAY));
    }

    public static ITextComponent darkGreenText(String content) {
        return new TextComponentString(content).setStyle(new Style().setColor(TextFormatting.DARK_GREEN));
    }

    public static ITextComponent darkPurpleText(String content) {
        return new TextComponentString(content).setStyle(new Style().setColor(TextFormatting.DARK_PURPLE));
    }

    public static ITextComponent darkRedText(String content) {
        return new TextComponentString(content).setStyle(new Style().setColor(TextFormatting.DARK_RED));
    }

    public static ITextComponent goldText(String content) {
        return new TextComponentString(content).setStyle(new Style().setColor(TextFormatting.GOLD));
    }

    public static ITextComponent grayText(String content) {
        return new TextComponentString(content).setStyle(new Style().setColor(TextFormatting.GRAY));
    }

    public static ITextComponent greenText(String content) {
        return new TextComponentString(content).setStyle(new Style().setColor(TextFormatting.GREEN));
    }

    public static ITextComponent lightPurpleText(String content) {
        return new TextComponentString(content).setStyle(new Style().setColor(TextFormatting.LIGHT_PURPLE));
    }

    public static ITextComponent redText(String content) {
        return new TextComponentString(content).setStyle(new Style().setColor(TextFormatting.RED));
    }

    public static ITextComponent whiteText(String content) {
        return new TextComponentString(content).setStyle(new Style().setColor(TextFormatting.WHITE));
    }

    public static ITextComponent yellowText(String content) {
        return new TextComponentString(content).setStyle(new Style().setColor(TextFormatting.YELLOW));
    }
}
