package com.boyk07.itemsinchat;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.UUID;

public class CommandHandler {

    // HashMap to store valid click IDs for Ender Chest openings
    private static final HashMap<String, UUID> validEnderchestClicks = new HashMap<>();

    // Method to register all commands in this file
    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        registerOpenEnderchestCommand(dispatcher);
    }

    // OpenEnderchest command: Only callable via chat click with a valid UUID
    private static void registerOpenEnderchestCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("openenderchest")
                .then(CommandManager.argument("clickId", StringArgumentType.string())
                        .executes(context -> {
                            String clickId = StringArgumentType.getString(context, "clickId");

                            // Check if the click ID is valid
                            if (validEnderchestClicks.containsKey(clickId)) {
                                UUID playerUUID = validEnderchestClicks.get(clickId);
                                ServerPlayerEntity player = context.getSource().getServer().getPlayerManager().getPlayer(playerUUID);

                                if (player != null) {
                                    // Open the player's Ender Chest using a NamedScreenHandlerFactory
                                    player.openHandledScreen(new NamedScreenHandlerFactory() {
                                        @Override
                                        public Text getDisplayName() {
                                            return Text.literal("Ender Chest");
                                        }

                                        @Override
                                        public GenericContainerScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                                            // Create the Ender Chest screen handler
                                            return GenericContainerScreenHandler.createGeneric9x3(syncId, playerInventory, player.getEnderChestInventory());
                                        }
                                    });

                                    // Remove the used clickId from the map
                                    validEnderchestClicks.remove(clickId);

                                    return 1;
                                } else {
                                    context.getSource().sendError(Text.literal("Player not found"));
                                    return 0;
                                }
                            } else {
                                context.getSource().sendError(Text.literal("Invalid Ender Chest opening"));
                                return 0;
                            }
                        })));
    }

    // Method to generate a valid click ID for the player's Ender Chest
    public static String generateEnderchestClickId(ServerPlayerEntity player) {
        String clickId = UUID.randomUUID().toString();
        validEnderchestClicks.put(clickId, player.getUuid());
        return clickId;
    }
}
