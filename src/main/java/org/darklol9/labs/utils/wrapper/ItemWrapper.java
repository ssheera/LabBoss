package org.darklol9.labs.utils.wrapper;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.darklol9.labs.utils.helper.GlowEnchantment;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Builder
public class ItemWrapper {

    private Material type;

    @Builder.Default
    private int amount = 1;
    private byte data;

    private String name;

    @Singular("lore")
    private List<String> lore;

    @Singular("enchantment")
    private Map<String, Integer> enchantments;

    @Singular("flag")
    private Map<String, Integer> flags;

    private boolean unbreakable;

    private String texture;

    private boolean glow;

    @Singular("nbt")
    private Map<String, Object> nbtMap;

    public ItemStack build() {
        ItemStack itemStack = new ItemStack(type, amount, data);
        ItemMeta meta = Bukkit.getItemFactory().getItemMeta(type);
        if (name != null) meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        if (lore != null)
            meta.setLore(lore.stream().map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList()));
        if (meta.spigot().isUnbreakable()) meta.spigot().setUnbreakable(unbreakable);
        if (flags != null) flags.forEach((k, v) -> meta.addItemFlags(ItemFlag.valueOf(k)));
        if (glow)
            meta.addEnchant(new GlowEnchantment(), 1, true);
        itemStack.setItemMeta(meta);
        if (enchantments != null)
            enchantments.forEach((k, v) -> itemStack.addUnsafeEnchantment(Enchantment.getByName(k), v));
        if (texture != null && texture.length() > 0) {
            SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
            GameProfile profile = new GameProfile(UUID.randomUUID(), null);
            profile.getProperties().put("textures", new Property("textures", texture));
            Field profileField;
            try {
                profileField = skullMeta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(skullMeta, profile);
            } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
                ex.printStackTrace();
            }
            itemStack.setItemMeta(skullMeta);
        }
        NbtWrapper nbtWrapper = new NbtWrapper(itemStack);
        if (nbtMap != null) nbtMap.forEach(nbtWrapper::set);
        return nbtWrapper.getItem();
    }

}
