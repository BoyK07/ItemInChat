package com.boyk07.itemsinchat;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class ItemDisplayHandler {

    // Function to handle [item] or [i] placeholders
    public static String handleItemDisplay(String chatText, ServerPlayerEntity player) {
        ItemStack heldItem = player.getStackInHand(Hand.MAIN_HAND);

        if (!heldItem.isEmpty()) {
            Text itemText = heldItem.getName();
            HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackContent(heldItem));

            itemText = itemText.copy().styled(style -> style.withHoverEvent(hoverEvent));

            if (heldItem.getCount() > 1) {
                itemText = Text.literal("§d" + heldItem.getName().getString() + " x" + heldItem.getCount() + "§f")
                        .styled(style -> style.withHoverEvent(hoverEvent));
            } else {
                itemText = Text.literal("§d" + heldItem.getName().getString() + "§f")
                        .styled(style -> style.withHoverEvent(hoverEvent));
            }

            String replacedMessageContent = chatText.replace("[item]", itemText.getString()).replace("[i]", itemText.getString());

           return replacedMessageContent;
        }
        return chatText;
    }

    // Function to handle [armor] or [a] placeholders
    public static String handleArmorDisplay(String chatText, ServerPlayerEntity player) {
        ItemStack[] armorItems = player.getInventory().armor.toArray(new ItemStack[0]);

        StringBuilder armorText = new StringBuilder();
        boolean hasArmor = false;

        // Loop through armor slots (0: boots, 1: leggings, 2: chestplate, 3: helmet)
        for (int i = armorItems.length - 1; i >= 0; i--) {
            ItemStack armorPiece = armorItems[i];
            if (!armorPiece.isEmpty()) {
                hasArmor = true;
                Text armorName = armorPiece.getName();
                HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackContent(armorPiece));

                Text armorDisplay = Text.literal("§d" + armorName.getString() + "§f")
                        .styled(style -> style.withHoverEvent(hoverEvent));

                // Append each armor piece in the order Helmet -> Chestplate -> Leggings -> Boots
                armorText.append(armorDisplay.getString()).append(" ");
            }
        }

        if (hasArmor) {
            // Replace [armor] or [a] in the message
            return chatText.replace("[armor]", armorText.toString().trim()).replace("[a]", armorText.toString().trim());
        }

        return chatText; // Return original message if no armor is present
    }

    public static String handleOffhandDisplay(String chatText, ServerPlayerEntity player) {
        ItemStack heldItem = player.getStackInHand(Hand.OFF_HAND);

        if (!heldItem.isEmpty()) {
            Text itemText = heldItem.getName();
            HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackContent(heldItem));

            itemText = itemText.copy().styled(style -> style.withHoverEvent(hoverEvent));

            if (heldItem.getCount() > 1) {
                itemText = Text.literal("§d" + heldItem.getName().getString() + " x" + heldItem.getCount() + "§f")
                        .styled(style -> style.withHoverEvent(hoverEvent));
            } else {
                itemText = Text.literal("§d" + heldItem.getName().getString() + "§f")
                        .styled(style -> style.withHoverEvent(hoverEvent));
            }

            String replacedMessageContent = chatText.replace("[item]", itemText.getString()).replace("[i]", itemText.getString());

            return replacedMessageContent;
        }
        return chatText;
    }
}
