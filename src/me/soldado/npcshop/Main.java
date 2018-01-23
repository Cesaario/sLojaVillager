package me.soldado.npcshop;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {
	
	Core core;
	Comandos cmd;
	Config cfg;
	Mensagens msg;
	ItensCore itcore;
	VendedoresCore vendcore;
	LojaMecanica mec;

	public Economy econ = null;
	
	public void onEnable(){
		core = new Core(this);
		cmd = new Comandos(this);
		cfg = new Config(this);
		msg = new Mensagens(this);
		itcore = new ItensCore(this);
		vendcore = new VendedoresCore(this);
		mec = new LojaMecanica(this);

		itcore.iniciarItens();
		itcore.carregarItens();
		
		vendcore.iniciarVendedores();
		vendcore.carregarVendedores();

		core.registrarEventos();
		mec.registrarEventos();
		
		cfg.iniciarConfig();
		msg.iniciarMensagens();

		this.getLogger().info("sLojaVillager ativado!!!");
		this.getLogger().info("Autor: Soldado_08");
		
		if(!iniciarEconomia()){
			getLogger().severe(
					String.format("[%s] - Desabilitado por falta do plugin Vault!", 
							new Object[] { getDescription().getName() }));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
	}
	
	public void onDisable(){
		this.getLogger().info("sLojaVillager desativado!!!");
		this.getLogger().info("Autor: Soldado_08");
	}
	
	private boolean iniciarEconomia(){
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer()
				.getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		this.econ = ((Economy)rsp.getProvider());
		return this.econ != null;
	}
}
