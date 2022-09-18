package cg.creamgod45.commands;

import cg.creamgod45.CGCommandTOGui;
import cg.creamgod45.Inventorys.InventoryList;
import cg.creamgod45.utils.Utils;
import com.ssomar.score.api.executableblocks.ExecutableBlocksAPI;
import com.ssomar.score.api.executableblocks.config.ExecutableBlocksManagerInterface;
import io.github.rysefoxx.pagination.RyseInventory;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.help.HelpTopic;
import org.bukkit.scoreboard.Score;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ctg implements CommandExecutor, TabCompleter {

    public void pagecontrol(Player player, Integer page) {
        TextComponent mainComponent = new TextComponent("頁面: ");
        mainComponent.setColor(ChatColor.YELLOW);
        if (page != 0) {
            TextComponent prev = new TextComponent("[上一頁]");
            prev.setColor(ChatColor.GREEN);
            prev.setBold(true);
            prev.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("點我執行").create()));
            prev.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cgcommandtogui help " + (page - 1)));
            mainComponent.addExtra(prev);
        } else {
            TextComponent prev = new TextComponent("上一頁");
            prev.setColor(ChatColor.GRAY);
            mainComponent.addExtra(prev);
        }
        TextComponent sy = new TextComponent(" | ");
        mainComponent.addExtra(sy);
        if (page != 2) {
            TextComponent next = new TextComponent("[下一頁]");
            next.setColor(ChatColor.GREEN);
            next.setBold(true);
            next.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("點我執行").create()));
            next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cgcommandtogui help " + (page + 1)));
            mainComponent.addExtra(next);
        } else {
            TextComponent prev = new TextComponent("下一頁");
            prev.setColor(ChatColor.GRAY);
            mainComponent.addExtra(prev);
        }
        player.spigot().sendMessage(mainComponent);
    }

    public void helpmanal(Player player, int page) {
        player.sendMessage(Utils.format(" "));
        player.sendMessage(Utils.format("&e&l============================================="));
        switch (page) {
            default:
            case 0:
                player.sendMessage(Utils.format("&e&l[CommandToGui] &c變數細項說明 頁面: (" + page + "/2)"));
                player.sendMessage(Utils.format("&9<page> &f數字"));
                player.sendMessage(Utils.format("&d<title> &f字串標題"));
                player.sendMessage(Utils.format("&f  - *space*          = 將這個文字替換為空格 "));
                player.sendMessage(Utils.format("&b<command> &f執行指令無須加入 / 符號"));
                player.sendMessage(Utils.format("&f  - *localplayer*   = 將這個文字替換為執行指令的玩家"));
                player.sendMessage(Utils.format("&f  - *selectplayer* = 將這個文字替換為介面點選的玩家"));
                pagecontrol(player, 0);
                break;
            case 1:
                player.sendMessage(Utils.format("&e&l[CommandToGui] &c指令幫助 頁面: (" + page + "/2)"));
                player.sendMessage(Utils.format("&a/ctg help &9<page> "));
                player.sendMessage(Utils.format(" &a查看指定的幫助頁面"));
                player.sendMessage(Utils.format("&a/ctg sp &d<title> &b<command> "));
                player.sendMessage(Utils.format(" &a指定玩家執行指令(介面包含執行指令的玩家在內)"));
                player.sendMessage(Utils.format("&a/ctg selectplayer &d<title> &b<command> "));
                player.sendMessage(Utils.format(" &a指定玩家執行指令(介面包含執行指令的玩家在內)"));
                player.sendMessage(Utils.format("&a/ctg spwm &d<title> &b<command> "));
                player.sendMessage(Utils.format(" &a指定玩家執行指令(介面不包含執行指令的玩家在內)"));
                player.sendMessage(Utils.format("&a/ctg selectplayerwithoutme &d<title> &b<command> "));
                player.sendMessage(Utils.format(" &a指定玩家執行指令(介面不包含執行指令的玩家在內)"));
                pagecontrol(player, 1);
                break;
            case 2:
                player.sendMessage(Utils.format("&e&l[CommandToGui] &c指令幫助 頁面: (" + page + "/2)"));
                player.sendMessage(Utils.format("&a/ctg CNC"));
                player.sendMessage(Utils.format(" &a開啟 CNC 儀錶板"));
                player.sendMessage(Utils.format("&a/ctg EBSET <EB_ID> <X> <Y> <Z> <WROLD>"));
                player.sendMessage(Utils.format(" &a新增開啟服務氣的 EB 放置方塊錯誤"));
                player.sendMessage(Utils.format("&a/ctg EBDEL <EB_ID> <X> <Y> <Z> <WROLD> <PlayerName>"));
                player.sendMessage(Utils.format(" &a刪除開啟服務氣的 EB 放置方塊錯誤"));
                pagecontrol(player, 2);
                break;
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (command.getName().equalsIgnoreCase("cgcommandtogui") && Utils.HasPermission(player, "CGCommandToGui.admin")) {
                if (args.length == 0) {
                    helpmanal(player, 0);
                } else {
                    String method = args[0];
                    switch (method) {
                        case "sp":
                        case "selectplayer":
                            if (args.length >= 3) {
                                String title = args[1];
                                title = title.replace("*space*", " ");
                                String commands = Utils.arraytostring(args, 2);
                                commands = commands.replaceAll("restart|stop|deop|op", "");

                                List<String> stringList = CGCommandTOGui.plugin.getConfig().getStringList("config.Filter.DenyCommandsRegex");
                                StringBuilder regex = new StringBuilder();
                                int k = 1;
                                for (String s : stringList) {
                                    if (stringList.size() == k) {
                                        regex.append(s);
                                    } else {
                                        regex.append(s).append("|");
                                    }
                                    k++;
                                }
                                commands = commands.replaceAll(regex.toString(), "");

                                CGCommandTOGui.command_data.put(player, commands);

                                InventoryList.SelectPlayers(title);

                                RyseInventory ryseInventory = InventoryList.inventoryList.get(0);
                                ryseInventory.open(player);
                            } else {
                                helpmanal(player, 0);
                            }
                            break;
                        case "spwm":
                        case "selectplayerwithoutme":
                            if (args.length >= 3) {
                                String title = args[1];
                                title = title.replace("*space*", " ");
                                String commands = Utils.arraytostring(args, 2);
                                commands = commands.replaceAll("restart|stop|deop|op", "");
                                List<String> stringList = CGCommandTOGui.plugin.getConfig().getStringList("config.Filter.DenyCommandsRegex");
                                StringBuilder regex = new StringBuilder();
                                int k = 1;
                                for (String s : stringList) {
                                    if (stringList.size() == k) {
                                        regex.append(s);
                                    } else {
                                        regex.append(s).append("|");
                                    }
                                    k++;
                                }
                                commands = commands.replaceAll(regex.toString(), "");

                                CGCommandTOGui.command_data.put(player, commands);

                                InventoryList.SelectPlayersWithMe(title);

                                RyseInventory ryseInventory = InventoryList.inventoryList.get(1);
                                ryseInventory.open(player);
                            } else {
                                helpmanal(player, 0);
                            }
                            break;
                        case "vault":
                            double balance = CGCommandTOGui.econ.getBalance(player);
                            player.sendMessage(String.valueOf("Money: " + balance));
                            break;
                        case "CNC":
                            if (InventoryList.queue_list.containsKey(player.getUniqueId().toString())) {
                                if (InventoryList.queue_list.get(player.getUniqueId().toString()).size() == 0) {
                                    InventoryList.queue_list.get(player.getUniqueId().toString()).clear();
                                }
                            }
                            RyseInventory ryseInventory = InventoryList.inventoryList.get(2);
                            ryseInventory.open(player);
                            break;

                        case "EBSET":
                            if (args.length >= 5) {
                                String EB_ID = args[1];
                                String X = args[2];
                                String Y = args[3];
                                String Z = args[4];
                                String WORLD = args[5];
                                if (CGCommandTOGui.plugin.getConfig().get("config.Storage") == null) {
                                    List<String> strings = new ArrayList<>();
                                    strings.add(EB_ID + " " + X + " " + Y + " " + Z + " " + WORLD);
                                    CGCommandTOGui.plugin.getConfig().set("config.Storage", strings);
                                    CGCommandTOGui.plugin.saveConfig();
                                } else {
                                    List<String> strings = CGCommandTOGui.plugin.getConfig().getStringList("config.Storage");
                                    strings.add(EB_ID + " " + X + " " + Y + " " + Z + " " + WORLD);
                                    CGCommandTOGui.plugin.getConfig().set("config.Storage", strings);
                                    CGCommandTOGui.plugin.saveConfig();
                                }
                            } else {
                                helpmanal(player, 0);
                            }
                            break;
                        case "EBDEL":
                            if (args.length >= 5) {
                                String EB_ID = args[1];
                                String X = args[2];
                                String Y = args[3];
                                String Z = args[4];
                                String WORLD = args[5];
                                List<String> stringList1 = CGCommandTOGui.plugin.getConfig().getStringList("config.Storage");
                                if (stringList1 != null) {
                                    Map<Integer, String> temp = new HashMap<>();
                                    Integer i = 0;
                                    for (String s : stringList1) {
                                        temp.put(i, s);
                                        i++;
                                    }
                                    if (!temp.isEmpty()) {
                                        try {
                                            for (Map.Entry<Integer, String> entry : temp.entrySet()) {
                                                if (Objects.equals(entry.getValue(), EB_ID + " " + X + " " + Y + " " + Z + " " + WORLD)) {
                                                    temp.remove(entry.getKey());
                                                }
                                            }
                                        } catch (ConcurrentModificationException e) {
                                            CGCommandTOGui.plugin.getLogger().info("config.yml:config.Storage:重複數值造成錯誤，刪除重複變數即可!!");
                                            CGCommandTOGui.plugin.getLogger().info("EDIT temp: " + temp.toString());
                                            CGCommandTOGui.plugin.getLogger().info("ERROR temp");
                                        }
                                        List<String> list = new ArrayList<>(temp.values());
                                        CGCommandTOGui.plugin.getConfig().set("config.Storage", list);
                                        CGCommandTOGui.plugin.saveConfig();
                                    }
                                }
                            } else {
                                helpmanal(player, 0);
                            }
                            break;
                        case "helpall":
                            for (HelpTopic cmdLabel : CGCommandTOGui.plugin.getServer().getHelpMap().getHelpTopics()) {
                                player.sendMessage(cmdLabel.getName());
                            }
                            break;
                        case "help":
                            if (args.length >= 2) {
                                String page = args[1];
                                helpmanal(player, Integer.parseInt(page));
                            } else {
                                helpmanal(player, 0);
                            }
                            break;
                        default:
                            CGCommandTOGui.plugin.getLogger().info("default");
                            helpmanal(player, 0);
                            break;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (Utils.HasPermission(player, "CGCommandToGui.admin")) {
                if(args.length==1){
                    List<String> arguments = new ArrayList<>();
                    arguments.add("!說明:選擇一個服務選項");
                    arguments.add("helpall");
                    arguments.add("help");
                    arguments.add("sp");
                    arguments.add("selectplayer");
                    arguments.add("spwm");
                    arguments.add("selectplayerwithoutme");
                    arguments.add("vault");
                    arguments.add("CNC");
                    arguments.add("EBSET");
                    arguments.add("EBDEL");
                    return arguments;
                }
                if (args.length > 0) {
                    if (args[0].equals("sp") || args[0].equals("selectplayer") || args[0].equals("spwm") || args[0].equals("selectplayerwithoutme")) {
                        if (args.length == 2) {
                            List<String> arguments = new ArrayList<>();
                            arguments.add("!說明:輸入標題名稱 *space* 新增空格");
                            arguments.add("*space*");
                            arguments.add("選擇一個玩家執行指令");
                            return arguments;
                        }
                        if (args.length >= 3) {
                            List<String> arguments = new ArrayList<>();
                            arguments.add("!說明:輸入指令");
                            for (HelpTopic cmdLabel : CGCommandTOGui.plugin.getServer().getHelpMap().getHelpTopics()) {
                                arguments.add(cmdLabel.getName().replaceFirst("/", ""));
                            }

                            Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
                            Bukkit.getServer().getOnlinePlayers().toArray(players);
                            for (int i = 0; i < players.length; i++) {
                                arguments.add(players[i].getName());
                            }

                            arguments.add("*localplayer*");
                            arguments.add("*selectplayer*");
                            return arguments;
                        }
                    }
                    if(args[0].equals("help")){
                        if (args.length == 2) {
                            List<String> arguments = new ArrayList<>();
                            arguments.add("0");
                            arguments.add("1");
                            arguments.add("2");
                            return arguments;
                        }
                    }


                    if(args[0].equals("EBSET") || args[0].equals("EBDEL")){
                        if (args.length == 2) {
                            ExecutableBlocksManagerInterface executableBlocksManager = ExecutableBlocksAPI.getExecutableBlocksManager();
                            return executableBlocksManager.getExecutableBlockIdsList();
                        }
                        if (args.length == 3) {
                            List<String> arguments = new ArrayList<>();
                            arguments.add("~");
                            arguments.add("%player_x%");
                            arguments.add("%player_x_int%");
                            arguments.add("%entity_x%");
                            arguments.add("%entity_x_int%");
                            arguments.add("%block_x%");
                            arguments.add("%block_x_int%");
                            arguments.add("%projectile_x%");
                            arguments.add("%projectile_x_int%");
                            arguments.add(String.valueOf(player.getLocation().getBlockX()));
                            return arguments;
                        }
                        if (args.length == 4) {
                            List<String> arguments = new ArrayList<>();
                            arguments.add("~");
                            arguments.add("%player_y%");
                            arguments.add("%player_y_int%");
                            arguments.add("%entity_y%");
                            arguments.add("%entity_y_int%");
                            arguments.add("%block_y%");
                            arguments.add("%block_y_int%");
                            arguments.add("%projectile_y%");
                            arguments.add("%projectile_y_int%");
                            arguments.add(String.valueOf(player.getLocation().getBlockY()));
                            return arguments;
                        }
                        if (args.length == 5) {
                            List<String> arguments = new ArrayList<>();
                            arguments.add("~");
                            arguments.add("%player_z%");
                            arguments.add("%player_z_int%");
                            arguments.add("%entity_z%");
                            arguments.add("%entity_z_int%");
                            arguments.add("%block_z%");
                            arguments.add("%block_z_int%");
                            arguments.add("%projectile_z%");
                            arguments.add("%projectile_z_int%");
                            arguments.add(String.valueOf(player.getLocation().getBlockZ()));
                            return arguments;
                        }
                        if (args.length == 6) {
                            List<String> arguments = new ArrayList<>();
                            arguments.add("%player_world%");
                            arguments.add("%player_world_lower%");
                            arguments.add("%entity_world%");
                            arguments.add("%entity_world_lower%");
                            arguments.add("%block_world%");
                            arguments.add("%block_world_lower%");
                            arguments.add("%projectile_world%");
                            arguments.add("%projectile_world_lower%");
                            arguments.add(String.valueOf(player.getLocation().getWorld()));
                            for (World world : Bukkit.getServer().getWorlds()) {
                                arguments.add(world.getName());
                            }
                            return arguments;
                        }
                    }
                }
            }
        }
        return null;
    }
}
