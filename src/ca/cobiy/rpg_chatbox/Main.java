package ca.cobiy.rpg_chatbox;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import ca.cobiy.rpg_chatbox.Events.MainEvents;

public class Main extends JavaPlugin{
	
	@Override
	public void onEnable() {
		deleteOldChatBox();
		Bukkit.getServer().getPluginManager().registerEvents(new MainEvents(this), this);
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public void deleteOldChatBox() {
		List<World> worlds = Bukkit.getWorlds();
		for(int i = 0; i < worlds.size(); i++) {
			List<Entity> entities = worlds.get(i).getEntities();
			for(int i2 = 0; i2 < entities.size(); i2++) {
				if(entities.get(i2).hasMetadata("isChatBox")){
					entities.get(i2).remove();
				}
			}
		}
	}
}