package com.boyk07.itemsinchat;

import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class ItemDisplayHandler {
    public static MutableText handleItemDisplay(String chatText, ServerPlayerEntity player) {
        ItemStack heldItem = player.getStackInHand(Hand.MAIN_HAND);

        if (!heldItem.isEmpty()) {
            // Create hover event for the item in the main hand
            HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackContent(heldItem));

            // Create the formatted item text with the hover event
            MutableText itemText;
            if (heldItem.getCount() > 1) {
                itemText = Text.literal("§d" + heldItem.getName().getString() + " x" + heldItem.getCount() + "§f")
                        .styled(style -> style.withHoverEvent(hoverEvent));
            } else {
                itemText = Text.literal("§d" + heldItem.getName().getString() + "§f")
                        .styled(style -> style.withHoverEvent(hoverEvent));
            }

            // Replace both [item] and [i] in the message with the itemText
            String[] splitText = chatText.split("\\[item\\]|\\[i\\]"); // Supports both [item] and [i]
            MutableText finalMessage = Text.literal(splitText[0]); // Start with the first part of the message

            // Add the item text and then the rest of the split text
            for (int i = 1; i < splitText.length; i++) {
                finalMessage = finalMessage.append(itemText).append(splitText[i]);
            }

            return finalMessage; // Return the final MutableText message with hoverable item
        }

        // If no item is held, return the original chatText as a MutableText
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

                // Add commas between armor pieces
                if (i != 0) {
                    armorText = armorText.append(armorDisplay).append(", ");
                } else {
                    armorText = armorText.append(armorDisplay).append(" ");
                }
            }
        }

        if (hasArmor) {
            // Split and rebuild the chat text
            String[] splitText = chatText.split("\\[armor\\]|\\[a\\]");
            MutableText finalMessage = Text.literal(splitText[0]);

            for (int i = 1; i < splitText.length; i++) {
                finalMessage = finalMessage.append(armorText).append(splitText[i]);
            }

            return finalMessage;
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

            // Split and rebuild the chat text
            String[] splitText = chatText.split("\\[offhand\\]|\\[o\\]");
            MutableText finalMessage = Text.literal(splitText[0]);

            for (int i = 1; i < splitText.length; i++) {
                finalMessage = finalMessage.append(itemText).append(splitText[i]);
            }

            return finalMessage;
        }

        return Text.literal(chatText);
    }

    public static MutableText handleEnderchestDisplay(String chatText, ServerPlayerEntity player) {
        EnderChestInventory enderChest = player.getEnderChestInventory();

        StringBuilder enderChestItems = new StringBuilder();
        boolean hasItems = false;

        // Build a string containing the Ender Chest's contents
        for (int i = 0; i < enderChest.size(); i++) {
            ItemStack itemStack = enderChest.getStack(i);
            if (!itemStack.isEmpty()) {
                hasItems = true;
                enderChestItems.append(itemStack.getName().getString())
                        .append(" x")
                        .append(itemStack.getCount())
                        .append("\n");
            }
        }

        if (hasItems) {
            // Generate a unique click ID
            String clickId = CommandHandler.generateEnderchestClickId(player);

            // Create the clickable [Enderchest] text with hover and click events
            MutableText enderChestText = Text.literal("§d[Enderchest]§f").styled(style ->
                    style.withHoverEvent(
                            new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(enderChestItems.toString()))
                    ).withClickEvent(
                            new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/openenderchest " + clickId)
                    )
            );

            // Split and rebuild the chat text
            String[] splitText = chatText.split("\\[enderchest\\]|\\[ec\\]");
            MutableText finalMessage = Text.literal(splitText[0]);

            for (int i = 1; i < splitText.length; i++) {
                finalMessage = finalMessage.append(enderChestText).append(splitText[i]);
            }

            return finalMessage;
        }

        return Text.literal(chatText);
    }
}
