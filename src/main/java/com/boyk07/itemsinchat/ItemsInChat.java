package com.boyk07.itemsinchat;

import net.fabricmc.api.ModInitializer;
import net.minecraft.server.network.ServerPlayerEntity;

public class ItemsInChat implements ModInitializer {
    @Override
    public void onInitialize() {
        // Register the server-side chat listener
        ChatListener.registerChatListener();
    }

    // Returns true if the message was modified
    public static boolean handleChatMessage(String chatMessage, ServerPlayerEntity sender) {
        boolean modified = ItemDisplayHandler.handleItemDisplay(chatMessage, sender);
        return modified;
    }
}
