package me.soldado.npcshop;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;

public class VendedoresCore {
	
	public Main plugin;
	
	public VendedoresCore(Main plugin)
	{
		this.plugin = plugin;
	}
	
	File itensFile;
	FileConfiguration it;
	
	ArrayList<Inventory> inventarios = new ArrayList<Inventory>();
	HashMap<String, Vendedor> vendedores = new HashMap<String, Vendedor>();

	public void carregarVendedores(){
		List<String> s = it.getStringList("Vendedores");
		int atual = 1;
		for (String str : s) {
			try{
				String[] c = str.split(":");
				String nome = c[0];
				String stringitens = c[1];
				ArrayList<Item> itens = getItens(stringitens);
				Vendedor vend = new Vendedor(nome, itens);
				vendedores.put(nome, vend);
				atual++;
			}catch(Exception e){
				plugin.getLogger().info("ERRO:  Erro ao carregar o vendedor número " + atual);
			}
		}
	}
	
	public void iniciarVendedores(){

		if (itensFile == null) {
			itensFile = new File(plugin.getDataFolder(), "vendedores.yml");
		}
		if (!itensFile.exists()) {
			plugin.saveResource("vendedores.yml", false);
		}
		it = YamlConfiguration.loadConfiguration(itensFile);
	}
	
	public ArrayList<Item> getItens(String s){
		
		ArrayList<Item> itens = new ArrayList<Item>();
		String[] its = s.split(";");

		for(int i = 0; i < its.length; i++){
			int id = Integer.parseInt(its[i]);
			if(plugin.itcore.itens.containsKey(id)){
				Item item = plugin.itcore.itens.get(id);
				itens.add(item);
			}
		}
		return itens;
	}
}
