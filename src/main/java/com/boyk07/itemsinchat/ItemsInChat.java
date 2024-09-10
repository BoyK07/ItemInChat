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
        // Process the message for item and armor placeholders
        chatMessage = ItemDisplayHandler.handleItemDisplay(chatMessage, sender);  // Replace [item] or [i]
        chatMessage = ItemDisplayHandler.handleArmorDisplay(chatMessage, sender); // Replace [armor] or [a]
        chatMessage = ItemDisplayHandler.handleOffhandDisplay(chatMessage, sender); // Replace [offhand] or [o]
        chatMessage = ItemDisplayHandler.handleEnderchestDisplay(chatMessage, sender); // Replace [enderchest] or [ec]

        // Return the modified message with all placeholders replaced
        return chatMessage;
    }
}
