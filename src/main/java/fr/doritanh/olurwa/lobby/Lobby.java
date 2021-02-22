package fr.doritanh.olurwa.lobby;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.plugin.java.JavaPlugin;

import fr.doritanh.olurwa.lobby.inventory.MenuInventory;
import fr.doritanh.olurwa.lobby.listener.MessageListener;
import fr.doritanh.olurwa.lobby.listener.PlayerListener;
import fr.doritanh.olurwa.lobby.tablist.TabList;

public class Lobby extends JavaPlugin {
	private World world;
	private Location spawn;
	private TabList tablist;
	
	private static Lobby instance;
	
	public Lobby() {
		instance = this;
	}
	
    @Override
    public void onEnable() {
    	// Register events
    	this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    	this.getServer().getPluginManager().registerEvents(new MenuInventory(), this);
    	
    	this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new MessageListener());
        
        // Set spawn
		for (World w : this.getServer().getWorlds()) {
			if (w.getEnvironment() == Environment.NORMAL) {
				this.world = w;
				this.spawn = w.getSpawnLocation();
			}
		}
		this.spawn.add(0.5, 0, 0.5);
		
		// Set class
		this.tablist = new TabList();
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
    
    public World getWorld() {
    	return this.world;
    }
    
    public Location getSpawn() {
    	return this.spawn;
    }
    
    public TabList getTabList() {
    	return this.tablist;
    }
}
