package fr.doritanh.olurwa.Lobby;

import org.bukkit.plugin.java.JavaPlugin;

import fr.doritanh.olurwa.Lobby.Inventory.MenuInventory;
import fr.doritanh.olurwa.Lobby.Listener.MessageListener;

public class Lobby extends JavaPlugin {
	
	private static Lobby instance;
	
	public Lobby() {
		this.instance = this;
	}
	
    @Override
    public void onEnable() {
    	PlayerManager pm = new PlayerManager();
    	
    	// Register events
    	this.getServer().getPluginManager().registerEvents(pm, this);
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
}
