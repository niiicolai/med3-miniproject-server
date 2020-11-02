package org.example.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Lobby {

	private int id = 0;
	private static final String lobbyJoinMsg = "Please provide an ID";
	private static final String lobbyjoinedMsg = "joined the lobby";
	private static final String lobbyWelcomeMsg = "Welcome to lobby ";
	private static final String lobbyNotFoundMsg = "Lobby not found!";
	private static final String lobbiesNotFoundMsg = "No lobbies were found!";
	private static final String lobbyIDMsg = "Lobby ID: ";
	private static final String lobbyNotInMsg = "Currently not in a lobby";
	private static final String lobbyLeftMsg = "left the lobby";
	private static final String lobbyQuitMsg = "Lobby was quitted!";
	private static final String lobbiesCurrent = "Current lobbies: ";

	private static final String newLine = "#n";

	public ArrayList<Handler> handlers = new ArrayList<Handler>();

	public static int nextID = 0;
	public static ArrayList<Lobby> lobbies = new ArrayList<Lobby>();

	public Lobby(int id) {
		this.id = id;
	}

	public int getID() {
		return id;
	}

	public ArrayList<Handler> getHandlers() {
		return handlers;
	}

	public void broadcast(Handler sender, String msg) {
		var message = sender.getFormattedName() + "(" + Date() + ")" + ": " + msg;

		try {
			for (Handler handler : handlers) {
				handler.message(message);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String join(String str, Handler handler) {
		try {
			var regex = "\\D+";
			var numberStr = str.replaceAll(regex, "");
			var id = Integer.parseInt(numberStr);
			return join(id, handler);
		} catch (NumberFormatException e) {
			return lobbyJoinMsg;
		}
	}

	public static String join(int id, Handler handler) {
		Lobby newLobby = null;
		for (int i = 0; i < lobbies.size(); i++) {
			var iLobby = lobbies.get(i);
			if (iLobby.getID() == id) {
				newLobby = iLobby;
				break;
			}
		}

		if (newLobby != null) {
			newLobby.broadcast(handler, lobbyjoinedMsg);

			handler.setLobby(newLobby);
			newLobby.getHandlers().add(handler);
			return lobbyWelcomeMsg + newLobby.getID();
		} else {
			return lobbyNotFoundMsg;
		}
	}

	public static String find() {

		if (lobbies.size() == 0) {
			return lobbiesNotFoundMsg;
		}

		var newLineSymbol = System.getProperty("line.separator");
		var strBuilder = new StringBuilder();
		strBuilder.append(newLine);
		strBuilder.append(lobbiesCurrent + newLine);
		for (int i = 0; i < lobbies.size(); i++) {
			strBuilder.append(lobbyIDMsg);
			strBuilder.append(lobbies.get(i).getID());
			strBuilder.append(newLine);
		}

		return strBuilder.toString();
	}

	public static String create(Handler handler) {
		var lobby = new Lobby(nextID);
		lobbies.add(lobby);
		handler.setLobby(lobby);
		lobby.getHandlers().add(handler);
		nextID++;

		return lobbyWelcomeMsg + lobby.getID();
	}

	public static String leave(Handler handler) {
		var currentLobby = handler.getLobby();
		if (currentLobby == null)
			return lobbyNotInMsg;

		currentLobby.getHandlers().remove(handler);
		handler.setLobby(null);

		if (currentLobby.getHandlers().size() == 0) {
			lobbies.remove(currentLobby);
		} else {
			currentLobby.broadcast(handler, lobbyLeftMsg);
		}

		return lobbyQuitMsg;
	}

	public String Date() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		return formatter.format(date);
	}

}