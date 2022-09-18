package cg.creamgod45.items;

import cg.creamgod45.utils.Utils;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

import static cg.creamgod45.CGCommandTOGui.plugin;

public class ItemManger {
    public static Integer register_key = 0;
    public static Map<Integer, ItemStack> items = new HashMap<>();

    public ItemManger() {
        plugin.getServer().getLogger().info(Utils.format("[CGCommandTOGui] &aCLASS: ItemManger Loaded"));
        items.put(register_key, register_item1());
        items.put(register_key, register_item2());
        items.put(register_key, register_item3());
        items.put(register_key, register_item4());
    }

    public ItemStack register_item1() {
        ItemStack item3 = new ItemStack(Material.MUSIC_DISC_STRAD);
        ItemMeta itemmeta3 = item3.getItemMeta();
        item3.setAmount(1);
        itemmeta3.setDisplayName(Utils.format("&e磨砂刀片"));
        itemmeta3.addItemFlags(ItemFlag.values());
        List<String> strings = new ArrayList<>();
        strings.add(Utils.format("&c看到這行字請等待系統更新物品"));
        strings.add(Utils.format("&c直到這兩行物品說明消失後即可使用!!"));
        itemmeta3.setLore(strings);
        item3.setItemMeta(itemmeta3);
        NBTItem itemnbt3 = new NBTItem(item3);
        itemnbt3.setString("CGCommandTOGui_Item_ID", "1");
        itemnbt3.addCompound("PublicBukkitValues");
        itemnbt3.getCompound("PublicBukkitValues").setString("executableitems:ei-id", "ctg_item_1");
        itemnbt3.getCompound("PublicBukkitValues").setInteger("score:usage", 0);
        register_key++;
        return itemnbt3.getItem();
    }

    public ItemStack register_item2(){
        ItemStack item1 = new ItemStack(Material.MUSIC_DISC_STRAD);
        ItemMeta itemmeta1 = item1.getItemMeta();
        item1.setAmount(1);
        itemmeta1.setDisplayName(Utils.format("&a製作 &e磨砂刀片 &a物品"));
        itemmeta1.setLore(Arrays.asList(Utils.format("&c需求物品"), Utils.format("&c - 白色混泥土 X 1"), Utils.format("&c - 玻璃片 X 1"), Utils.format("&e&l需要30秒的製作時間")));
        itemmeta1.addItemFlags(ItemFlag.values());
        item1.setItemMeta(itemmeta1);
        register_key++;
        return item1;
    }

    public static ItemStack register_item3(){
        ItemStack item1 = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta itemmeta1 = item1.getItemMeta();
        item1.setAmount(1);
        itemmeta1.setDisplayName(Utils.format("&a確認"));
        item1.setItemMeta(itemmeta1);
        register_key++;
        return item1;
    }


    public static ItemStack register_item4(){
        ItemStack item2 = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta itemmeta2 = item2.getItemMeta();
        item2.setAmount(1);
        itemmeta2.setDisplayName(Utils.format("&c取消"));
        item2.setItemMeta(itemmeta2);
        register_key++;
        return item2;
    }

    public static ItemStack DdynamicItem(Material material, List<String> lore, Integer amount, String DisplayName){
        ItemStack item = new ItemStack(material);
        item.setAmount(amount);
        ItemMeta itemmeta = item.getItemMeta();
        itemmeta.setLore(lore);
        itemmeta.setDisplayName(DisplayName);
        item.setItemMeta(itemmeta);
        return item;
    }
}
