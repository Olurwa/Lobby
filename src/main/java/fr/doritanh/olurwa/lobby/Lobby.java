package fr.doritanh.olurwa.lobby;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import fr.doritanh.olurwa.lobby.commands.HelpCommand;
import fr.doritanh.olurwa.lobby.inventory.MenuInventory;
import fr.doritanh.olurwa.lobby.listener.BungeeMessageListener;
import fr.doritanh.olurwa.lobby.listener.CoreMessageListener;
import fr.doritanh.olurwa.lobby.listener.PlayerListener;

public class Lobby extends JavaPlugin {
	private World world;
	private Location spawn;

	private static Lobby instance;

	private CommandExecutor helpCommand;

	public Lobby() {
		instance = this;
	}

	@Override
	public void onEnable() {
		// Register commands
		this.helpCommand = new HelpCommand();
		this.getCommand("help").setExecutor(this.helpCommand);

		// Register events
		this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		this.getServer().getPluginManager().registerEvents(new MenuInventory(), this);

		// Register channels
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeMessageListener());
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "olurwa:core");
		this.getServer().getMessenger().registerIncomingPluginChannel(this, "olurwa:core", new CoreMessageListener());

		// Set spawn
		for (World w : this.getServer().getWorlds()) {
			if (w.getEnvironment() == Environment.NORMAL) {
				this.world = w;
				this.spawn = w.getSpawnLocation();
			}
		}
		this.spawn.add(0.5, 0, 0.5);
	}

	@Override
	public void onDisable() {
	}

	/**
	 * Get an instance of lobby
	 * 
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

	public void sendToServer(Player player, String serverName) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(serverName);

		player.sendPluginMessage(this, "BungeeCord", out.toByteArray());
	}
}
