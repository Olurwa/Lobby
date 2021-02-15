package fr.doritanh.olurwa.Lobby.listener;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import fr.doritanh.olurwa.Lobby.inventory.MenuInventory;
import fr.doritanh.olurwa.Lobby.listener.PlayerListener;
import net.md_5.bungee.api.ChatColor;

public class PlayerListener implements Listener {
	
	private void sendMenu(Player p) {
		final ItemStack itemMenu = new ItemStack(Material.CLOCK, 1);
		final ItemMeta meta = itemMenu.getItemMeta();
		meta.setDisplayName("Lobby menu");
		itemMenu.setItemMeta(meta);
		p.getInventory().addItem(itemMenu);
	}
	
	/**
	 * When player login
	 * @param e
	 */
	@EventHandler
	public void onPlayerLogin(PlayerJoinEvent e) {
		e.setJoinMessage(null);
		e.getPlayer().getInventory().clear();
		
		this.sendMenu(e.getPlayer());
	}
	
	/**
	 * When player disconnect
	 * @param e
	 */
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		e.setQuitMessage(null);
	}
	
	/**
	 * When player interact
	 * @param e
	 */
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getItem() != null && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("Lobby menu")) {
				MenuInventory m = new MenuInventory();
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
		if (e.getEntityType() != EntityType.PLAYER) return;
		e.setFoodLevel(20);
		// e.setCancelled(true);
	}
	
	/**
	 * Disable player damage
	 * @param e
	 */
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e) {
		if (e.getCause() == DamageCause.DROWNING 
				|| e.getCause() == DamageCause.FALL
				|| e.getCause() == DamageCause.FIRE) {
			e.setCancelled(true);
		}
	}
	
	/**
	 * Player respawn
	 * @param e
	 */
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		e.setRespawnLocation(e.getPlayer().getWorld().getSpawnLocation());
		this.sendMenu(e.getPlayer());
	}
	
	/**
	 * Delete menu when player die
	 * @param e
	 */
	@EventHandler
	public void onPlayerDie(EntityDeathEvent e) {
		if (e.getEntityType() == EntityType.PLAYER) {
			Player p = (Player) e.getEntity();
			p.getInventory().clear();
		}
	}
}
