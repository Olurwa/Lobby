package fr.doritanh.olurwa.Lobby;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MenuGUI implements Listener {
	private String name;
    private final Inventory i;

    public MenuGUI() {
    	this.name = "Olurwa Menu";
        this.i = Bukkit.createInventory(null, 9, this.name);        

        this.i.addItem(this.createItemStack(Material.GRASS_BLOCK, "Survie"));
        this.i.addItem(this.createItemStack(Material.ANVIL, "Créatif"));
    }
    
    private ItemStack createItemStack(Material m, String name) {
        final ItemStack item = new ItemStack(m, 1);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Open the menu GUI for a HUmanEntity
     * @param ent
     */
    public void openInventory(final HumanEntity ent) {
        ent.openInventory(this.i);
    }

    /**
     * When the user open an inventory
     * @param e
     */
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!e.getView().getTitle().equalsIgnoreCase(this.name)) return;

        e.setCancelled(true);
        final ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        final Player p = (Player) e.getWhoClicked();
        // Utiliser e.getRawSlot() pour avoir le solt, départ à 0
        p.sendMessage("Désolé, cette fonctionnalité n'est pas disponible pour le moment. ");
    }

    /**
     * Cancel dragging
     * @param e
     */
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory() == this.i) {
          e.setCancelled(true);
        }
    }
}