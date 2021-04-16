package me.noel.hu;

import java.util.HashMap;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;
import org.bukkit.util.Vector;



public class ExperienceX extends JavaPlugin implements Listener, CommandExecutor{

    HashMap<Player,Entity> target = new HashMap<Player, Entity>();

    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "This plugin has started!");
    }
	/*
	private void getKozeliBlockok(Location loc) {
		Location temploc = loc;
		temploc.add(1, 0, 0).getBlock().getType() == Material.leave
	}

	public void BrekoBrekoGyerekekittNosika(BlockBreakEvent e) {
		if(e.getPlayer().getItemInHand() != null && e.getPlayer().getItemInHand().getAmount() > 0) {
			if(e.getPlayer().getInventory().getItemInMainHand() == new ItemStack(Material.DIAMOND_AXE)) {
				Location loc = e.getBlock().getLocation();
			}
		}
	}
	*/

    @EventHandler
    public void onPlayerAttak(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player) {
            Player user = (Player) event.getDamager();
            if(user.getItemInHand() != null) {
                if(user.getItemInHand().getType() != Material.AIR) {
                    if(user.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("§6MobStick")) {
                        if(target.get(user) != null) {
                            event.setCancelled(true);
                            event.getEntity().setPassenger(target.get(user));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        Player user = event.getPlayer();
        if(user.getItemInHand() != null) {
            if(user.getItemInHand().getType() != Material.AIR) {
                if(user.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("§6MobStick")) {
                    target.put(event.getPlayer(), event.getRightClicked());
                    user.sendMessage("§aMob selected!");
                }
            }
        }
    }

    @EventHandler
    public void Pdeath(PlayerDeathEvent e) {
        ItemStack bot = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = bot.getItemMeta();
        meta.setDisplayName("§6Liferod");
        bot.setItemMeta(meta);
        for(ItemStack stack : e.getEntity().getInventory().getContents()) {
            if(stack.getItemMeta().getDisplayName().equalsIgnoreCase("§6Liferod")) {
                e.getDrops().clear();
                e.setKeepInventory(true);
                stack.setAmount(stack.getAmount()-1);
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equalsIgnoreCase("exhelp")) {
            sender.sendMessage(ChatColor.GREEN + "----------- | " + ChatColor.DARK_GREEN + "[ExperienceX] " + ChatColor.GREEN + "| -----------");
            sender.sendMessage(ChatColor.DARK_GREEN + "/exhelp: " + ChatColor.GREEN + "Shows this help list");
            sender.sendMessage(ChatColor.DARK_GREEN + "/extroll: " + ChatColor.GREEN + "Troll the players");
            sender.sendMessage(ChatColor.DARK_GREEN + "/mobstick: " + ChatColor.GREEN + "Gives you a stick");
            sender.sendMessage(ChatColor.DARK_GREEN + "/liferod: " + ChatColor.GREEN + "Gives you a liferod");
            sender.sendMessage(ChatColor.DARK_GREEN + "/playerinfo: " + ChatColor.GREEN + "Playerinfo of you");
            sender.sendMessage(ChatColor.DARK_GREEN + "--------------------------------------------");
        }
        if(label.equalsIgnoreCase("playerinfo")) {
            sender.sendMessage(ChatColor.DARK_AQUA + "----------------------------------------");
            sender.sendMessage(ChatColor.AQUA + "Name: " + ((Player) sender).getDisplayName());
            sender.sendMessage(ChatColor.AQUA + "Health: " + ((Player) sender).getHealth());
            sender.sendMessage(ChatColor.AQUA + "Food Level: " + ((Player) sender).getFoodLevel());
            sender.sendMessage(ChatColor.DARK_AQUA + "----------------------------------------");
        }
        if(label.equalsIgnoreCase("liferod")) {
            ItemStack bot = new ItemStack(Material.BLAZE_ROD,1);
            ItemMeta meta = bot.getItemMeta();
            meta.setDisplayName("§6Liferod");
            bot.setItemMeta(meta);
            ((Player) sender).getInventory().addItem(bot);
            sender.sendMessage("§aDone :)");
        }
        if(label.equalsIgnoreCase("mobstick")) {
            ItemStack bot = new ItemStack(Material.STICK,1);
            ItemMeta meta = bot.getItemMeta();
            meta.setDisplayName("§6MOBStick");
            bot.setItemMeta(meta);
            ((Player) sender).getInventory().addItem(bot);
            sender.sendMessage("§aDone :)");
        }
        if(label.equalsIgnoreCase("extroll")) {
            if(args.length == 2) {
                if(args[0].equalsIgnoreCase("tnt")) {
                    Player target = Bukkit.getPlayer(args[1]);
                    target.getWorld().spawn(target.getLocation(), TNTPrimed.class);
                    sender.sendMessage("§cDone :)"+target.getDisplayName());
                }else if(args[0].equalsIgnoreCase("lava")) {
                    Player target = Bukkit.getPlayer(args[1]);
                    Location loc = target.getLocation();
                    int x = -1;
                    int z = -1;
                    for(int i = 0;i<9; i++) {
                        if(i%3 == 0) {
                            x++;
                            z=-1;
                        }else {
                            z++;
                        }
                        loc.setX(target.getLocation().getBlockX()+x);
                        loc.setZ(target.getLocation().getBlockZ()+z);
                        loc.getBlock().setType(Material.LAVA);
                        sender.sendMessage("§cDone :)"+target.getDisplayName());
                    }
                }
            }else {
                sender.sendMessage("§c------------------\nCommands: \n/extroll lava <name>\n/extroll tnt <name>\n------------------");
            }
        }
        return false;
    }

}
