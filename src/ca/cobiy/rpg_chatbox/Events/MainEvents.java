package ca.cobiy.rpg_chatbox.Events;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.metadata.FixedMetadataValue;

import ca.cobiy.rpg_chatbox.Main;

public class MainEvents implements Listener {
	HashMap<UUID, Boolean> playersHasBox = new HashMap<UUID, Boolean>();
	
	private Main plugin;
	
	public MainEvents(Main main) {
		plugin = main;
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		String message = event.getMessage();
		event.setCancelled(true);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
            	if(!playersHasBox.containsKey(player.getUniqueId())) {
                	spawnMessageBox(player, message);
                	playersHasBox.put(player.getUniqueId(), true);
            	}else {
            		List<Entity> entities = player.getWorld().getEntities();
            		for(int i = 0; i < entities.size(); i++) {
            			if(entities.get(i).hasMetadata("user/"+player.getName())){
            				entities.get(i).remove();
            				endTask1(player);
            				endTask2(player);
                        	spawnMessageBox(player, message);
            			}
            		}
            	}

            }
        });
	}
	
	public Map<UUID, Integer> tID1 = new HashMap<UUID, Integer>();
	public Map<UUID, Integer> tID2 = new HashMap<UUID, Integer>();

	public void spawnMessageBox(Player player, String message){
		ArmorStand as = (ArmorStand) player.getLocation().getWorld().spawn(player.getLocation().add(0, 2.3, 0), ArmorStand.class);
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			@Override
			public void run(){
				String formatedMessage = message.substring(0, 1).toUpperCase() + message.substring(1);
				as.setVisible(false);
				as.setMarker(true);
				as.setCustomName(formatedMessage+".");
				as.setMetadata("isChatBox", new FixedMetadataValue(plugin, true));
				as.setMetadata("user/"+player.getName(), new FixedMetadataValue(plugin, true));
				as.setGravity(false);
				as.setCollidable(false);
				as.setCustomNameVisible(true);
			}
		});
		final int tid1 = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
	        @Override
	        public void run() {
	        	if(player.isSneaking()) {
		    		as.teleport(player.getLocation().add(0, 1.7, 0));
	        	}else {
	        		as.teleport(player.getLocation().add(0, 2.3, 0));
	        	}
	        }
		}, 1L, 1L);
		final int tid2 = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
	        @Override
	        public void run() {
	    		  as.remove();
	    		  playersHasBox.remove(player.getUniqueId());
	    		  endTask1(player);
	    		  endTask2(player);
	        }
		}, 100L);
		tID1.put(player.getUniqueId(), tid1);
		tID2.put(player.getUniqueId(), tid2);
	}
	
	public void endTask1(Player player){
		if(tID1.containsKey(player.getUniqueId())){
			int tid = tID1.get(player.getUniqueId());
		    plugin.getServer().getScheduler().cancelTask(tid);
		    tID1.remove(player.getUniqueId());
		}
	}
	
	public void endTask2(Player player){
		if(tID2.containsKey(player.getUniqueId())){
		    int tid = tID2.get(player.getUniqueId());
		    plugin.getServer().getScheduler().cancelTask(tid);
		    tID2.remove(player.getUniqueId());
		}
	}
}