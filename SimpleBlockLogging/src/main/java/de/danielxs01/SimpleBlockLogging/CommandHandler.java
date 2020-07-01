package de.danielxs01.SimpleBlockLogging;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.confuser.barapi.BarAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class CommandHandler implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1 && args[0].equalsIgnoreCase("tool")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;

				ItemStack wand = new ItemStack(Material.BEDROCK);
				ItemMeta im = wand.getItemMeta(); // get the itemmeta of the item
				im.setDisplayName(SimpleBlockLogging.TOOL_NAME); // set the displayname
				im.setLore(Arrays.asList(SimpleBlockLogging.TOOL_LORE));
				wand.setItemMeta(im);

				if (!Bukkit.getBukkitVersion().contains("1.7"))
					p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
							new ComponentBuilder(SimpleBlockLogging.TOOL_ACTIONBAR).create());
				else if (SimpleBlockLogging.barAPIloaded())
					BarAPI.setMessage(p, SimpleBlockLogging.TOOL_ACTIONBAR, 5);
				else
					p.sendMessage(SimpleBlockLogging.TOOL_ACTIONBAR);
				p.getInventory().addItem(wand);
			} else {
				sender.sendMessage("Dieser Befehl ist nur als Spieler ausführbar!");
			}
			return true;
		}
		return false;
	}

}
