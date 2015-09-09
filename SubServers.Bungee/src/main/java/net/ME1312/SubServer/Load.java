package net.ME1312.SubServer;

import net.md_5.bungee.api.plugin.Plugin;
import net.ME1312.SubServer.Main;

public class Load extends Plugin {
	private Main Main;
	
	@Override
	public void onEnable() {
		try {
			Main = new Main(this);
			Main.EnablePlugin();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
}
