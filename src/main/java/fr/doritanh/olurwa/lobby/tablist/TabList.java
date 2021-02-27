package fr.doritanh.olurwa.lobby.tablist;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import fr.doritanh.olurwa.lobby.Lobby;
import net.minecraft.server.v1_16_R3.ChatComponentText;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_16_R3.MinecraftServer;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_16_R3.PlayerConnection;
import net.minecraft.server.v1_16_R3.PlayerInteractManager;
import net.minecraft.server.v1_16_R3.WorldServer;

public class TabList {
	private final int MAX_SIZE = 80;
	private final int MAX_PLAYERS_COLUMN = 18;

	private final MinecraftServer server;
	private final WorldServer worldserver;
	private final PlayerInteractManager playerinteractmanager;

	private final String header;
	private final String footer;

	private final String baseTexture;
	private final String baseSignature;

	private EntityPlayer[] players;
	private final Scoreboard sTab;
	private final Team tLobbyTitle;
	private final Team tLobbyPlayers;
	private final Team tCreativeTitle;
	private final Team tCreativePlayers;
	private final Team tSurvivalTitle;
	private final Team tSurvivalPlayers;
	private final Team tOthersTitle;
	private final Team tOthersPlayers;
	private final Team tRemoveLobby;
	private final Team tRemoveCreative;
	private final Team tRemoveSurvival;
	private final Team tRemoveOthers;

	public TabList() {
		this.server = ((CraftServer) Bukkit.getServer()).getServer();
		this.worldserver = ((CraftWorld) Lobby.get().getWorld()).getHandle();
		this.playerinteractmanager = new PlayerInteractManager(worldserver);

		this.header = "§bBienvenue sur §cOlurwa §b! \\n ";
		this.footer = "";// "§7Vous êtes sur le lobby.";

		this.sTab = Bukkit.getScoreboardManager().getNewScoreboard();
		tLobbyTitle = sTab.registerNewTeam("a_tab");
		tLobbyPlayers = sTab.registerNewTeam("b_tab");
		tCreativeTitle = sTab.registerNewTeam("c_tab");
		tCreativePlayers = sTab.registerNewTeam("d_tab");
		tSurvivalTitle = sTab.registerNewTeam("e_tab");
		tSurvivalPlayers = sTab.registerNewTeam("f_tab");
		tOthersTitle = sTab.registerNewTeam("g_tab");
		tOthersPlayers = sTab.registerNewTeam("h_tab");
		tRemoveLobby = sTab.registerNewTeam("w_tab");
		tRemoveCreative = sTab.registerNewTeam("x_tab");
		tRemoveSurvival = sTab.registerNewTeam("y_tab");
		tRemoveOthers = sTab.registerNewTeam("z_tab");

		this.baseTexture = "ewogICJ0aW1lc3RhbXAiIDogMTYxMzk0NTE0NTgyNCwKICAicH"
				+ "JvZmlsZUlkIiA6ICJmYzUwMjkzYTVkMGI0NzViYWYwNDJhNzIwMWJhMzBkM"
				+ "iIsCiAgInByb2ZpbGVOYW1lIiA6ICJDVUNGTDE3IiwKICAic2lnbmF0dXJl"
				+ "UmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4"
				+ "iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYW"
				+ "Z0Lm5ldC90ZXh0dXJlL2RlM2ZiODA5MmI3ZWExZmJlOTBhODE5NzVhYWVkY"
				+ "mE2NWVlZGUwNGZjNGY3ODg0MTk3ODY0ZGI3YzZmYTgxNyIsCiAgICAgICJt"
				+ "ZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB" + "9CiAgICB9CiAgfQp9";
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
			name = "zzzzzzzzzzzzzz" + name;
			GameProfile profile = new GameProfile(UUID.randomUUID(), name);
			profile.getProperties().put("textures", new Property("textures", this.baseTexture, this.baseSignature));
			this.players[i] = new EntityPlayer(server, worldserver, profile, playerinteractmanager);
			this.players[i].listName = new ChatComponentText("");
			this.players[i].ping = 1000;
		}

		this.players[0].listName = new ChatComponentText("§lLobby");
		this.players[20].listName = new ChatComponentText("§lCréatif");
		this.players[40].listName = new ChatComponentText("§lSurvie");
		this.players[60].listName = new ChatComponentText("§lAutre");
		tLobbyTitle.addEntry(this.players[0].getName());
		tLobbyTitle.addEntry(this.players[1].getName());
		tCreativeTitle.addEntry(this.players[20].getName());
		tCreativeTitle.addEntry(this.players[21].getName());
		tSurvivalTitle.addEntry(this.players[40].getName());
		tSurvivalTitle.addEntry(this.players[41].getName());
		tOthersTitle.addEntry(this.players[60].getName());
		tOthersTitle.addEntry(this.players[61].getName());

		for (int i = 2; i < 20; i++) {
			tLobbyPlayers.addEntry(this.players[i].getName());
		}
		for (int i = 22; i < 40; i++) {
			tCreativePlayers.addEntry(this.players[i].getName());
		}
		for (int i = 42; i < 60; i++) {
			tSurvivalPlayers.addEntry(this.players[i].getName());
		}
		for (int i = 62; i < 80; i++) {
			tOthersPlayers.addEntry(this.players[i].getName());
		}
	}

	private void reset() {
		for (String entry : tSurvivalPlayers.getEntries()) {
			tSurvivalPlayers.removeEntry(entry);
		}
		for (String entry : tOthersPlayers.getEntries()) {
			tOthersPlayers.removeEntry(entry);
		}
		for (int i = 42; i < 60; i++) {
			tSurvivalPlayers.addEntry(this.players[i].getName());
		}
		for (int i = 62; i < 80; i++) {
			tOthersPlayers.addEntry(this.players[i].getName());
		}
	}

	/**
	 * Send the tablist header and footer to player.
	 * 
	 * @param p - The player that will receive the packet.
	 */
	public void sendHeaderFooter(Player p) {
		IChatBaseComponent titleHeader = ChatSerializer.a("{\"text\": \"" + this.header + "\"}");
		IChatBaseComponent titleFooter = ChatSerializer.a("{\"text\": \"" + this.footer + "\"}");
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

	private void resetLobby() {
		// Remove players in lobby and players in tOthers
		for (String entry : tLobbyPlayers.getEntries()) {
			tLobbyPlayers.removeEntry(entry);
		}
		for (String entry : tRemoveLobby.getEntries()) {
			tRemoveLobby.removeEntry(entry);
		}
		// Add false players to lobby
		for (int i = 2; i < 20; i++) {
			tLobbyPlayers.addEntry(this.players[i].getName());
		}
	}

	public void updateLobby() {
		this.resetLobby();

		int count = 2;
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (count < 20) {
				tLobbyPlayers.removeEntry(this.players[count].getName());
				tRemoveLobby.addEntry(this.players[count].getName());
				tLobbyPlayers.addEntry(p.getName());
			} else {
				tRemoveLobby.addEntry(p.getName());
			}
			count++;
		}
	}

	public void updateLobby(Player playerQuitting) {
		this.resetLobby();

		int count = 2;
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getName() == playerQuitting.getName())
				continue;
			if (count < 20) {
				tLobbyPlayers.removeEntry(this.players[count].getName());
				tRemoveLobby.addEntry(this.players[count].getName());
				tLobbyPlayers.addEntry(p.getName());
			} else {
				tRemoveLobby.addEntry(p.getName());
			}
			count++;
		}
	}

	/**
	 * Update the current tablist with players on creative
	 * 
	 * @param playersNames - Names of players
	 */
	public void updateCreative(String[] playersNames) {
		// Clean creative teams
//		for (String entry : tCreativePlayers.getEntries()) {
//			tCreativePlayers.removeEntry(entry);
//		}
//		for (String entry : tRemoveCreative.getEntries()) {
//			tRemoveCreative.removeEntry(entry);
//		}
//		// Add false players to creative team
//		for (int i = 22; i < 40; i++) {
//			tCreativePlayers.addEntry(this.players[i].getName());
//		}
		for (int i = 22; i < 40; i++) {
			this.players[i].listName = new ChatComponentText("");
		}

		int count = 22;
		for (String name : playersNames) {
			if (name.equalsIgnoreCase(""))
				continue;
			if (count < 40) {
//				@SuppressWarnings("deprecation")
//				UUID uuid = Bukkit.getOfflinePlayer(name).getUniqueId();
//				GameProfile profile = new GameProfile(uuid, name);
//				profile.getProperties().put("textures", new Property("textures", this.baseTexture, this.baseSignature));
//				EntityPlayer p = new EntityPlayer(server, worldserver, profile, playerinteractmanager);
				this.players[count].listName = new ChatComponentText(name);

//				tCreativePlayers.removeEntry(this.players[count].getName());
//				tRemoveCreative.addEntry(this.players[count].getName());
//				tCreativePlayers.addEntry(name);
			}
			count++;
		}
	}

	public void requestUpdateServers() {
		Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);

		// Creative
		ByteArrayDataOutput creative = ByteStreams.newDataOutput();
		creative.writeUTF("PlayerList");
		creative.writeUTF("creative");
		player.sendPluginMessage(Lobby.get(), "BungeeCord", creative.toByteArray());
		// Survival
		ByteArrayDataOutput survival = ByteStreams.newDataOutput();
		survival.writeUTF("PlayerList");
		survival.writeUTF("survival");
		player.sendPluginMessage(Lobby.get(), "BungeeCord", survival.toByteArray());
	}

	public void send(Player packetReceiver) {
		PlayerConnection pc = ((CraftPlayer) packetReceiver).getHandle().playerConnection;
		pc.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, this.players));
		packetReceiver.setScoreboard(sTab);
		pc.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, this.players));
	}

}
