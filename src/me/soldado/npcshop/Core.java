package me.soldado.npcshop;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Core implements Listener {

	public Main plugin;
	
	public Core(Main plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void clicar(PlayerInteractEntityEvent event){
	
		Entity ent = event.getRightClicked();
		Player p = event.getPlayer();
		if(ent instanceof LivingEntity){
			if(ent.getCustomName() == null) return;
			String s = ent.getCustomName();
			if(!s.contains("§")) return;
			if(!plugin.vendcore.vendedores.containsKey(ChatColor.stripColor(s))) return;
			Vendedor vend = plugin.vendcore.vendedores.get(ChatColor.stripColor(s));
			abrirVendedor(p, vend);
			p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
		}
	}
	
	public void abrirVendedor(Player p, Vendedor vend){
		int slots = 0;
		int itens = vend.getItens().size();
		
		if(itens < 10) slots = 9; else
		if(itens < 19) slots = 18; else
		if(itens < 28) slots = 27; else
		if(itens < 37) slots = 36; else
		if(itens < 46) slots = 45; else
		if(itens < 55) slots = 54;
		
		Inventory inv = Bukkit.getServer().createInventory(null, slots, vend.getNome());
		for(int i = 0; i < vend.getItens().size(); i++){
			ItemStack itgui = getItemGUI(vend.getItens().get(i));
			inv.setItem(i, itgui);
		}
		p.openInventory(inv);
		if(!plugin.vendcore.inventarios.contains(inv)) plugin.vendcore.inventarios.add(inv);
		
	}
	
	public ItemStack getItemGUI(Item item){
		
		int data = item.getData();
		ItemStack itemgui = new ItemStack(Material.getMaterial(item.getTipo().getId()), 1, (byte) data);
		ItemMeta itemguim = itemgui.getItemMeta();
		itemguim.setDisplayName(ChatColor.YELLOW + item.getNome() + ChatColor.DARK_GRAY + " #" + item.getId());
		List<String> lore = new ArrayList<String>();
		lore.add("");
		if(item.getCompra() > 0){
			String valor = getValorString(item.getCompra());
			int qnt = item.getQuantidade();
			lore.add(ChatColor.GREEN + " Para comprar:");
			lore.add(ChatColor.WHITE + " " + valor + ChatColor.GRAY + "$.");
			lore.add(ChatColor.GRAY + " Quantidade: " + ChatColor.WHITE + qnt);
			lore.add("");
		}
		if(item.getVenda() > 0){
			int qntmax = item.getTipo().getMaxStackSize();
			double valorunidade = item.getVenda();
			double valorpack = valorunidade * 64;
			double valorinv = valorpack * 36;
			String un = getValorString(valorunidade);
			String pk = getValorString(valorpack);
			String iv = getValorString(valorinv);
			lore.add(ChatColor.GREEN + " Para vender:");
			lore.add(ChatColor.WHITE + " "  + un + ChatColor.GRAY + "$ por unidade.");
			lore.add(ChatColor.WHITE + " "  + pk + ChatColor.GRAY + "$ por pack.");
			lore.add(ChatColor.WHITE + " " + iv + ChatColor.GRAY + "$ por inventário.");
			lore.add("");
		}
		if(item.getCompra() > 0) lore.add(ChatColor.GREEN + " [Botão direito para comprar]");
		if(item.getVenda() > 0) lore.add(ChatColor.RED + " [Botão esquerdo para vender]");
		itemguim.setLore(lore);
		itemgui.setItemMeta(itemguim);
		return itemgui;
	}
	
    public String getValorString(double d){
    	
    	double nd = Math.round(d * 100.0) / 100.0;
    	DecimalFormat df = new DecimalFormat("###,###,###,###.##");
    	String s = df.format(nd);
    	return s;
    	
    }

    public void registrarEventos(){
    	plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
}
