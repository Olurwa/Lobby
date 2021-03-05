package fr.doritanh.olurwa.lobby.inventory;

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

import fr.doritanh.olurwa.lobby.Lobby;
import net.kyori.adventure.text.Component;

public class MenuInventory implements Listener {
	private String name;
	private final Inventory i;

	public MenuInventory() {
		this.name = "Olurwa Menu";
		this.i = Bukkit.createInventory(null, 27, this.name);

		this.i.setItem(10, this.createItemStack(Material.ANVIL, "Créatif"));
		this.i.setItem(11, this.createItemStack(Material.GRASS_BLOCK, "Survie"));
	}

	private ItemStack createItemStack(Material m, String name) {
		final ItemStack item = new ItemStack(m, 1);
		final ItemMeta meta = item.getItemMeta();
		// meta.setDisplayName(name);
		meta.displayName(Component.text("Lobby menu"));
		item.setItemMeta(meta);
		return item;
	}

	/**
	 * Open the menu GUI for a HUmanEntity
	 * 
	 * @param ent
	 */
	public void openInventory(final HumanEntity ent) {
		ent.openInventory(this.i);
	}

	/**
	 * When the user open an inventory
	 * 
	 * @param e
	 */
	@EventHandler
	public void onInventoryClick(final InventoryClickEvent e) {
		if (!e.getView().getTitle().equalsIgnoreCase(this.name))
			return;

		e.setCancelled(true);
		final ItemStack clickedItem = e.getCurrentItem();
		if (clickedItem == null || clickedItem.getType() == Material.AIR)
			return;

		final Player p = (Player) e.getWhoClicked();
		switch (e.getRawSlot()) {
		case 10:
			try {
				ByteArrayDataOutput out = ByteStreams.newDataOutput();
				out.writeUTF("Connect");
				out.writeUTF("creative");

				p.sendPluginMessage(Lobby.get(), "BungeeCord", out.toByteArray());
			} catch (Exception error) {
				System.out.println("Le serveur créatif semble down.");
				p.sendMessage("Le serveur créatif ne seùbme pas joignable.");
				;
			}
			break;
		case 11:
			try {
				ByteArrayDataOutput out = ByteStreams.newDataOutput();
				out.writeUTF("Connect");
				out.writeUTF("survival");

				p.sendPluginMessage(Lobby.get(), "BungeeCord", out.toByteArray());
			} catch (Exception error) {
				System.out.println("Le serveur survival semble down.");
				p.sendMessage("Le serveur créatif ne semble pas joignable.");
				;
			}
			break;
		default:
			p.sendMessage("Désolé, cette fonctionnalité n'est pas disponible pour le moment. ");
			break;
		}
	}

	/**
	 * Cancel dragging
	 * 
	 * @param e
	 */
	@EventHandler
	public void onInventoryClick(final InventoryDragEvent e) {
		if (e.getInventory() == this.i) {
			e.setCancelled(true);
		}
	}

	public static void sendMenu(Player p) {
		final ItemStack itemMenu = new ItemStack(Material.CLOCK, 1);
		final ItemMeta meta = itemMenu.getItemMeta();
		meta.setDisplayName("Lobby menu");
		itemMenu.setItemMeta(meta);
		p.getInventory().addItem(itemMenu);
	}
}