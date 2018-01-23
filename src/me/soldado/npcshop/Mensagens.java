package me.soldado.npcshop;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Mensagens {
	
	public Main plugin;
	
	public Mensagens(Main plugin)
	{
		this.plugin = plugin;
	}
	
	File msgFile;
	FileConfiguration msgs;
	
	String naocompra;
	String semdinheiro;
	String descontovipcompra;
	String descontovipvenda;
	String comprou;
	String naovende;
	String vendeu;
	String semespaço;
	String semitens;
	
	private void iniciarValores(){
		naocompra = getString("NaoCompra");
		semdinheiro = getString("SemDinheiro");
		descontovipcompra = getString("DescontoVipCompra").replace("%desconto%", plugin.cfg.descontovip+"%");
		descontovipvenda = getString("DescontoVipVenda").replace("%desconto%", plugin.cfg.descontovip+"%");
		comprou = getString("Comprou");
		naovende = getString("NaoVende");
		vendeu = getString("Vendeu");
		semespaço = getString("SemEspaço");
		semitens = getString("SemItens");
	}
	
	public void iniciarMensagens(){

		if (msgFile == null) {
			msgFile = new File(plugin.getDataFolder(), "mensagens.yml");
		}
		if (!msgFile.exists()) {
			plugin.saveResource("mensagens.yml", false);
		}
		msgs = YamlConfiguration.loadConfiguration(msgFile);
		iniciarValores();
	}
	
	public String getString(String s){
		return msgs.getString(s).replace("&", "§");
	}
}
