package com.boyk07.itemsinchat;

import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class ItemDisplayHandler {
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

    public static String handleArmorDisplay(String chatText, ServerPlayerEntity player) {
        ItemStack[] armorItems = player.getInventory().armor.toArray(new ItemStack[0]);

        StringBuilder armorText = new StringBuilder();
        boolean hasArmor = false;

        for (int i = armorItems.length - 1; i >= 0; i--) {
            ItemStack armorPiece = armorItems[i];
            if (!armorPiece.isEmpty()) {
                hasArmor = true;
                Text armorName = armorPiece.getName();
                HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackContent(armorPiece));

                Text armorDisplay = Text.literal("§d" + armorName.getString() + "§f")
                        .styled(style -> style.withHoverEvent(hoverEvent));

                armorText.append(armorDisplay.getString()).append(" ");
            }
        }

        if (hasArmor) {
            return chatText.replace("[armor]", armorText.toString().trim()).replace("[a]", armorText.toString().trim());
        }
        return chatText;
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

    public static String handleEnderchestDisplay(String chatText, ServerPlayerEntity player) {
        EnderChestInventory enderChest = player.getEnderChestInventory();

        StringBuilder enderChestItems = new StringBuilder();
        boolean hasItems = false;

        for (int i = 0; i < enderChest.size(); i++) {
            ItemStack itemStack = enderChest.getStack(i);
            if (!itemStack.isEmpty()) {
                hasItems = true;
                Text itemName = itemStack.getName();

                enderChestItems.append(itemName.getString())
                        .append(" x")
                        .append(itemStack.getCount())
                        .append("\n");
            }
        }

        if (hasItems) {
            Text enderChestText = Text.literal("§dEnderchest§f").styled(style ->
                    style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(enderChestItems.toString())))
            );

            return chatText.replace("[enderchest]", enderChestText.getString()).replace("[ec]", enderChestText.getString());
        }

        return chatText;
    }
}
