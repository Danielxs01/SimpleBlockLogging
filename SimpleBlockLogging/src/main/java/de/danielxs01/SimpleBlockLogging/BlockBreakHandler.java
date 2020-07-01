package de.danielxs01.SimpleBlockLogging;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class BlockBreakHandler implements Listener {

	private SimpleBlockLogging simpleBlockLogging;

	public BlockBreakHandler(SimpleBlockLogging simpleBlockLogging) {
		this.simpleBlockLogging = simpleBlockLogging;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Block b = event.getBlock();
		Player p = event.getPlayer();

		ItemMeta meta = p.getItemInHand().getItemMeta();
		if (meta != null && meta.getDisplayName() != null
				&& meta.getDisplayName().equalsIgnoreCase(SimpleBlockLogging.TOOL_NAME)) {
			event.setCancelled(true);
			Thread newThread = new Thread(() -> simpleBlockLogging.showDetails(p, b));
			newThread.start();
			return;
		}

		simpleBlockLogging.config().add(new SimpleBlockLoggingObject(b, p, Actions.BROKE));

		if (simpleBlockLogging.debug() && p.getName().equalsIgnoreCase("SeltixSub")) {
			p.sendMessage("Type: " + b.getType().name());
			p.sendMessage("ID: " + b.getTypeId() + ":" + ((int) b.getData()));
		}

	}

}
