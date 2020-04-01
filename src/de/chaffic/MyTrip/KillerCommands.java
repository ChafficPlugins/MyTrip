package de.chaffic.MyTrip;

import java.io.File;
import java.util.Arrays;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class Main
  extends JavaPlugin
{
	
	public static ItemStack wheatd = new ItemStack(Material.WHEAT);
    public static ItemStack suagrd = new ItemStack(Material.SUGAR);
	public static ItemStack paperd = new ItemStack(Material.PAPER);
	public static ItemStack sugarcaned = new ItemStack(Material.SUGAR_CANE);
	public static ItemStack mushroomd = new ItemStack(Material.RED_MUSHROOM);



	
	public static String weed = "Weed: Just a gateway drug??";
	public static String coke = "Cocaine: Come sniff it!";
	public static String acid = "LSD: Should be fun, but maybe it's not...";
	public static String speziale = "Heroin: Feel the power!";
	public static String shrooms = "Mushroom: Champignon or porcino???";
  Logger logger = Logger.getLogger("Minecraft");
  File f = new File(this.getDataFolder() + "/");
  
  public void onEnable()
  {

	  //Adding in the Recipes for the Drugs
	  WeedRec();
	  CokeRec();
	  AcidRec();
	  SugarcaneRec();
	  ShroomsRec();
	  
	  //Registering Commands and Events
	    getCommand("mytrip").setExecutor(new KillerCommands(this));
	    getServer().getPluginManager().registerEvents(new Items(this), this);
	  
	    //Checking and Creating a folder for the plugin
	  if(!f.exists()) {
		  f.mkdir();
	  }else {
		  System.out.println("Folder already exists.");
	  }
	  
	 //Checking and loading in plugin config
    getConfig().options().header("MyTrip Config.");
    loadConfiguration();
  }
  
  
  //What is inside the config
  public void loadConfiguration()
  {
    String wheat = "mytrip.Toggle.wheat";
    String suagr = "mytrip.Toggle.sugar";
    String paper = "mytrip.Toggle.paper";
    String sugarcane = "mytrip.Toggle.sugarcane";
	String red_mushroom = "mytrip.Toggle.red_mushroom";
    String effect = "mytrip.Effect.length";
    String effect2 = "mytrip.Effect.confusion.length";

    
    getConfig().addDefault(wheat, Boolean.valueOf(true));
    getConfig().addDefault(suagr, Boolean.valueOf(true));
    getConfig().addDefault(paper, Boolean.valueOf(true));
    getConfig().addDefault(sugarcane, Boolean.valueOf(true));
    getConfig().addDefault(red_mushroom, Boolean.valueOf(true)); 
    
    getConfig().addDefault(effect, Integer.valueOf(5220));
    getConfig().addDefault(effect2, Integer.valueOf(2600));
    
    getConfig().options().copyDefaults(true);
    saveConfig();
  }
  //Weed Rezept
  public void WeedRec() {
	  ItemMeta meta = wheatd.getItemMeta();
	  
	  
	  meta.setDisplayName(ChatColor.GREEN+ "Weed");
	  meta.setLore(Arrays.asList(weed));
	  meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
	  meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
	  wheatd.setItemMeta(meta);
	  
	  NamespacedKey key = new NamespacedKey(this, "mytrip_weed");
	  ShapedRecipe recipe = new ShapedRecipe(key, wheatd);
	  
	  recipe.shape(" W ",
			       " W ",
			       " W ");
	  
	  recipe.setIngredient('W', Material.WHEAT);
	  
	  Bukkit.addRecipe(recipe);
  }
  //Kokain Rezept
  public void CokeRec() {
	  ItemMeta meta = suagrd.getItemMeta();
	  
	  meta.setLore(Arrays.asList(coke));
	  meta.setDisplayName(ChatColor.GOLD + "Cocaine");
	  meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
	  meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
	  suagrd.setItemMeta(meta);
	  
	  NamespacedKey key = new NamespacedKey(this,"mytrip_coke");
	  ShapedRecipe recipe = new ShapedRecipe(key, suagrd);
	  
	  recipe.shape(" S ",
			       " S ",
			       " S ");
	  
	  recipe.setIngredient('S', Material.SUGAR);
	  
	  Bukkit.addRecipe(recipe);
	  }
  //LSD Rezept
  public void AcidRec() {
	  ItemMeta meta = paperd.getItemMeta();

	  meta.setLore(Arrays.asList(acid));
	  meta.setDisplayName(ChatColor.BLUE + "LSD");
	  meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
	  meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
	  paperd.setItemMeta(meta);
	  
	  NamespacedKey key = new NamespacedKey(this, "mytrip_acid");
	  ShapedRecipe recipe = new ShapedRecipe(key, paperd);
	  
	  recipe.shape(" P ",
			       " P ",
			       " P ");
	  
	  recipe.setIngredient('P', Material.PAPER);
	  
	  Bukkit.addRecipe(recipe);
  	}
  //Speziale Rezept
  public void SugarcaneRec() {
	  ItemMeta meta = sugarcaned.getItemMeta();

	  meta.setLore(Arrays.asList(speziale));
	  meta.setDisplayName(ChatColor.DARK_PURPLE + "Heroin");
	  meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
	  meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
	  sugarcaned.setItemMeta(meta);

	  NamespacedKey key = new NamespacedKey(this, "mytrip_sugarcane");
	  ShapedRecipe recipe = new ShapedRecipe(key, sugarcaned);

	  recipe.shape(" U ",
			       " U ",
			       " U ");

	  recipe.setIngredient('U', Material.SUGAR_CANE);

	  Bukkit.addRecipe(recipe);

  	}
  public void ShroomsRec() {
	  ItemMeta meta = mushroomd.getItemMeta();

	  meta.setLore(Arrays.asList(shrooms));
	  meta.setDisplayName(ChatColor.RED + "Mushroom");
	  meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
	  meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
	  mushroomd.setItemMeta(meta);

	  NamespacedKey key = new NamespacedKey(this, "mytrip_red_mushroom");
	  ShapedRecipe recipe = new ShapedRecipe(key, mushroomd);

	  recipe.shape(" R ",
			       " R ",
			       " R ");

	  recipe.setIngredient('R', Material.RED_MUSHROOM);

	  Bukkit.addRecipe(recipe);

  	}
  


//Nothing
  public void onDisable() {}
  
}
