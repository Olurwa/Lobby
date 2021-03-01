package fr.doritanh.olurwa.lobby.tablist;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import fr.doritanh.olurwa.lobby.Lobby;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_16_R3.PlayerConnection;

public class TabList {

	private final String header;
	private final String footer;

	private EntityPlayer[] players;
	private final ScoreboardTab sTab;

	public TabList() {

		this.header = "§bBienvenue sur §cOlurwa §b! \\n ";
		this.footer = "";// "§7Vous êtes sur le lobby.";

		this.sTab = new ScoreboardTab();
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

	public void requestUpdateServers() {
		Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
		if (player == null)
			return;

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

	public void updateLobby() {
		this.sTab.updateLobby();
	}

	public void updateCreative(String[] playersNames) {
		this.sTab.updateCreative(playersNames);
	}

	public void send(Player packetReceiver) {
		PlayerConnection pc = ((CraftPlayer) packetReceiver).getHandle().playerConnection;
		// Remove old players
		pc.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, this.sTab.getEntityRemoved()));
		// Set scoreboard to the packetreceiver
		packetReceiver.setScoreboard(this.sTab.getScoreboard());
		// Get all players and send them
		pc.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, this.sTab.getEntityPlayers()));
	}

}
