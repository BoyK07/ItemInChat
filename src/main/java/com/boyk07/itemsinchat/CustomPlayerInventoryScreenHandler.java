package com.boyk07.itemsinchat;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class CustomPlayerInventoryScreenHandler extends ScreenHandler {
    private final PlayerInventory playerInventory;

    public CustomPlayerInventoryScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(null, syncId);
        this.playerInventory = playerInventory;

        // Main inventory (9x3)
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        // Hotbar (9 slots)
        for (int hotbarSlot = 0; hotbarSlot < 9; ++hotbarSlot) {
            this.addSlot(new Slot(playerInventory, hotbarSlot, 8 + hotbarSlot * 18, 142));
        }

        // Armor slots (4 slots)
        for (int armorSlot = 0; armorSlot < 4; ++armorSlot) {
            this.addSlot(new Slot(playerInventory, armorSlot + 36, 8, 8 + armorSlot * 18));
        }

        // Offhand slot (1 slot)
        this.addSlot(new Slot(playerInventory, 40, 77, 62)); // Slot 40 is offhand
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) { // 'quickMove' is now implemented
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            // If the item is in the main inventory (slots 0-35), move to hotbar (slots 36-44)
            if (index < 36) {
                if (!this.insertItem(itemstack1, 36, 45, false)) {
                    return ItemStack.EMPTY;
                }
            }
            // If the item is in the hotbar (slots 36-44), move to the main inventory (slots 0-35)
            else if (index >= 36 && index < 45) {
                if (!this.insertItem(itemstack1, 0, 36, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, itemstack1);
        }

        return itemstack;
    }
}
