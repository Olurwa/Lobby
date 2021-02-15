package fr.doritanh.olurwa.Lobby.inventory;

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

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import fr.doritanh.olurwa.Lobby.Lobby;

public class MenuInventory implements Listener {
	private String name;
    private final Inventory i;

    public MenuInventory() {
    	this.name = "Olurwa Menu";
        this.i = Bukkit.createInventory(null, 9, this.name);        

        //this.i.addItem(this.createItemStack(Material.GRASS_BLOCK, "Survie"));
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
        switch (e.getRawSlot()) {
        case 0:
        	try {
          	  ByteArrayDataOutput out = ByteStreams.newDataOutput();
          	  out.writeUTF("Connect");
          	  out.writeUTF("creative");

          	  p.sendPluginMessage(Lobby.get(), "BungeeCord", out.toByteArray());
        	} catch (Exception error) {
        		System.out.println("Something didn't work. Is BungeeCoord installed ?");
        	}
        	break;
        default:
            p.sendMessage("D�sol�, cette fonctionnalit� n'est pas disponible pour le moment. ");
            break;
        }
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