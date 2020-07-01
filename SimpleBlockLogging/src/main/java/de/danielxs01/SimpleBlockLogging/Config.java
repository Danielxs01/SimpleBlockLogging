package de.danielxs01.SimpleBlockLogging;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {

	private String fileName;
	private YamlConfiguration yamlconfig;
	private SimpleBlockLogging plugin;
	private File file;

	private static final String LIST = "blocks";

	public Config(SimpleBlockLogging plugin, String fileName) {
		this.fileName = fileName;
		this.plugin = plugin;
		checkFile();
	}

	private void checkFile() {

		String d = plugin.getDataFolder().getPath();
		if (file == null)
			file = new File(d, fileName);
		file.getParentFile().mkdirs();
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		yamlconfig = YamlConfiguration.loadConfiguration(file);

	}

	public synchronized void save() throws IOException {
		try {
			yamlconfig.save(file);
		} catch (Exception e) {
			Bukkit.getConsoleSender()
					.sendMessage("§8[§aSimpleBlockLogging§8] §cDie Config konnte nicht gespeichert werden!");
			Bukkit.getConsoleSender().sendMessage(e.getCause().toString());
		}
	}

	public YamlConfiguration getConfig() {
		return yamlconfig;
	}

	public void reloadConfig() {
		checkFile();
	}

	public void add(SimpleBlockLoggingObject logblockobject) {
		Thread newThread = new Thread(() -> addBlock(logblockobject));
		newThread.start();

	}

	private void addBlock(SimpleBlockLoggingObject logblockobject) {

		List<SimpleBlockLoggingObject> objects = new ArrayList<>();
		if (yamlconfig.getList(LIST) != null)
			for (Object object : yamlconfig.getList(LIST)) {
				objects.add((SimpleBlockLoggingObject) object);
			}
		objects.add(logblockobject);
		yamlconfig.set(LIST, objects);

		try {
			save();
		} catch (IOException e) {
			plugin.getLogger().severe(e.getMessage());
		}
	}

	public List<SimpleBlockLoggingObject> getBlocks(Location location) {
		reloadConfig();
		List<SimpleBlockLoggingObject> objects = new ArrayList<>();
		if (yamlconfig.getList(LIST) != null)
			for (Object object : yamlconfig.getList(LIST)) {
				SimpleBlockLoggingObject lbo = ((SimpleBlockLoggingObject) object);
				if (sameLocation(lbo.getLocation(), location))
					objects.add(lbo);
			}
		return objects;
	}

	private boolean sameLocation(Location location, Location other) {

		if (!location.getWorld().getName().equalsIgnoreCase(other.getWorld().getName()))
			return false;
		if (location.getBlockX() != other.getBlockX())
			return false;
		if (location.getBlockY() != other.getBlockY())
			return false;
		return (location.getBlockZ() == other.getBlockZ());
	}

}
