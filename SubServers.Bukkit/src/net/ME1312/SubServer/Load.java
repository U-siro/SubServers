package net.ME1312.SubServer;

import org.bukkit.plugin.java.JavaPlugin;

public class Load extends JavaPlugin {
	private Main Main;
	
	@Override
	public void onEnable() {
		try {
			Main = new Main(this);
			Main.EnablePlugin();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			setEnabled(false);
		}
	}
	
	@Override
	public void onDisable() {
		if (Main != null) {
			Main.DisablePlugin();
		}
	}

}
