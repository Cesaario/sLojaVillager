package me.soldado.npcshop;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ItensCore {
	
	public Main plugin;
	
	public ItensCore(Main plugin)
	{
		this.plugin = plugin;
	}
	
	File itensFile;
	FileConfiguration it;
	
	HashMap<Integer, Item> itens = new HashMap<Integer, Item>();

	public void carregarItens(){
		List<String> s = it.getStringList("Itens");
		int atual = 1;
		for (String str : s) {
			try{
				String[] c = str.split(":");
				int i = Integer.parseInt(c[0]);
				String config = c[1];
				Item item = desserializarItem(config, i);
				if(item != null){
					itens.put(i, item);
				}
				atual++;
			}catch(Exception e){
				plugin.getLogger().info("ERRO:  Erro ao carregar o item número " + atual);
			}
		}
	}
	
	public void iniciarItens(){

		if (itensFile == null) {
			itensFile = new File(plugin.getDataFolder(), "itens.yml");
		}
		if (!itensFile.exists()) {
			plugin.saveResource("itens.yml", false);
		}
		it = YamlConfiguration.loadConfiguration(itensFile);
	}
	
	public Item desserializarItem(String s, int i){
		
		try{
			String[] param = s.split(";");
			int idtipo = 0;
			int data = 0;
			if(param[0].contains("-")){
				String l = param[0];
				String[] lados = l.split("-");
				idtipo = Integer.parseInt(lados[0]);
				data = Integer.parseInt(lados[1]);
			}else{
				idtipo = Integer.parseInt(param[0]);
			}
			String nome = param[1];
			double compra = Double.parseDouble(param[2]);
			double venda = Double.parseDouble(param[3]);
			int quantidade = Integer.parseInt(param[4]);
			Item item = new Item(Material.getMaterial(idtipo), nome, compra, venda, quantidade, i, data);
			return item;
		}catch(Exception e){
			e.printStackTrace();
			plugin.getLogger().info("ERRO AO CARREGAR ITEM");
			return null;
		}
		
	}
	
}
