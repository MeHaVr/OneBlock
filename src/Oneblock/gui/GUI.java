package Oneblock.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import Oneblock.ChestItems;
import Oneblock.PlayerInfo;
import Oneblock.WorldGuard.OBWorldGuard;
import XSeriesOneBlock.XMaterial;

public class GUI {
	public static boolean enabled = true;
	public static final GUIHolder holder = new GUIHolder();

	static Inventory baseGUI = null;
	static Inventory topGUI = null;

	public static void openGUI(Player p) {
		if (!enabled) return;
		if (baseGUI == null) {
			baseGUI = Bukkit.createInventory(holder, 3 * 9, String.format("§x§F§B§0§0§F§8§lʟ§x§D§C§1§F§F§9§lʏ§x§B§C§3§E§F§A§lᴛ§x§9§D§5§D§F§B§lʜ§x§7§E§7§C§F§C§lɪ§x§5§E§9§A§F§C§lᴀ§x§3§F§B§9§F§D§l.§x§1§F§D§8§F§E§lᴅ§x§0§0§F§7§F§F§lᴇ §8• §7ᴏɴᴇʙʟᴏᴄᴋ ᴍᴇɴᴜ"));

			int i;
			for (i = 0; i < 27; ++i) {
				baseGUI.setItem(i, setMeta(XMaterial.GRAY_STAINED_GLASS_PANE, "§f"));
			}

			baseGUI.setItem(11, setMeta(XMaterial.GRASS_BLOCK, "§8» | §x§F§B§C§E§0§8§lᴏ§x§F§C§B§1§0§D§lɴ§x§F§C§9§3§1§2§lᴇ§x§F§D§7§6§1§7§lʙ§x§F§D§5§8§1§C§lʟ§x§F§E§3§B§2§1§lᴏ§x§F§E§1§D§2§6§lᴄ§x§F§F§0§0§2§B§lᴋ"));
			baseGUI.setItem(13, setMeta(XMaterial.NETHER_STAR, "§8» | §x§7§D§F§B§0§0§lꜱ§x§6§0§D§3§0§0§lᴘ§x§4§2§A§A§0§0§lᴀ§x§2§5§8§2§0§0§lᴡ§x§0§7§5§9§0§0§lɴ"));
			baseGUI.setItem(15, setMeta(XMaterial.GOLD_BLOCK, "§8» | §x§F§B§D§3§0§0§lʟ§x§F§B§C§D§0§0§lᴇ§x§F§C§C§7§0§0§lᴀ§x§F§C§C§2§0§0§lᴅ§x§F§D§B§C§0§0§lᴇ§x§F§D§B§6§0§0§lʀ§x§F§D§B§0§0§0§lʙ§x§F§E§A§A§0§0§lᴏ§x§F§E§A§5§0§0§lᴀ§x§F§F§9§F§0§0§lʀ§x§F§F§9§9§0§0§lᴅ"));


		}
		p.openInventory(baseGUI);
	}

	public static void acceptGUI(Player p, String name) {
		if (!enabled) return;
		Inventory acceptGUI = Bukkit.createInventory(holder, 3 * 9, String.format("§x§F§B§0§0§F§8§lʟ§x§D§C§1§F§F§9§lʏ§x§B§C§3§E§F§A§lᴛ§x§9§D§5§D§F§B§lʜ§x§7§E§7§C§F§C§lɪ§x§5§E§9§A§F§C§lᴀ§x§3§F§B§9§F§D§l.§x§1§F§D§8§F§E§lᴅ§x§0§0§F§7§F§F§lᴇ §8• §aɪɴᴠɪᴛᴇ"));

		int i;
		for (i = 0; i < 27; ++i) {
			acceptGUI.setItem(i, setMeta(XMaterial.GRAY_STAINED_GLASS_PANE, "§f"));
		}

		acceptGUI.setItem(11, setMeta(XMaterial.REDSTONE_BLOCK, ChatColor.RED + "§8» | §cignorieren."));
		acceptGUI.setItem(15, setMeta(XMaterial.EMERALD_BLOCK, String.format("§8» | §aInsel §7beitreten"), String.format("§7Die Daten Ihrer §bInsel§7 gehen §cverloren§8.")));
		p.openInventory(acceptGUI);
	}

	public static void topGUI(Player p) {
		if (!enabled) return;
		if (topGUI == null)
			topGUI = Bukkit.createInventory(holder, 27, String.format("§x§D§0§0§8§F§Bʟ§x§A§6§2§9§F§Cʏ§x§7§D§4§A§F§Dᴛ§x§5§3§6§A§F§Dʜ§x§2§A§8§B§F§Eɪ§x§0§0§A§C§F§Fᴀ §8• §eʙᴇꜱᴛᴇɴʟɪꜱᴛᴇ"));

		int i;
		for (i = 0; i < 27; ++i) {
			topGUI.setItem(i, setMeta(XMaterial.GRAY_STAINED_GLASS_PANE, "§f"));
		}

		PlayerInfo inf = Oneblock.Oneblock.gettop(0);
		topGUI.setItem(4, setMeta(XMaterial.NETHERITE_BLOCK, "§8» | §x§F§F§9§9§0§0§lᴇ§x§F§F§A§B§0§0§lʀ§x§F§F§B§E§0§0§lꜱ§x§F§F§D§0§0§0§lᴛ§x§F§F§E§3§0§0§lᴇ§x§F§F§F§5§0§0§lʀ §7- " + ChatColor.GOLD + parseUUID(inf.uuid) + " §8[§d" + inf.lvl + "§8]"));
		inf = Oneblock.Oneblock.gettop(1);
		topGUI.setItem(12, setMeta(XMaterial.DIAMOND_BLOCK, String.format("§8» | §x§1§4§8§8§C§C§lᴢ§x§1§8§7§A§C§8§lᴡ§x§1§C§6§B§C§3§lᴇ§x§2§0§5§D§B§F§lɪ§x§2§3§4§F§B§B§lᴛ§x§2§7§4§0§B§6§lᴇ§x§2§B§3§2§B§2§lʀ §7- §b" + parseUUID(inf.uuid) + " §8[§d" + inf.lvl + "§8]")));
		inf = Oneblock.Oneblock.gettop(2);
		topGUI.setItem(14, setMeta(XMaterial.IRON_BLOCK, String.format("§8» | §x§F§F§F§F§F§F§lᴅ§x§F§2§F§2§F§2§lʀ§x§E§5§E§5§E§5§lɪ§x§D§9§D§9§D§9§lᴛ§x§C§C§C§C§C§C§lᴛ§x§B§F§B§F§B§F§lᴇ§x§B§2§B§2§B§2§lʀ  §7- §f" + parseUUID(inf.uuid) + " §8[§d" + inf.lvl + "§8]")));
		inf = Oneblock.Oneblock.gettop(3);
		topGUI.setItem(20, setMeta(XMaterial.GOLD_BLOCK, String.format("§8» | §x§6§9§6§9§6§9§lᴠ§x§7§2§7§2§7§2§lɪ§x§7§B§7§B§7§B§lᴇ§x§8§5§8§5§8§5§lʀ§x§8§E§8§E§8§E§lᴛ§x§9§7§9§7§9§7§lᴇ§x§A§0§A§0§A§0§lʀ §7- §7" + parseUUID(inf.uuid) + " §8[§d" + inf.lvl + "§8]")));
		inf = Oneblock.Oneblock.gettop(4);
		topGUI.setItem(22, setMeta(XMaterial.COPPER_BLOCK, String.format("§8» | §x§6§2§6§2§6§2§lꜰ§x§6§D§6§D§6§D§lᴜ§x§7§9§7§9§7§9§lᴇ§x§8§4§8§4§8§4§lɴ§x§9§0§9§0§9§0§lꜰ§x§9§B§9§B§9§B§lᴛ§x§A§7§A§7§A§7§lᴇ§x§B§2§B§2§B§2§lʀ §7- §7" + parseUUID(inf.uuid) + " §8[§d" + inf.lvl + "§8]")));
		inf = Oneblock.Oneblock.gettop(5);
		topGUI.setItem(24, setFillMeta(XMaterial.COAL_BLOCK, String.format("§8» | §x§6§9§6§9§6§9§lꜱ§x§7§2§7§2§7§2§lᴇ§x§7§B§7§B§7§B§lᴄ§x§8§5§8§5§8§5§lʜ§x§8§E§8§E§8§E§lꜱ§x§9§7§9§7§9§7§lᴛ§x§A§0§A§0§A§0§lᴇ §7- §7" + parseUUID(inf.uuid) + " §8[§d" + inf.lvl + "§8]"), 1, null));

		p.openInventory(topGUI);
	}

	public static void visitGUI(Player p, List<Player> plonl) {
		if (!enabled) return;
		Inventory visitGUI = Bukkit.createInventory(holder, 54, String.format("§x§D§0§0§8§F§Bʟ§x§A§6§2§9§F§Cʏ§x§7§D§4§A§F§Dᴛ§x§5§3§6§A§F§Dʜ§x§2§A§8§B§F§Eɪ§x§0§0§A§C§F§Fᴀ §8• §7ᴠɪꜱɪᴛ", ChatColor.GREEN, ChatColor.WHITE, ChatColor.BOLD));
		ArrayList<PlayerInfo> list = new ArrayList<>();
		int size = 0;
		for (Player pl : plonl) {
			PlayerInfo inf = PlayerInfo.get(pl.getUniqueId());
			if (inf == null) continue;
			if (!inf.allow_visit) continue;
			list.add(inf);
			size++;
		}
		size = size > 54 ? 54 : size;
		for (int i = 0; i < size; i++)
			visitGUI.setItem(i, getPlayerHead(plonl.get(i), parseUUID(list.get(i).uuid)));
		p.openInventory(visitGUI);
	}

	public static ItemStack getPlayerHead(Player player, String title) {
		ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
		skullMeta.setOwningPlayer(player);
		skullMeta.setDisplayName(title);
		skull.setItemMeta(skullMeta);
		return skull;
	}

	public static void chestGUI(Player p) {
		chestGUI(p, ChestItems.type.SMALL);
	}

	public static void chestGUI(Player p, ChestItems.type chestType) {
		List<ItemStack> list;
		switch (chestType) {
			case MEDIUM:
				list = ChestItems.m_ch;
				break;
			case HIGH:
				list = ChestItems.h_ch;
				break;
			default:
				list = ChestItems.s_ch;
		}
		Inventory chestGUI = Bukkit.createInventory(new ChestHolder(), 54, String.format(" chest. ", chestType.name(), ChatColor.BLACK
				, OBWorldGuard.canUse ? "" : "[Edit only in premium]"));
		for (ItemStack itm : list)
			if (itm != null)
				chestGUI.addItem(itm);
		p.openInventory(chestGUI);
	}

	private static ItemStack setMeta(XMaterial material, String title) {
		return setFillMeta(material, title, 1, null);
	}

	private static ItemStack setMeta(XMaterial material, String title, String Lore) {
		return setMeta(material, title, 1, Lore);
	}

	private static ItemStack setMeta(XMaterial material, String title, int amount, String Lore) {
		ArrayList<String> lore = null;
		if (Lore != null) {
			lore = new ArrayList<String>();
			lore.add(Lore);
		}
		return setFillMeta(material, title, 1, lore);
	}

	private static List<String> parseUUIDs(List<UUID> uuids) {
		List<String> names = new ArrayList<>();
		try {
			for (UUID uuid : uuids) {
				names.add(Bukkit.getOfflinePlayer(uuid).getName());
			}
		} catch (Exception e) {
			names.add("Error");
		}
		return names;
	}


	private static String parseUUID(UUID uuid) {
		try {
			return Bukkit.getOfflinePlayer(uuid).getName();
		} catch (Exception e) {
			return "NICHT BEZETZT";
		}
	}

	private static ItemStack setFillMeta(XMaterial material, String title, int amount, List<String> Lore) {
		Material m = material.parseMaterial();
		ItemStack join = new ItemStack(m == null ? Material.EMERALD_BLOCK : m, amount);
		ItemMeta meta = join.getItemMeta();
		meta.setDisplayName(title);
		meta.setLore(Lore);
		join.setItemMeta(meta);
		return join;
	}
}
