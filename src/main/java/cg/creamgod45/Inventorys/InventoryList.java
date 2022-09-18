package cg.creamgod45.Inventorys;

import cg.creamgod45.CGCommandTOGui;
import cg.creamgod45.items.ItemManger;
import cg.creamgod45.utils.ItemBuilder;
import cg.creamgod45.utils.Utils;
import io.github.rysefoxx.SlotIterator;
import io.github.rysefoxx.content.IntelligentItem;
import io.github.rysefoxx.content.InventoryProvider;
import io.github.rysefoxx.pagination.InventoryContents;
import io.github.rysefoxx.pagination.Pagination;
import io.github.rysefoxx.pagination.RyseInventory;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

import static cg.creamgod45.CGCommandTOGui.command_data;
import static cg.creamgod45.CGCommandTOGui.plugin;

public class InventoryList {
    public static List<RyseInventory> inventoryList = new ArrayList<>();
    public static Map<String, ArrayList<String>> queue_list = new HashMap<>();
    public static Map<String, Integer> queues_cooldown = new HashMap<>();
    public static Map<String, ItemStack> queues_ITEM = new HashMap<>();
    public static Map<String, Integer> task = new HashMap<>();

    public InventoryList() {
        plugin.getServer().getLogger().info(Utils.format("[CGCommandTOGui] &aCLASS: InventoryList Loaded"));
        inventoryList = new ArrayList<>();
        // 建立空實例
        inventoryList.add(RyseInventory.builder().manager(CGCommandTOGui.InventoryManager).title("").rows(6).provider(new InventoryProvider() {
            @Override
            public void init(Player player, InventoryContents contents) {
            }
        }).build(CGCommandTOGui.plugin));
        inventoryList.add(RyseInventory.builder().manager(CGCommandTOGui.InventoryManager).title("").rows(6).provider(new InventoryProvider() {
            @Override
            public void init(Player player, InventoryContents contents) {
            }
        }).build(CGCommandTOGui.plugin));
        inventoryList.add(RyseInventory.builder().manager(CGCommandTOGui.InventoryManager).title("").rows(6).provider(new InventoryProvider() {
            @Override
            public void init(Player player, InventoryContents contents) {
            }
        }).build(CGCommandTOGui.plugin));
        inventoryList.add(RyseInventory.builder().manager(CGCommandTOGui.InventoryManager).title("").rows(6).provider(new InventoryProvider() {
            @Override
            public void init(Player player, InventoryContents contents) {
            }
        }).build(CGCommandTOGui.plugin));
        SelectPlayers("選擇一個玩家執行指令");
        SelectPlayersWithMe("選擇一個玩家執行指令");
        CNC_MAIN("CNC 儀表板 ");
        CNC_CONFIRM("CNC 製作確認畫面");
    }

    public static void SelectPlayers(String title) {
        plugin.getServer().getLogger().info(Utils.format("[CGCommandTOGui] &aGUI: SelectPlayers Loaded"));
        if (Objects.equals(title, "")) {
            title = "選擇一個玩家執行指令";
        }
        // 強制覆蓋先前的空實例
        inventoryList.set(0,
                RyseInventory.builder()
                        .manager(CGCommandTOGui.InventoryManager)
                        .title(title)
                        .rows(6)
                        .provider(new InventoryProvider() {
                            @Override
                            public void init(Player player, InventoryContents contents) {
                                Pagination pagination = contents.pagination();
                                pagination.setItemsPerPage(28);
                                pagination.iterator(SlotIterator.builder().startPosition(1, 1).type(SlotIterator.SlotIteratorType.HORIZONTAL)/*.blackList(Arrays.asList(25, 26, 27, 28))*/.build());

                                contents.fillBorders(new ItemBuilder(Material.GLASS_PANE, 1, "").build());

                                contents.set(5, 3, IntelligentItem.of(new ItemBuilder(Material.ARROW).amount(1).displayname(Utils.format("&a&l上一頁")).build(), event -> {
                                    if (pagination.isFirst()) {
                                        player.sendMessage("§c§o你已經在第一頁了。");
                                        return;
                                    }

                                    RyseInventory currentInventory = pagination.inventory();
                                    currentInventory.open(player, pagination.previous().page());
                                }));

                                int page = pagination.newInstance(pagination).next().page();
                                int maxpage = Utils.getPlayerNum() / 27;
                                if (maxpage <= 0) maxpage = 1;
                                contents.set(5, 4, IntelligentItem.empty(new ItemBuilder(Material.BOOK).amount(page).displayname(Utils.format("&7頁數: &e" + page + " / " + maxpage)).build()));

                                List<Player> player_list = new ArrayList<>(Bukkit.getOnlinePlayers());
                                for (Player p : player_list) {
                                    ItemStack skull = new ItemStack(Material.PLAYER_HEAD); // Create a new ItemStack of the Player Head type.
                                    SkullMeta skullMeta = (SkullMeta) skull.getItemMeta(); // Get the created item's ItemMeta and cast it to SkullMeta so we can access the skull properties
                                    skullMeta.setOwningPlayer(p); // Set the skull's owner so it will adapt the skin of the provided username (case sensitive).
                                    skullMeta.setDisplayName(p.getName());
                                    skull.setItemMeta(skullMeta); //

                                    pagination.addItem(IntelligentItem.of(skull, event -> {
                                        //event.getWhoClicked().sendMessage(p.getName());
                                        //event.getWhoClicked().sendMessage("SLOT " + event.getSlot());
                                        String commands = command_data.get(player);
                                        if (commands.contains("*localplayer*")) {
                                            commands = commands.replace("*localplayer*", player.getName());
                                        }

                                        if (commands.contains("*selectplayer*")) {
                                            commands = commands.replace("*selectplayer*", p.getName());
                                        }
                                        player.setOp(true);
                                        Bukkit.dispatchCommand(player, commands);
                                        player.setOp(false);
                                        player.sendMessage(Utils.format(" &dCGCommandTOGui &7| &e&l執行 /" + commands));
                                        CGCommandTOGui.plugin.getLogger().info(player.getName() + "執行 /" + commands);
                                        command_data.remove(player);
                                        InventoryList.inventoryList.get(0).close(player);
                                    }));
                                }

                                contents.set(5, 5, IntelligentItem.of(new ItemBuilder(Material.ARROW).amount(1).displayname(Utils.format("&a&l下一頁")).build(), event -> {
                                    if (pagination.isLast()) {
                                        player.sendMessage("§c§o你已經在最後一頁了。");
                                        return;
                                    }

                                    RyseInventory currentInventory = pagination.inventory();
                                    currentInventory.open(player, pagination.next().page());
                                }));
                            }
                        })
                        .build(CGCommandTOGui.plugin)
        );
    }

    public static void SelectPlayersWithMe(String title) {
        plugin.getServer().getLogger().info(Utils.format("[CGCommandTOGui] &aGUI: SelectPlayersWithMe Loaded"));
        if (Objects.equals(title, "")) {
            title = "選擇一個玩家執行指令";
        }
        // 強制覆蓋先前的空實例
        inventoryList.set(1,
                RyseInventory.builder()
                        .manager(CGCommandTOGui.InventoryManager)
                        .title(title)
                        .rows(6)
                        .provider(new InventoryProvider() {
                            @Override
                            public void init(Player player, InventoryContents contents) {
                                Pagination pagination = contents.pagination();
                                pagination.setItemsPerPage(28);
                                pagination.iterator(SlotIterator.builder().startPosition(1, 1).type(SlotIterator.SlotIteratorType.HORIZONTAL)/*.blackList(Arrays.asList(25, 26, 27, 28))*/.build());

                                contents.fillBorders(new ItemBuilder(Material.GLASS_PANE, 1, "").build());

                                contents.set(5, 3, IntelligentItem.of(new ItemBuilder(Material.ARROW).amount(1).displayname(Utils.format("&a&l上一頁")).build(), event -> {
                                    if (pagination.isFirst()) {
                                        player.sendMessage("§c§o你已經在第一頁了。");
                                        return;
                                    }

                                    RyseInventory currentInventory = pagination.inventory();
                                    currentInventory.open(player, pagination.previous().page());
                                }));

                                int page = pagination.newInstance(pagination).next().page();
                                int maxpage = Utils.getPlayerNum() / 27;
                                if (maxpage <= 0) maxpage = 1;
                                contents.set(5, 4, IntelligentItem.empty(new ItemBuilder(Material.BOOK).amount(page).displayname(Utils.format("&7頁數: &e" + page + " / " + maxpage)).build()));

                                List<Player> player_list = new ArrayList<>(Bukkit.getOnlinePlayers());
                                for (Player p : player_list) {
                                    if (p.equals(player)) continue;
                                    ItemStack skull = new ItemStack(Material.PLAYER_HEAD); // Create a new ItemStack of the Player Head type.
                                    SkullMeta skullMeta = (SkullMeta) skull.getItemMeta(); // Get the created item's ItemMeta and cast it to SkullMeta so we can access the skull properties
                                    skullMeta.setOwningPlayer(p); // Set the skull's owner so it will adapt the skin of the provided username (case sensitive).
                                    skullMeta.setDisplayName(p.getName());
                                    skull.setItemMeta(skullMeta); //

                                    pagination.addItem(IntelligentItem.of(skull, event -> {
                                        //event.getWhoClicked().sendMessage(p.getName());
                                        //event.getWhoClicked().sendMessage("SLOT " + event.getSlot());
                                        String commands = command_data.get(player);
                                        if (commands.contains("*localplayer*")) {
                                            commands = commands.replace("*localplayer*", player.getName());
                                        }

                                        if (commands.contains("*selectplayer*")) {
                                            commands = commands.replace("*selectplayer*", p.getName());
                                        }
                                        Bukkit.dispatchCommand(player, commands);
                                        player.sendMessage(Utils.format(" &dCGCommandTOGui &7| &e&l執行 /" + commands));
                                        CGCommandTOGui.plugin.getLogger().info(player.getName() + "執行 /" + commands);
                                        command_data.remove(player);
                                        InventoryList.inventoryList.get(1).close(player);
                                    }));
                                }

                                contents.set(5, 5, IntelligentItem.of(new ItemBuilder(Material.ARROW).amount(1).displayname(Utils.format("&a&l下一頁")).build(), event -> {
                                    if (pagination.isLast()) {
                                        player.sendMessage("§c§o你已經在最後一頁了。");
                                        return;
                                    }

                                    RyseInventory currentInventory = pagination.inventory();
                                    currentInventory.open(player, pagination.next().page());
                                }));
                            }
                        })
                        .build(CGCommandTOGui.plugin)
        );
    }

    public static void CNC_MAIN(String title) {
        plugin.getServer().getLogger().info(Utils.format("[CGCommandTOGui] &aGUI: CNC_MAIN Loaded"));
        if (Objects.equals(title, "")) {
            title = "CNC 儀表板";
        }
        // 強制覆蓋先前的空實例
        inventoryList.set(2,
                RyseInventory.builder()
                        .manager(CGCommandTOGui.InventoryManager)
                        .title(title)
                        .rows(6)
                        .provider(new InventoryProvider() {
                            @Override
                            public void init(Player player, InventoryContents contents) {
                                contents.fillBorders(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, 1, "").build());

                                // 磨砂刀片
                                Utils.MenuItem(
                                        contents,
                                        1,
                                        1,
                                        ItemManger.items.get(1),
                                        player,
                                        ItemManger.items.get(0).getItemMeta().getDisplayName(),
                                        (
                                                Utils.hasItem(player.getInventory(), new ItemStack(Material.GLASS_PANE)) &&
                                                        Utils.hasItem(player.getInventory(), new ItemStack(Material.WHITE_CONCRETE))
                                        )
                                );

                                contents.set(4, 1, IntelligentItem.of(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).amount(1).displayname(Utils.format("&c你沒有正在製作的物品")).build(), event -> {
                                    player.sendMessage(Utils.format("&7[&6CNC&7]&c你沒有正在製作的物品"));
                                }));
                                if (queue_list.containsKey(player.getUniqueId().toString())) {
                                    if (!queue_list.isEmpty()) {
                                        List<String> lore = new ArrayList<>();
                                        ArrayList<String> strings1 = queue_list.get(player.getUniqueId().toString());
                                        ItemStack dynamicitem = null;
                                        if (!strings1.isEmpty()) {
                                            Map<Integer, String> temp = new HashMap<>();
                                            int num = 0;
                                            int makeing = 0;
                                            for (String s : strings1) {
                                                long unixTime = System.currentTimeMillis() / 1000L;
                                                ItemStack itemStack = queues_ITEM.get(s);
                                                String time = "";
                                                if ((queues_cooldown.get(s) - unixTime) <= 0) {
                                                    time = "已經完成";
                                                    makeing++;
                                                } else {
                                                    time = (queues_cooldown.get(s) - unixTime) + " 秒";
                                                }
                                                if (num < 10) {
                                                    lore.add(Utils.format("&e - ") + itemStack.getItemMeta().getDisplayName() + " 製作時間: " + time);
                                                } else {
                                                    lore.set(9, Utils.format("&e還有 " + (num - 8) + " 個物品~"));
                                                }
                                                num++;
                                            }
                                            if (makeing != num) {
                                                player.playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1F, (float) 0.1);
                                            }
                                            lore.add(0, Utils.format("&a點擊領取一個已完成的物品"));
                                            lore.add(1, Utils.format("&e製作數量: " + makeing + " / " + num));
                                            if (num == 0) {
                                                num = 1;
                                            }
                                            dynamicitem = ItemManger.DdynamicItem(Material.EMERALD, lore, num, Utils.format("&c----------------------"));
                                            ArrayList<String> strings2 = queue_list.get(player.getUniqueId().toString());
                                            String tQueueID = "";
                                            for (String s : strings2) {
                                                tQueueID = s;
                                                break;
                                            }
                                            String finalTQueueID = tQueueID;
                                            contents.set(4, 1, IntelligentItem.of(dynamicitem, event -> {
                                                plugin.getLogger().info("QueueID: " + finalTQueueID);
                                                long unixTime = System.currentTimeMillis() / 1000L;
                                                Boolean EnoughInventory = Utils.EnoughInventory(player);
                                                ArrayList<String> strings = queue_list.get(player.getUniqueId().toString());
                                                if (queues_cooldown.get(finalTQueueID) < unixTime && EnoughInventory) {
                                                    ItemStack additem = queues_ITEM.get(finalTQueueID);
                                                    Boolean Added = false;
                                                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.1F, (float) 0.1);
                                                    for (ItemStack itemStack : player.getInventory()) {
                                                        // 如果物品欄位的物品等於新增物品時
                                                        if (itemStack == null) continue;
                                                        if (itemStack.toString().equals(additem.toString())) {
                                                            // 如果物品欄位的物品不是最大堆疊值
                                                            if (!(itemStack.getMaxStackSize() == itemStack.getAmount())) {
                                                                if ((itemStack.getAmount() + additem.getAmount()) < itemStack.getMaxStackSize()) {
                                                                    itemStack.setAmount(itemStack.getAmount() + additem.getAmount());
                                                                    Added = true;
                                                                }
                                                            }
                                                        }
                                                    }
                                                    if (!Added) {
                                                        player.getInventory().addItem(additem);
                                                        player.sendMessage(Utils.format("&7[&6CNC&7]&e領取 " + queues_ITEM.get(finalTQueueID).getItemMeta().getDisplayName() + " X " + queues_ITEM.get(finalTQueueID).getAmount()));
                                                        queues_cooldown.remove(finalTQueueID);
                                                        queues_ITEM.remove(finalTQueueID);
                                                        strings.remove(finalTQueueID);
                                                        queue_list.put(player.getUniqueId().toString(), strings);
                                                        InventoryList.inventoryList.get(2).open(player);
                                                        return;
                                                    }
                                                } else if (!EnoughInventory) {
                                                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.1F, (float) 1);
                                                    player.sendMessage(Utils.format("&7[&6CNC&7]&e你背包沒有空間!!"));
                                                    InventoryList.inventoryList.get(2).close(player);
                                                } else {
                                                    player.sendMessage(Utils.format("&7[&6CNC&7]&e列隊數量: " + queue_list.get(player.getUniqueId().toString()).size() + " 個 製作物品當中..."));
                                                    InventoryList.inventoryList.get(2).open(player);
                                                }
                                            }));
                                            contents.set(4, 2, IntelligentItem.of(new ItemBuilder(Material.EMERALD_BLOCK).amount(1).displayname(Utils.format("&a領取所有物品")).build(), event -> {
                                                /*CGCommandTOGui.plugin.getLogger().info("-----------------------------------------------");*/
                                                StringBuilder msg = new StringBuilder("&7[&6CNC&7]&e");
                                                List<String> ListQueueID = new ArrayList<>();
                                                List<String> PrepareList = new ArrayList<>();
                                                /*CGCommandTOGui.plugin.getLogger().info("INIT PrepareList: " + PrepareList);
                                                CGCommandTOGui.plugin.getLogger().info("INIT ListQueueID: " + ListQueueID);*/
                                                for (String tqueueid : queue_list.get(player.getUniqueId().toString())) {
                                                    Integer cooldown = queues_cooldown.get(tqueueid);
                                                    long unixTime = System.currentTimeMillis() / 1000L;
                                                    if (cooldown < unixTime) {
                                                        PrepareList.add(tqueueid);
                                                    }
                                                }
                                                /*CGCommandTOGui.plugin.getLogger().info("CHANGE PrepareList: " + PrepareList);
                                                CGCommandTOGui.plugin.getLogger().info("-----------------------------------------------");
                                                CGCommandTOGui.plugin.getLogger().info("Note: 判斷是否空值");*/
                                                if (PrepareList.isEmpty()) {
                                                    /*CGCommandTOGui.plugin.getLogger().info("-----------------------------------------------");
                                                    CGCommandTOGui.plugin.getLogger().info("Note: 是空值");
                                                    CGCommandTOGui.plugin.getLogger().info("EMPTY PrepareList: null");*/
                                                    player.sendMessage(Utils.format("&7[&6CNC&7]&c你沒有任何可以領取的物品!!"));
                                                    InventoryList.inventoryList.get(2).close(player);
                                                    return;
                                                } else {
                                                    /*CGCommandTOGui.plugin.getLogger().info("-----------------------------------------------");
                                                    CGCommandTOGui.plugin.getLogger().info("Note: 不是空值");
                                                    CGCommandTOGui.plugin.getLogger().info("BEFORE PrepareList: " + PrepareList);
                                                    CGCommandTOGui.plugin.getLogger().info("BEFORE ListQueueID: " + ListQueueID);*/
                                                    for (String s : PrepareList) {
                                                        ItemStack thisItem = queues_ITEM.get(s);
                                                        ListQueueID.add(s);
                                                    }
                                                    /*CGCommandTOGui.plugin.getLogger().info("AFTER ListQueueID: " + ListQueueID);
                                                    CGCommandTOGui.plugin.getLogger().info("AFTER PrepareList: " + PrepareList);
                                                    CGCommandTOGui.plugin.getLogger().info("-----------------------------------------------");*/
                                                    int addsuccess = 0;
                                                    int times = 0;
                                                    Map<ItemStack, Integer> temp1 = new HashMap<>();
                                                    /*CGCommandTOGui.plugin.getLogger().info("Note: 給物品操作");
                                                    CGCommandTOGui.plugin.getLogger().info("INIT temp1: " + temp1.size());
                                                    CGCommandTOGui.plugin.getLogger().info("BEFORE temp1: " + temp1.size());
                                                    CGCommandTOGui.plugin.getLogger().info("BEFORE addsuccess: " + addsuccess);
                                                    CGCommandTOGui.plugin.getLogger().info("BEFORE times: " + times);
                                                    CGCommandTOGui.plugin.getLogger().info("BEFORE ListQueueID: " + ListQueueID);
                                                    CGCommandTOGui.plugin.getLogger().info("BEFORE ListQueueID size: " + ListQueueID.size());*/
                                                    for (String tqueueid : ListQueueID) {
                                                        if (Utils.EnoughInventory(player)) {
                                                            if (temp1.containsKey(queues_ITEM.get(tqueueid))) {
                                                                temp1.put(queues_ITEM.get(tqueueid), temp1.get(queues_ITEM.get(tqueueid)) + 1);
                                                            } else {
                                                                temp1.put(queues_ITEM.get(tqueueid), 1);
                                                            }
                                                            player.getInventory().addItem(queues_ITEM.get(tqueueid));
                                                            queue_list.remove(tqueueid);
                                                            queues_cooldown.remove(tqueueid);
                                                            ArrayList<String> strings = queue_list.get(player.getUniqueId().toString());
                                                            strings.remove(tqueueid);
                                                            queue_list.put(player.getUniqueId().toString(), strings);
                                                            addsuccess++;
                                                        } else {
                                                            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.1F, (float) 1);
                                                            if (addsuccess > 0) {
                                                                msg.append("領取 ").append(addsuccess).append(" 個物品 ");
                                                                int g = 1;
                                                                for (Map.Entry<ItemStack, Integer> entry : temp1.entrySet()) {
                                                                    ItemStack iiiiii = entry.getKey();
                                                                    Integer integer = entry.getValue();
                                                                    if (g == temp1.size()) {
                                                                        msg.append(iiiiii.getItemMeta().getDisplayName() + " X " + integer);
                                                                    } else {
                                                                        msg.append(iiiiii.getItemMeta().getDisplayName() + " X " + integer + ", ");
                                                                    }
                                                                    g++;
                                                                }
                                                                player.sendMessage(Utils.format(msg.toString()));
                                                            }
                                                            player.sendMessage(Utils.format("&7[&6CNC&7]&e你背包沒有足夠的空間繼續領取!!"));
                                                            InventoryList.inventoryList.get(2).close(player);
                                                            return;
                                                        }
                                                        times++;
                                                    }
                                                    msg.append("領取 ").append(addsuccess).append(" 個物品 ");
                                                    int g = 1;
                                                    for (Map.Entry<ItemStack, Integer> entry : temp1.entrySet()) {
                                                        ItemStack iiiiii = entry.getKey();
                                                        Integer integer = entry.getValue();
                                                        if (g == temp1.size()) {
                                                            msg.append(iiiiii.getItemMeta().getDisplayName()).append(" X ").append(integer);
                                                        } else {
                                                            msg.append(iiiiii.getItemMeta().getDisplayName()).append(" X ").append(integer).append(", ");
                                                        }
                                                        g++;
                                                    }
                                                    /*CGCommandTOGui.plugin.getLogger().info("AFTER temp1: " + temp1.size());
                                                    CGCommandTOGui.plugin.getLogger().info("AFTER addsuccess: " + addsuccess);
                                                    CGCommandTOGui.plugin.getLogger().info("AFTER times: " + times);
                                                    CGCommandTOGui.plugin.getLogger().info("AFTER ListQueueID: " + ListQueueID);
                                                    CGCommandTOGui.plugin.getLogger().info("AFTER ListQueueID size: " + ListQueueID.size());
                                                    CGCommandTOGui.plugin.getLogger().info("-----------------------------------------------");*/
                                                    if (addsuccess == 0) {
                                                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.1F, (float) 1);
                                                        player.sendMessage(Utils.format("&7[&6CNC&7]&e你背包沒有空間!!"));
                                                        InventoryList.inventoryList.get(2).close(player);
                                                    } else {
                                                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.1F, (float) 0.1);
                                                        player.sendMessage(Utils.format(msg.toString()));
                                                        InventoryList.inventoryList.get(2).close(player);
                                                    }
                                                }
                                                /*CGCommandTOGui.plugin.getLogger().info("-----------------------------------------------");*/
                                            }));
                                        }
                                    }

                                }
                            }
                        })
                        .build(CGCommandTOGui.plugin)
        );
    }

    private static void settask(String player, int taskid) {
        task.put(player, taskid);
        plugin.getLogger().info("Task " + taskid + " Start");
    }

    private static void cancel(String player) {
        Bukkit.getServer().getScheduler().cancelTask(task.get(player));
        plugin.getLogger().info("Task " + task.get(player) + " Stop");
        task.remove(player);
    }

    public static void CNC_CONFIRM(String title) {
        plugin.getServer().getLogger().info(Utils.format("[CGCommandTOGui] &aGUI: CNC_CONFIRM Loaded"));
        if (Objects.equals(title, "")) {
            title = "CNC 製作確認畫面";
        }
        // 強制覆蓋先前的空實例
        inventoryList.set(3,
                RyseInventory.builder()
                        .manager(CGCommandTOGui.InventoryManager)
                        .title(title)
                        .rows(1)
                        .provider(new InventoryProvider() {
                            @Override
                            public void init(Player player, InventoryContents contents) {
                                ItemStack item1 = ItemManger.items.get(2);
                                ItemStack item2 = ItemManger.items.get(3);

                                contents.fillBorders(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, 1, "").build());
                                contents.set(0, 0, IntelligentItem.of(item1, event -> {
                                    if (Utils.hasItem(player.getInventory(), new ItemStack(Material.GLASS_PANE)) && Utils.hasItem(player.getInventory(), new ItemStack(Material.WHITE_CONCRETE))) {
                                        Utils.removeOne(player.getInventory(), new ItemStack(Material.GLASS_PANE));
                                        Utils.removeOne(player.getInventory(), new ItemStack(Material.WHITE_CONCRETE));
                                        long unixTime = System.currentTimeMillis() / 1000L;
                                        ArrayList<String> Strings = new ArrayList<>();
                                        ItemStack item = ItemManger.items.get(0);
                                        //如果玩家還沒註冊狀態
                                        if (!queue_list.containsKey(player.getUniqueId().toString())) {
                                            String QueueID = Utils.getRandomString(10);
                                            Strings.add(QueueID);
                                            queue_list.put(player.getUniqueId().toString(), Strings);
                                            queues_cooldown.put(QueueID, (int) (unixTime + 30));
                                            queues_ITEM.put(QueueID, item);
                                        } else if (queue_list.containsKey(player.getUniqueId().toString())) {
                                            Strings = queue_list.get(player.getUniqueId().toString());
                                            String QueueID = Utils.getRandomString(10);
                                            Strings.add(QueueID);
                                            queue_list.put(player.getUniqueId().toString(), Strings);
                                            queues_cooldown.put(QueueID, (int) (unixTime + 30));
                                            queues_ITEM.put(QueueID, item);
                                        }

                                    } else if (player.getGameMode() == GameMode.CREATIVE) {
                                        player.sendMessage(Utils.format("&7[&6CNC&7]&c&l現在是創造模式，所以繞過物品需求和等待時間功能!!"));
                                        long unixTime = System.currentTimeMillis() / 1000L;
                                        ArrayList<String> Strings = new ArrayList<>();
                                        ItemStack item = ItemManger.items.get(0);
                                        //如果玩家還沒註冊狀態
                                        if (!queue_list.containsKey(player.getUniqueId().toString())) {
                                            String QueueID = Utils.getRandomString(10);
                                            Strings.add(QueueID);
                                            queue_list.put(player.getUniqueId().toString(), Strings);
                                            queues_cooldown.put(QueueID, (int) (unixTime));
                                            queues_ITEM.put(QueueID, item);
                                        } else if (queue_list.containsKey(player.getUniqueId().toString())) {
                                            Strings = queue_list.get(player.getUniqueId().toString());
                                            String QueueID = Utils.getRandomString(10);
                                            Strings.add(QueueID);
                                            queue_list.put(player.getUniqueId().toString(), Strings);
                                            queues_cooldown.put(QueueID, (int) (unixTime));
                                            queues_ITEM.put(QueueID, item);
                                        }
                                    }
                                    InventoryList.inventoryList.get(2).open(player);
                                }));
                                contents.set(0, 8, IntelligentItem.of(item2, event -> {
                                    InventoryList.inventoryList.get(2).open(player);
                                }));
                            }
                        })
                        .build(CGCommandTOGui.plugin)
        );
    }
}
