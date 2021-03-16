package fr.doritanh.olurwa.lobby.inventory;

import java.util.HashMap;

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

import fr.doritanh.olurwa.lobby.Lobby;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class MenuInventory implements Listener {
	public final static String name = "Menu";
	private final Inventory i;
	private final HashMap<String, Integer> invSlots;

	public MenuInventory() {
		this.i = Bukkit.createInventory(null, 27, Component.text(name));
		this.invSlots = new HashMap<String, Integer>();
		this.invSlots.put("creative", 11);
		this.invSlots.put("survival", 13);
		this.invSlots.put("frontier", 15);

		this.i.setItem(this.invSlots.get("creative"), this.createCreativeStack());
		this.i.setItem(this.invSlots.get("survival"), this.createSurvivalStack());
		this.i.setItem(this.invSlots.get("frontier"), this.createFrontierStack());
	}

	private ItemStack createCreativeStack() {
		final ItemStack item = new ItemStack(Material.CYAN_WOOL, 1);
		final ItemMeta meta = item.getItemMeta();

		Component name = Component.text("Créatif").color(TextColor.color(66, 211, 221)).decorate(TextDecoration.BOLD);
		meta.displayName(name);

		item.setItemMeta(meta);
		return item;
	}

	private ItemStack createSurvivalStack() {
		final ItemStack item = new ItemStack(Material.NETHERITE_PICKAXE, 1);
		final ItemMeta meta = item.getItemMeta();

		Component name = Component.text("Survie").color(TextColor.color(70, 232, 64)).decorate(TextDecoration.BOLD);
		meta.displayName(name);

		item.setItemMeta(meta);
		return item;
	}

	private ItemStack createFrontierStack() {
		final ItemStack item = new ItemStack(Material.GOLD_INGOT, 1);
		final ItemMeta meta = item.getItemMeta();

		Component name = Component.text("Frontier").color(TextColor.color(239, 200, 71)).decorate(TextDecoration.BOLD);
		meta.displayName(name);

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
		TextComponent title = (TextComponent) e.getView().title();
		if (!title.content().equalsIgnoreCase(name))
			return;

		e.setCancelled(true);
		final ItemStack clickedItem = e.getCurrentItem();
		if (clickedItem == null || clickedItem.getType() == Material.AIR)
			return;

		final Player p = (Player) e.getWhoClicked();
		if (e.getRawSlot() == this.invSlots.get("creative")) {
			Lobby.get().sendToServer(p, "creative");
		} else if (e.getRawSlot() == this.invSlots.get("survival")) {
			Lobby.get().sendToServer(p, "survival");
		} else if (e.getRawSlot() == this.invSlots.get("frontier")) {
			Lobby.get().sendToServer(p, "frontier");
		}
	}

	/**
	 * Cancel dragging
	 * 
	 * @param e
	 */
	@EventHandler
	public void onInventoryClick(final InventoryDragEvent e) {
		if (((TextComponent) e.getView().title()).content().equalsIgnoreCase(name)) {
			e.setCancelled(true);
		}
	}

	public static void sendMenu(Player p) {
		final ItemStack itemMenu = new ItemStack(Material.CLOCK, 1);
		final ItemMeta meta = itemMenu.getItemMeta();
		meta.displayName(Component.text(name));
		itemMenu.setItemMeta(meta);
		p.getInventory().addItem(itemMenu);
	}
}