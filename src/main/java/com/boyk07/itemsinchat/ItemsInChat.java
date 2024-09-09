package com.boyk07.itemsinchat;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.event.HoverEvent;
import net.minecraft.text.HoverEvent.Action;
import net.fabricmc.fabric.api.event.player.PlayerChatCallback;

public class ItemsInChat implements ModInitializer {

    @Override
    public void onInitialize() {
        // Register chat event listener
        PlayerChatCallback.EVENT.register(this::onChatMessage);
    }

    private void onChatMessage(ServerPlayerEntity player, String message, Text chatMessage) {
        // Check if the player wants to display their item, armor, offhand, or ender chest
        if (message.contains("[item]") || message.contains("[i]") || 
            message.contains("[armor]") || message.contains("[a]") || 
            message.contains("[offhand]") || message.contains("[o]") ||
            message.contains("[enderchest]") || message.contains("[ec]")) {
            
            // Handle [item] or [i] 
            if (message.contains("[item]") || message.contains("[i]")) {
                message = handleItem(player, message);
            }
            
            // Handle [armor] or [a]
            if (message.contains("[armor]") || message.contains("[a]")) {
                message = handleArmor(player, message);
            }

            // Handle [offhand] or [o]
            if (message.contains("[offhand]") || message.contains("[o]")) {
                message = handleOffhand(player, message);
            }

            // Handle [enderchest] or [ec]
            if (message.contains("[enderchest]") || message.contains("[ec]")) {
                message = handleEnderChest(player, message);
            }
            
            // Send the updated message
            player.server.getPlayerManager().broadcastChatMessage(new LiteralText(message), false);
        }
    }

    // Function to handle [item] or [i]
    private String handleItem(ServerPlayerEntity player, String message) {
        ItemStack heldItem = player.getMainHandStack();
        if (!heldItem.isEmpty()) {
            Text itemText = createHoverText(heldItem);
            message = message.replace("[item]", itemText.getString()).replace("[i]", itemText.getString());
        }
        return message;
    }

    // Function to handle [armor] or [a]
    private String handleArmor(ServerPlayerEntity player, String message) {
        StringBuilder armorText = new StringBuilder();
        ItemStack[] armorPieces = {
            player.getInventory().getArmorStack(3), // Helmet
            player.getInventory().getArmorStack(2), // Chestplate
            player.getInventory().getArmorStack(1), // Pants
            player.getInventory().getArmorStack(0)  // Boots
        };

        for (ItemStack armorPiece : armorPieces) {
            if (!armorPiece.isEmpty()) {
                armorText.append(createHoverText(armorPiece).getString()).append(" ");
            }
        }
        
        message = message.replace("[armor]", armorText.toString().trim()).replace("[a]", armorText.toString().trim());
        return message;
    }

    // Function to handle [offhand] or [o]
    private String handleOffhand(ServerPlayerEntity player, String message) {
        ItemStack offhandItem = player.getOffHandStack();
        if (!offhandItem.isEmpty()) {
            Text offhandText = createHoverText(offhandItem);
            message = message.replace("[offhand]", offhandText.getString()).replace("[o]", offhandText.getString());
        }
        return message;
    }

    // Function to handle [enderchest] or [ec]
    private String handleEnderChest(ServerPlayerEntity player, String message) {
        StringBuilder enderChestText = new StringBuilder();
        for (int i = 0; i < player.getEnderChestInventory().size(); i++) {
            ItemStack enderItem = player.getEnderChestInventory().getStack(i);
            if (!enderItem.isEmpty()) {
                enderChestText.append(createHoverText(enderItem).getString()).append(" ");
            }
        }
        message = message.replace("[enderchest]", enderChestText.toString().trim()).replace("[ec]", enderChestText.toString().trim());
        return message;
    }

    // Helper function to create hoverable text with item details
    private Text createHoverText(ItemStack itemStack) {
        Text hoverText = new LiteralText("Item: " + itemStack.getName().getString())
                .append("\nAmount: " + itemStack.getCount())
                .formatted(Formatting.GREEN);

        // Add enchantments to hover text if available
        if (itemStack.hasEnchantments()) {
            hoverText = hoverText.append("\nEnchantments: ").formatted(Formatting.GOLD);
            itemStack.getEnchantments().forEach(enchantment -> {
                hoverText = hoverText.append("\n - " + enchantment.asString());
            });
        }

        return new LiteralText(itemStack.getName().getString())
                .formatted(Formatting.AQUA)
                .styled(style -> style.withHoverEvent(new HoverEvent(Action.SHOW_TEXT, hoverText)));
    }
}
