package org.example.server;

public class CommandHandler {
	// Empty constructor
	public static int nextID = 0;

	var serverMsg = "";
	var currentLobby = Handler.getLobby();
	Pattern pattern = Pattern.compile(stringCmd, Pattern.CASE_INSENSITIVE);


	 
	  if (pattern.matcher("Join").find()) {
	    var id = parseInt(clientMsg);
	                            
	    for (int i = 0; i < lobbies.size(); i++) {
	      var l = lobbies.get(i);
	      if (id == l.getID()) {
	        handler.setLobby(lobby);
	        lobby.join(writer);
	        break;
	      }
	    }
	  }
	  else if (pattern.matcher("Create").find()) {
	    
	    var lobby = new Lobby(nextID);
	    var writer = handler.getWriter();
	    handler.setLobby(lobby);
	    lobby.join(writer);
	        
	    nextID++;

	    // Create functionality    
	  }
	  else if (pattern.matcher("Find").find()) {
	    var newLineSymbol = System.getProperty("line.separator");
	    var strBuilder = new StringBuilder();
	    for (int i = 0; i i < lobbies.size(); i++) {
	      strBuilder.append("Lobby ID: ");
	      strBuilder.append(lobbies[i].getID());
	      strBuilder.append(newLineSymbol);
	    }
	    
	    serverMsg = strBuilder.toString();
	  }
	}

	// Return some server msg
	return serverMsg
}