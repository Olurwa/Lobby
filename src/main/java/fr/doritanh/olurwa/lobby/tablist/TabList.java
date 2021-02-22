package fr.doritanh.olurwa.lobby.tablist;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import fr.doritanh.olurwa.lobby.Lobby;
import net.minecraft.server.v1_16_R3.ChatComponentText;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.DataWatcherRegistry;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.MinecraftServer;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_16_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_16_R3.PlayerConnection;
import net.minecraft.server.v1_16_R3.PlayerInteractManager;
import net.minecraft.server.v1_16_R3.WorldServer;
import net.minecraft.server.v1_16_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;

public class TabList {
	private final int MAX_SIZE = 80;
	
	private final MinecraftServer server;
	private final WorldServer worldserver;
	private final PlayerInteractManager playerinteractmanager;
	
	private final String header;
	private final String footer;
	
	private final String baseTexture;
	private final String baseSignature;
	
	private EntityPlayer[] players;
	
	public TabList() {
		this.server = ((CraftServer) Bukkit.getServer()).getServer();
		this.worldserver = ((CraftWorld) Lobby.get().getWorld()).getHandle();
		this.playerinteractmanager = new PlayerInteractManager(worldserver);
		
		this.header = "§bBienvenue sur §cOlurwa §b! \\n ";
		this.footer = "";//"§7Vous êtes sur le lobby.";
		
		this.baseTexture = "ewogICJ0aW1lc3RhbXAiIDogMTYxMzk0NTE0NTgyNCwKICAicH"
				+ "JvZmlsZUlkIiA6ICJmYzUwMjkzYTVkMGI0NzViYWYwNDJhNzIwMWJhMzBkM"
				+ "iIsCiAgInByb2ZpbGVOYW1lIiA6ICJDVUNGTDE3IiwKICAic2lnbmF0dXJl"
				+ "UmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4"
				+ "iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYW"
				+ "Z0Lm5ldC90ZXh0dXJlL2RlM2ZiODA5MmI3ZWExZmJlOTBhODE5NzVhYWVkY"
				+ "mE2NWVlZGUwNGZjNGY3ODg0MTk3ODY0ZGI3YzZmYTgxNyIsCiAgICAgICJt"
				+ "ZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB"
				+ "9CiAgICB9CiAgfQp9";
		this.baseSignature = "WkELIZgnBpj9LjubOPJKWvlnelrf85R8QA38H8aJN3r7i1fH"
				+ "dSZgC+5Zq9ej/ZMxv/2iWhJLdf05MpHTEIfbPiHlb4kV+kN0y3S7b0wdUwc"
				+ "6HqsETTclNneDLT0tDBm7IcqOwQw/IP2eRJF/xIPvuX3DzRkJUme6ygRAuS"
				+ "2YIEXSEd1gz8JzYAYvpPuNEfD0IL8lrZ1h5LPOEAtPUxnKbd8HIj8hx6kcw"
				+ "jLrz42Dqqyh6yzPBkGAEXA+1zff2kmSNjA4JJKrirdgoAsaA9EXh2FTman8"
				+ "4c4DLpIARJyIuKdbkD1XozPGipzzv9AvJT8yzf311bgH4J8m9mt7IY4Reaq"
				+ "h8ktwRZLsBlnLsjK4/ai/UnZ5FgWfVefw1pHAGfLu0s2Z3fPCgwJBxv1a7P"
				+ "wjefzm+6SdzgoFn1j5Ocx+3TIjEwrGJtOc07vdHJyVaBs57iEXHe3yTDoWZ"
				+ "f8NgsaO7vq/tLOugWnSNtTH+C+FRNTLMoK1DIHheCvJV1eEFeJBTPOehv2N"
				+ "QeXZWnmv5TR0KLf7/z5NcMD7yyoIjDxWYETp+RcsRYEymA/6LiBcwzDdmGG"
				+ "Es8C/qwYqfvko0hn8eXdZvfvKltjVQSqqo5GX5jfhbnS0hnBzJ3DlrwVN2k"
				+ "YvldnZqdstngG6d1b9YxtBUa+DkQhZdDLTalePc+OfJ0g=";
		
		this.players = new EntityPlayer[MAX_SIZE];

		for (int i = 0; i < MAX_SIZE; i++) {
			String name = String.valueOf(i);
			if (name.length() < 2) {
				name = "0" + name;
			}
			name = " " + name;
			GameProfile profile = new GameProfile(UUID.randomUUID(), name);
			profile.getProperties().put("textures", new Property("textures", this.baseTexture, this.baseSignature));
			this.players[i] = new EntityPlayer(server, worldserver, profile, playerinteractmanager);
			this.players[i].listName = new ChatComponentText("");
			this.players[i].ping = 1000;
		}
	}

	/**
	 * Send the tablist header and footer to player
	 * @param p
	 */
	public void sendHeaderFooter(Player p) {
		IChatBaseComponent titleHeader = ChatSerializer.a("{\"text\": \"" + this.header + "\"}");
        IChatBaseComponent titleFooter = ChatSerializer.a("{\"text\": \""+ this.footer + "\"}");
        PacketPlayOutPlayerListHeaderFooter pHeaderFooter = new PacketPlayOutPlayerListHeaderFooter();
        try {
        	pHeaderFooter.header = titleHeader;
        	pHeaderFooter.footer = titleFooter;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(pHeaderFooter);
        }
	}

	private void clear(Player packetReceiver) {
		Packet packet = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, this.players);
		((CraftPlayer) packetReceiver).getHandle().playerConnection.sendPacket(packet);
	}
	
	public void update(Player packetReceiver) {
		this.clear(packetReceiver);
		
		PlayerConnection pc = ((CraftPlayer) packetReceiver).getHandle().playerConnection;
		
		this.players[0].listName = new ChatComponentText("§lLobby");
		this.players[20].listName = new ChatComponentText("§lSurvie");
		this.players[40].listName = new ChatComponentText("§lCréatif");
		this.players[60].listName = new ChatComponentText("§lAutre");
		
		// Players in lobby
		int i = 2;
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (i < 20) {
				final EntityPlayer pNMS = ((CraftPlayer) p).getHandle();
				// Set display name
				this.players[i].listName = new ChatComponentText(p.getName());
				// Set ping
				this.players[i].ping = pNMS.ping;
				// Set texture
				Property prop = pNMS.getProfile().getProperties().get("textures").iterator().next();
				this.players[i].getProfile().getProperties().clear();
				this.players[i].getProfile().getProperties().put("textures", new Property("textures", prop.getValue(), prop.getSignature()));
				
				Location loc = p.getLocation();
				//this.players[i].setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
				this.players[i].setLocation(104, 92, 40, loc.getYaw(), loc.getPitch());
				
				i++;
			}
		}

		pc.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, this.players));
		for (EntityPlayer p : this.players) {
//			pc.sendPacket(new PacketPlayOutNamedEntitySpawn(p));
			DataWatcher watcher = p.getDataWatcher();
	        watcher.set(new DataWatcherObject<>(16, DataWatcherRegistry.a), (byte)127);
	        pc.sendPacket(new PacketPlayOutEntityMetadata(p.getId(), watcher, true));
		}
		
	}
	
	
//	/**
//	 * Remove all players from the packetReceiver tablist.
//	 * @param packetReceveiver The player that will get the packet.
//	 */
//	public void removePlayers(Player packetReceveiver) {
//		// Getting all players and cast them to EntityPlayer
//		Collection<? extends Player> players = Bukkit.getOnlinePlayers();
//        EntityPlayer[] playersNMS = new EntityPlayer[players.size()];
//        int current = 0;
//        for (Player player : players) {
//            playersNMS[current] = ((CraftPlayer) player).getHandle();
//            current++;
//        }
//        System.out.println(playersNMS.toString());
//        // Prepare the packet and send it
//        Packet packet = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, playersNMS);
//        ((CraftPlayer) packetReceveiver).getHandle().playerConnection.sendPacket(packet);
//	}
//	
//	public void addPlayers(Player packetReceiver) {
//		MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
//		WorldServer worldserver = ((CraftWorld) Lobby.get().getWorld()).getHandle();
//		PlayerInteractManager playerinteractmanager = new PlayerInteractManager(worldserver);
//		
//		EntityPlayer[] players = new EntityPlayer[MAX_SIZE];
//		for (int i = 0; i < MAX_SIZE; i++) {
//			// GameProfile profile = new GameProfile(UUID.randomUUID(), "name");
//			// profile.getProperties().put("textures", new Property("textures", texture, signature));
//			String name = "name";
//			if (i < 10) {
//				name += "0" + String.valueOf(i);
//			} else {
//				name += String.valueOf(i);
//			}
//			players[i] = new EntityPlayer(server, worldserver, new GameProfile(UUID.randomUUID(), name), playerinteractmanager);
//			players[i].listName = new ChatComponentText(name);
//		}
//		
//		Packet packet = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, players);
//		((CraftPlayer) packetReceiver).getHandle().playerConnection.sendPacket(packet);
//	}
//	
//	public Property getTextureSignature(String playerName) {
//		return new Property("", "");
//	}
	
}
