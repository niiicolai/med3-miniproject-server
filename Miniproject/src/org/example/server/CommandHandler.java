package org.example.server;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class CommandHandler {

	private boolean stopped = false;
	
	public CommandHandler() {
		
	}

	public String process(String stringCmd, Handler handler) {
		
		var cmd = stringCmd.toUpperCase();
		var serverMsg = "";
		var currentLobby = handler.getLobby();

		// If the client is in a lobby ->
	    if(currentLobby != null)
	    {
	        // If the client wants to exit the lobby
	    	Pattern leavePattern = Pattern.compile("LEAVE", Pattern.CASE_INSENSITIVE);
	        if (leavePattern.matcher(cmd).lookingAt()) {
	        	serverMsg = Lobby.leave(handler);
	        } else {
	        	currentLobby.broadcast(handler, stringCmd);
	        }
	    }
	    // if the client is NOT in a lobby ->
	    else
	    {
	    	Pattern joinPattern = Pattern.compile("JOIN", Pattern.CASE_INSENSITIVE);
	    	Pattern createPattern = Pattern.compile("CREATE", Pattern.CASE_INSENSITIVE);
	    	Pattern findPattern = Pattern.compile("FIND", Pattern.CASE_INSENSITIVE);
	    	Pattern exitPattern = Pattern.compile("HELP", Pattern.CASE_INSENSITIVE);
	    	Pattern helpPattern = Pattern.compile("EXIT", Pattern.CASE_INSENSITIVE);
	    	
		    if (joinPattern.matcher(cmd).lookingAt()) {
			    serverMsg = Lobby.join(stringCmd, handler);
		    }
		    else if (createPattern.matcher(cmd).lookingAt()) {
			    serverMsg = Lobby.create(handler);
		    }
		    else if (findPattern.matcher(cmd).lookingAt()) {	    
		        serverMsg = Lobby.find();
		    }
		    else if (exitPattern.matcher(cmd).lookingAt()) {	    
			    serverMsg = help(handler);
		    }
		    else if (exitPattern.matcher(cmd).lookingAt()) {	    
			    serverMsg = this.stop(handler);
		    }
		    else {
		    	serverMsg = "Invalid command! Try with 'help'.";
		    }
		}

		return serverMsg;
	}
	
	private String help(Handler handler) {
		var helpMsg = "#n" +
					  "Client number: " + handler.getID() + " #n" +
				      "You are currently not in a lobby. #n #n" +
					  "Lobby commands: (only availabe when connected to a lobby) #n" +
					  "Leave - leaves the current lobby #n #n" +
					  "Menu commands: #n" +
					  "Create - creates a new lobby and connects to it, #n" +
					  "Join <id> - connect to a lobby with id, #n" +
					  "Find - returns a list of current lobbies, #n" +
					  "Exit - Close the client's connection #n";
	    
	    return helpMsg;
	}

	private String stop(Handler handler) {
		Lobby.leave(handler);
		stopped = true;
    	return "Goodbye";
	}
	
	public boolean isStopped() {
		return stopped;
	}
}