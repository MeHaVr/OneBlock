package Oneblock.gui;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUIListener implements Listener {

	@EventHandler
    public void onPlayerClickInventory(final InventoryClickEvent e){
    	if (!GUI.enabled) return;
        Inventory inv = e.getInventory();
        if (inv.getHolder() == null) return;
        if (!inv.getHolder().getClass().isAssignableFrom(GUIHolder.class)) return;
        
        e.setCancelled(true);
        
        HumanEntity he = e.getWhoClicked();
	    if (!(he instanceof Player)) return;
	    Player pl = (Player) he;
	        
	    if (inv.getSize() == 27) {
		    if (GUI.baseGUI != null && GUI.baseGUI.equals(inv)) {
		    	if (e.getClickedInventory() != inv) return;
		        ItemStack item = e.getCurrentItem();
		        if (item == null) return;
		        pl.closeInventory();
		        ItemMeta meta = item.getItemMeta();
		        if (meta == null) return;
		       	//String command = meta.getDisplayName();
		       	//if (command.contains("/"))
		       	//	pl.performCommand(command.split("/")[1]);

				if (e.getCurrentItem().getType() == Material.GRASS_BLOCK) {
					pl.performCommand("ob j");
				}
				if (e.getCurrentItem().getType() == Material.NETHER_STAR) {
					pl.performCommand("spawn");
				}
				if (e.getCurrentItem().getType() == Material.GOLD_BLOCK) {
					pl.performCommand("ob top");
				}

			}
		    else {
		        if (e.getCurrentItem().getType() == Material.EMERALD_BLOCK)
		        	pl.performCommand("ob accept");
		    	}

				if (e.getCurrentItem().getType() == Material.REDSTONE_BLOCK) {
					pl.closeInventory();
				}

		} else if (inv.getSize() == 54) {
		    if (e.getClickedInventory() != inv) return;
	        ItemStack item = e.getCurrentItem();
	        if (item == null) return;
	        pl.closeInventory();
	        ItemMeta meta = item.getItemMeta();
	        if (meta == null) return;
	        String command = meta.getDisplayName();
	        pl.performCommand("ob visit " + command);
        }
    }
}