package fr.doritanh.olurwa.Lobby;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import fr.doritanh.olurwa.Lobby.inventory.MenuInventory;
import fr.doritanh.olurwa.Lobby.listener.MessageListener;
import fr.doritanh.olurwa.Lobby.listener.PlayerListener;

public class Lobby extends JavaPlugin {
	private Location spawn;
	
	private static Lobby instance;
	
	public Lobby() {
		instance = this;
		
		World wspawn = this.getServer().getWorld("world");
		this.spawn = wspawn.getSpawnLocation();
		this.spawn.add(0.5, 0, 0.5);
	}
	
    @Override
    public void onEnable() {
    	// Register events
    	this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    	this.getServer().getPluginManager().registerEvents(new MenuInventory(), this);
    	
    	this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new MessageListener());
    }
    
    @Override
    public void onDisable() { }
    
    /**
     * Get an instance of lobby
     * @return
     */
    public static Lobby get() {
        return Lobby.instance;
    }
    
    public Location getSpawn() {
    	return this.spawn;
    }
}
