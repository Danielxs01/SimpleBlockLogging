package de.danielxs01.SimpleBlockLogging;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

public class SimpleBlockLoggingObject implements ConfigurationSerializable {

	private String location;
	private String type;
	private String id;
	private String data;
	private UUID player;
	private LocalDateTime timestamp;
	private String gamemode;
	private String action;

	public SimpleBlockLoggingObject(SimpleBlockLoggingObject clone) {
		this.location = clone.location;
		this.type = clone.type;
		this.id = clone.id;
		this.data = clone.data;
		this.player = clone.player;
		this.timestamp = clone.timestamp;
		this.gamemode = clone.gamemode;
		this.action = clone.action;
	}

	public SimpleBlockLoggingObject(Location location, String type, String id, String damage, UUID player,
			String gamemode, Actions action) {
		this.location = locationToString(location);
		this.type = type;
		this.id = id;
		this.data = damage;
		this.player = player;
		this.action = action.name();
		this.gamemode = gamemode;
		this.timestamp = LocalDateTime.now();

	}

	public SimpleBlockLoggingObject(Map<String, Object> args) {
		this.location = (String) args.get("location");
		this.type = (String) args.get("type");
		this.id = (String) args.get("id");
		this.data = (String) args.get("data");
		this.player = UUID.fromString((String) args.get("player"));
		this.timestamp = LocalDateTime.parse((String) args.get("timestamp"));
		this.gamemode = (String) args.get("gamemode");
		this.action = (String) args.get("action");
	}

	@SuppressWarnings("deprecation")
	public SimpleBlockLoggingObject(Block b, Player p, Actions action) {
		this.location = locationToString(b.getLocation());
		this.type = b.getType().name();
		this.id = "" + b.getTypeId();
		this.data = "" + (int) b.getData();
		this.player = p.getUniqueId();
		this.timestamp = LocalDateTime.now();
		this.gamemode = p.getGameMode().name();
		this.action = action.name();
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> result = new HashMap<>();
		result.put("location", location);
		result.put("type", type);
		result.put("id", id);
		result.put("data", data);
		result.put("player", player.toString());
		result.put("timestamp", timestamp.toString());
		result.put("gamemode", gamemode);
		result.put("action", action);
		return result;
	}

	private String locationToString(Location location) {
		return location.getWorld().getName() + ";" + location.getBlockX() + ";" + location.getBlockY() + ";"
				+ location.getBlockZ();
	}

	private Location locationFromString() {
		String[] parts = location.split(";");
		int x = Integer.parseInt(parts[1]);
		int y = Integer.parseInt(parts[2]);
		int z = Integer.parseInt(parts[3]);
		return new Location(Bukkit.getWorld(parts[0]), x, y, z);
	}

	public static SimpleBlockLoggingObject deserialize(Map<String, Object> args) {
		return new SimpleBlockLoggingObject(args);
	}

	public Location getLocation() {
		return locationFromString();
	}

	public String getType() {
		return type;
	}

	public String getId() {
		return id;
	}

	public String getData() {
		return data;
	}

	public UUID getPlayer() {
		return player;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public Actions getAction() {
		return Actions.valueOf(action);
	}

	public String getGamemode() {
		return gamemode;
	}

	@Override
	public String toString() {
		String out = "";
		for (Entry<String, Object> entrySet : serialize().entrySet()) {
			out += "[" + entrySet.getKey() + ": " + entrySet.getValue() + "]";
		}
		return out;
	}

}
