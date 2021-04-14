package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import common.*;


public interface TrivialSolitaire extends Remote {
	
	// starts a new game. The result is the id of the newly created game
	// this id will have to be supplied in future interactions with the "server"
	public int Hello () throws RemoteException;
	
	// request a question of the type of the second argument
	public Question next (int id, String type) throws RemoteException;
	
	// quits the game with the given id
	public void stop(int id) throws RemoteException; 
	
}
