package org.example.server;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class CommandHandler {

	private boolean stopped = false;
	private static final String handlerIsStoppedMsg = "Goodbye";
	private static final String invalidCommandMsg = "Invalid command! Try with 'help'.";

	private static final String cmdClientNum = "Client number: ";
	private static final String cmdUsername = "Username: ";
	private static final String cmdNoLobby = "You are currently not in a lobby.";
	private static final String cmdLobbyCommands = "Lobby commands: (only availabe when connected to a lobby) ";
	private static final String cmdLeave = "Leave - leaves the current lobby";
	private static final String cmdMenuCommands = "Menu commands: ";
	private static final String cmdCreate = "Create - creates a new lobby and connects to it, ";
	private static final String cmdJoin = "Join <id> - connect to a lobby with id, ";
	private static final String cmdFind = "Find - returns a list of current lobbies, ";
	private static final String cmdExit = "Exit - Close the client's connection ";

	private static final String newLine = "#n";
	private static final String newLineSpace = " #n";
	private static final String newLineSpaceDouble = " #n #n";

	public CommandHandler() {

	}

	public String process(String stringCmd, Handler handler) {

		var cmd = stringCmd.toUpperCase();
		var serverMsg = "";
		var currentLobby = handler.getLobby();

		// If the client is in a lobby ->
		if (currentLobby != null) {
			// If the client wants to exit the lobby
			Pattern leavePattern = Pattern.compile("LEAVE", Pattern.CASE_INSENSITIVE);
			if (leavePattern.matcher(cmd).lookingAt()) {
				serverMsg = Lobby.leave(handler);
			} else {
				currentLobby.broadcast(handler, stringCmd);
			}
		}
		// if the client is NOT in a lobby ->
		else {
			Pattern joinPattern = Pattern.compile("JOIN", Pattern.CASE_INSENSITIVE);
			Pattern createPattern = Pattern.compile("CREATE", Pattern.CASE_INSENSITIVE);
			Pattern findPattern = Pattern.compile("FIND", Pattern.CASE_INSENSITIVE);
			Pattern exitPattern = Pattern.compile("HELP", Pattern.CASE_INSENSITIVE);
			Pattern helpPattern = Pattern.compile("EXIT", Pattern.CASE_INSENSITIVE);

			if (joinPattern.matcher(cmd).lookingAt()) {
				serverMsg = Lobby.join(stringCmd, handler);
			} else if (createPattern.matcher(cmd).lookingAt()) {
				serverMsg = Lobby.create(handler);
			} else if (findPattern.matcher(cmd).lookingAt()) {
				serverMsg = Lobby.find();
			} else if (exitPattern.matcher(cmd).lookingAt()) {
				serverMsg = help(handler);
			} else if (exitPattern.matcher(cmd).lookingAt()) {
				serverMsg = this.stop(handler);
			} else {
				serverMsg = invalidCommandMsg;
			}
		}

		return serverMsg;
	}

	private String help(Handler handler) {
		var helpMsg = newLine + 
				  cmdClientNum + handler.getID() + newLineSpace + 
				  cmdUsername + handler.getFormattedName() + newLineSpace + 
				  cmdNoLobby + newLineSpaceDouble + 
				  cmdLobbyCommands + newLine + 
				  cmdLeave + newLineSpaceDouble + 
				  cmdMenuCommands + newLine + 
				  cmdCreate + newLine + 
				  cmdJoin + newLine + 
				  cmdFind + newLine + 
				  cmdExit + newLine;

		return helpMsg;
	}

	private String stop(Handler handler) {
		Lobby.leave(handler);
		stopped = true;
		return handlerIsStoppedMsg;
	}

	public boolean isStopped() {
		return stopped;
	}
}