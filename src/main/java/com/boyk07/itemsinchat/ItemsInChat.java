package com.boyk07.itemsinchat;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class ItemsInChat implements ModInitializer {
    @Override
    public void onInitialize() {
        // Register the server-side chat listener
        ChatListener.registerChatListener();

        // Register some needed commands
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            CommandHandler.registerCommands(dispatcher);
        });
    }

    public static MutableText handleChatMessage(String chatMessage, ServerPlayerEntity sender) {
        // Initialize the message with the original chat message
        MutableText processedMessage = Text.literal(chatMessage);

        // Process the message for item, armor, offhand, and ender chest placeholders
        processedMessage = ItemDisplayHandler.handleItemDisplay(processedMessage.getString(), sender);  // Replace [item] or [i]
        processedMessage = ItemDisplayHandler.handleArmorDisplay(processedMessage.getString(), sender); // Replace [armor] or [a]
        processedMessage = ItemDisplayHandler.handleOffhandDisplay(processedMessage.getString(), sender); // Replace [offhand] or [o]
        processedMessage = ItemDisplayHandler.handleEnderchestDisplay(processedMessage.getString(), sender); // Replace [enderchest] or [ec]
        processedMessage = ItemDisplayHandler.handleInventoryDisplay(processedMessage.getString(), sender); // Replace [inventory] or [inv]

        // Return the modified message with all placeholders replaced
        return processedMessage;
    }
}
