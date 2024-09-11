package com.boyk07.itemsinchat;

import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class ChatListener {

    public static void registerChatListener() {
        // Register for server chat messages
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((message, sender, type) -> {
            if (sender instanceof ServerPlayerEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity) sender;
                String originalMessage = message.getContent().getString();

                // Modify the message using ItemDisplayHandler, returning a MutableText
                MutableText modifiedMessage = ItemsInChat.handleChatMessage(originalMessage, player);

                // Compare the original and modified messages
                if (!modifiedMessage.getString().equals(originalMessage)) {
                    // Construct a new message to broadcast with the player's name
                    MutableText newMessage = Text.literal("<" + player.getDisplayName().getString() + "> ")
                            .append(modifiedMessage);

                    // Broadcast the modified message to all players
                    player.getServer().getPlayerManager().broadcast(newMessage, false);

                    return false; // Cancel the original message
                }

                return true; // Allow the original message if no modification was made
            }
            return true; // Let other messages continue
        });
    }
}
