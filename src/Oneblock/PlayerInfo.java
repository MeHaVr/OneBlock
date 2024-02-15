package Oneblock;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;

import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class PlayerInfo {
	public static ArrayList <PlayerInfo> list = new ArrayList <>();
	
	//public String nick;
	public UUID uuid;
	//public ArrayList<String> nicks = new ArrayList<String>();
	public ArrayList<UUID> uuids = new ArrayList<UUID>();
	public int lvl = 0;
	public int breaks = 0;
	public BossBar bar = null;
	
	public PlayerInfo(UUID uuid) {
		//nick = Bukkit.getServer().getOfflinePlayer(uuid).getName();
		this.uuid = uuid;
	}
	
	public void lvlup() {
		++lvl;
		breaks = 0;
	}
	
	public void removeBar(Player p) {
		if (bar == null)
			return;
		bar.removePlayer(p);
	}
	
	public int getNeed() {
        return Level.get(lvl).length;
    }
	
	public double getPercent() {
		return (double) breaks / getNeed();
	}
	
	public static void removeBarStatic(Player p) {
		get(p.getUniqueId()).removeBar(p);
	}
	
	public static int GetId(UUID name) {
    	for(int i = 0; i<PlayerInfo.size() ;i++) {
    		PlayerInfo pl = PlayerInfo.get(i);
    		if (pl.uuid == null)
    			continue;
    		if (pl.uuid.equals(name))
    			return i;
    		if (pl.uuids.contains(name))
    			return i;
    	}
    	return 0;
    }
	
	public static boolean ExistNoInvaitId(UUID name) {
    	for(PlayerInfo pl:PlayerInfo.list) {
    		if (pl.uuid == null)
    			continue;
    		if (pl.uuid.equals(name))
    			return true;
    	}
    	return false;
    }
    
	public static boolean ExistId(UUID name) {
    	for(PlayerInfo pl:PlayerInfo.list) {
    		if (pl.uuid == null)
    			continue;
    		if (pl.uuid.equals(name))
    			return true;
    		if (pl.uuids.contains(name))
    			return true;
    	}
    	return false;
    }
	
	public static PlayerInfo get(int id) {
		return list.get(id);
	}
	
	public static PlayerInfo get(UUID name) {
    	return list.get(GetId(name));
	}
	
	public static void set(int id, PlayerInfo pInf) {
		if (id < list.size())
			list.set(id, pInf);
		else
			list.add(pInf);
	}
	
	public static int size() {
		return list.size();
	}
	
	public static int getNull() {
		for (int i = 0; list.size() > i; i++)
			if (list.get(i).uuid == null) 
				return i;
		return list.size();
	}
	
	public static final Comparator<PlayerInfo> COMPARE_BY_LVL = new Comparator<PlayerInfo>() {
		@Override
		public int compare(PlayerInfo lhs, PlayerInfo rhs) {
			if (rhs.uuid == null)
				return -1;
			return rhs.lvl - lhs.lvl;
		}
	};
}