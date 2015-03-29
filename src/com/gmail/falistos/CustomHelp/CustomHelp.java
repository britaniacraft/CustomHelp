package com.gmail.falistos.CustomHelp;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomHelp extends JavaPlugin implements Listener {

	public void onEnable() {
		this.getConfig().options().copyDefaults(true);
		this.saveDefaultConfig();
		
		this.getServer().getPluginManager().registerEvents(this, this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("help")) {
			if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
				if (!sender.hasPermission("customhelp.reload")) {
					sender.sendMessage(ChatColor.RED+"You are not allowed to do this.");
					return true;
				}
				
				this.reloadConfig();
				sender.sendMessage(ChatColor.GREEN+this.getName()+" "+this.getDescription().getVersion()+" reloaded.");
				return true;
			}
			
			int page = 0;
			
			if (args.length >= 1) {
				try {
			        page = Integer.parseInt(args[0]) - 1; 
			    } catch(NumberFormatException e) { 
			    	page = 0;
			    }
			}
			
			ArrayList<String> list = new ArrayList<String>();
			Map<String, Object> commandsMap = this.getConfig().getConfigurationSection("commands").getValues(true);
			
			for (Entry<String, Object> entry : commandsMap.entrySet()) {
				list.add(ChatColor.GOLD+entry.getKey()+ChatColor.WHITE+": "+entry.getValue());
			}
			
			new PaginatedMessageList(this.getConfig().getString("header"), this.getConfig().getString("footer")).send(sender, list, page);
			
			return true;
		}
		
		return false;
	}
}