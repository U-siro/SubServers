package net.ME1312.SubServer.Libraries.Events;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import net.ME1312.SubServer.Main;
import net.ME1312.SubServer.Executable.SubServerCreator.ServerTypes;
import net.ME1312.SubServer.Executable.SubServer;
import org.spongepowered.api.entity.living.player.Player;
import net.ME1312.SubServer.Libraries.Events.SubEvent.SubPlayerEvent.SubCreateEvent;

/**
 * SubEvents class is the main Class for SubServer Events
 * 
 * @author ME1312
 *
 */
public class SubEvent {
	protected boolean isCancelled = false;
	protected SubServer Server;
	protected Main Main;
	private Events Event;
	public static enum Events {
		SubCreateEvent,
		SubStartEvent,
		SubRunCommandEvent,
		SubShellExitEvent,
		SubStopEvent,
	}
	
	public SubEvent(Main Main, Events Event, SubServer Server) {
		this.Main = Main;
		this.Server = Server;
		this.Event = Event;
	}
	
	@Override 
	public String toString() { return Event.toString(); }
	
	/**
	 * Gets the Event Name
	 * @return Event Name
	 */
	public String getEventName() { return Event.toString(); }
	
	/**
	 * Gets the Server Effected
	 * @return The Server Effected
	 */
	public SubServer getServer() { return Server; }
	
	/**
	 * Check if the Event was Cancelled
	 * @return Event Cancellation Status (true/false)
	 */
	public boolean isCancelled() { return isCancelled; }
	
	/**
	 * Set the Event's Cancellation Status
	 * 
	 * @param value Cancel Event? (true/false)
	 */
	public void setCancelled(boolean value) { isCancelled = value; }
	
	/**
	 * General Event Class for events that can be Triggered by a Player
	 * 
	 * @author ME1312
	 *
	 */
	public static class SubPlayerEvent extends SubEvent {
		protected Player Player;

		protected SubPlayerEvent(Main Main, Events Event, SubServer Server, Player player) {
			super(Main, Event, Server);
			Player = player;
		}
		
		/**
		 * Gets the player that Triggered the Event
		 * @return The Player that triggered this Event or Null if Console
		 */
		public Player getPlayer() { return Player; }
		
		/**
		 * The Event for a Player Creating a SubServer
		 * 
		 * @author ME1312
		 *
		 */
		public static class SubCreateEvent extends SubPlayerEvent {
			private ServerTypes Type;
			
			public SubCreateEvent(Main Main, SubServer Server, Player Player, ServerTypes Type) {
				super(Main, Events.SubCreateEvent, Server, Player);
				this.Type = Type;
			}
			
			public ServerTypes getType() { return Type; }
		}
		
		/**
		 * The Event for a Server Running a Command
		 * 
		 * @author ME1312
		 */
		public static class SubRunCommandEvent extends SubPlayerEvent {
			private String Command;
			
			public SubRunCommandEvent(Main Main, SubServer Server, Player Player, String Command) {
				super(Main, Events.SubRunCommandEvent, Server, Player);
				this.Command = Command;
			}
			
			/**
			 * Gets the inputed Command
			 * @return Command in String Form
			 */
			public String getCommand() { return Command; }
		}
	}
	
	/**
	 * Runs a SubEvent
	 * 
	 * @param Main SubServers Main Class
	 * @param Event The event to Trigger
	 * @param args A List of Objects to be Cast to the proper Arguments
	 * @return False if Cancelled
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public static boolean RunEvent(Main Main, Events Event, Object... args)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		
		boolean EventStatus = true;
		for(Iterator<List<SubListener>> List = Main.EventHandlers.values().iterator(); List.hasNext(); ) {
			List<SubListener> item = List.next();
			
			for(Iterator<SubListener> Listeners = item.iterator(); Listeners.hasNext(); ) {
				SubListener Listener = Listeners.next();
				
				if (Event.equals(SubEvent.Events.SubCreateEvent)) {
					SubPlayerEvent EventClass = new SubCreateEvent(Main, (SubServer) args[0], (Player) args[1], (ServerTypes) args[2]);
					Listener.getClass().getMethod("onSubServerCreate", SubCreateEvent.class).invoke(Listener, EventClass);
					if (EventClass.isCancelled()) EventStatus = false;
					
				} else if (Event.equals(SubEvent.Events.SubStartEvent)) {
					SubPlayerEvent EventClass = new SubPlayerEvent(Main, SubEvent.Events.SubStartEvent, (SubServer) args[0], (Player) args[1]);
					Listener.getClass().getMethod("onSubServerStart", SubPlayerEvent.class).invoke(Listener, EventClass);
					if (EventClass.isCancelled()) EventStatus = false;
					
				} else if (Event.equals(SubEvent.Events.SubRunCommandEvent)) {
					SubPlayerEvent.SubRunCommandEvent EventClass = new SubPlayerEvent.SubRunCommandEvent(Main, (SubServer) args[0], (Player) args[1], (String) args[2]);
					Listener.getClass().getMethod("onSubServerCommand", SubPlayerEvent.SubRunCommandEvent.class).invoke(Listener, EventClass);
					if (EventClass.isCancelled()) EventStatus = false;
					
				} else if (Event.equals(SubEvent.Events.SubShellExitEvent)) {
					Listener.getClass().getMethod("onSubServerShellExit", SubEvent.class)
						.invoke(Listener, new SubEvent(Main, SubEvent.Events.SubShellExitEvent, (SubServer) args[0]));
					
				} else if (Event.equals(SubEvent.Events.SubStopEvent)) {
					SubPlayerEvent EventClass = new SubPlayerEvent(Main, SubEvent.Events.SubStopEvent, (SubServer) args[0], (Player) args[1]);
					Listener.getClass().getMethod("onSubServerStop", SubPlayerEvent.class).invoke(Listener, EventClass);
					if (EventClass.isCancelled()) EventStatus = false;
					
				} else {
					throw new IllegalArgumentException("an Invalid Event was Called");
					
				}
			}
		}
		
		return EventStatus;
	}
}
