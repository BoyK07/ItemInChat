package com.boyk07.itemsinchat;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Formatting;

public class ItemDisplayHandler {

	public static String handleItemDisplay(String chatText, ServerPlayerEntity player) {
		// Check if the message contains [item] or [i]
        ItemStack heldItem = player.getStackInHand(Hand.MAIN_HAND);

        if (!heldItem.isEmpty()) {
            Text itemText = heldItem.getName();
            HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackContent(heldItem));

            itemText = itemText.copy().styled(style -> style.withHoverEvent(hoverEvent));

            // Handle stack size
            if (heldItem.getCount() > 1) {
                itemText = Text.literal("§d" + heldItem.getName().getString() + " x" + heldItem.getCount() + "§f")
                    .styled(style -> style.withHoverEvent(hoverEvent));
            } else {
                itemText = Text.literal("§d" + heldItem.getName().getString() + " x1" + "§f")
                    .styled(style -> style.withHoverEvent(hoverEvent));
            }

            // Replace [item] or [i] in the message with the hoverable item text
            String replacedMessageContent = chatText.replace("[item]", itemText.getString()).replace("[i]", itemText.getString());

            // Return the modified message to the event handler
            return replacedMessageContent;
        }
		return chatText; // Return the original if no modification
	}
}
