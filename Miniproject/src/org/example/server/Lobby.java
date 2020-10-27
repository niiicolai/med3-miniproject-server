package org.example.server;

import java.io.PrintWriter;
import java.util.ArrayList;

public class Lobby {
private int id = 0;

	ArrayList<PrintWriter> writers = new ArrayList<PrintWriter>();

	public Lobby(int id) {
		
		this.id = id;
		
	}
	public void join(PrintWriter writer) {
		writers.add(writer);
	}
	public void leave(PrintWriter writer) {
		writers.remove(writer);
	}
	public int getID() {
		return id;
	}
}
