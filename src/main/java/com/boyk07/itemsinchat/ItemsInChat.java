package com.boyk07.itemsinchat;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.HoverEvent;
import net.minecraft.network.message.GameMessageS2CPacket;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.nbt.NbtList;

public class ItemsInChat implements ModInitializer {

    @Override
    public void onInitialize() {
        // Register player join event (for potential chat message handling)
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            ServerPlayConnectionEvents.JOIN.register((handler, sender, server1) -> {
                ServerPlayerEntity player = handler.player;
                // Perform actions on player join (if necessary)
            });
        });
    }

    // Chat message handler
    private void handleChatMessage(ServerPlayerEntity player, Text message) {
        String msgString = message.getString();

        Text finalMessage = Text.literal(""); // Construct message as Text

        // Check for placeholders and replace accordingly
        if (msgString.contains("[item]") || msgString.contains("[i]")) {
            finalMessage = handleItem(player, message);
        }

        if (msgString.contains("[armor]") || msgString.contains("[a]")) {
            finalMessage = finalMessage.append(handleArmor(player));
        }

        if (msgString.contains("[offhand]") || msgString.contains("[o]")) {
            finalMessage = finalMessage.append(handleOffhand(player));
        }

        if (msgString.contains("[enderchest]") || msgString.contains("[ec]")) {
            finalMessage = finalMessage.append(handleEnderChest(player));
        }

        // Broadcast the final message to all players
        player.getServer().getPlayerManager().broadcast(new GameMessageS2CPacket(finalMessage, false));
    }

    // Function to handle [item] or [i]
    private Text handleItem(ServerPlayerEntity player, Text message) {
        ItemStack heldItem = player.getMainHandStack();
        if (!heldItem.isEmpty()) {
            Text itemText = createHoverText(heldItem);
            return message.copy().replace("[item]", itemText).replace("[i]", itemText);
        }
        return message;
    }

    // Function to handle [armor] or [a]
    private Text handleArmor(ServerPlayerEntity player) {
        Text armorText = Text.literal("");
        ItemStack[] armorPieces = {
            player.getInventory().getArmorStack(3), // Helmet
            player.getInventory().getArmorStack(2), // Chestplate
            player.getInventory().getArmorStack(1), // Pants
            player.getInventory().getArmorStack(0)  // Boots
        };

        for (ItemStack armorPiece : armorPieces) {
            if (!armorPiece.isEmpty()) {
                armorText = armorText.append(createHoverText(armorPiece)).append(Text.literal(" "));
            }
        }
        
        return armorText.trim();
    }

    // Function to handle [offhand] or [o]
    private Text handleOffhand(ServerPlayerEntity player) {
        ItemStack offhandItem = player.getOffHandStack();
        if (!offhandItem.isEmpty()) {
            Text offhandText = createHoverText(offhandItem);
            return Text.literal("").replace("[offhand]", offhandText).replace("[o]", offhandText);
        }
        return Text.literal("");
    }

    // Function to handle [enderchest] or [ec]
    private Text handleEnderChest(ServerPlayerEntity player) {
        Text enderChestText = Text.literal("");
        for (int i = 0; i < player.getEnderChestInventory().size(); i++) {
            ItemStack enderItem = player.getEnderChestInventory().getStack(i);
            if (!enderItem.isEmpty()) {
                enderChestText = enderChestText.append(createHoverText(enderItem)).append(Text.literal(" "));
            }
        }
        return enderChestText.trim();
    }

    // Helper function to create hoverable text with item details
    private Text createHoverText(ItemStack itemStack) {
        Text hoverText = Text.literal("Item: " + itemStack.getName().getString())
                .append(Text.literal("\nAmount: " + itemStack.getCount()).formatted(Formatting.GREEN));

        // Check and append enchantments
        if (itemStack.hasEnchantments()) {
            hoverText = hoverText.append(Text.literal("\nEnchantments: ").formatted(Formatting.GOLD));
            NbtList enchantments = itemStack.getEnchantments();
            enchantments.forEach(enchantment -> {
                // Use EnchantmentHelper to get human-readable enchantment info
                Text enchantmentText = Text.literal(EnchantmentHelper.getLevelFromNbt(enchantment).toString())
                        .formatted(Formatting.GOLD);
                hoverText = hoverText.append(enchantmentText).append(Text.literal("\n"));
            });
        }

        return Text.literal(itemStack.getName().getString())
                .formatted(Formatting.AQUA)
                .styled(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText)));
    }
}
