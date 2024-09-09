package com.boyk07.itemsinchat;

import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class ChatListener {

    public static void registerChatListener() {
        // Register for server chat messages
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((message, sender, type) -> {
            if (sender instanceof ServerPlayerEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity) sender;
                String chatMessage = message.getContent().getString();

                // Modify the message using ItemDisplayHandler
                String modifiedMessage = ItemsInChat.handleChatMessage(chatMessage, player);

                // Broadcast the modified message if needed
                if (!modifiedMessage.equals(chatMessage)) {
                    // Cancel the original message
                    Text newMessage = Text.literal("<" + player.getDisplayName().getString() + "> " + modifiedMessage);
                    player.getServer().getPlayerManager().broadcast(newMessage, false);
                    return false; // Cancel the original message
                }
                return true; // Allow the original message if no modification was made
            }
            return true; // Let other messages continue
        });
    }
}
