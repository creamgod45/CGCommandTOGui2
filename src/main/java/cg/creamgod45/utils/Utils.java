package cg.creamgod45.utils;

import cg.creamgod45.CGCommandTOGui;
import cg.creamgod45.Inventorys.InventoryList;
import io.github.rysefoxx.content.IntelligentItem;
import io.github.rysefoxx.pagination.InventoryContents;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Utils {
    public static Boolean HasPermission(Player player, String Permission) {
        if (Permission == null) {
            Permission = "CGCommandToGui.admin";
            return false;
        }
        if (player.hasPermission("CGCommandToGui.admin")) return true;
        if (player.isOp()) return true;

        return player.hasPermission(Permission);
    }

    public static String format(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String decolor(String string) {
        return string.replaceAll("&1|&2|&3|&4|&5|&6|&7|&8|&9|&a|&b|&c|&d|&e|&f|&l|&m|&n|&o|&r", "");
    }

    public static Player getPlayer(String player_name) {
        List<Player> player_list = new ArrayList<>(Bukkit.getOnlinePlayers());
        for (Player player : player_list) {
            if (player.getName().equals(player_name)) {
                return player;
            }
        }

        return null;
    }

    public static int getPlayerNum() {
        int num = 0;
        List<Player> player_list = new ArrayList<>(Bukkit.getOnlinePlayers());
        for (Player player : player_list) {
            num++;
        }
        return num;
    }

    public static String arraytostring(String[] array, int start) {
        if (array == null) return "null";
        if (start < 0) return "null";
        array = Arrays.copyOfRange(array, start, array.length);
        return String.join(" ", array);
    }

    public static void removeOne(Inventory inventory, ItemStack item) {
        int size = inventory.getSize();
        for (int i = 0; i < size; i++) {
            ItemStack other = inventory.getItem(i);
            if (item.isSimilar(other)) {
                int amount = other.getAmount();
                if (amount > 1) {
                    other.setAmount(amount - 1);
                } else {
                    other.setAmount(other.getAmount() - 1);
                }
                inventory.setItem(i, other);
                break;
            }
        }
    }

    public static Boolean hasItem(Inventory inventory, ItemStack item) {
        int size = inventory.getSize();
        for (int i = 0; i < size; i++) {
            ItemStack other = inventory.getItem(i);
            if (item.isSimilar(other)) {
                return true;
            }
        }
        return false;
    }

    public static Boolean EnoughInventory(Player player) {
        int space = 0;
        for (ItemStack itemStack : player.getInventory()) {
            try {
                String s = itemStack.toString();
            }catch (NullPointerException e){
                space += 1;
            }
        }
        try {
            ItemStack itemInOffHand = player.getInventory().getItemInOffHand();
            if(itemInOffHand.getType() == Material.AIR){
                space -= 1;
            }
        }catch (NullPointerException e){
            space -= 1;
        }

        ItemStack[] armorContents = player.getInventory().getArmorContents();
        for (ItemStack armorContent : armorContents) {
            try {
                armorContent.toString();
            }catch (NullPointerException e){
                space -= 1;
            }
        }
        return space > 0;
    }

    public static void MenuItem(InventoryContents contents, Integer row, Integer col, ItemStack item1, Player player, String ItemName, Boolean HaveItem){
        contents.set(row, col, IntelligentItem.of(item1, event -> {
            if (InventoryList.queue_list.containsKey(player.getUniqueId().toString())) {
                if (InventoryList.queue_list.get(player.getUniqueId().toString()).size() >= 64) {
                    player.sendMessage(Utils.format("&7[&6CNC&7]&e已經超過製作物品上限!!"));
                    InventoryList.inventoryList.get(2).close(player);
                } else {
                    if (HaveItem) {
                        InventoryList.CNC_CONFIRM(Utils.format("&c確認製作 "+ItemName+"?"));
                        InventoryList.inventoryList.get(3).open(player);
                    } else if(player.getGameMode() == GameMode.CREATIVE){
                        InventoryList.CNC_CONFIRM(Utils.format("&c確認製作 "+ItemName+"?"));
                        InventoryList.inventoryList.get(3).open(player);
                    } else {
                        player.sendMessage(Utils.format("&7[&6CNC&7]&c你沒有指定材料!!"));
                        InventoryList.inventoryList.get(2).close(player);
                    }
                }
            } else {
                if (Utils.hasItem(player.getInventory(), new ItemStack(Material.GLASS_PANE)) && Utils.hasItem(player.getInventory(), new ItemStack(Material.WHITE_CONCRETE))) {
                    InventoryList.CNC_CONFIRM(Utils.format("&c確認製作 "+ItemName+"?"));
                    InventoryList.inventoryList.get(3).open(player);
                } else if(player.getGameMode() == GameMode.CREATIVE){
                    InventoryList.CNC_CONFIRM(Utils.format("&c確認製作 "+ItemName+"?"));
                    InventoryList.inventoryList.get(3).open(player);
                } else {
                    player.sendMessage(Utils.format("&7[&6CNC&7]&c你沒有指定材料!!"));
                    InventoryList.inventoryList.get(2).close(player);
                }
            }
        }));
    }

    public static String getRandomString(int i) {

        // bind the length
        byte[] bytearray;
        bytearray = new byte[256];
        String mystring;
        StringBuffer thebuffer;
        String theAlphaNumericS;

        new Random().nextBytes(bytearray);

        mystring = new String(bytearray, Charset.forName("UTF-8"));

        thebuffer = new StringBuffer();

        //remove all spacial char
        theAlphaNumericS
                = mystring
                .replaceAll("[^A-Z0-9]", "");

        //random selection
        for (int m = 0; m < theAlphaNumericS.length(); m++) {

            if (Character.isLetter(theAlphaNumericS.charAt(m))
                    && (i > 0)
                    || Character.isDigit(theAlphaNumericS.charAt(m))
                    && (i > 0)) {

                thebuffer.append(theAlphaNumericS.charAt(m));
                i--;
            }
        }

        // the resulting string
        return thebuffer.toString();
    }
}
