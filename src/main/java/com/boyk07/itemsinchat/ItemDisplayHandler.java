package com.boyk07.itemsinchat;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemDisplayHandler {
    public static MutableText handleItemDisplay(String chatText, ServerPlayerEntity player) {
        ItemStack heldItem = player.getStackInHand(Hand.MAIN_HAND);

        if (!heldItem.isEmpty()) {
            HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackContent(heldItem));

            MutableText itemText;
            if (heldItem.getCount() > 1) {
                itemText = Text.literal("§d" + heldItem.getName().getString() + " x" + heldItem.getCount() + "§f")
                        .styled(style -> style.withHoverEvent(hoverEvent));
            } else {
                itemText = Text.literal("§d" + heldItem.getName().getString() + "§f")
                        .styled(style -> style.withHoverEvent(hoverEvent));
            }

            MutableText finalMessage = Text.literal("");
            int start = 0;

            Pattern pattern = Pattern.compile("\\[item\\]|\\[i\\]");
            return getMutableText(chatText, itemText, finalMessage, start, pattern);
        }

        return Text.literal(chatText);
    }

    public static MutableText handleArmorDisplay(String chatText, ServerPlayerEntity player) {
        ItemStack[] armorItems = player.getInventory().armor.toArray(new ItemStack[0]);

        MutableText armorText = Text.literal("");
        boolean hasArmor = false;

        for (int i = armorItems.length - 1; i >= 0; i--) {
            ItemStack armorPiece = armorItems[i];
            if (!armorPiece.isEmpty()) {
                hasArmor = true;
                Text armorName = armorPiece.getName();
                HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackContent(armorPiece));

                MutableText armorDisplay = Text.literal("§d" + armorName.getString() + "§f")
                        .styled(style -> style.withHoverEvent(hoverEvent));

                if (i != 0) {
                    armorText = armorText.append(armorDisplay).append(", ");
                } else {
                    armorText = armorText.append(armorDisplay).append(" ");
                }
            }
        }

        if (hasArmor) {
            MutableText finalMessage = Text.literal("");
            int start = 0;

            Pattern pattern = Pattern.compile("\\[armor\\]|\\[a\\]");
            return getMutableText(chatText, armorText, finalMessage, start, pattern);
        }

        return Text.literal(chatText);
    }

    public static MutableText handleOffhandDisplay(String chatText, ServerPlayerEntity player) {
        ItemStack heldItem = player.getStackInHand(Hand.OFF_HAND);

        if (!heldItem.isEmpty()) {
            HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackContent(heldItem));

            MutableText itemText;
            if (heldItem.getCount() > 1) {
                itemText = Text.literal("§d" + heldItem.getName().getString() + " x" + heldItem.getCount() + "§f")
                        .styled(style -> style.withHoverEvent(hoverEvent));
            } else {
                itemText = Text.literal("§d" + heldItem.getName().getString() + "§f")
                        .styled(style -> style.withHoverEvent(hoverEvent));
            }

            MutableText finalMessage = Text.literal("");
            int start = 0;

            Pattern pattern = Pattern.compile("\\[offhand\\]|\\[o\\]");
            return getMutableText(chatText, itemText, finalMessage, start, pattern);
        }

        return Text.literal(chatText);
    }

    public static MutableText handleEnderchestDisplay(String chatText, ServerPlayerEntity player) {
        String clickId = CommandHandler.generateEnderchestClickId(player);

        MutableText enderChestText = Text.literal("§d[Enderchest]§f").styled(style ->
                style.withHoverEvent(
                        new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Click to open " + player.getDisplayName().getString() + "'s Enderchest"))
                ).withClickEvent(
                        new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/openenderchest " + clickId)
                )
        );

        MutableText finalMessage = Text.literal("");
        int start = 0;

        Pattern pattern = Pattern.compile("\\[enderchest\\]|\\[ec\\]");
        return getMutableText(chatText, enderChestText, finalMessage, start, pattern);
    }

    public static MutableText handleInventoryDisplay(String chatText, ServerPlayerEntity player) {
        String clickId = CommandHandler.generateInventoryClickId(player);

        MutableText inventoryText = Text.literal("§d[Inventory]§f").styled(style ->
                style.withHoverEvent(
                        new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Click to open " + player.getDisplayName().getString() + "'s inventory"))
                ).withClickEvent(
                        new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/openinventory " + clickId)
                )
        );

        MutableText finalMessage = Text.literal("");
        int start = 0;

        Pattern pattern = Pattern.compile("\\[inventory\\]|\\[inv\\]");
        return getMutableText(chatText, inventoryText, finalMessage, start, pattern);
    }

    private static MutableText getMutableText(String chatText, MutableText inventoryText, MutableText finalMessage, int start, Pattern pattern) {
        Matcher matcher = pattern.matcher(chatText);
        while (matcher.find()) {
            finalMessage = finalMessage.append(chatText.substring(start, matcher.start()));
            finalMessage = finalMessage.append(inventoryText);
            start = matcher.end();
        }

        finalMessage = finalMessage.append(chatText.substring(start));
        return finalMessage;
    }
}
