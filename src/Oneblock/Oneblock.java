// Copyright © 2024 MrMarL. All rights reserved.
package Oneblock;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import Oneblock.ChestItems.type;
import Oneblock.PlData.JsonSimple;
import Oneblock.PlData.ReadOldData;
import Oneblock.WorldGuard.OBWorldGuard;
import Oneblock.WorldGuard.OBWorldGuard6;
import Oneblock.WorldGuard.OBWorldGuard7;
import Oneblock.gui.GUI;
import Oneblock.gui.GUIListener;
import XSeriesOneBlock.XBlock;
import XSeriesOneBlock.XMaterial;
import me.clip.placeholderapi.PlaceholderAPI;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.Inventory;

public class Oneblock extends JavaPlugin {
    static final Random rnd = new Random(System.currentTimeMillis());
    boolean on = false;
    static int x = 0;
    static int y = 0;
    static int z = 0;
    FileConfiguration config, newConfigz;
    static World wor;
    World leavewor;
    int random = 0;
    boolean superlegacy, legacy;
    ArrayList <Object> blocks = new ArrayList <>();
    ArrayList <EntityType> mobs = new ArrayList <>();
    ArrayList <XMaterial> flowers = new ArrayList <>();
    static List <Player> plonl;
    String TextP = "";
    int sto = 100;
    BarColor Progress_color;
    boolean il3x3 = false, rebirth = false, autojoin = false;
    boolean droptossup = true, physics = false;
    boolean lvl_bar_mode = false, chat_alert = false;
    boolean protection = false;
    boolean PAPI = false;
    boolean WorldGuard = OBWorldGuard.canUse;
    boolean Border = true;
    boolean Progress_bar = true;
    boolean СircleMode = false;
    boolean UseEmptyIslands = true;
    boolean saveplayerinventory = false;
    int max_players_team = 0;
    OBWorldGuard OBWG;
    final XMaterial GRASS_BLOCK = XMaterial.GRASS_BLOCK, GRASS = XMaterial.GRASS;
    final VoidChunkGenerator GenVoid = new VoidChunkGenerator();
    
    public static World wor() { return wor; }
    
	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {return GenVoid;}
	
	public boolean findMethod(final Class<?> cl, String name) {
		for (Method m : cl.getMethods())
			if (m.getName().equals(name))
				return true;
		return false;
	}
    
    @Override
    public void onEnable() {
        superlegacy = !XMaterial.supports(9);// Is version 1.9 supported?
        legacy = !XMaterial.supports(13);// Is version 1.13 supported?
        Border = findMethod(Bukkit.class, "createWorldBorder");// Is virtual border supported?
        final Metrics metrics = new Metrics(this, 14477);
        final PluginManager pluginManager = Bukkit.getPluginManager();
        getLogger().info(
                "\n┏━┓····┏━━┓·····┏┓\n"
        		  + "┃┃┣━┳┳━┫┏┓┣┓┏━┳━┫┣┓\n"
        		  + "┃┃┃┃┃┃┻┫┏┓┃┗┫╋┃━┫━┫\n"
        		  + "┗━┻┻━┻━┻━━┻━┻━┻━┻┻┛ by MrMarL a fork by MeHaVr");
        if (PAPI = pluginManager.isPluginEnabled("PlaceholderAPI")) {
        	getLogger().info("PlaceholderAPI has been found!");
            new OBP().register();
        }
        Configfile();
        Datafile();
        Blockfile();
        Flowerfile();
        Chestfile();
        Messagefile();
        metrics.addCustomChart(new SimplePie("premium", () -> String.valueOf(OBWorldGuard.canUse)));
        metrics.addCustomChart(new SimplePie("circle_mode", () -> String.valueOf(СircleMode)));
        metrics.addCustomChart(new SimplePie("use_empty_islands", () -> String.valueOf(UseEmptyIslands)));
        metrics.addCustomChart(new SimplePie("gui", () -> String.valueOf(GUI.enabled)));
        pluginManager.registerEvents(new RespawnJoinEvent(), this);
        pluginManager.registerEvents(new BlockEvent(), this);
        pluginManager.registerEvents(new GUIListener(), this);
        
        if (config.getDouble("y") == 0) return;	
        if (wor == null || (config.getDouble("yleave") != 0 && leavewor == null))
            Bukkit.getScheduler().runTaskTimerAsynchronously(this, (Runnable) new wor_null(), 32, 64);
        else 
            runMainTask();
    }
    public class RespawnJoinEvent implements Listener {
        @EventHandler
        public void Respawn(final PlayerRespawnEvent e) {
			if (!rebirth)
				return;
			Player pl = e.getPlayer();
			if (pl.getWorld().equals(wor) && PlayerInfo.ExistId(pl.getUniqueId())) {
				int result[] = getFullCoord(PlayerInfo.GetId(pl.getUniqueId()));
				e.setRespawnLocation(new Location(wor, result[0] + 0.5, y + 1.2013, result[1] + 0.5));
				if (Border) UpdateBorder(pl);
			}
        }
        @EventHandler
        public void AutoJoin(final PlayerTeleportEvent e) {
			if (!autojoin)
				return;
			Location loc = e.getTo();
			World from = e.getFrom().getWorld();
			World to = loc.getWorld();
			if (!from.equals(wor) && to.equals(wor) && loc.getY() != y + 1.2013) {
				e.setCancelled(true);
				e.getPlayer().performCommand("ob j");
			}
        }
        @EventHandler
        public void JoinAuto(final PlayerJoinEvent e) {
			Player pl = e.getPlayer();
			if (pl.getWorld().equals(wor)) {
				if (autojoin) pl.performCommand("ob j");
				if (Border) UpdateBorder(pl);
			}
		}
    }
    
    public void UpdateBorder(final Player pl) {
    	Bukkit.getScheduler().runTaskLaterAsynchronously(this, 
    		() -> { pl.setWorldBorder(pl.getWorldBorder()); }, 10L);
    }
    
    public class ChangedWorld implements Listener {
    	@EventHandler
        public void PlayerChangedWorldEvent(PlayerChangedWorldEvent e) {
    		if (PlayerInfo.size() == 0)
    			return;
        	if (e.getFrom().equals(wor))
        		PlayerInfo.removeBarStatic(e.getPlayer());
        }
    }
    
    public class BlockEvent implements Listener {
    	@EventHandler(ignoreCancelled = true)
        public void ItemStackSpawn(final EntitySpawnEvent e) {
    		if (!droptossup) return;
    		if (wor == null) return;
            if (!EntityType.DROPPED_ITEM.equals(e.getEntityType())) return;
                
            Location loc = e.getLocation();
            if (!wor.equals(loc.getWorld())) return;
            if (loc.getBlockY() != y) return;
            if ((x - loc.getBlockX()) % sto != 0) return;
            if ((z - loc.getBlockZ()) % sto != 0) return;

            Entity drop = e.getEntity();
            drop.teleport(loc.add(0, .75, 0));
            drop.setVelocity(new Vector(0, .1, 0));
        }
    	@EventHandler
    	public void BlockBreak(final BlockBreakEvent e) {
    		if (wor == null) return;
    		final Block block = e.getBlock();
    		if (block.getWorld() != wor) return;
    		if (block.getY() != y) return;
    		final Player ponl = e.getPlayer();
    		final UUID uuid = ponl.getUniqueId();
        	if (!PlayerInfo.ExistId(uuid)) return;
        	final int plID = PlayerInfo.GetId(uuid);
        	final int result[] = getFullCoord(plID);
        	if (block.getX() != result[0]) return;
        	if (block.getZ() != result[1]) return;

            Bukkit.getScheduler().runTaskLater(Oneblock.this, () -> { BlockGen(result[0], result[1], plID, ponl, block); }, 1L);
    	}
    }
    public class wor_null implements Runnable {
        public void run() {
            if (wor == null) {
            	getLogger().info("Waiting for the initialization of the world");
            	getLogger().info("Trying to initialize the world again...");
                wor = Bukkit.getWorld(config.getString("world"));
                leavewor = Bukkit.getWorld(config.getString("leaveworld"));
            } else {
            	getLogger().info("The initialization of the world was successful!");
            	runMainTask();
            }
        }
    }
    public void runMainTask() {
    	Bukkit.getScheduler().cancelTasks(this);
		if (config.getDouble("y") == 0) return;
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, new TaskUpdatePlayers(), 0, 110);
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, new TaskSaveData(), 200, 6000);
		Bukkit.getScheduler().runTaskTimer(this, new Task(), 40, 80);
		on = true;
		
    	if (OBWorldGuard.canUse && Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) {
        	getLogger().info("WorldGuard has been found!");
        	if (legacy) OBWG = new OBWorldGuard6();
			else OBWG = new OBWorldGuard7();
        	ReCreateRegions();
        }
        else WorldGuard = false;
    }

    public void addinvite(UUID name, UUID to) {
    	for(Invitation item: Invitation.list) 
			if (item.equals(name, to))
				return;
    	Invitation inv_ = new Invitation(name, to);
    	Invitation.list.add(inv_);
    	Bukkit.getScheduler().runTaskLaterAsynchronously(this, 
    			() -> { Invitation.list.remove(inv_); }, 300L);
    }
    public boolean checkinvite(Player pl) {
		UUID uuid = pl.getUniqueId();
		Invitation inv_ = Invitation.check(uuid);
		if (inv_ == null) return false; 
		if (!PlayerInfo.ExistId(inv_.Inviting)) return false;
			
		if (PlayerInfo.ExistId(uuid)) {
			if (Progress_bar)
				PlayerInfo.get(uuid).bar.removePlayer(pl);
			pl.performCommand("ob idreset /n");
		}
		PlayerInfo.get(inv_.Inviting).uuids.add(uuid);
		pl.performCommand("ob j"); 
		Invitation.list.remove(inv_);
		return true; 
    }
    
    public PlayerInfo checkguest(UUID uuid) {
    	Guest g = Guest.check(uuid);
		if (g == null) return null;
		if (!PlayerInfo.ExistId(g.Inviting)) return null;
		return PlayerInfo.get(g.Inviting);
    }

	public int[] getFullCoord(final int id) {
		int x = 0, z = 0;
		if (!СircleMode)
			return new int[] {id * sto + Oneblock.x, Oneblock.z};
		for (int i = 0; i < id; i++) {
			if (x > z)
			    if (x > -z)
				    z--;
				else
				    x--;
			else if (-x > z || x == z && z < 0)
				z++;
			else
				x++;
		}
		x = x * sto + Oneblock.x;
		z = z * sto + Oneblock.z;
		return new int[] {x, z};
	}
	
	public class TaskUpdatePlayers implements Runnable {
		public void run() { plonl = wor.getPlayers(); }
	}
	
	public class TaskSaveData implements Runnable {
		public void run() { saveData(); }
	}

    public class Task implements Runnable {
        public void run() {
            for (Player ponl: plonl) {
            	final UUID uuid = ponl.getUniqueId();
            	if (!PlayerInfo.ExistId(uuid))
            		continue;
            	final int plID = PlayerInfo.GetId(uuid);
            	final int result[] = getFullCoord(plID);
                final int X_pl = result[0], Z_pl = result[1];
            	
                if (protection && !ponl.hasPermission("Oneblock.ignoreBarrier")) {
                	boolean CheckGuest = false;
                	Location loc = ponl.getLocation();
            		PlayerInfo inf = checkguest(ponl.getUniqueId());
            		if (inf != null) {
                    	int crd[] = getFullCoord(PlayerInfo.GetId(inf.uuid));
                        CheckGuest = CheckPosition(loc, crd[0], crd[1]);
                        if (!CheckGuest) Guest.remove(uuid);
            		}
            		if (!CheckPosition(loc, X_pl, Z_pl) && !CheckGuest) {
                    	ponl.performCommand("ob j");
                		ponl.sendMessage(Messages.protection);
                    	continue;
                    }
                }
                
                final Block block = wor.getBlockAt(X_pl, y, Z_pl);
                if (block.getType().equals(Material.AIR)) 
                	BlockGen(X_pl, Z_pl, plID, ponl, block);
            }
        }
    }
    
    public boolean CheckPosition(Location loc, int X_pl, int Z_pl) {
    	X_pl = loc.getBlockX()-X_pl;
    	Z_pl = СircleMode ? loc.getBlockZ()-Z_pl : 0;
    	int val = Math.abs(sto/2) + 1;
    	return (Math.abs(X_pl) <= val && Math.abs(Z_pl) <= val);
    }
    
    public void BlockGen(final int X_pl, final int Z_pl, final int plID, final Player ponl, final Block block) {
    	final PlayerInfo inf = PlayerInfo.get(plID);
    	Level lvl_inf = Level.get(inf.lvl); 
        if (++inf.breaks >= inf.getNeed()) {
        	inf.lvlup();
        	lvl_inf = Level.get(inf.lvl); 
            if (Progress_bar) {
            	inf.bar.setColor(lvl_inf.color);
            	if (lvl_bar_mode)
            		inf.bar.setTitle(lvl_inf.name);
            }
            if (chat_alert)
            	ponl.sendMessage(String.format("%s%s", ChatColor.GREEN, lvl_inf.name));
        }
        if (Progress_bar) {
            if (!lvl_bar_mode && PAPI)
            	inf.bar.setTitle(PlaceholderAPI.setPlaceholders(ponl, TextP));
            inf.bar.setProgress(inf.getPercent());
            inf.bar.addPlayer(ponl);
        }
        random = lvl_inf.blocks;
        if (random != 0) random = rnd.nextInt(random);
        if (blocks.get(random) == null) {
            XBlock.setType(block, GRASS_BLOCK);
            if (rnd.nextInt(3) == 1)
                XBlock.setType(wor.getBlockAt(X_pl, y + 1, Z_pl), flowers.get(rnd.nextInt(flowers.size())));
        } else if (blocks.get(random) == Material.CHEST) {
            block.setType(Material.CHEST);
            Chest chest = (Chest) block.getState();
            Inventory inv = chest.getInventory();
            ChestItems.type chestType;
            if (random < blocks.size() / 3)	chestType = ChestItems.type.SMALL;
            else if (random < blocks.size() / 1.5)	chestType = ChestItems.type.MEDIUM;
            else	chestType = ChestItems.type.HIGH;
            	
            if (!ChestItems.fillChest(inv, chestType))
            	getLogger().warning("Error when generating items for the chest! Pls redo chests.yml!");
        } 
        else XBlock.setType(block, blocks.get(random), physics);

        if (rnd.nextInt(9) == 0) {
            if ((random = lvl_inf.mobs) != 0) random = rnd.nextInt(random);
            wor.spawnEntity(new Location(wor, X_pl + .5, y + 1, Z_pl + .5), mobs.get(random));
        }
	}

    public void onDisable() {
        config.set("custom_island", Island.map());
        saveData();
        Config.Save(config);
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("spawn")) {
            Player p = (Player) sender;
            PlayerInfo.removeBarStatic(p);
            if (config.getDouble("yleave") == 0 || leavewor == null) {
                sender.sendMessage(String.format("%sSorry, but the position was not set.", ChatColor.YELLOW));
                return true;
            }
            p.teleport(new Location(leavewor, config.getDouble("xleave"), config.getDouble("yleave"), config.getDouble("zleave"),
                    (float)config.getDouble("yawleave"), 0f));
            return true;}

        if (cmd.getName().equalsIgnoreCase("oneblock")) {
            //
            if (args.length == 0)
                return ((Player)sender).performCommand("ob j");
            
            if (!sender.hasPermission("Oneblock.join")) {
                sender.sendMessage(String.format("%sYou don't have permission [Oneblock.join].", ChatColor.RED));
                return true;
            }
            //
            String parametr = args[0].toLowerCase();
            switch (parametr)
            {
            case ("j"):
            case ("join"):{
                if (config.getInt("y") == 0 || wor == null) {
                	sender.sendMessage(String.format("%sFirst you need to set the reference coordinates '/ob set'.", ChatColor.YELLOW));
                	return true;
                }
                Player p = (Player) sender;
                UUID uuid = p.getUniqueId();
                int plID = 0;
                int X_pl = 0, Z_pl = 0;
                if (!PlayerInfo.ExistId(uuid)) {
                	PlayerInfo inf = new PlayerInfo(uuid);
                	if (UseEmptyIslands)
                		plID = PlayerInfo.getNull();
                	else
                		plID = PlayerInfo.size();
                	int result[] = getFullCoord(plID);
                	X_pl = result[0]; Z_pl = result[1];
                	if (plID != PlayerInfo.size())
                		Island.clear(wor, X_pl, y, Z_pl, sto/4);
                    if (il3x3)
                    	Island.place(wor, X_pl, y, Z_pl);
                    if (WorldGuard) {
                    	Vector Block1 = new Vector(X_pl - sto/2 + 1, 0, Z_pl - sto/2 + 1);
                    	Vector Block2 = new Vector(X_pl + sto/2 - 1, 255, Z_pl + sto/2 - 1);
                    	OBWG.CreateRegion(uuid, Block1, Block2, plID);
                    }
					PlayerInfo.set(plID, inf);
                    if (!superlegacy && Progress_bar) {
                    	String temp = TextP;
                        if (lvl_bar_mode)
                        	temp = Level.get(0).name;
                        else if (PAPI)
                        	temp = PlaceholderAPI.setPlaceholders(p, TextP);
                        inf.bar = Bukkit.createBossBar(temp, Level.get(0).color, BarStyle.SEGMENTED_10, BarFlag.DARKEN_SKY);
                    }
                } 
                else {
                	plID = PlayerInfo.GetId(uuid);
                	int result[] = getFullCoord(plID);
                    X_pl = result[0]; Z_pl = result[1];
                }
                if (!on) runMainTask();
                if (Progress_bar) PlayerInfo.get(plID).bar.setVisible(true);
                p.teleport(new Location(wor, X_pl + 0.5, y + 1.2013, Z_pl + 0.5));
                if (WorldGuard) OBWG.addMember(uuid, plID);
                if (Border) {
                	WorldBorder br = Bukkit.createWorldBorder();
                	br.setCenter(X_pl, Z_pl);
                	br.setSize(sto);
                	p.setWorldBorder(br);
                }
                return true;
            }
            //Leave command was here
            case ("visit"):{
            	if (!sender.hasPermission("Oneblock.visit")) {
                    sender.sendMessage(Messages.noperm_inv);
                    return true;
                }
            	if (OBWG == null || !WorldGuard) {
                    sender.sendMessage(String.format("%sThis feature is only available when worldguard is enabled.", ChatColor.YELLOW));
                    return true;
                }
            	Player pl = (Player) sender;
                if (args.length < 2) {
            		GUI.visitGUI(pl, plonl);
            		return true;
            	}
                Player inv = Bukkit.getPlayer(args[1]);
            	if (inv == null) return true;
        		if (inv == pl) {
        			pl.performCommand("ob j");
        			return true;
        		}
        		UUID uuid = inv.getUniqueId();
        		if (!PlayerInfo.ExistId(uuid)) {
        			sender.sendMessage(Messages.invite_no_island);
        			return true;
        		}
        		PlayerInfo pinf = PlayerInfo.get(uuid);
        		if (!pinf.allow_visit) return true;
        		final int plID = PlayerInfo.GetId(uuid);
            	final int result[] = getFullCoord(plID);
                final int X_pl = result[0], Z_pl = result[1];
        		
                if (protection) Guest.list.add(new Guest(uuid, pl.getUniqueId()));
                pl.teleport(new Location(wor, X_pl + 0.5, y + 1.2013, Z_pl + 0.5));
                if (Border) {
                	WorldBorder br = Bukkit.createWorldBorder();
                	br.setCenter(X_pl, Z_pl);
                	br.setSize(sto);
                	pl.setWorldBorder(br);
                }
        		PlayerInfo.removeBarStatic(pl);
                return true;
            }
            case ("allow_visit"):{
            	if (!sender.hasPermission("Oneblock.visit")) {
                    sender.sendMessage(Messages.noperm_inv);
                    return true;
                }
            	Player pl = (Player) sender;
            	UUID uuid = pl.getUniqueId();
            	if (!PlayerInfo.ExistId(uuid)) return true;
            	PlayerInfo inf = PlayerInfo.get(uuid);
            	inf.allow_visit = !inf.allow_visit;
            	pl.sendMessage(String.format("%sYou have %s a visit to your island.", ChatColor.GREEN, (inf.allow_visit?"allowed":"forbidden")));
            	return true;
            }
            case ("set"):{
                if (!sender.hasPermission("Oneblock.set")) {
                    sender.sendMessage(Messages.noperm);
                    return true;
                }
                Player p = (Player) sender;
                Location l = p.getLocation();
                x = l.getBlockX();
                y = l.getBlockY();
                z = l.getBlockZ();
                wor = l.getWorld();
                int temp = 100;
                if (args.length >= 2) {
                    try {
                    	temp = Integer.parseInt(args[1]);
                    } catch (NumberFormatException nfe) {
                    	sender.sendMessage(Messages.invalid_value);
                    	return true;
                    }
                    if (temp > 1000 || temp < -1000) {
                    	sender.sendMessage(String.format("%spossible values are from -1000 to 1000", ChatColor.RED));
                    	return true;
                    }
                    sto = temp;
                    config.set("set", sto);
                }
                config.set("world", wor.getName());
                config.set("x", (double) x);
                config.set("y", (double) y);
                config.set("z", (double) z);
                Config.Save(config);
                if (!on) runMainTask();
                wor.getBlockAt(x, y, z).setType(GRASS_BLOCK.parseMaterial());
                ReCreateRegions();
                return true;
            }
            case ("setleave"):{
                if (!sender.hasPermission("Oneblock.set")) {
                    sender.sendMessage(Messages.noperm);
                    return true;
                }
                Player p = (Player) sender;
                Location l = p.getLocation();
                leavewor = l.getWorld();
                config.set("leaveworld", leavewor.getName());
                config.set("xleave", l.getX());
                config.set("yleave", l.getY());
                config.set("zleave", l.getZ());
                config.set("yawleave", l.getYaw());
                Config.Save(config);
                return true;
            }
            case ("invite"):{
            	if (!sender.hasPermission("Oneblock.invite")) {
                    sender.sendMessage(Messages.noperm_inv);
                    return true;
                }
            	if (args.length < 2) {
            		sender.sendMessage(Messages.invite_usage);
            		return true;
            	}
            	Player inv = Bukkit.getPlayer(args[1]);
            	if (inv == null) 
            		return true;
            	Player pl = (Player) sender;
        		if (inv == pl) {
        			sender.sendMessage(Messages.invite_yourself);
        			return true;
        		}
        		UUID uuid = pl.getUniqueId();
        		if (!PlayerInfo.ExistId(uuid)) {
        			sender.sendMessage(Messages.invite_no_island);
        			return true;
        		}
        		if (max_players_team != 0) {
        			PlayerInfo pinf = PlayerInfo.get(uuid);
        			if (pinf.uuids.size() >= max_players_team) {
            			sender.sendMessage(String.format(Messages.invite_team, max_players_team));
            			return true;
        			}
        		}
        		addinvite(uuid, inv.getUniqueId());
        		String name = pl.getName();
        		GUI.acceptGUI(inv, name);
        		inv.sendMessage(String.format(Messages.invited, name));
        		sender.sendMessage(String.format(Messages.invited_succes, inv.getName()));
            	return true;
            }
            case ("kick"):{
            	if (args.length < 2) {
            		sender.sendMessage(Messages.kick_usage);
            		return true;
            	}
            	Player member = Bukkit.getPlayer(args[1]);
            	if (member == null) return true;
            	Player owner = (Player) sender;
            	if (member == owner) {
            		sender.sendMessage(Messages.kick_yourself);
            		return true;
            	}
            	UUID owner_uuid = owner.getUniqueId(), member_uuid = member.getUniqueId();
            	if (!PlayerInfo.ExistNoInvaitId(owner_uuid))
            		return true;
            	int plID = PlayerInfo.GetId(owner_uuid);
            	PlayerInfo info = PlayerInfo.get(plID);
            	if (info.uuids.contains(member_uuid)) {
            		info.uuids.remove(member_uuid);
            		if (WorldGuard)
        				OBWG.removeMember(member_uuid, plID);
            		member.performCommand("ob j");
            	}
            	return true;
            }
            case ("accept"):{
            	Player pl = (Player) sender;
           	 	if (checkinvite(pl))
           	 		sender.sendMessage(Messages.accept_succes);
           	 	else
           	 		sender.sendMessage(Messages.accept_none);
           		return true;
            }
            case ("idreset"):{
            	Player pl = (Player)sender;
            	UUID uuid = pl.getUniqueId();
            	if (!PlayerInfo.ExistId(uuid))
            		return true;
            	int PlId = PlayerInfo.GetId(uuid);
            	PlayerInfo plp = PlayerInfo.get(PlId);
            	if (Progress_bar)
            		plp.bar.removePlayer(pl);
            	if (plp.uuid.equals(uuid)) {
            		if (plp.uuids.size() > 0) {
            			plp.uuid = plp.uuids.get(0);
            			plp.uuids.remove(0);
            		}
            		else plp.uuid = null;
            	}
            	else plp.uuids.remove(uuid);
            	
            	if (!saveplayerinventory) pl.getInventory().clear();
            		
            	if (WorldGuard) 
            		OBWG.removeMember(uuid, PlId);
            	if (!args[args.length-1].equals("/n"))
            		sender.sendMessage(Messages.idreset);
            	return true;
            }
            case ("worldguard"):{
            	if (!sender.hasPermission("Oneblock.set")) {
                    sender.sendMessage(Messages.noperm);
                    return true;
                }
            	if (!Bukkit.getPluginManager().isPluginEnabled("WorldGuard")){
                    sender.sendMessage(String.format("%sThe WorldGuard plugin was not detected!", ChatColor.YELLOW));
                    return true;
                }
            	if (OBWG == null || !OBWorldGuard.canUse) {
                    sender.sendMessage(String.format("%sThis feature is only available in the premium version of the plugin!", ChatColor.YELLOW));
                    return true;
                }
            	if (args.length > 1 &&
                	(args[1].equals("true") || args[1].equals("false"))) {
                    	WorldGuard = Boolean.valueOf(args[1]);
                    	config.set("WorldGuard", WorldGuard);
                    	if (WorldGuard)
                    		ReCreateRegions();
                    	else
                    		OBWG.RemoveRegions(PlayerInfo.size());
                }
                else sender.sendMessage(Messages.bool_format);
            	sender.sendMessage(String.format("%sthe OBWorldGuard is now %s", ChatColor.GREEN, (WorldGuard?"enabled.":"disabled.")));
           		return true;
            }
            case ("border"):{
            	if (!sender.hasPermission("Oneblock.set")) {
                    sender.sendMessage(Messages.noperm);
                    return true;
                }
            	if (!findMethod(Bukkit.class, "createWorldBorder")){
                    sender.sendMessage(String.format("%sThe border can only be used on version 1.18.2 and above!", ChatColor.YELLOW));
                    return true;
                }
            	if (args.length > 1 &&
                	(args[1].equals("true") || args[1].equals("false"))) {
            			Border = Boolean.valueOf(args[1]);
                    	config.set("Border", Border);
                    	if (Border) {
                    		for (Player pl: plonl) {
                    			UUID uuid = pl.getUniqueId();
                            	if (!PlayerInfo.ExistId(uuid))
                            		continue;
                            	int result[] = getFullCoord(PlayerInfo.GetId(uuid));
                                int X_pl = result[0], Z_pl = result[1];
                            	WorldBorder br = Bukkit.createWorldBorder();
                            	br.setCenter(X_pl, Z_pl);
                            	br.setSize(sto);
                            	pl.setWorldBorder(br);
                            }
                    	}
                    	else
                    		for (Player pl: plonl) 
                    			pl.setWorldBorder(null);
                }
                else sender.sendMessage(Messages.bool_format);
            	sender.sendMessage(String.format("%sthe Border is now %s", ChatColor.GREEN, (Border?"enabled.":"disabled.")));
           		return true;
            }
            case ("circlemode"):
            	parametr = "СircleMode";
            case ("useemptyislands"):
            case ("protection"):
            case ("droptossup"):
            case ("physics"):
            case ("autojoin"):
            case ("saveplayerinventory"):{
            	if (!sender.hasPermission("Oneblock.set")) {
                    sender.sendMessage(Messages.noperm);
                    return true;
                }
            	if (args.length > 1 &&
                    	(args[1].equals("true") || args[1].equals("false"))) {
                    	config.set(parametr, Boolean.valueOf(args[1]));
                    	UpdateParametrs();
                }
                else sender.sendMessage(Messages.bool_format);
                sender.sendMessage(String.format("%s%s is now %s", ChatColor.GREEN, parametr, (config.getBoolean(parametr)?"enabled.":"disabled.")));
           		return true;
            }
            //LVL
            case ("setlevel"):{
                if (!sender.hasPermission("Oneblock.set")) {
                    sender.sendMessage(Messages.noperm);
                    return true;
                }
                if (args.length <= 2) {
                    sender.sendMessage(String.format("%sinvalid format. try: /ob setlevel 'nickname' 'level'", ChatColor.RED));
                    return true;
                }
                UUID uuid = Bukkit.getPlayer(args[1]).getUniqueId();
                if (PlayerInfo.ExistId(uuid)) {
                    int setlvl = 0;
                    try {
                        setlvl = Integer.parseInt(args[2]);
                    } catch (NumberFormatException nfe) {
                        sender.sendMessage(String.format("%sinvalid level value.", ChatColor.RED));
                        return true;
                    }
                    if (setlvl >= 0 && 10000 > setlvl) {
                        int i = PlayerInfo.GetId(uuid);
                        PlayerInfo inf = PlayerInfo.get(i);
                        inf.breaks = 0;
                        inf.lvl = setlvl;
                        if (lvl_bar_mode) {
                        	Level lvl = Level.get(inf.lvl);
	                    	inf.bar.setTitle(lvl.name);
	                    	inf.bar.setColor(lvl.color);
                        }
                        sender.sendMessage(String.format("%sfor player %s, level %s is set.", ChatColor.GREEN, args[1], args[2]));
                        return true;
                    }
                    sender.sendMessage(String.format("%sinvalid level value.", ChatColor.RED));
                    return true;
                }
                sender.sendMessage(String.format("%sa player named %s was not found.", ChatColor.RED, args[1]));
                return true;
            }
            case ("clear"):{
                if (!sender.hasPermission("Oneblock.set")) {
                    sender.sendMessage(Messages.noperm);
                    return true;
                }
                if (args.length <= 1) {
                    sender.sendMessage(String.format("%sinvalid format. try: /ob clear 'nickname'", ChatColor.RED));
                    return true;
                }
                UUID uuid = Bukkit.getPlayer(args[1]).getUniqueId();
                if (PlayerInfo.ExistId(uuid)) {
                    int i = PlayerInfo.GetId(uuid);
                    PlayerInfo inf = PlayerInfo.get(i);
                    inf.breaks = 0;
                    inf.lvl = 0;
                    if (Progress_bar)
                    	inf.bar.setVisible(false);
                    int result[] = getFullCoord(i);
                    Island.clear(wor, result[0], y, result[1], sto/4);
                    sender.sendMessage(String.format("%splayer %s island is destroyed! :D", ChatColor.GREEN, args[1]));
                    return true;
                }
                sender.sendMessage(String.format("%sa player named %s was not found.", ChatColor.RED, args[1]));
                return true;
            }
            case ("lvl_mult"):{
                if (!sender.hasPermission("Oneblock.set")) {
                    sender.sendMessage(Messages.noperm);
                    return true;
                }
                if (args.length <= 1) {
                    sender.sendMessage(String.format("%slevel multiplier now: %d\n5 by default", ChatColor.GREEN, Level.multiplier));
                    return true;
                }
                int lvl = Level.multiplier;
                try {
                    lvl = Integer.parseInt(args[1]);
                } catch (NumberFormatException nfe) {
                    sender.sendMessage(String.format("%sinvalid multiplier value.", ChatColor.RED));
                    return true;
                }
                if (lvl <= 20 && lvl >= 0) {
                	Level.multiplier = lvl;
                    config.set("level_multiplier", Level.multiplier);
                    Blockfile();
                } else
                    sender.sendMessage(String.format("%spossible values: from 0 to 20.", ChatColor.RED));
                sender.sendMessage(String.format("%slevel multiplier now: %d\n5 by default", ChatColor.GREEN, Level.multiplier));
                return true;
            }
            case ("max_players_team"):{
                if (!sender.hasPermission("Oneblock.set")) {
                    sender.sendMessage(Messages.noperm);
                    return true;
                }
                if (args.length <= 1) {
                    sender.sendMessage(String.format("%smax_players_team now: %d\n5 by default", ChatColor.GREEN, max_players_team));
                    return true;
                }
                int mpt = max_players_team;
                try {
                	mpt = Integer.parseInt(args[1]);
                } catch (NumberFormatException nfe) {
                    sender.sendMessage(String.format("%sinvalid max_players_team value.", ChatColor.RED));
                    return true;
                }
                if (mpt <= 20 && mpt >= 0) 
                    config.set("max_players_team", max_players_team = mpt);
                else
                    sender.sendMessage(String.format("%spossible values: from 0 to 20.", ChatColor.RED));
                sender.sendMessage(String.format("%smax_players_team now: %d", ChatColor.GREEN, max_players_team));
                return true;
            }
            case ("progress_bar"):{
                if (!sender.hasPermission("Oneblock.set")) {
                    sender.sendMessage(Messages.noperm);
                    return true;
                }
                if (superlegacy) {
                    sender.sendMessage(String.format("%sYou server version is super legacy! ProgressBar unsupported!", ChatColor.RED));
                    return true;
                }
                if (args.length == 1) {
                    sender.sendMessage(String.format("%sand?", ChatColor.YELLOW));
                    return true;
                }
                if (args[1].equals("true") || args[1].equals("false")) {
                    Progress_bar = Boolean.valueOf(args[1]);
                    if (Progress_bar) {
                        if (Progress_color == null)
                            Progress_color = BarColor.GREEN;
                    	Blockfile();
                    }
                    for (PlayerInfo bb:PlayerInfo.list)
                    	if (bb.bar != null)
                    		bb.bar.setVisible(Progress_bar);
                    config.set("Progress_bar", Progress_bar);
                    return true;
                }
                if (args[1].equalsIgnoreCase("color")) {
                    if (args.length == 2) {
                        sender.sendMessage(String.format("%senter a color name.", ChatColor.YELLOW));
                        return true;
                    }
                    try {
                        Progress_color = BarColor.valueOf(args[2]);
                        for (PlayerInfo bb:PlayerInfo.list)
                            bb.bar.setColor(Progress_color);
                        Blockfile();
                        config.set("Progress_bar_color", Progress_color.toString());
                    } catch (Exception e) {
                        sender.sendMessage(String.format("%sPlease enter a valid color. For example: RED", ChatColor.YELLOW));
                    }
                    sender.sendMessage(String.format("%sProgress bar color = %s", ChatColor.GREEN, Progress_color.toString()));
                    return true;
                }
                if (args[1].equalsIgnoreCase("level")) {
                	if (!Progress_bar)
                		return true;
                    if (lvl_bar_mode = !lvl_bar_mode) {
                        for (PlayerInfo inf:PlayerInfo.list)
                        	inf.bar.setTitle(Level.get(inf.lvl).name);
                        config.set("Progress_bar_text", "level");
                        return true;
                    } else {
                        for (PlayerInfo inf:PlayerInfo.list)
                        	inf.bar.setTitle("Progress bar");
                        config.set("Progress_bar_text", "Progress bar");
                        return true;
                    }
                }
                if (args[1].equalsIgnoreCase("settext")) {
                	if (!Progress_bar)
                		return true;
                    String txt_bar = "";
					for (int i = 2; i < args.length; i++)
						txt_bar = i == 2 ? args[i] : String.format("%s %s", txt_bar, args[i]);
                    lvl_bar_mode = false;
                    if (PAPI) for (Player ponl : plonl)
                    	PlayerInfo.get(ponl.getUniqueId()).bar.setTitle(PlaceholderAPI.setPlaceholders(ponl, txt_bar));
                    else 
                    	for (PlayerInfo bb : PlayerInfo.list) bb.bar.setTitle(txt_bar);
                    config.set("Progress_bar_text", txt_bar);
                    TextP = txt_bar;
                    return true;
                }
                sender.sendMessage(String.format("%strue, false, settext or level only!", ChatColor.RED));
                return true;
            }
            case ("listlvl"):{
                if (!sender.hasPermission("Oneblock.set")) {
                    sender.sendMessage(Messages.noperm);
                    return true;
                }
                if (args.length >= 2) {
                	int temp = 0;
                    try {
                    	temp = Integer.parseInt(args[1]);
                    } catch (NumberFormatException nfe) {
                    	sender.sendMessage(Messages.invalid_value);
                    	return true;
                    }
                    if (Level.size()<=temp||temp<0) {
                    	sender.sendMessage(String.format("%sundefined lvl", ChatColor.RED));
                    	return true;
                    }
                    sender.sendMessage(String.format("%s%s",ChatColor.GREEN, Level.get(temp).name));
                    int i = 0;
                    if (temp !=0)
                    	i = Level.get(temp-1).blocks;
                    for(;i<Level.get(temp).blocks;i++)
                    	if (blocks.get(i) == null)
                    		sender.sendMessage("Grass or undefined");
                    	else if (blocks.get(i).getClass() == Material.class)
                    		sender.sendMessage(((Material)blocks.get(i)).name());
                    	else if (blocks.get(i).getClass() == XMaterial.class)
                    		sender.sendMessage(((XMaterial)blocks.get(i)).name());
                    	else
                    		sender.sendMessage((String)blocks.get(i));
                    return true;
                }
                for(int i = 0;i<Level.size();i++)
                	sender.sendMessage(String.format("%d: %s%s", i, ChatColor.GREEN, Level.get(i).name));
                return true;
            }
            case ("reload"):{
                if (!sender.hasPermission("Oneblock.set")) {
                    sender.sendMessage(Messages.noperm);
                    return true;
                }
                if (args.length == 1) {
                    sender.sendMessage(String.format("%sReloading Plugin & Plugin Modules.", ChatColor.YELLOW));
                    Blockfile();
                    Flowerfile();
                    Chestfile();
                    Messagefile();
                    ReCreateRegions();
                    sender.sendMessage(String.format("%sAll *.yml reloaded!", ChatColor.GREEN));
                    return true;
                }
            }
            case ("chat_alert"):{
                if (!sender.hasPermission("Oneblock.set")) {
                    sender.sendMessage(Messages.noperm);
                    return true;
                }
                chat_alert = !chat_alert;
                sender.sendMessage(ChatColor.GREEN + (chat_alert?"Alerts are now on!":"Alerts are now disabled!"));
                config.set("Chat_alert", chat_alert);
                return true;
            }
            case ("islands"):{
                if (!sender.hasPermission("Oneblock.set")) {
                    sender.sendMessage(Messages.noperm);
                    return true;
                }
                if (args.length == 1) {
                    sender.sendMessage(Messages.bool_format);
                    return true;
                }
                if (args[1].equals("true") || args[1].equals("false")) {
                    il3x3 = Boolean.valueOf(args[1]);
                    config.set("Island_for_new_players", il3x3);
                    sender.sendMessage(ChatColor.GREEN + "Island_for_new_players = " + il3x3);
                    return true;
                }
                if (args[1].equals("set_my_by_def")) {
                	if (legacy) {
                		sender.sendMessage(ChatColor.RED + "Not supported in legacy versions!");
                		return true;
                	}
                	Player p = (Player) sender;
                	UUID uuid = p.getUniqueId();
                    if (PlayerInfo.ExistId(uuid)) {
                        int result[] = getFullCoord(PlayerInfo.GetId(uuid));
                        Island.scan(wor, result[0], y, result[1]);
                        sender.sendMessage(ChatColor.GREEN + "Your island has been successfully saved and set as default for new players!");
                    } else
                        sender.sendMessage(ChatColor.RED + "You don't have an island!");
                    return true;
                }
                if (args[1].equalsIgnoreCase("default")) {
                	if (legacy) {
                		sender.sendMessage(ChatColor.RED + "Not supported in legacy versions!");
                		return true;
                	}
                    config.set("custom_island", Island.island = null);
                    sender.sendMessage(ChatColor.GREEN + "The default island is installed.");
                    return true;
                }
                sender.sendMessage(Messages.bool_format);
                return true;
            }
            case ("island_rebirth"):{
                if (!sender.hasPermission("Oneblock.set")) {
                    sender.sendMessage(Messages.noperm);
                    return true;
                }
                if (args.length == 1) {
                    sender.sendMessage(Messages.bool_format);
                    return true;
                }
                if (args[1].equals("true") || args[1].equals("false")) {
                    rebirth = Boolean.valueOf(args[1]);
                    config.set("Rebirth_on_the_island", rebirth);
                    sender.sendMessage(ChatColor.GREEN + "Rebirth_on_the_island = " + rebirth);
                    return true;
                }
                sender.sendMessage(Messages.bool_format);
                return true;
            }
            case ("gui"):{
            	if (args.length == 1) {
            		GUI.openGUI((Player) sender);
            		return true;
            	}
            	if (!sender.hasPermission("Oneblock.set")) {
                    sender.sendMessage(Messages.noperm);
                    return true;
                }
            	if (args.length > 1 &&
                    	(args[1].equals("true") || args[1].equals("false"))) {
                    	config.set(parametr, Boolean.valueOf(args[1]));
                        GUI.enabled = Check("gui", GUI.enabled);
                }
                else sender.sendMessage(Messages.bool_format);
            	if (!GUI.enabled)
            		for (Player pl : plonl)
            			pl.closeInventory();
                sender.sendMessage(String.format("%s%s is now %s", ChatColor.GREEN, parametr, (GUI.enabled?"enabled.":"disabled.")));
            	return true;
            }
            case ("top"):{
            	GUI.topGUI((Player) sender);
            	return true;
            }
            case ("chest"):{
            	if (!sender.hasPermission("Oneblock.set"))
            		return true;
            	if (args.length < 2) {
            		GUI.chestGUI((Player) sender, ChestItems.type.SMALL);
            		return true;
            	}
            	
            	for (type t : type.values()) 
            		if (args[1].equals(t.name())) 
            			GUI.chestGUI((Player) sender, ChestItems.type.valueOf(args[1]));
            	return true;
            }
            case ("help"):{
            	if (sender.hasPermission("Oneblock.set"))
            		sender.sendMessage(Messages.help_adm);
            	else
            		sender.sendMessage(Messages.help);
            	return true;
            }
            default:
            //ver
            sender.sendMessage(String.format("%s%s\n%s\n%s\n%s\n%s%s 1.%d.X",
            	ChatColor.values()[rnd.nextInt(ChatColor.values().length)],
            	"  ▄▄    ▄▄",
            	"█    █  █▄▀",
            	"▀▄▄▀ █▄▀",
            	"Create by MrMarL\nPlugin version: v1.1.6f",
            	"Server version: ", superlegacy?"super legacy":(legacy?"legacy":""), XMaterial.getVersion()));
            return true;
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Error.");
            return false;
        }
    }

    private void Datafile() {
    	File PlData = new File(getDataFolder(), "PlData.json");
		if (PlData.exists())
			PlayerInfo.list = JsonSimple.Read(PlData);
		else
			PlayerInfo.list = ReadOldData.Read(new File(getDataFolder(), "PlData.yml"));
    }
    
    private void ReCreateRegions() {
    	if (!WorldGuard || !OBWorldGuard.canUse)
    		return;
    	int id = PlayerInfo.size();
    	OBWG.RemoveRegions(id);
		for (int i = 0; i < id; i++) {
			PlayerInfo owner = PlayerInfo.get(i);
			if (owner.uuid == null)
    			continue;
            int result[] = getFullCoord(i);
            int X_pl = result[0], Z_pl = result[1];
			Vector Block1 = new Vector(X_pl - sto/2 + 1, 0, Z_pl - sto/2 + 1);
        	Vector Block2 = new Vector(X_pl + sto/2 - 1, 255, Z_pl + sto/2 - 1);
        	OBWG.CreateRegion(owner.uuid, Block1, Block2, i);
            for (UUID member: owner.uuids) 
            	OBWG.addMember(member, i);
        }
    }

    public void saveData() {
        try {
        	File PlData = new File(getDataFolder(), "PlData.json");
    		JsonSimple.Write(PlayerInfo.list, PlData);
        } 
        catch (Exception e) { e.printStackTrace(); }
    }

    private void Blockfile() {
    	blocks.clear();
    	mobs.clear();
    	Level.levels.clear();
        File block = new File(getDataFolder(), "blocks.yml");
        if (!block.exists())
            saveResource("blocks.yml", false);
        newConfigz = YamlConfiguration.loadConfiguration(block);
        if (newConfigz.isString("MaxLevel"))
        	Level.max.name = newConfigz.getString("MaxLevel");
        for (int i = 0; newConfigz.isList(String.format("%d", i)); i++) {
        	List <String> bl_temp = newConfigz.getStringList(String.format("%d", i));
        	Level level = new Level(bl_temp.get(0));
        	Level.levels.add(level);
        	int q = 1;
        	if (Progress_bar && q<bl_temp.size())
        		try {//reading a custom color for the level.
        			level.color = BarColor.valueOf(bl_temp.get(q).toUpperCase());
        			q++;
        		} catch(Exception e) {level.color = Progress_color;}
	        	try {//reading a custom size for the level.
	        		int value = Integer.parseInt(bl_temp.get(q));
	    			level.length = value > 0 ? value : 1;
	    			q++;
	    		} catch(Exception e) {level.length = 16 + level.getId() * Level.multiplier;}
        	while (q < bl_temp.size()) {
        		String text = bl_temp.get(q++);
        		//reading a custom block (command).
        		if (text.charAt(0) == '/') {
	            	blocks.add(text.replaceFirst("/", ""));
	            	continue;
        		}
        		//reading a mob.
        		try { mobs.add(EntityType.valueOf(text)); continue; }
        		catch (Exception e) {}
        		//read a material
        		if (legacy){
        			Optional <XMaterial> a = XMaterial.matchXMaterial(text);
	        		if (!a.isPresent()) {
	        			blocks.add(null);
	        			continue;
	        		}
	        		XMaterial xmt = a.get();
	        		if (xmt == GRASS_BLOCK)
		                blocks.add(null);
	        		else if (xmt == XMaterial.CHEST)
		                blocks.add(Material.CHEST);
	        		else
	        			blocks.add(xmt);
        		}
        		else {
        			Material a = Material.matchMaterial(text);
        			if (a != null && a.equals(Material.GRASS_BLOCK))
        				a = null;
        			blocks.add(a);
        		}
        	}
        	level.blocks = blocks.size();
        	level.mobs = mobs.size();
        }
        Level.max.blocks = blocks.size();
        if ((Level.max.mobs = mobs.size()) == 0) 
        	{ mobs.add(EntityType.CREEPER); getLogger().warning("Mobs are not set in the blocks.yml"); }
        //Progress_bar
        if (!superlegacy && Progress_bar && PlayerInfo.size() > 0 && PlayerInfo.get(0).bar == null) {
        	Level.max.color = Progress_color;
            for (PlayerInfo inf:PlayerInfo.list) {
                Level lvl = Level.get(inf.lvl);
                inf.bar = Bukkit.createBossBar(lvl_bar_mode?lvl.name:TextP, lvl.color, BarStyle.SEGMENTED_10, BarFlag.DARKEN_SKY);
            }
            Bukkit.getPluginManager().registerEvents(new ChangedWorld(), this);
    	}
    }
    private void Messagefile() {
        File message = new File(getDataFolder(), "messages.yml");
        if (!message.exists())
            saveResource("messages.yml", false);
        newConfigz = YamlConfiguration.loadConfiguration(message);
        
        Messages.noperm = MessageCheck("noperm", Messages.noperm);
        Messages.noperm_inv = MessageCheck("noperm_inv", Messages.noperm_inv);
        Messages.help = MessageCheck("help", Messages.help);
        Messages.help_adm = MessageCheck("help_adm", Messages.help_adm);
        Messages.invite_usage = MessageCheck("invite_usage", Messages.invite_usage);
        Messages.invite_yourself = MessageCheck("invite_yourself", Messages.invite_yourself);
        Messages.invite_no_island = MessageCheck("invite_no_island", Messages.invite_no_island);
        Messages.invite_team = MessageCheck("invite_team", Messages.invite_team);
        Messages.invited = MessageCheck("invited", Messages.invited);
        Messages.invited_succes = MessageCheck("invited_succes", Messages.invited_succes);
        Messages.kick_usage = MessageCheck("kick_usage", Messages.kick_usage);
        Messages.kick_yourself = MessageCheck("kick_yourself", Messages.kick_yourself);
        Messages.accept_succes = MessageCheck("accept_succes", Messages.accept_succes);
        Messages.accept_none = MessageCheck("accept_none", Messages.accept_none);
        Messages.idreset = MessageCheck("idreset", Messages.idreset);
        Messages.protection = MessageCheck("protection", Messages.protection);
    }
    private String MessageCheck(String name, String def_message) {
    	if (newConfigz.isString(name))
        	return ChatColor.translateAlternateColorCodes('&',newConfigz.getString(name));
    	return def_message;
    }
    private void Flowerfile() {
        flowers.clear();
        File flower = new File(getDataFolder(), "flowers.yml");
        if (!flower.exists())
            saveResource("flowers.yml", false);
        newConfigz = YamlConfiguration.loadConfiguration(flower);
        flowers.add(GRASS);
        for(String list:newConfigz.getStringList("flowers"))
        	if (!XMaterial.matchXMaterial(list).isPresent())
        		flowers.add(GRASS);
        	else
        		flowers.add(XMaterial.matchXMaterial(list).get());
    }
    private void Chestfile() {
        File chest = new File(getDataFolder(), "chests.yml");
        if (!chest.exists())
            saveResource("chests.yml", false);
        ChestItems.chest = chest;
        ChestItems.load();
    }
    
    String Check(String type, String data) {
    	if (!config.isString(type))
            config.set(type, data);
    	return config.getString(type);
    }
    int Check(String type, int data) {
    	if (!config.isInt(type))
            config.set(type, data);
    	return config.getInt(type);
    }
    double Check(String type, double data) {
    	if (!config.isDouble(type))
            config.set(type, data);
    	return config.getDouble(type);
    }
    boolean Check(String type, boolean data) {
    	if (!config.isBoolean(type))
            config.set(type, data);
    	return config.getBoolean(type);
    }
    List<String> Check(String type, List<String> data) {
    	if (!config.isList(type))
            config.set(type, data);
    	return config.getStringList(type);
    }

    private void Configfile() {
    	File con = new File(getDataFolder(), "config.yml");
        if (!con.exists())
            saveResource("config.yml", false);
        config = this.getConfig();
        wor = Bukkit.getWorld(Check("world", "world"));
        x = (int) Check("x", (double) x);
        y = (int) Check("y", (double) y);
        z = (int) Check("z", (double) z);
        leavewor = Bukkit.getWorld(Check("leaveworld", "world"));
        Check("xleave", .0);
        Check("yleave", .0);
        Check("zleave", .0);
        Check("yawleave", .0);
        if (Progress_bar = !superlegacy && Check("Progress_bar", true))
        	Progress_color = BarColor.valueOf(Check("Progress_bar_color", "GREEN"));
        if (!superlegacy) {
	        TextP = Check("Progress_bar_text", "level");
	        lvl_bar_mode = TextP.equals("level");
        }
        chat_alert = Check("Chat_alert", !lvl_bar_mode);
        il3x3 = Check("Island_for_new_players", true);
        rebirth = Check("Rebirth_on_the_island", true);
        Level.multiplier = Check("level_multiplier", Level.multiplier);
        max_players_team = Check("max_players_team", max_players_team);
        UpdateParametrs();// СircleMode;protection;autojoin;droptossup
        GUI.enabled = Check("gui", GUI.enabled);
        WorldGuard = Check("WorldGuard", WorldGuard);
        OBWorldGuard.flags = Check("WGflags", OBWorldGuard.flags);
        if (Border) Border = Check("Border", Border);
        sto = Check("set", 100);
        if (config.isSet("custom_island") && !legacy)
        	Island.read(config);
        Config.Save(config, con);
    }
    
    public void UpdateParametrs() {
    	СircleMode = Check("СircleMode", СircleMode);
    	UseEmptyIslands = Check("useemptyislands", UseEmptyIslands);
    	saveplayerinventory = Check("saveplayerinventory", saveplayerinventory);
        protection = Check("protection", protection);
        autojoin = Check("autojoin", autojoin);
        droptossup = Check("droptossup", droptossup);
        physics = Check("physics", physics);
    }
    
    public static int getlvl(UUID pl_uuid) {
    	return PlayerInfo.get(pl_uuid).lvl;
    }
    public static int getnextlvl(UUID pl_uuid) {
    	return getlvl(pl_uuid) + 1;
    }
    public static String getlvlname(UUID pl_uuid) {
    	int lvl = getlvl(pl_uuid);
    	return Level.get(lvl).name;
    }
    public static String getnextlvlname(UUID pl_uuid) {
    	int lvl = getnextlvl(pl_uuid);
    	return Level.get(lvl).name;
    }
    public static int getblocks(UUID pl_uuid) {
        return PlayerInfo.get(pl_uuid).breaks;
    }
    public static int getneed(UUID pl_uuid) {
    	PlayerInfo inf = PlayerInfo.get(pl_uuid);
    	return inf.getNeed() - inf.breaks;
    }
    public static int getlenght(UUID pl_uuid) {
    	return PlayerInfo.get(pl_uuid).getNeed();
    }
  
    @SuppressWarnings("unchecked")
	public static PlayerInfo gettop(int i) {
    	if (PlayerInfo.size() <= i)
    		return new PlayerInfo(null);
    	ArrayList<PlayerInfo> ppii = (ArrayList<PlayerInfo>) PlayerInfo.list.clone();
    	Collections.sort(ppii, PlayerInfo.COMPARE_BY_LVL);
    	if (ppii.get(i).uuid == null)
    		return new PlayerInfo(null);
        return ppii.get(i);
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> commands = new ArrayList<>();

        if (args.length == 1) {
        	commands.addAll(Arrays.asList("j","join","invite","accept","kick","ver","IDreset","help","gui","top"));
        	if (sender.hasPermission("Oneblock.visit")) commands.addAll(Arrays.asList("visit","allow_visit"));
            if (sender.hasPermission("Oneblock.set")) {
            	commands.addAll(Arrays.asList("set","setleave","Progress_bar","chat_alert","setlevel","clear","circlemode","lvl_mult","max_players_team", "chest", "saveplayerinventory",
            		"reload","islands","island_rebirth","protection","worldguard","border","listlvl","autoJoin","droptossup","physics","UseEmptyIslands"));
            }
        } else if (args.length == 2) {
        	if (args[0].equals("invite") || args[0].equals("kick") || args[0].equals("visit")) {
        		for (Player ponl: plonl)
        			commands.add(ponl.getName());
        	}
        	else if (sender.hasPermission("Oneblock.set")) {
        		switch (args[0])
                {
        		case ("chest"):
        			for (type t : type.values())
        				commands.add(t.name());
                case ("clear"):
                case ("setlevel"):{
            		for (Player ponl: plonl)
            			commands.add(ponl.getName());
            		break;
            	}
                case ("Progress_bar"):{
	                commands.add("true");
	                commands.add("false");
	                commands.add("level");
	                commands.add("settext");
	                commands.add("color");
	                break;
	            }
                case ("islands"):
	                commands.add("set_my_by_def");
	                commands.add("default");
                case ("UseEmptyIslands"):
                case ("island_rebirth"):
                case ("saveplayerinventory"):
                case ("protection"):
                case ("circlemode"):
                case ("worldguard"):
                case ("border"):
                case ("autoJoin"):
                case ("droptossup"):
                case ("physics"):
                case ("gui"):
	                commands.add("true");
	                commands.add("false");
	                break;
                case ("listlvl"):
	            	for(int i = 0;i<Level.size();)
	            		commands.add(String.format("%d", i++));
	            	break;
                case ("lvl_mult"):
                case ("max_players_team"):
                	for(int i = 0;i<4;)
	            		commands.add(String.format("%d", i++));
                case ("set"):
                	commands.add("100");
                	commands.add("500");
                }
        	}
        }
        else if (sender.hasPermission("Oneblock.set") && args.length == 3) 
        	if (args[0].equals("Progress_bar")) {
        		if (args[1].equals("color"))
        			for (BarColor bc:BarColor.values())
        				commands.add(bc.name());
        		if (args[1].equals("settext")) {
        			commands.add("...");
        			if (PAPI)
        				commands.add("%OB_lvl_name%. There are %OB_need_to_lvl_up% block(s) left.");
        		}
        	}
        	else if (args[0].equals("setlevel"))
            	for (int i = 0;i<Level.size();)
            		commands.add(String.format("%d", i++));
        Collections.sort(commands);
        return commands;
    }
}