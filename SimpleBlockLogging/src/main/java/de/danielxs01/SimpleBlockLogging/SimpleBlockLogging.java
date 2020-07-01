package de.danielxs01.SimpleBlockLogging;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleBlockLogging extends JavaPlugin {

	public static final String TOOL_NAME = "§7§lSimpleBlockLogging Tool";
	public static final String TOOL_LORE = "§fZeigt dir Informationen über den angeklickten / zerstörten Block!";
	public static final String TOOL_ACTIONBAR = "§8~ §7Dein Tool: §8~";

	private Config config;
	private boolean debug = false;

	@Override
	public void onEnable() {
		getLogger().info("Starte..");
		config = new Config(this, "blocks.yml");

		ConfigurationSerialization.registerClass(SimpleBlockLoggingObject.class, "SimpleBlockLoggingObject");

		getCommand("simpleBlockLogging").setExecutor(new CommandHandler());
		Bukkit.getPluginManager().registerEvents(new BlockBreakHandler(this), this);
		Bukkit.getPluginManager().registerEvents(new BlockPlaceHandler(this), this);

	}

	@Override
	public void onDisable() {
		getLogger().info("Stoppe..");
	}

	public Config config() {
		return config;
	}

	public boolean debug() {
		return debug;
	}

	public void showDetails(Player p, Block b) {

		if (!p.hasPermission(getCommand("simpleBlockLogging").getPermission())) {
			p.sendMessage(getCommand("simpleBlockLogging").getPermissionMessage());
			return;
		}

		List<SimpleBlockLoggingObject> list = config().getBlocks(b.getLocation());
		Location l = b.getLocation();
		String header = "§8---------- §7§lSimpleBlockLogging §8----------";
		String splitter = "§7,§a";
		if (list.isEmpty()) {
			p.sendMessage(header);
			p.sendMessage(
					"§7Location (X,Y,Z): §a" + l.getBlockX() + splitter + l.getBlockY() + splitter + l.getBlockZ());
			p.sendMessage("§7Info: §eKeine Einträge vorhanden! ");
		} else {
			for (SimpleBlockLoggingObject lbo : list) {
				OfflinePlayer player = Bukkit.getOfflinePlayer(lbo.getPlayer());
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
				String info = (player == null ? "§cUnbekannt" : "§e" + player.getName());
				info += " §6" + lbo.getAction().name().toLowerCase();
				info += " §e" + firstUpElseDown(lbo.getType());
				p.sendMessage(header);
				p.sendMessage(
						"§7Location (X,Y,Z): §a" + l.getBlockX() + splitter + l.getBlockY() + splitter + l.getBlockZ());
				p.sendMessage("§7Info: " + info);
				p.sendMessage("§7ID & Gamemode: §b" + lbo.getId() + ":" + lbo.getData() + " §7- §3"
						+ firstUpElseDown(lbo.getGamemode()));
				p.sendMessage("§7Zeitstempel: §d" + lbo.getTimestamp().format(formatter));
			}
		}

	}

	private String firstUpElseDown(String text) {
		String out = Character.toUpperCase(text.charAt(0)) + "";
		out += text.substring(1, text.length()).toLowerCase();
		return out;
	}

}
