package com.boyk07.itemsinchat;

import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.server.network.ServerPlayerEntity;

public class ChatListener {

    public static void registerChatListener() {
        // Listen to chat messages
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((message, sender, type) -> {
            if (sender instanceof ServerPlayerEntity) {
                String chatMessage = message.getContent().getString();
                boolean handled = ItemsInChat.handleChatMessage(chatMessage, (ServerPlayerEntity) sender);

                // If the message was handled and modified, cancel the original one
                return !handled; // Return false to cancel the original message if handled
            }
            return true; // Let unhandled messages continue
        });
    }
}
