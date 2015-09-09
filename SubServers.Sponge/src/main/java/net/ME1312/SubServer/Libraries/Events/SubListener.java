package net.ME1312.SubServer.Libraries.Events;

import net.ME1312.SubServer.Libraries.Events.SubEvent.SubPlayerEvent;
import net.ME1312.SubServer.Libraries.Events.SubEvent.SubPlayerEvent.SubCreateEvent;
import net.ME1312.SubServer.Libraries.Events.SubEvent.SubPlayerEvent.SubRunCommandEvent;

/**
 * Sublistener Class allows for Subservers Listeners and Bukkit Listeners to be called<br><br>
 * Override these methods in your class to use them
 * 
 * @author ME1312
 *
 */
public interface SubListener {
	
	/**
	 * Called when a SubServer is Created
	 * 
	 * @param event The event attached to this method
	 */
	default void onSubServerCreate(SubCreateEvent event) { return; }
	
	/**
	 * Called when a Subserver is Started
	 * 
	 * @param event The event attached to this method
	 */
	default void onSubServerStart(SubPlayerEvent event) { return; }
	
	/**
	 * Called when a Subserver is Stopped/Terminated
	 * 
	 * @param event The event attached to this method
	 */
	default void onSubServerStop(SubPlayerEvent event) { return; }
	
	/**
	 * Called when a Subserver's Shell Exits
	 * 
	 * @param event The event attached to this method
	 */
	default void onSubServerShellExit(SubEvent event) { return; }
	
	/**
	 * Called when a Subserver Receives a Command
	 * 
	 * @param event The event attached to this method
	 */
	default void onSubServerCommand(SubRunCommandEvent event) { return; }
	
}
