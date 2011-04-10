package com.gmail.nossr50;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.plugin.Plugin;


public class mcUsers {
    private static volatile mcUsers instance;
    protected static final Logger log = Logger.getLogger("Minecraft");
    String location = "plugins/mcMMO/mcmmo.users";
    public static PlayerList players = new PlayerList();
    private Properties properties = new Properties();
    
    //To load
    public void load() throws IOException {
        properties.load(new FileInputStream(location));
    }
    //To save
    public void save() {
        try {
        properties.store(new FileOutputStream(location), null);
        }catch(IOException ex) {
        }
    }
    
    
    public void loadUsers(){
        File theDir = new File(location);
		if(!theDir.exists()){
			//properties = new PropertiesFile(location);
			FileWriter writer = null;
			try {
				writer = new FileWriter(location);
				writer.write("#Storage place for user information\r\n");
			} catch (Exception e) {
				log.log(Level.SEVERE, "Exception while creating " + location, e);
			} finally {
				try {
					if (writer != null) {
						writer.close();
					}
				} catch (IOException e) {
					log.log(Level.SEVERE, "Exception while closing writer for " + location, e);
				}
			}

		} else {
			//properties = new PropertiesFile(location);
			try {
				load();
			} catch (IOException e) {
				log.log(Level.SEVERE, "Exception while loading " + location, e);
			}
		}
    }

	//=====================================================================
	//Function:	addUser
	//Input:	Player player: The player to create a profile for
	//Output:	none
	//Use:		Loads the profile for the specified player
	//=====================================================================
    public static void addUser(Player player){
    	players.addPlayer(player);
    }

	//=====================================================================
	//Function:	removeUser
	//Input:	Player player: The player to stop following
	//Output:	none
	//Use:		Creates the player profile
	//=====================================================================
    public static void removeUser(Player player){
    	players.removePlayer(player);
    }

	//=====================================================================
	//Function:	getProfile
	//Input:	Player player: The player to find the profile for
	//Output:	PlayerList.PlayerProfile: The profile
	//Use:		Gets the player profile
	//=====================================================================
    public static PlayerList.PlayerProfile getProfile(Player player){
    	return players.findProfile(player);
    }
    
    public static mcUsers getInstance() {
		if (instance == null) {
			instance = new mcUsers();
		}
		return instance;
	}
    public static void getRow(){

    }
}
class PlayerList
{       
    protected static final Logger log = Logger.getLogger("Minecraft");
	ArrayList<PlayerProfile> players;
	
	//=====================================================================
	//Function:	PlayerList
	//Input:	Player player: The player to create a profile object for
	//Output:	none
	//Use:		Initializes the ArrayList
	//=====================================================================
	public PlayerList() { players = new ArrayList<PlayerProfile>(); }

	//=====================================================================
	//Function:	addPlayer
	//Input:	Player player: The player to add
	//Output:	None
	//Use:		Add a profile of the specified player
	//=====================================================================
	public void addPlayer(Player player)
	{
		players.add(new PlayerProfile(player));
	}

	//=====================================================================
	//Function:	removePlayer
	//Input:	Player player: The player to remove
	//Output:	None
	//Use:		Remove the profile of the specified player
	//=====================================================================
	public void removePlayer(Player player)
	{
		players.remove(findProfile(player));
	}

	//=====================================================================
	//Function:	findProfile
	//Input:	Player player: The player to find's profile
	//Output:	PlayerProfile: The profile of the specified player
	//Use:		Get the profile for the specified player
	//=====================================================================
	public PlayerProfile findProfile(Player player)
	{
		for(PlayerProfile ply : players)
		{
			if(ply.isPlayer(player))
				return ply;
		}
		return null;
	}
	
	class PlayerProfile
	{
	    protected final Logger log = Logger.getLogger("Minecraft");
		private String playerName, gather, wgather, woodcutting, repair, mining, party, myspawn, myspawnworld, unarmed, herbalism, excavation,
		archery, swords, axes, invite, acrobatics, repairgather, unarmedgather, herbalismgather, excavationgather, archerygather, swordsgather, axesgather, acrobaticsgather;
		private boolean berserkInformed = true, skullSplitterInformed = true, gigaDrillBreakerInformed = true, superBreakerInformed = true, serratedStrikesInformed = true, treeFellerInformed = true, dead, abilityuse = true, treeFellerMode, superBreakerMode, gigaDrillBreakerMode, serratedStrikesMode, shovelPreparationMode, swordsPreparationMode, fistsPreparationMode, pickaxePreparationMode, axePreparationMode, skullSplitterMode, berserkMode;
		private long gigaDrillBreakerCooldown = 0, berserkCooldown = 0, superBreakerCooldown = 0, skullSplitterCooldown = 0, serratedStrikesCooldown = 0,
		treeFellerCooldown = 0, recentlyHurt = 0, archeryShotATS = 0, berserkATS = 0, berserkDATS = 0, gigaDrillBreakerATS = 0, gigaDrillBreakerDATS = 0,
		superBreakerATS = 0, superBreakerDATS = 0, serratedStrikesATS = 0, serratedStrikesDATS = 0, treeFellerATS = 0, treeFellerDATS = 0, 
		skullSplitterATS = 0, skullSplitterDATS = 0, axePreparationATS = 0, pickaxePreparationATS = 0, fistsPreparationATS = 0, shovelPreparationATS = 0, swordsPreparationATS = 0;
		private int berserkTicks = 0, bleedticks = 0, gigaDrillBreakerTicks = 0, superBreakerTicks = 0, serratedStrikesTicks = 0, skullSplitterTicks = 0, treeFellerTicks = 0;
		//ATS = (Time of) Activation Time Stamp
		//DATS = (Time of) Deactivation Time Stamp
		Player thisplayer;
		char defaultColor;

        String location = "plugins/mcMMO/mcmmo.users";
		
		
		//=====================================================================
		//Function:	PlayerProfile
		//Input:	Player player: The player to create a profile object for
		//Output:	none
		//Use:		Loads settings for the player or creates them if they don't
		//			exist.
		//=====================================================================
		public PlayerProfile(Player player)
		{
            //Declare things
			playerName = player.getName();
			thisplayer = player;
            party = new String();
            myspawn = new String();
            myspawnworld = new String();
            mining = new String();
            repair = new String();
            repairgather = new String();
            unarmed = new String();
            unarmedgather = new String();
            herbalism = new String();
            herbalismgather = new String();
            excavation = new String();
            excavationgather = new String();
            archery = new String();
            archerygather = new String();
            swords = new String();
            swordsgather = new String();
            axes = new String();
            axesgather = new String();
            acrobatics = new String();
            acrobaticsgather = new String();
            invite = new String();
            //mining = "0";
            wgather = new String();
            //wgather = "0";
            woodcutting = new String();
            //woodcutting = "0";
            gather = new String();
            //gather = "0";
            party = null;
            dead = false;
            treeFellerMode = false;
            //Try to load the player and if they aren't found, append them
            if(!load())
            	addPlayer();
		}
		
		public void scoreBoard()
		{
            try {
            	//Open the user file
            	FileReader file = new FileReader(location);
            	BufferedReader in = new BufferedReader(file);
            	String line = "";
            	while((line = in.readLine()) != null)
            	{
            		
            	}
            	in.close();
	        } catch (Exception e) {
	            log.log(Level.SEVERE, "Exception while reading "
	            		+ location + " (Are you sure you formatted it correctly?)", e);
	        }
		}
		public boolean load()
		{
            try {
            	//Open the user file
            	FileReader file = new FileReader(location);
            	BufferedReader in = new BufferedReader(file);
            	String line = "";
            	while((line = in.readLine()) != null)
            	{
            		//Find if the line contains the player we want.
            		String[] character = line.split(":");
            		if(!character[0].equals(playerName)){continue;}
            		
        			//Get Mining
        			if(character.length > 1)
        				mining = character[1];
        			//Myspawn
        			if(character.length > 2)
        				myspawn = character[2];
        			//Party
        			if(character.length > 3)
        				party = character[3];
        			//Mining Gather
        			if(character.length > 4)
        				gather = character[4];
        			if(character.length > 5)
        				woodcutting = character[5];
        			if(character.length > 6)
        				wgather = character[6];
        			if(character.length > 7)
        				repair = character[7];
        			if(character.length > 8)
        				unarmed = character[8];
        			if(character.length > 9)
        				herbalism = character[9];
        			if(character.length > 10)
        				excavation = character[10];
        			if(character.length > 11)
        				archery = character[11];
        			if(character.length > 12)
        				swords = character[12];
        			if(character.length > 13)
        				axes = character[13];
        			if(character.length > 14)
        				acrobatics = character[14];
        			if(character.length > 15)
        				repairgather = character[15];
        			if(character.length > 16)
        				unarmedgather = character[16];
        			if(character.length > 17)
        				herbalismgather = character[17];
        			if(character.length > 18)
        				excavationgather = character[18];
        			if(character.length > 19)
        				archerygather = character[19];
        			if(character.length > 20)
        				swordsgather = character[20];
        			if(character.length > 21)
        				axesgather = character[21];
        			if(character.length > 22)
        				acrobaticsgather = character[22];
        			if(character.length > 23)
        				myspawnworld = character[23];
                	in.close();
        			return true;
            	}
            	in.close();
	        } catch (Exception e) {
	            log.log(Level.SEVERE, "Exception while reading "
	            		+ location + " (Are you sure you formatted it correctly?)", e);
	        }
	        return false;
		}
		
        //=====================================================================
        // Function:    save
        // Input:       none
        // Output:      None
        // Use:         Writes current values of PlayerProfile to disk
		//				Call this function to save current values
        //=====================================================================
        public void save()
        {
            try {
            	//Open the file
            	FileReader file = new FileReader(location);
                BufferedReader in = new BufferedReader(file);
                StringBuilder writer = new StringBuilder();
            	String line = "";
            	
            	//While not at the end of the file
            	while((line = in.readLine()) != null)
            	{
            		//Read the line in and copy it to the output it's not the player
            		//we want to edit
            		if(!line.split(":")[0].equalsIgnoreCase(playerName))
            		{
                        writer.append(line).append("\r\n");
                        
                    //Otherwise write the new player information
            		} else {
            			writer.append(playerName + ":");
            			writer.append(mining + ":");
            			writer.append(myspawn + ":");
            			writer.append(party+":");
            			writer.append(gather+":");
            			writer.append(woodcutting+":");
            			writer.append(wgather+":");
            			writer.append(repair+":");
            			writer.append(unarmed+":");
            			writer.append(herbalism+":");
            			writer.append(excavation+":");
            			writer.append(archery+":");
            			writer.append(swords+":");
            			writer.append(axes+":");
            			writer.append(acrobatics+":");
            			writer.append(repairgather+":");
            			writer.append(unarmedgather+":");
            			writer.append(herbalismgather+":");
            			writer.append(excavationgather+":");
            			writer.append(archerygather+":");
            			writer.append(swordsgather+":");
            			writer.append(axesgather+":");
            			writer.append(acrobaticsgather+":");
            			writer.append(myspawnworld+":");
            			writer.append("\r\n");                   			
            		}
            	}
            	in.close();
            	//Write the new file
                FileWriter out = new FileWriter(location);
                out.write(writer.toString());
                out.close();
	        } catch (Exception e) {
                    log.log(Level.SEVERE, "Exception while writing to " + location + " (Are you sure you formatted it correctly?)", e);
	        }
		}
        public void addPlayer()
        {
            try {
            	//Open the file to write the player
            	FileWriter file = new FileWriter(location, true);
                BufferedWriter out = new BufferedWriter(file);
                
                //Add the player to the end
                out.append(playerName + ":");
                out.append(0 + ":"); //mining
                out.append(myspawn+":");
                out.append(party+":");
                out.append(0+":"); //gather
                out.append(0+":"); //woodcutting
                out.append(0+":"); //wgather
                out.append(0+":"); //repair
                out.append(0+":"); //unarmed
                out.append(0+":"); //herbalism
                out.append(0+":"); //excavation
                out.append(0+":"); //archery
                out.append(0+":"); //swords
                out.append(0+":"); //axes
                out.append(0+":"); //acrobatics
                out.append(0+":"); //repairgather
                out.append(0+":"); //unarmedgather
                out.append(0+":"); //herbalismgather
                out.append(0+":"); //excavationgather
                out.append(0+":"); //archerygather
                out.append(0+":"); //swordsgather
                out.append(0+":"); //axesgather
                out.append(0+":"); //acrobaticsgather
                out.append(thisplayer.getWorld().getName());
                //Add more in the same format as the line above
                
    			out.newLine();
    			out.close();
	        } catch (Exception e) {
                    log.log(Level.SEVERE, "Exception while writing to " + location + " (Are you sure you formatted it correctly?)", e);
	        }
        }

		//=====================================================================
		//Function:	isPlayer
		//Input:	None
		//Output:	Player: The player this profile belongs to
		//Use:		Finds if this profile belongs to a specified player
		//=====================================================================
		public boolean isPlayer(Player player)
		{
			return player.getName().equals(playerName);
		}
		public boolean getAbilityUse(){
			return abilityuse;
		}
		public void toggleAbilityUse(){
			if(abilityuse == false){
				abilityuse = true;
			} else {
				abilityuse = false;
			}
		}
		public void decreaseBleedTicks(){
			if(bleedticks >= 1){
				bleedticks--;
			}
		}
		public Integer getBleedTicks(){
			return bleedticks;
		}
		public void setBleedTicks(Integer newvalue){
			bleedticks = newvalue;
		}
		public void addBleedTicks(Integer newvalue){
			bleedticks+=newvalue;
		}
		public Boolean hasCooldowns(){
			if((treeFellerCooldown + superBreakerCooldown) >= 1){
				return true;
			} else {
				return false;
			}
		}
		/*
		 * ARCHERY NERF STUFF
		 */
		public long getArcheryShotATS() {return archeryShotATS;}
		public void setArcheryShotATS(long newvalue) {archeryShotATS = newvalue;}
		
		/*
		 * SWORDS PREPARATION
		 */
		public boolean getSwordsPreparationMode(){
			return swordsPreparationMode;
		}
		public void setSwordsPreparationMode(Boolean bool){
			swordsPreparationMode = bool;
		}
		public long getSwordsPreparationATS(){
			return swordsPreparationATS;
		}
		public void setSwordsPreparationATS(long newvalue){
			swordsPreparationATS = newvalue;
		}
		/*
		 * SHOVEL PREPARATION
		 */
		public boolean getShovelPreparationMode(){
			return shovelPreparationMode;
		}
		public void setShovelPreparationMode(Boolean bool){
			shovelPreparationMode = bool;
		}
		public long getShovelPreparationATS(){
			return shovelPreparationATS;
		}
		public void setShovelPreparationATS(long newvalue){
			shovelPreparationATS = newvalue;
		}
		/*
		 * FISTS PREPARATION
		 */
		public boolean getFistsPreparationMode(){
			return fistsPreparationMode;
		}
		public void setFistsPreparationMode(Boolean bool){
			fistsPreparationMode = bool;
		}
		public long getFistsPreparationATS(){
			return fistsPreparationATS;
		}
		public void setFistsPreparationATS(long newvalue){
			fistsPreparationATS = newvalue;
		}
		/*
		 * AXE PREPARATION
		 */
		public boolean getAxePreparationMode(){
			return axePreparationMode;
		}
		public void setAxePreparationMode(Boolean bool){
			axePreparationMode = bool;
		}
		public long getAxePreparationATS(){
			return axePreparationATS;
		}
		public void setAxePreparationATS(long newvalue){
			axePreparationATS = newvalue;
		}
		/*
		 * PICKAXE PREPARATION
		 */
		public boolean getPickaxePreparationMode(){
			return pickaxePreparationMode;
		}
		public void setPickaxePreparationMode(Boolean bool){
			pickaxePreparationMode = bool;
		}
		public long getPickaxePreparationATS(){
			return pickaxePreparationATS;
		}
		public void setPickaxePreparationATS(long newvalue){
			pickaxePreparationATS = newvalue;
		}
		/*
		 * BERSERK MODE
		 */
		public boolean getBerserkInformed() {return berserkInformed;}
		public void setBerserkInformed(Boolean bool){
			berserkInformed = bool;
		}
		public boolean getBerserkMode(){
			return berserkMode;
		}
		public void setBerserkMode(Boolean bool){
			berserkMode = bool;
		}
		public long getBerserkActivatedTimeStamp() {return berserkATS;}
		public void setBerserkActivatedTimeStamp(Long newvalue){
			berserkATS = newvalue;
		}
		public long getBerserkDeactivatedTimeStamp() {return berserkDATS;}
		public void setBerserkDeactivatedTimeStamp(Long newvalue){
			berserkDATS = newvalue;
		}
		public void setBerserkCooldown(Long newvalue){
			berserkCooldown = newvalue;
		}
		public long getBerserkCooldown(){
			return berserkCooldown;
		}
		public void setBerserkTicks(Integer newvalue){berserkTicks = newvalue;}
		public int getBerserkTicks(){return berserkTicks;}
		/*
		 * SKULL SPLITTER
		 */
		public boolean getSkullSplitterInformed() {return skullSplitterInformed;}
		public void setSkullSplitterInformed(Boolean bool){
			skullSplitterInformed = bool;
		}
		public boolean getSkullSplitterMode(){
			return skullSplitterMode;
		}
		public void setSkullSplitterMode(Boolean bool){
			skullSplitterMode = bool;
		}
		public long getSkullSplitterActivatedTimeStamp() {return skullSplitterATS;}
		public void setSkullSplitterActivatedTimeStamp(Long newvalue){
			skullSplitterATS = newvalue;
		}
		public long getSkullSplitterDeactivatedTimeStamp() {return skullSplitterDATS;}
		public void setSkullSplitterDeactivatedTimeStamp(Long newvalue){
			skullSplitterDATS = newvalue;
		}
		public void setSkullSplitterCooldown(Long newvalue){
			skullSplitterCooldown = newvalue;
		}
		public long getSkullSplitterCooldown(){
			return skullSplitterCooldown;
		}
		public void setSkullSplitterTicks(Integer newvalue){skullSplitterTicks = newvalue;}
		public int getSkullSplitterTicks(){return skullSplitterTicks;}
		/*
		 * SERRATED STRIKES
		 */
		public boolean getSerratedStrikesInformed() {return serratedStrikesInformed;}
		public void setSerratedStrikesInformed(Boolean bool){
			serratedStrikesInformed = bool;
		}
		public boolean getSerratedStrikesMode(){
			return serratedStrikesMode;
		}
		public void setSerratedStrikesMode(Boolean bool){
			serratedStrikesMode = bool;
		}
		public long getSerratedStrikesActivatedTimeStamp() {return serratedStrikesATS;}
		public void setSerratedStrikesActivatedTimeStamp(Long newvalue){
			serratedStrikesATS = newvalue;
		}
		public long getSerratedStrikesDeactivatedTimeStamp() {return serratedStrikesDATS;}
		public void setSerratedStrikesDeactivatedTimeStamp(Long newvalue){
			serratedStrikesDATS = newvalue;
		}
		public void setSerratedStrikesCooldown(Long newvalue){
			serratedStrikesCooldown = newvalue;
		}
		public long getSerratedStrikesCooldown(){
			return serratedStrikesCooldown;
		}
		public void setSerratedStrikesTicks(Integer newvalue){serratedStrikesTicks = newvalue;}
		public int getSerratedStrikesTicks(){return serratedStrikesTicks;}
		/*
		 * GIGA DRILL BREAKER
		 */
		public boolean getGigaDrillBreakerInformed() {return gigaDrillBreakerInformed;}
		public void setGigaDrillBreakerInformed(Boolean bool){
			gigaDrillBreakerInformed = bool;
		}
		public boolean getGigaDrillBreakerMode(){
			return gigaDrillBreakerMode;
		}
		public void setGigaDrillBreakerMode(Boolean bool){
			gigaDrillBreakerMode = bool;
		}
		public long getGigaDrillBreakerActivatedTimeStamp() {return gigaDrillBreakerATS;}
		public void setGigaDrillBreakerActivatedTimeStamp(Long newvalue){
			gigaDrillBreakerATS = newvalue;
		}
		public long getGigaDrillBreakerDeactivatedTimeStamp() {return gigaDrillBreakerDATS;}
		public void setGigaDrillBreakerDeactivatedTimeStamp(Long newvalue){
			gigaDrillBreakerDATS = newvalue;
		}
		public void setGigaDrillBreakerCooldown(Long newvalue){
			gigaDrillBreakerCooldown = newvalue;
		}
		public long getGigaDrillBreakerCooldown(){
			return gigaDrillBreakerCooldown;
		}
		public void setGigaDrillBreakerTicks(Integer newvalue){gigaDrillBreakerTicks = newvalue;}
		public int getGigaDrillBreakerTicks(){return gigaDrillBreakerTicks;}
		/*
		 * TREE FELLER STUFF
		 */
		public boolean getTreeFellerInformed() {return treeFellerInformed;}
		public void setTreeFellerInformed(Boolean bool){
			treeFellerInformed = bool;
		}
		public boolean getTreeFellerMode(){
			return treeFellerMode;
		}
		public void setTreeFellerMode(Boolean bool){
			treeFellerMode = bool;
		}
		public long getTreeFellerActivatedTimeStamp() {return treeFellerATS;}
		public void setTreeFellerActivatedTimeStamp(Long newvalue){
			treeFellerATS = newvalue;
		}
		public long getTreeFellerDeactivatedTimeStamp() {return treeFellerDATS;}
		public void setTreeFellerDeactivatedTimeStamp(Long newvalue){
			treeFellerDATS = newvalue;
		}
		public void setTreeFellerCooldown(Long newvalue){
			treeFellerCooldown = newvalue;
		}
		public long getTreeFellerCooldown(){
			return treeFellerCooldown;
		}
		public void setTreeFellerTicks(Integer newvalue){treeFellerTicks = newvalue;}
		public int getTreeFellerTicks(){return treeFellerTicks;}
		/*
		 * MINING
		 */
		public boolean getSuperBreakerInformed() {return superBreakerInformed;}
		public void setSuperBreakerInformed(Boolean bool){
			superBreakerInformed = bool;
		}
		public boolean getSuperBreakerMode(){
			return superBreakerMode;
		}
		public void setSuperBreakerMode(Boolean bool){
			superBreakerMode = bool;
		}
		public long getSuperBreakerActivatedTimeStamp() {return superBreakerATS;}
		public void setSuperBreakerActivatedTimeStamp(Long newvalue){
			superBreakerATS = newvalue;
		}
		public long getSuperBreakerDeactivatedTimeStamp() {return superBreakerDATS;}
		public void setSuperBreakerDeactivatedTimeStamp(Long newvalue){
			superBreakerDATS = newvalue;
		}
		public void setSuperBreakerCooldown(Long newvalue){
			superBreakerCooldown = newvalue;
		}
		public long getSuperBreakerCooldown(){
			return superBreakerCooldown;
		}
		public void setSuperBreakerTicks(Integer newvalue){superBreakerTicks = newvalue;}
		public int getSuperBreakerTicks(){return superBreakerTicks;}
		
		public long getRecentlyHurt(){
			return recentlyHurt;
		}
		public void setRecentlyHurt(long newvalue){
			recentlyHurt = newvalue;
		}
		public void skillUpAxes(int newskill){
			int x = 0;
			if(axes != null){
			if(isInt(axes)){
			x = Integer.parseInt(axes);
			}else {
				axes = "0";
				x = Integer.parseInt(axes);
			}
			}
			x += newskill;
			axes = Integer.toString(x);
			save();
		}
		public void skillUpAcrobatics(int newskill){
			int x = 0;
			if(acrobatics != null){
			if(isInt(acrobatics)){
			x = Integer.parseInt(acrobatics);
			}else {
				acrobatics = "0";
				x = Integer.parseInt(acrobatics);
			}
			}
			x += newskill;
			acrobatics = Integer.toString(x);
			save();
		}
		public void skillUpSwords(int newskill){
			int x = 0;
			if(swords != null){
			if(isInt(swords)){
			x = Integer.parseInt(swords);
			}else {
				swords = "0";
				x = Integer.parseInt(swords);
			}
			}
			x += newskill;
			swords = Integer.toString(x);
			save();
		}
		public void skillUpArchery(int newskill){
			int x = 0;
			if(archery != null){
			if(isInt(archery)){
			x = Integer.parseInt(archery);
			}else {
				archery = "0";
				x = Integer.parseInt(archery);
			}
			}
			x += newskill;
			archery = Integer.toString(x);
			save();
		}
		public void skillUpRepair(int newskill){
			int x = 0;
			if(repair != null){
			if(isInt(repair)){
			x = Integer.parseInt(repair);
			}else {
				repair = "0";
				x = Integer.parseInt(repair);
			}
			}
			x += newskill;
			repair = Integer.toString(x);
			save();
		}
		public void skillUpMining(int newmining){
			int x = 0;
			if(mining != null){
			if(isInt(mining)){
			x = Integer.parseInt(mining);
			}else {
				mining = "0";
				x = Integer.parseInt(mining);
			}
			}
			x += newmining;
			mining = Integer.toString(x);
			save();
		}
		public void skillUpUnarmed(int newskill){
			int x = 0;
			if(unarmed != null){
			if(isInt(unarmed)){
			x = Integer.parseInt(unarmed);
			}else {
				unarmed = "0";
				x = Integer.parseInt(unarmed);
			}
			}
			x += newskill;
			unarmed = Integer.toString(x);
			save();
		}
		public void skillUpHerbalism(int newskill){
			int x = 0;
			if(herbalism != null){
			if(isInt(herbalism)){
			x = Integer.parseInt(herbalism);
			}else {
				herbalism = "0";
				x = Integer.parseInt(herbalism);
			}
			}
			x += newskill;
			herbalism = Integer.toString(x);
			save();
		}
		public void skillUpExcavation(int newskill){
			int x = 0;
			if(excavation != null){
			if(isInt(excavation)){
			x = Integer.parseInt(excavation);
			}else {
				excavation = "0";
				x = Integer.parseInt(excavation);
			}
			}
			x += newskill;
			excavation = Integer.toString(x);
			save();
		}
		public void skillUpWoodCutting(int newskill){
			int x = 0;
			if(woodcutting != null){
			if(isInt(woodcutting)){
			x = Integer.parseInt(woodcutting);
			}else {
				woodcutting = "0";
				x = Integer.parseInt(woodcutting);
			}
			}
			x += newskill;
			woodcutting = Integer.toString(x);
			save();
		}
		public String getRepair(){
			if(repair != null && !repair.equals("") && !repair.equals("null")){
			return repair;
			} else {
				return "0";
			}
		}
		public String getMining(){
			if(mining != null && !mining.equals("") && !mining.equals("null")){
				return mining;
				} else {
					return "0";
				}
		}
		public String getUnarmed(){
			if(unarmed != null && !unarmed.equals("") && !unarmed.equals("null")){
				return unarmed;
				} else {
					return "0";
				}
		}
		public String getHerbalism(){
			if(herbalism != null && !herbalism.equals("") && !herbalism.equals("null")){
				return herbalism;
				} else {
					return "0";
				}
		}
		public String getExcavation(){
			if(excavation != null && !excavation.equals("") && !excavation.equals("null")){
				return excavation;
				} else {
					return "0";
				}
		}
		public String getArchery(){
			if(archery != null && !archery.equals("") && !archery.equals("null")){
				return archery;
				} else {
					return "0";
				}
		}
		public String getSwords(){
			if(swords != null && !swords.equals("") && !swords.equals("null")){
				return swords;
				} else {
					return "0";
				}
		}
		public String getAxes(){
			if(axes != null && !axes.equals("") && !axes.equals("null")){
				return axes;
				} else {
					return "0";
				}
		}
		public String getAcrobatics(){
			if(acrobatics != null && !acrobatics.equals("") && !acrobatics.equals("null")){
				return acrobatics;
				} else {
					return "0";
				}
		}
		public int getMiningInt(){
			if(isInt(mining)){
				int x = Integer.parseInt(mining);
				return x;
			} else{
				return 0;
			}
		}
		public int getUnarmedInt(){
			if(isInt(unarmed)){
				int x = Integer.parseInt(unarmed);
				return x;
			} else{
				return 0;
			}
		}
		public int getArcheryInt(){
			if(isInt(archery)){
				int x = Integer.parseInt(archery);
				return x;
			} else{
				return 0;
			}
		}
		public int getSwordsInt(){
			if(isInt(swords)){
				int x = Integer.parseInt(swords);
				return x;
			} else{
				return 0;
			}
		}
		public int getAxesInt(){
			if(isInt(axes)){
				int x = Integer.parseInt(axes);
				return x;
			} else{
				return 0;
			}
		}
		public int getAcrobaticsInt(){
			if(isInt(acrobatics)){
				int x = Integer.parseInt(acrobatics);
				return x;
			} else{
				return 0;
			}
		}
		public int getHerbalismInt(){
			if(isInt(herbalism)){
				int x = Integer.parseInt(herbalism);
				return x;
			} else{
				return 0;
			}
		}
		public int getExcavationInt(){
			if(isInt(excavation)){
				int x = Integer.parseInt(excavation);
				return x;
			} else{
				return 0;
			}
		}
		public int getRepairInt(){
			if(isInt(repair)){
				int x = Integer.parseInt(repair);
				return x;
			} else{
				return 0;
			}
		}
		public int getWoodCuttingInt(){
			if(isInt(woodcutting)){
				int x = Integer.parseInt(woodcutting);
				return x;
			} else{
				return 0;
			}
		}
		public String getWoodCutting(){
			if(woodcutting != null && !woodcutting.equals("") && !woodcutting.equals("null")){
				return woodcutting;
				} else {
					return "0";
				}
		}
		/*
		 * EXPERIENCE STUFF
		 */
		public void clearRepairGather(){
			repairgather = "0";
		}
		public void clearUnarmedGather(){
			unarmedgather = "0";
		}
		public void clearHerbalismGather(){
			herbalismgather = "0";
		}
		public void clearExcavationGather(){
			excavationgather = "0";
		}
		public void clearArcheryGather(){
			archerygather = "0";
		}
		public void clearSwordsGather(){
			swordsgather = "0";
		}
		public void clearAxesGather(){
			axesgather = "0";
		}
		public void clearAcrobaticsGather(){
			acrobaticsgather = "0";
		}
		public void addAcrobaticsGather(int newgather)
		{
			int x = 0;
			if(isInt(acrobaticsgather)){
			x = Integer.parseInt(acrobaticsgather);
			}
			x += newgather;
			acrobaticsgather = String.valueOf(x);
			save();
		}
		public void addAxesGather(int newgather)
		{
			int x = 0;
			if(isInt(axesgather)){
			x = Integer.parseInt(axesgather);
			}
			x += newgather;
			axesgather = String.valueOf(x);
			save();
		}
		public void addSwordsGather(int newgather)
		{
			int x = 0;
			if(isInt(swordsgather)){
			x = Integer.parseInt(swordsgather);
			}
			x += newgather;
			swordsgather = String.valueOf(x);
			save();
		}
		public void addArcheryGather(int newgather)
		{
			int x = 0;
			if(isInt(archerygather)){
			x = Integer.parseInt(archerygather);
			}
			x += newgather;
			archerygather = String.valueOf(x);
			save();
		}
		public void addExcavationGather(int newgather)
		{
			int x = 0;
			if(isInt(excavationgather)){
			x = Integer.parseInt(excavationgather);
			}
			x += newgather;
			excavationgather = String.valueOf(x);
			save();
		}
		public void addHerbalismGather(int newgather)
		{
			int x = 0;
			if(isInt(herbalismgather)){
			x = Integer.parseInt(herbalismgather);
			}
			x += newgather;
			herbalismgather = String.valueOf(x);
			save();
		}
		public void addRepairGather(int newgather)
		{
			int x = 0;
			if(isInt(repairgather)){
			x = Integer.parseInt(repairgather);
			}
			x += newgather;
			repairgather = String.valueOf(x);
			save();
		}
		public void addUnarmedGather(int newgather)
		{
			int x = 0;
			if(isInt(unarmedgather)){
			x = Integer.parseInt(unarmedgather);
			}
			x += newgather;
			unarmedgather = String.valueOf(x);
			save();
		}
		public void addWoodcuttingGather(int newgather)
		{
			int x = 0;
			if(isInt(wgather)){
			x = Integer.parseInt(wgather);
			}
			x += newgather;
			wgather = String.valueOf(x);
			save();
		}
		public void removeWoodCuttingGather(int newgather){
			int x = 0;
			if(isInt(wgather)){
			x = Integer.parseInt(wgather);
			}
			x -= newgather;
			wgather = String.valueOf(x);
			save();
		}
		public void addMiningGather(int newgather)
		{
			int x = 0;
			if(isInt(gather)){
			x = Integer.parseInt(gather);
			} else {
				x = 0;
			}
			x += newgather;
			gather = String.valueOf(x);
			save();
		}
		public void removeMiningGather(int newgather){
			int x = 0;
			if(isInt(gather)){
			x = Integer.parseInt(gather);
			}
			x -= newgather;
			gather = String.valueOf(x);
			save();
		}
		public void removeRepairGather(int newgather){
			int x = 0;
			if(isInt(repairgather)){
			x = Integer.parseInt(repairgather);
			}
			x -= newgather;
			repairgather = String.valueOf(x);
			save();
		}
		public void removeUnarmedGather(int newgather){
			int x = 0;
			if(isInt(unarmedgather)){
			x = Integer.parseInt(unarmedgather);
			}
			x -= newgather;
			unarmedgather = String.valueOf(x);
			save();
		}
		public void removeHerbalismGather(int newgather){
			int x = 0;
			if(isInt(herbalismgather)){
			x = Integer.parseInt(herbalismgather);
			}
			x -= newgather;
			herbalismgather = String.valueOf(x);
			save();
		}
		public void removeExcavationGather(int newgather){
			int x = 0;
			if(isInt(excavationgather)){
			x = Integer.parseInt(excavationgather);
			}
			x -= newgather;
			excavationgather = String.valueOf(x);
			save();
		}
		public void removeArcheryGather(int newgather){
			int x = 0;
			if(isInt(archerygather)){
			x = Integer.parseInt(archerygather);
			}
			x -= newgather;
			archerygather = String.valueOf(x);
			save();
		}
		public void removeSwordsGather(int newgather){
			int x = 0;
			if(isInt(swordsgather)){
			x = Integer.parseInt(swordsgather);
			}
			x -= newgather;
			swordsgather = String.valueOf(x);
			save();
		}
		public void removeAxesGather(int newgather){
			int x = 0;
			if(isInt(axesgather)){
			x = Integer.parseInt(axesgather);
			}
			x -= newgather;
			axesgather = String.valueOf(x);
			save();
		}
		public void removeAcrobaticsGather(int newgather){
			int x = 0;
			if(isInt(acrobaticsgather)){
			x = Integer.parseInt(acrobaticsgather);
			}
			x -= newgather;
			acrobaticsgather = String.valueOf(x);
			save();
		}

		public boolean isInt(String string){
			try {
			    int x = Integer.parseInt(string);
			}
			catch(NumberFormatException nFE) {
			    return false;
			}
			return true;
		}
		public boolean isDouble(String string){
			try {
			    Double x = Double.valueOf(string);
			}
			catch(NumberFormatException nFE) {
			    return false;
			}
			return true;
		}
		public void acceptInvite(){
			party = invite;
			invite = "";
			save();
		}
		public void modifyInvite(String invitename){
			invite = invitename;
		}
		//Returns player gather
		public String getMiningGather(){
			if(gather != null && !gather.equals("") && !gather.equals("null")){
				return gather;
				} else {
					return "0";
				}
		}
		public String getInvite() { return invite; }
		public String getWoodCuttingGather(){
			if(wgather != null && !wgather.equals("") && !wgather.equals("null")){
				return wgather;
				} else {
					return "0";
				}
		}
		public String getRepairGather(){
			if(repairgather != null && !repairgather.equals("") && !repairgather.equals("null")){
				return repairgather;
				} else {
					return "0";
				}
		}
		public String getHerbalismGather(){
			if(herbalismgather != null && !herbalismgather.equals("") && !herbalismgather.equals("null")){
				return herbalismgather;
				} else {
					return "0";
				}
		}
		public String getExcavationGather(){
			if(excavationgather != null && !excavationgather.equals("") && !excavationgather.equals("null")){
				return excavationgather;
				} else {
					return "0";
				}
		}
		public String getArcheryGather(){
			if(archerygather != null && !archerygather.equals("") && !archerygather.equals("null")){
				return archerygather;
				} else {
					return "0";
				}
		}
		public String getSwordsGather(){
			if(swordsgather != null && !swordsgather.equals("") && !swordsgather.equals("null")){
				return swordsgather;
				} else {
					return "0";
				}
		}
		public String getAxesGather(){
			if(axesgather != null && !axesgather.equals("") && !axesgather.equals("null")){
				return axesgather;
				} else {
					return "0";
				}
		}
		public String getAcrobaticsGather(){
			if(acrobaticsgather != null && !acrobaticsgather.equals("") && !acrobaticsgather.equals("null")){
				return acrobaticsgather;
				} else {
					return "0";
				}
		}
		public String getUnarmedGather(){
			if(unarmedgather != null && !unarmedgather.equals("") && !unarmedgather.equals("null")){
				return unarmedgather;
				} else {
					return "0";
				}
		}
		
		public int getWoodCuttingGatherInt() {
			if(isInt(wgather)){
			return Integer.parseInt(wgather);
			} else {
				wgather = "0";
				save();
				return 0;
			}
		}
		public int getRepairGatherInt() {
			if(isInt(repairgather)){
			return Integer.parseInt(repairgather);
			} else {
				repairgather = "0";
				save();
				return 0;
			}
		}
		public int getUnarmedGatherInt() {
			if(isInt(unarmedgather)){
			return Integer.parseInt(unarmedgather);
			} else {
				unarmedgather = "0";
				save();
				return 0;
			}
		}
		public int getHerbalismGatherInt() {
			if(isInt(herbalismgather)){
			return Integer.parseInt(herbalismgather);
			} else {
				herbalismgather = "0";
				save();
				return 0;
			}
		}
		public int getExcavationGatherInt() {
			if(isInt(excavationgather)){
			return Integer.parseInt(excavationgather);
			} else {
				excavationgather = "0";
				save();
				return 0;
			}
		}
		public int getArcheryGatherInt() {
			if(isInt(archerygather)){
			return Integer.parseInt(archerygather);
			} else {
				archerygather = "0";
				save();
				return 0;
			}
		}
		public int getSwordsGatherInt() {
			if(isInt(swordsgather)){
			return Integer.parseInt(swordsgather);
			} else {
				swordsgather = "0";
				save();
				return 0;
			}
		}
		public int getAxesGatherInt() {
			if(isInt(axesgather)){
			return Integer.parseInt(axesgather);
			} else {
				axesgather = "0";
				save();
				return 0;
			}
		}
		public int getAcrobaticsGatherInt() {
			if(isInt(acrobaticsgather)){
			return Integer.parseInt(acrobaticsgather);
			} else {
				acrobaticsgather = "0";
				save();
				return 0;
			}
		}
		public void addXpToSkill(int newvalue, String skillname){
			if(!isInt(gather))
				gather = String.valueOf(0);
			if(!isInt(wgather))
				wgather = String.valueOf(0);
			if(!isInt(repairgather))
				repairgather = String.valueOf(0);
			if(!isInt(herbalismgather))
				herbalismgather = String.valueOf(0);
			if(!isInt(acrobaticsgather))
				acrobaticsgather = String.valueOf(0);
			if(!isInt(swordsgather))
				swordsgather = String.valueOf(0);
			if(!isInt(archerygather))
				archerygather = String.valueOf(0);
			if(!isInt(unarmedgather))
				unarmedgather = String.valueOf(0);
			if(!isInt(excavationgather))
				excavationgather = String.valueOf(0);
			if(!isInt(axesgather))
				axesgather = String.valueOf(0);
			
			if(skillname.toLowerCase().equals("mining")){
				gather = String.valueOf(Integer.valueOf(gather)+newvalue);
			}
			if(skillname.toLowerCase().equals("woodcutting")){
				wgather = String.valueOf(Integer.valueOf(wgather)+newvalue);
			}
			if(skillname.toLowerCase().equals("repair")){
				repairgather = String.valueOf(Integer.valueOf(repairgather)+newvalue);
			}
			if(skillname.toLowerCase().equals("herbalism")){
				herbalismgather = String.valueOf(Integer.valueOf(herbalismgather)+newvalue);
			}
			if(skillname.toLowerCase().equals("acrobatics")){
				acrobaticsgather = String.valueOf(Integer.valueOf(acrobaticsgather)+newvalue);
			}
			if(skillname.toLowerCase().equals("swords")){
				swordsgather = String.valueOf(Integer.valueOf(swordsgather)+newvalue);
			}
			if(skillname.toLowerCase().equals("archery")){
				archerygather = String.valueOf(Integer.valueOf(archerygather)+newvalue);
			}
			if(skillname.toLowerCase().equals("unarmed")){
				unarmedgather = String.valueOf(Integer.valueOf(unarmedgather)+newvalue);
			}
			if(skillname.toLowerCase().equals("excavation")){
				excavationgather = String.valueOf(Integer.valueOf(excavationgather)+newvalue);
			}
			if(skillname.toLowerCase().equals("axes")){
				axesgather = String.valueOf(Integer.valueOf(axesgather)+newvalue);
			}
			if(skillname.toLowerCase().equals("all")){
				gather = String.valueOf(Integer.valueOf(gather)+newvalue);
				wgather = String.valueOf(Integer.valueOf(wgather)+newvalue);
				repairgather = String.valueOf(Integer.valueOf(repairgather)+newvalue);
				herbalismgather = String.valueOf(Integer.valueOf(herbalismgather)+newvalue);
				acrobaticsgather = String.valueOf(Integer.valueOf(acrobaticsgather)+newvalue);
				swordsgather = String.valueOf(Integer.valueOf(swordsgather)+newvalue);
				archerygather = String.valueOf(Integer.valueOf(archerygather)+newvalue);
				unarmedgather = String.valueOf(Integer.valueOf(unarmedgather)+newvalue);
				excavationgather = String.valueOf(Integer.valueOf(excavationgather)+newvalue);
				axesgather = String.valueOf(Integer.valueOf(axesgather)+newvalue);
			}
			save();
			mcSkills.getInstance().XpCheck(thisplayer);
		}
		public void modifyskill(int newvalue, String skillname){
			if(skillname.toLowerCase().equals("mining")){
				 mining = String.valueOf(newvalue);
			}
			if(skillname.toLowerCase().equals("woodcutting")){
				 woodcutting = String.valueOf(newvalue);
			}
			if(skillname.toLowerCase().equals("repair")){
				 repair = String.valueOf(newvalue);
			}
			if(skillname.toLowerCase().equals("herbalism")){
				 herbalism = String.valueOf(newvalue);
			}
			if(skillname.toLowerCase().equals("acrobatics")){
				 acrobatics = String.valueOf(newvalue);
			}
			if(skillname.toLowerCase().equals("swords")){
				 swords = String.valueOf(newvalue);
			}
			if(skillname.toLowerCase().equals("archery")){
				 archery = String.valueOf(newvalue);
			}
			if(skillname.toLowerCase().equals("unarmed")){
				 unarmed = String.valueOf(newvalue);
			}
			if(skillname.toLowerCase().equals("excavation")){
				 excavation = String.valueOf(newvalue);
			}
			if(skillname.toLowerCase().equals("axes")){
				axes = String.valueOf(newvalue);
			}
			if(skillname.toLowerCase().equals("all")){
				mining = String.valueOf(newvalue);
				woodcutting = String.valueOf(newvalue);
				repair = String.valueOf(newvalue);
				herbalism = String.valueOf(newvalue);
				acrobatics = String.valueOf(newvalue);
				swords = String.valueOf(newvalue);
				archery = String.valueOf(newvalue);
				unarmed = String.valueOf(newvalue);
				excavation = String.valueOf(newvalue);
				axes = String.valueOf(newvalue);
			}
			save();
		}
		public Integer getXpToLevel(String skillname){
			if(skillname.equals("mining")){
				return ((getMiningInt() + 50) * mcLoadProperties.miningxpmodifier) * mcLoadProperties.globalxpmodifier;
			}
			if(skillname.equals("woodcutting")){
				return ((getWoodCuttingInt() + 50) * mcLoadProperties.woodcuttingxpmodifier) * mcLoadProperties.globalxpmodifier;
			}
			if(skillname.equals("repair")){
				return ((getRepairInt() + 50) * mcLoadProperties.repairxpmodifier) * mcLoadProperties.globalxpmodifier;
			}
			if(skillname.equals("herbalism")){
				return ((getHerbalismInt() + 50) * mcLoadProperties.herbalismxpmodifier) * mcLoadProperties.globalxpmodifier;
			}
			if(skillname.equals("acrobatics")){
				return ((getAcrobaticsInt() + 50) * mcLoadProperties.acrobaticsxpmodifier) * mcLoadProperties.globalxpmodifier;
			}
			if(skillname.equals("swords")){
				return ((getSwordsInt() + 50) * mcLoadProperties.swordsxpmodifier) * mcLoadProperties.globalxpmodifier;
			}
			if(skillname.equals("archery")){
				return ((getArcheryInt() + 50) * mcLoadProperties.archeryxpmodifier) * mcLoadProperties.globalxpmodifier;
			}
			if(skillname.equals("unarmed")){
				return ((getUnarmedInt() + 50) * mcLoadProperties.unarmedxpmodifier) * mcLoadProperties.globalxpmodifier;
			}
			if(skillname.equals("excavation")){
				return ((getExcavationInt() + 50) * mcLoadProperties.excavationxpmodifier) * mcLoadProperties.globalxpmodifier;
			}
			if(skillname.equals("axes")){
				return ((getAxesInt() + 50) * mcLoadProperties.axesxpmodifier) * mcLoadProperties.globalxpmodifier;
			} else {
				return 0;
			}
		}
		public int getPowerLevel(){
			int x = 0;
			if(mcPermissions.getInstance().mining(thisplayer))
				x+=getMiningInt();
			if(mcPermissions.getInstance().woodcutting(thisplayer))
				x+=getWoodCuttingInt();
			if(mcPermissions.getInstance().unarmed(thisplayer))
				x+=getUnarmedInt();
			if(mcPermissions.getInstance().herbalism(thisplayer))
				x+=getHerbalismInt();
			if(mcPermissions.getInstance().excavation(thisplayer))
				x+=getExcavationInt();
			if(mcPermissions.getInstance().archery(thisplayer))
				x+=getArcheryInt();
			if(mcPermissions.getInstance().swords(thisplayer))
				x+=getSwordsInt();
			if(mcPermissions.getInstance().axes(thisplayer))
				x+=getAxesInt();
			if(mcPermissions.getInstance().acrobatics(thisplayer))
				x+=getAcrobaticsInt();
			if(mcPermissions.getInstance().repair(thisplayer))
				x+=getRepairInt();
			return x;
		}
		public int getMiningGatherInt() {
			if(isInt(gather)){
			return Integer.parseInt(gather);
			} else {
				gather = "0";
				save();
				return 0;
			}
		}
                
                //Store the player's party
                public void setParty(String newParty)
                {
                    party = newParty;
                    save();
                }
                //Retrieve the player's party
                public String getParty() {return party;}
                //Remove party
                public void removeParty() {
                    party = null;
                    save();
                }
                //Retrieve whether or not the player is in a party
                public boolean inParty() {
                    if(party != null && !party.equals("") && !party.equals("null")){
                        return true;
                    } else {
                        return false;
                    }
                }
              //Retrieve whether or not the player has an invite
                public boolean hasPartyInvite() {
                    if(invite != null && !invite.equals("") && !invite.equals("null")){
                        return true;
                    } else {
                        return false;
                    }
                }
                public String getMySpawnWorld(Plugin plugin){
                	 if(myspawnworld != null && !myspawnworld.equals("") && !myspawnworld.equals("null")){
                		 return myspawnworld;
                	 } else {
                		 return plugin.getServer().getWorlds().get(0).toString();
                	 }
                }
                //Save a users spawn location
                public void setMySpawn(double x, double y, double z, String myspawnworldlocation){
            		myspawn = x+","+y+","+z;
            		myspawnworld = myspawnworldlocation;
            		save();
            	}
                public String getX(){
                	String[] split = myspawn.split(",");
                	String x = split[0];
                	return x;
                }
                public String getY(){
                	String[] split = myspawn.split(",");
                	String y = split[1];
                	return y;
                }
                public String getZ(){
                	String[] split = myspawn.split(",");
                	String z = split[2];
                	return z;
                }
                public void setDead(boolean x){
                	dead = x;
                	save();
                }
                public boolean isDead(){
                	return dead;
                }
                public Location getMySpawn(Player player){
                	Location loc = player.getWorld().getSpawnLocation();
                	if(isDouble(getX()) && isDouble(getY()) && isDouble(getX())){
            		loc.setX(Double.parseDouble(mcUsers.getProfile(player).getX()));
            		loc.setY(Double.parseDouble(mcUsers.getProfile(player).getY()));
            		loc.setZ(Double.parseDouble(mcUsers.getProfile(player).getZ()));
                	} else {
                		return null;
                	}
            		loc.setYaw(0);
            		loc.setPitch(0);
            		return loc;
                }
	}
	
}



