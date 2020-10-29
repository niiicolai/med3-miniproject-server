package org.example.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Lobby {
	
	private int id = 0;

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
		var message = "Client " + sender.getID() + ": " + msg;
		try {
			for (Handler handler : handlers) {	
				handler.message(message);	
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String join(String str, Handler handler) {
		try {  
			 var id = Integer.parseInt(str.replaceAll("\\D+",""));
	         return join(id, handler);
	      } catch (NumberFormatException e) {  
	         return "Please provide a ID";  
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
	    	newLobby.broadcast(handler, "joined the lobby");
	    	
	    	handler.setLobby(newLobby);
	    	newLobby.getHandlers().add(handler);
	    	return "Welcome to lobby " + newLobby.getID();
	    } else {
	    	return "Lobby not found!";
	    }	    
	}
	
	public static String find() {
		
		if (lobbies.size() == 0) {
			return "No lobbies were found!";
		}
		
		var newLineSymbol = System.getProperty("line.separator");
	    var strBuilder = new StringBuilder();
	    strBuilder.append("#n");
	    strBuilder.append("Current lobbies: #n");
	    for (int i = 0; i < lobbies.size(); i++) {
	      strBuilder.append("Lobby ID: ");
	      strBuilder.append(lobbies.get(i).getID());
	      strBuilder.append("#n");
	    }
	    
	    return strBuilder.toString();
	}
	
	public static String create(Handler handler) {
		var lobby = new Lobby(nextID);
	    lobbies.add(lobby);
	    handler.setLobby(lobby);
	    lobby.getHandlers().add(handler);
	    nextID++;
	    
	    return "Welcome to lobby " + lobby.getID();
	}
	
	public static String leave(Handler handler) {
		var currentLobby = handler.getLobby();
		if (currentLobby == null) return "Currently not in a lobby";

    	currentLobby.getHandlers().remove(handler);
    	handler.setLobby(null);
		
		if (currentLobby.getHandlers().size() == 0) {
			lobbies.remove(currentLobby);
		} else {
			currentLobby.broadcast(handler, "left the lobby");
		}

    	return "Lobby was quitted!";
	}
}