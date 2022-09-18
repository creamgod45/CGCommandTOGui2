package cg.creamgod45;

import cg.creamgod45.Inventorys.InventoryList;
import cg.creamgod45.commands.ctg;
import cg.creamgod45.items.ItemManger;
import cg.creamgod45.utils.Utils;
import io.github.rysefoxx.pagination.InventoryManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class CGCommandTOGui extends JavaPlugin {
    public static boolean hasExecutableBlocks = false;
    public static CGCommandTOGui plugin;
    public static Map<Player, String> command_data = new HashMap<>();
    public static InventoryManager InventoryManager;
    public static Economy econ = null;
    public static Boolean ConfigLoad = false;

    @Override
    public void onEnable() {
        plugin = this;
        initConfig();

        getCommand("cgcommandtogui").setExecutor(new ctg());
        getLogger().info("");
        getLogger().info("");
        getLogger().info("");
        getServer().getLogger().info("                                                                                                                                                                          ");
        getServer().getLogger().info("       :7Y555YY~                                                                                   ^#GG#~~5YYY555YYYY^                 ^7Y5555Y7:               ?GPG?     ");
        getServer().getLogger().info("    .JB5~^~~~~!@7      .             ..    .           ..    ..          .            ..         ..J@. @G&#~!~:.^~!!&&      .       .JB5~~~!!~^G@.             .@P.P@.    ");
        getServer().getLogger().info("   .#B  5B5YY5PB: .7PP5555G?: .5B5BG5YPGPP5Y5GY. ^BPPBP55GGP555PG~  .JP55Y5PP^  JB5GG5Y5GP~  .!GP5YG&. &G~5Y5@! J@5YY~ .JP5555PG?. :&G .PB5Y55PB5..5G5BY !BPPB: B@#@G     ");
        getServer().getLogger().info("   5@  &B..   .  ^&P.:JYY^.Y@!.@? :Y5? .7?5J. B&.5@  75Y~ ~7YY~ ~@? 7@YYPP? !@7.&G .JYY^ 7@~:#B:.?YJ~  &G...:@7 Y@... !&5.^YYJ:.P&^B&  @G~B555PGG::@J Y@.B& .@J.@? 5@.    ");
        getServer().getLogger().info("   B&  @P ..     ## .@5^J@^ G&:@7 7@7@Y ~@?@P ~@^Y@  &B5@. ##Y@. &B.?@#P5YJ  &B.&P ^@?P@  @P5@  &B^P@. &G...^@7 Y@....&G ^@J^5@. &### .@Y7@?7! .@J:@? Y@.G& .@J.@? 5@.    ");
        getServer().getLogger().info("   ~@7 ^#G??7YPP.G& .&P!5@. ##:@7 Y@:@5 ?@:&G ^@:Y@  @Y?@. @P!@. #B^@! J&&&  &B.&P ~@^?@  &P5@  &B!B&  &G...:@7 Y@....#B :@5!P@  &G!@! ~#GGB@@  @J.@J 7@?&B .@J.@? 5@.    ");
        getServer().getLogger().info("    ~#G~.^!!7!7@7.#B^:7??^^B#:.@J 5@:@G Y@:&B !@:Y@..@Y7@:.@P!@^ &B:@5.^JJJ..&B.&B 7@:?@..@P.#B:.!J?7..@P . .@J P@.   ^&G^:???^~#B:.!#G~:~!!!^.!@? 5&~.~JJ7 :@?.@Y P@     ");
        getServer().getLogger().info("      ^J555555Y7.  ~Y55555Y~   ?P5P7 7P5P? !P5PJ :P555:.555P:.Y55P^ .JP555PP5P^ !P5PJ..555P^ .!55555PP55:    7P557      ~Y55555Y^    .^J5555555J~.  ~5P555P5P5. ?P55!     ");
        getServer().getLogger().info("                                                                                                                                                                          ");
        getServer().getLogger().info("                                                                                                                                                                          ");
        getLogger().info("========================================");
        getServer().getLogger().info(Utils.format("[CGCommandTOGui] &e正在載入插件..."));
        getServer().getLogger().info(Utils.format("[CGCommandTOGui] &e作者: CreamGod45"));
        getServer().getLogger().info(Utils.format("[CGCommandTOGui] &e版本: " + getConfig().getString("config.General.Version")));
        if (ConfigLoad) {
            getServer().getLogger().info(Utils.format("[CGCommandTOGui] &e設定: &a加載成功"));
        } else {
            getServer().getLogger().warning(Utils.format("[CGCommandTOGui] &e設定: &4加載失敗"));
        }
        getServer().getLogger().info(Utils.format("[CGCommandTOGui] &e更新: 無模塊"));
        InventoryManager = new InventoryManager(this);
        InventoryManager.invoke();
        new ItemManger();
        new InventoryList();
        Plugin executableBlocks = null;
        executableBlocks = Bukkit.getPluginManager().getPlugin("ExecutableBlocks");
        if (Objects.requireNonNull(executableBlocks).isEnabled()) {
            hasExecutableBlocks = true;
            getServer().getLogger().info(Utils.format("[CGCommandTOGui] &aDepend: ExecutableBlocks Loaded"));
        }
        // Load Vault Economy
        if (!setupEconomy()) {
            getLogger().info(String.format("[%s] - 由於未找到 Vault 依賴項而被禁用！", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(CGCommandTOGui.plugin);
            return;
        } else {
            getServer().getLogger().info(Utils.format("[CGCommandTOGui] &aDepend: Vault Loaded"));
        }

        getServer().getLogger().info(Utils.format("[CGCommandTOGui] &a載入插件完成"));
        getLogger().info("========================================");
        getLogger().info("");
        getLogger().info("");
        getLogger().info("");
    }

    public void initConfig() {
        File config = new File("plugins/CGCommandTOGui/config.yml");
        if (!(config.exists())) {
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
            ConfigLoad = true;
        } else {
            try {
                getConfig().load("plugins/CGCommandTOGui/config.yml");
                ConfigLoad = true;
            } catch (IOException e) {
                getLogger().info("CONFIGLOADER: 表示發生了某種 I/O 異常。此類是由失敗或中斷的 I/O 操作產生的一般異常類。");
                ConfigLoad = false;
            } catch (InvalidConfigurationException e) {
                getLogger().info("CONFIGLOADER: 嘗試加載無效配置時拋出異常");
                ConfigLoad = false;
            }
        }
        if (getConfig().getBoolean("config.General.refesh", true)) {
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
            ConfigLoad = true;
        }
    }


    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    @Override
    public void onDisable() {
        getLogger().info(Utils.format("&c關閉成功!!"));
    }
}
