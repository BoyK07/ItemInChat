package com.boyk07.itemsinchat;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.UUID;

public class CommandHandler {
    private static final HashMap<String, UUID> validEnderchestClicks = new HashMap<>();
    private static final HashMap<String, UUID> validInventoryClicks = new HashMap<>();

    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        registerOpenEnderchestCommand(dispatcher);
        registerOpenInventoryCommand(dispatcher);
    }

    private static void registerOpenInventoryCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("openinventory")
                .then(CommandManager.argument("clickId", StringArgumentType.string())
                        .executes(context -> {
                            String clickId = StringArgumentType.getString(context, "clickId");

                            if (validInventoryClicks.containsKey(clickId)) {
                                UUID playerUUID = validInventoryClicks.get(clickId);
                                ServerPlayerEntity player = context.getSource().getServer().getPlayerManager().getPlayer(playerUUID);

                                if (player != null) {
                                    player.openHandledScreen(new NamedScreenHandlerFactory() {
                                        @Override
                                        public Text getDisplayName() {
                                            return Text.literal("Player Inventory");
                                        }

                                        @Override
                                        public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                                            return new CustomPlayerInventoryScreenHandler(syncId, playerInventory);
                                        }
                                    });

                                    validInventoryClicks.remove(clickId);

                                    return 1;
                                } else {
                                    context.getSource().sendError(Text.literal("Player not found"));
                                    return 0;
                                }
                            } else {
                                context.getSource().sendError(Text.literal("Invalid Inventory opening"));
                                return 0;
                            }
                        })));
    }

    private static void registerOpenEnderchestCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("openenderchest")
                .then(CommandManager.argument("clickId", StringArgumentType.string())
                        .executes(context -> {
                            String clickId = StringArgumentType.getString(context, "clickId");

                            if (validEnderchestClicks.containsKey(clickId)) {
                                UUID playerUUID = validEnderchestClicks.get(clickId);
                                ServerPlayerEntity player = context.getSource().getServer().getPlayerManager().getPlayer(playerUUID);

                                if (player != null) {
                                    player.openHandledScreen(new NamedScreenHandlerFactory() {
                                        @Override
                                        public Text getDisplayName() {
                                            return Text.literal("Ender Chest");
                                        }

                                        @Override
                                        public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                                            return GenericContainerScreenHandler.createGeneric9x3(syncId, playerInventory, player.getEnderChestInventory());
                                        }
                                    });

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

    public static String generateInventoryClickId(ServerPlayerEntity player) {
        String clickId = UUID.randomUUID().toString();
        validInventoryClicks.put(clickId, player.getUuid());
        return clickId;
    }

    public static String generateEnderchestClickId(ServerPlayerEntity player) {
        String clickId = UUID.randomUUID().toString();
        validEnderchestClicks.put(clickId, player.getUuid());
        return clickId;
    }
}