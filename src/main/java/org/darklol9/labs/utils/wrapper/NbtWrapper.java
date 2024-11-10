package org.darklol9.labs.utils.wrapper;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.inventory.ItemStack;

public class NbtWrapper {

    private final net.minecraft.server.v1_8_R3.ItemStack item;
    private NBTTagCompound tag;

    public NbtWrapper(ItemStack item) {
        this.item = org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack.asNMSCopy(item);
        this.tag = this.item.getTag();
        if (this.tag == null) {
            this.tag = new NBTTagCompound();
            this.item.save(this.tag);
            this.item.setTag(this.tag);
        }
    }

    public NbtWrapper setString(String key, String value) {
        this.tag.setString(key, value);
        return this;
    }

    public NbtWrapper setInt(String key, int value) {
        this.tag.setInt(key, value);
        return this;
    }

    public NbtWrapper setDouble(String key, double value) {
        this.tag.setDouble(key, value);
        return this;
    }

    public NbtWrapper setBoolean(String key, boolean value) {
        this.tag.setBoolean(key, value);
        return this;
    }

    public NbtWrapper setLong(String key, long value) {
        this.tag.setLong(key, value);
        return this;
    }

    public NbtWrapper setShort(String key, short value) {
        this.tag.setShort(key, value);
        return this;
    }

    public NbtWrapper setByte(String key, byte value) {
        this.tag.setByte(key, value);
        return this;
    }

    public NbtWrapper setFloat(String key, float value) {
        this.tag.setFloat(key, value);
        return this;
    }

    public NbtWrapper setIntArray(String key, int[] value) {
        this.tag.setIntArray(key, value);
        return this;
    }

    public NbtWrapper setByteArray(String key, byte[] value) {
        this.tag.setByteArray(key, value);
        return this;
    }

    public NbtWrapper set(String key, NBTTagCompound value) {
        this.tag.set(key, value);
        return this;
    }

    public NbtWrapper set(String key, net.minecraft.server.v1_8_R3.ItemStack value) {
        this.tag.set(key, value.save(new NBTTagCompound()));
        return this;
    }

    public NbtWrapper set(String key, Object value) {
        if (value instanceof String) {
            setString(key, (String) value);
        } else if (value instanceof Integer) {
            setInt(key, (int) value);
        } else if (value instanceof Double) {
            setDouble(key, (double) value);
        } else if (value instanceof Boolean) {
            setBoolean(key, (boolean) value);
        } else if (value instanceof Long) {
            setLong(key, (long) value);
        } else if (value instanceof Short) {
            setShort(key, (short) value);
        } else if (value instanceof Byte) {
            setByte(key, (byte) value);
        } else if (value instanceof Float) {
            setFloat(key, (float) value);
        } else if (value instanceof int[]) {
            setIntArray(key, (int[]) value);
        } else if (value instanceof byte[]) {
            setByteArray(key, (byte[]) value);
        } else if (value instanceof NBTTagCompound) {
            set(key, (NBTTagCompound) value);
        } else if (value instanceof net.minecraft.server.v1_8_R3.ItemStack) {
            set(key, (net.minecraft.server.v1_8_R3.ItemStack) value);
        } else {
            throw new IllegalArgumentException("Invalid value type: " + value.getClass().getName());
        }
        return this;
    }

    public String getString(String key) {
        return this.tag.getString(key);
    }

    public int getInt(String key) {
        return this.tag.getInt(key);
    }

    public double getDouble(String key) {
        return this.tag.getDouble(key);
    }

    public boolean getBoolean(String key) {
        return this.tag.getBoolean(key);
    }

    public long getLong(String key) {
        return this.tag.getLong(key);
    }

    public short getShort(String key) {
        return this.tag.getShort(key);
    }

    public byte getByte(String key) {
        return this.tag.getByte(key);
    }

    public float getFloat(String key) {
        return this.tag.getFloat(key);
    }

    public int[] getIntArray(String key) {
        return this.tag.getIntArray(key);
    }

    public byte[] getByteArray(String key) {
        return this.tag.getByteArray(key);
    }

    public NBTTagCompound get(String key) {
        return this.tag.getCompound(key);
    }

    public net.minecraft.server.v1_8_R3.ItemStack getItemStack(String key) {
        return net.minecraft.server.v1_8_R3.ItemStack.createStack(this.tag.getCompound(key));
    }

    public boolean hasKey(String key) {
        return this.tag.hasKey(key);
    }

    public boolean hasKeyOfType(String key, int type) {
        return this.tag.hasKeyOfType(key, type);
    }

    public ItemStack getItem() {
        item.setTag(this.tag);
        return org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack.asBukkitCopy(this.item);
    }
}
