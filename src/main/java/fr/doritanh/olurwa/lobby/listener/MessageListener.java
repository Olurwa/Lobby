package fr.doritanh.olurwa.lobby.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import fr.doritanh.olurwa.lobby.Lobby;

public class MessageListener implements PluginMessageListener {

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("BungeeCord")) {
			return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();
		System.out.println("Passage message recu");
		if (subchannel.equals("PlayerList")) {
			System.out.println("COMING PLAYERLIST");
			String server = in.readUTF();
			String[] playerList = in.readUTF().split(", ");
			if (server == "creative") {
				Lobby.get().getTabList().updateCreative(playerList);
			}
			for (Player p : Bukkit.getOnlinePlayers()) {
				Lobby.get().getTabList().send(p);
			}
		}
	}

}
