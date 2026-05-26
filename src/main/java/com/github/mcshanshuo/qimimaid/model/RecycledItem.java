
package com.github.mcshanshuo.qimimaid.model;

import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.UUID;

public class RecycledItem implements Serializable {

    private UUID id;
    private ItemStack itemStack;
    private String category;
    private long timestamp;
    private double price;
    private boolean valuable;

    public RecycledItem(ItemStack itemStack, String category) {
        this.id = UUID.randomUUID();
        this.itemStack = itemStack;
        this.category = category;
        this.timestamp = System.currentTimeMillis();
        this.valuable = false;
        this.price = 0;
    }

    public RecycledItem(ItemStack itemStack, String category, boolean valuable, double price) {
        this.id = UUID.randomUUID();
        this.itemStack = itemStack;
        this.category = category;
        this.timestamp = System.currentTimeMillis();
        this.valuable = valuable;
        this.price = price;
    }

    public UUID getId() {
        return id;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public String getCategory() {
        return category;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isValuable() {
        return valuable;
    }

    public void setValuable(boolean valuable) {
        this.valuable = valuable;
    }

    public int getAmount() {
        return itemStack.getAmount();
    }
}
