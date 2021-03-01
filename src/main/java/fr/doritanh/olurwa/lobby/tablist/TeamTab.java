package fr.doritanh.olurwa.lobby.tablist;

import java.util.ArrayList;

import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TeamTab {

	private final Team tTitle;
	private final Team tPlayers;

	private final ArrayList<EntityPlayerTab> titlePlayers;
	private final ArrayList<EntityPlayerTab> players;

	private ArrayList<EntityPlayerTab> removed;

	public TeamTab(Scoreboard sb, int index, String name) {
		String pos = " ";
		if (index < 10) {
			pos += "0";
		}
		pos += String.valueOf(index);
		this.tTitle = sb.registerNewTeam(pos + "0_tab");
		this.tPlayers = sb.registerNewTeam(pos + "1_tab");

		this.titlePlayers = new ArrayList<EntityPlayerTab>();
		this.titlePlayers.add(new EntityPlayerTab(" " + pos + "0_tab", name));
		this.titlePlayers.add(new EntityPlayerTab(" " + pos + "1_tab", ""));

		this.tTitle.addEntry(this.titlePlayers.get(0).getPlayer().getName());
		this.tTitle.addEntry(this.titlePlayers.get(1).getPlayer().getName());

		this.players = new ArrayList<EntityPlayerTab>();
		this.removed = new ArrayList<EntityPlayerTab>();
	}

	public ArrayList<EntityPlayerTab> getPlayers() {
		ArrayList<EntityPlayerTab> ept = new ArrayList<EntityPlayerTab>();
		ept.addAll(titlePlayers);
		ept.addAll(players);
		return ept;
	}

	public ArrayList<EntityPlayerTab> getRemoved() {
		return this.removed;
	}

	@SuppressWarnings("unchecked")
	public void clear() {
		this.removed = (ArrayList<EntityPlayerTab>) this.players.clone();
		this.players.clear();
		for (String entry : this.tPlayers.getEntries()) {
			this.tPlayers.removeEntry(entry);
		}
	}

	public void add(EntityPlayerTab ept) {
		this.tPlayers.addEntry(ept.getPlayer().getName());
	}

}
