package com.boyk07.itemsinchat;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.HoverEvent;
import net.minecraft.nbt.NbtList;
import net.fabricmc.fabric.api.networking.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.network.ServerPlayNetworkHandler;

public class ItemsInChat implements ModInitializer {

    @Override
    public void onInitialize() {
        // Capture chat events using ServerMessageEvents
        ServerMessageEvents.CHAT_MESSAGE.register((message, sender, type) -> {
            String msgString = message.getString();
            ServerPlayerEntity player = sender;

            // Check for placeholders in the message
            if (msgString.contains("[item]") || msgString.contains("[i]") || 
                msgString.contains("[armor]") || msgString.contains("[a]") || 
                msgString.contains("[offhand]") || msgString.contains("[o]") ||
                msgString.contains("[enderchest]") || msgString.contains("[ec]")) {
                
                // Handle item, armor, offhand, or enderchest display
                if (msgString.contains("[item]") || msgString.contains("[i]")) {
                    msgString = handleItem(player, msgString);
                }

                if (msgString.contains("[armor]") || msgString.contains("[a]")) {
                    msgString = handleArmor(player, msgString);
                }

                if (msgString.contains("[offhand]") || msgString.contains("[o]")) {
                    msgString = handleOffhand(player, msgString);
                }

                if (msgString.contains("[enderchest]") || msgString.contains("[ec]")) {
                    msgString = handleEnderChest(player, msgString);
                }

                // Broadcast the updated message
                player.getServer().getPlayerManager().broadcast(Text.literal(msgString), false);
            }
        });
    }

    private String handleItem(ServerPlayerEntity player, String message) {
        ItemStack heldItem = player.getMainHandStack();
        if (!heldItem.isEmpty()) {
            Text itemText = createHoverText(heldItem);
            message = message.replace("[item]", itemText.getString()).replace("[i]", itemText.getString());
        }
        return message;
    }

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

    private String handleOffhand(ServerPlayerEntity player, String message) {
        ItemStack offhandItem = player.getOffHandStack();
        if (!offhandItem.isEmpty()) {
            Text offhandText = createHoverText(offhandItem);
            message = message.replace("[offhand]", offhandText.getString()).replace("[o]", offhandText.getString());
        }
        return message;
    }

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

    private Text createHoverText(ItemStack itemStack) {
        Text hoverText = Text.literal("Item: " + itemStack.getName().getString())
                .append(Text.literal("\nAmount: " + itemStack.getCount()).formatted(Formatting.GREEN));

        if (itemStack.hasEnchantments()) {
            hoverText = hoverText.append(Text.literal("\nEnchantments: ").formatted(Formatting.GOLD));
            NbtList enchantments = itemStack.getEnchantments();
            enchantments.forEach(enchantment -> {
                hoverText = hoverText.append(Text.literal("\n - " + enchantment.toString()));
            });
        }

        return Text.literal(itemStack.getName().getString())
                .formatted(Formatting.AQUA)
                .styled(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText)));
    }
}
