package fr.doritanh.olurwa.Lobby;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class PlayerManager implements Listener {
	
	/**
	 * When player login
	 * @param e
	 */
	@EventHandler
	public void onPlayerLogin(PlayerJoinEvent e) {
		e.setJoinMessage(ChatColor.LIGHT_PURPLE + "Salut, " + ChatColor.GREEN + e.getPlayer().getName() + " !");
		e.getPlayer().getInventory().clear();
		
		final ItemStack itemMenu = new ItemStack(Material.COMPASS, 1);
		final ItemMeta meta = itemMenu.getItemMeta();
		meta.setDisplayName("Lobby menu");
		itemMenu.setItemMeta(meta);
		e.getPlayer().getInventory().addItem(itemMenu);
	}
	
	/**
	 * When player interact
	 * @param e
	 */
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getItem() != null && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("Lobby menu")) {
				MenuGUI m = new MenuGUI();
				m.openInventory(e.getPlayer());
			}
		}
	}
	
	/**
	 * Disable player drop
	 * @param e
	 */
	@EventHandler
	public void onPlayerDrop(PlayerDropItemEvent e) {
		e.setCancelled(true);
	}
	
	/**
	 * Disable player food
	 * @param e
	 */
	@EventHandler
	public void onPlayerLooseFood(FoodLevelChangeEvent e) {
		if (e.getEntityType() == EntityType.PLAYER) {
			e.setCancelled(true);
		}
	}
}
