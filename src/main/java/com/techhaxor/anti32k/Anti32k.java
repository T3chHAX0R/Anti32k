package com.techhaxor.anti32k;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

public final class Anti32k extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Successfully Loaded Anti32k");
        getServer().getPluginManager().registerEvents(this, this);

    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Successfully UnLoaded Anti32k");
    }

    @EventHandler
    public void onOpenInventory(InventoryOpenEvent event){
        Block block = event.getInventory().getLocation().getBlock();
        if (block.getState() instanceof Container){
            InventoryHolder ih = (InventoryHolder)block.getState();
            Inventory i = ih.getInventory();
            for(ItemStack is : i ){
                if (is != null && is.getType() != Material.AIR){
                    if(is.getAmount() > is.getMaxStackSize()){
                        is.setAmount(is.getMaxStackSize());
                    }
                    fixEnchantments(is);
                }
            }
        }

    }
    @EventHandler
    public void onCloseInventory(InventoryCloseEvent event){
        PlayerInventory playerInventory = event.getPlayer().getInventory();
        for (ItemStack is : playerInventory){
            if (is != null && is.getType() != Material.AIR){
                fixEnchantments(is);
            }
        }
    }
    @EventHandler
    public void onPickUpEvent(EntityPickupItemEvent event){
        fixEnchantments(event.getItem().getItemStack());
    }
    public static void fixEnchantments(ItemStack item) {
        for (Enchantment enchantment : item.getEnchantments().keySet()) {
            int currentLevel = item.getEnchantmentLevel(enchantment);
            int maxLevel = enchantment.getMaxLevel();

            if (currentLevel > maxLevel) {
                item.removeEnchantment(enchantment);
                item.addEnchantment(enchantment, maxLevel);
            }
        }
    }

}
