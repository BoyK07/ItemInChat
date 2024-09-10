package com.boyk07.itemsinchat;

import net.fabricmc.api.ModInitializer;
import net.minecraft.server.network.ServerPlayerEntity;

public class ItemsInChat implements ModInitializer {
    @Override
    public void onInitialize() {
        // Register the server-side chat listener
        ChatListener.registerChatListener();
    }

    // Handles the message modification
    public static String handleChatMessage(String chatMessage, ServerPlayerEntity sender) {
        // Check if the message contains [item] or [i]
        if (chatMessage.contains("[item]") || chatMessage.contains("[i]")) {
            return ItemDisplayHandler.handleItemDisplay(chatMessage, sender);
        }
        return chatMessage; // Always return the original message if no modification is made
    }
}
