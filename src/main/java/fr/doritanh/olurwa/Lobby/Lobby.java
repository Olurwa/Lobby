package fr.doritanh.olurwa.Lobby;

import org.bukkit.plugin.java.JavaPlugin;

public class Lobby extends JavaPlugin {
    @Override
    public void onEnable() {
    	this.getServer().getPluginManager().registerEvents(new PlayerManager(), this);
    	this.getServer().getPluginManager().registerEvents(new MenuGUI(), this);
    }
    
    @Override
    public void onDisable() { }
}
