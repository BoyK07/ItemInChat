package com.boyk07.itemsinchat;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class ItemDisplayHandler {

    // Returns true if the message was modified
    public static boolean handleItemDisplay(String chatText, ServerPlayerEntity player) {
        // Check if the message contains [item] or [i]
        if (chatText.contains("[item]") || chatText.contains("[i]")) {
            ItemStack heldItem = player.getStackInHand(Hand.MAIN_HAND);

            if (!heldItem.isEmpty()) {
                Text itemText = heldItem.getName();
                HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackContent(heldItem));

                itemText = itemText.copy().styled(style -> style.withHoverEvent(hoverEvent));

                // Handle stack size
                if (heldItem.getCount() > 1) {
                    itemText = Text.literal(heldItem.getName().getString() + " x" + heldItem.getCount())
                            .styled(style -> style.withHoverEvent(hoverEvent));
                }

                // Replace [item] or [i] in the message with the hoverable item text
                String replacedMessage = chatText.replace("[item]", itemText.getString()).replace("[i]", itemText.getString());

                // Broadcast the modified message to all players on the server
                MinecraftServer server = player.getServer();
                if (server != null) {
                    server.getPlayerManager().broadcast(Text.literal(replacedMessage), false);
                }

                return true; // Message was modified
            }
        }
        return false; // No modification was made
    }
}
