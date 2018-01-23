package me.soldado.npcshop;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class LojaMecanica implements Listener{

	public Main plugin;
	
	public LojaMecanica(Main plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void click(InventoryClickEvent event){
		
		Inventory inv = event.getInventory();
		if(!plugin.vendcore.inventarios.contains(inv)) return;
		
		Item item = getItem(event.getCurrentItem());
		Player p = (Player) event.getWhoClicked();
		if(item != null){
			if(event.getClick().equals(ClickType.RIGHT)){
				comprar(p, item);
			}else if(event.getClick().equals(ClickType.LEFT)){
				vender(p, item, false);
			}else if(event.getClick().equals(ClickType.SHIFT_LEFT)){
				vender(p, item, true);
			}
			
		}
		event.setCancelled(true);
	}
	
	public void comprar(Player p, Item item){
		
		if(item.getCompra() <= 0){
			p.sendMessage(plugin.msg.naocompra);
			return;
		}
		
		int quantidade = item.getQuantidade();
		double valor = item.getCompra();
		Material tipo = item.getTipo();
		int data = item.getData();
		
		if(possuiDinheiro(p, valor)){
			if(temEspaço(p.getInventory(), new ItemStack(tipo), quantidade)){
				if(p.hasPermission("lojavillager.vip")){
					double desconto = plugin.cfg.descontovip;
					desconto *= 0.01;
					desconto = 1 - desconto;
					valor *= desconto;
					p.sendMessage(plugin.msg.descontovipcompra);
					valor *= 0.9;
//					p.sendMessage("§6Por ser um jogador VIP, você ganhou 10% de desconto na compra.");
				}//else if(p.hasPermission("lojavillager.vip2")){
//					double desconto = plugin.cfg.descontovip;
//					desconto *= 0.01;
//					desconto = 1 - desconto;
//					valor *= desconto;
//					p.sendMessage(plugin.msg.descontovipcompra);
//					valor *= 0.8;
//					p.sendMessage("§6Por ser um jogador VIP+, você ganhou 20% de desconto na compra.");
				//}
				retirarDinheiro(p, valor);
				ItemStack itemcomprado = new ItemStack(tipo, 1, (byte) data);
				adicionarItem(p.getInventory(), itemcomprado, quantidade);
				String mensagem = plugin.msg.comprou.replace("%valor%", ChatColor.WHITE + getValorString(valor)
				+ ChatColor.getLastColors(plugin.msg.comprou)).replace("%item%", 
				ChatColor.WHITE + item.getNome() + ChatColor.getLastColors(plugin.msg.comprou))
				.replace("%quantidade%", quantidade + ChatColor.getLastColors(plugin.msg.comprou));
				p.sendMessage(mensagem);
				p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 1, 1);
			}else{
				p.sendMessage(plugin.msg.semespaço);
				p.playSound(p.getLocation(), Sound.NOTE_PLING, 1, 1);
			}
		}else{
			p.sendMessage(plugin.msg.semdinheiro);
			p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
		}
	}
	
	public void vender(Player p, Item item, boolean tudo){

		if(item.getVenda() <= 0){
			p.sendMessage(plugin.msg.naovende);
			return;
		}

		int quantidade = 1;
		if(tudo && quantidadeNoInv(p.getInventory(), item.getTipo(), item.getData()) > 0){
			quantidade = quantidadeNoInv(p.getInventory(), item.getTipo(), item.getData());
		}
		double valor = item.getVenda() * quantidade;

		if(quantidadeNoInv(p.getInventory(), item.getTipo(), item.getData()) >= quantidade){

			if(p.hasPermission("lojavillager.vip1")){
				double desconto = plugin.cfg.descontovip;
				desconto *= 0.01;
				valor *= (1 + desconto);
				p.sendMessage(plugin.msg.descontovipvenda);
//				valor *= 1.1;
//				p.sendMessage("§6Por ser um jogador VIP, você ganhou 10% de bônus na venda.");
//			}else if(p.hasPermission("lojavillager.vip2")){
////				double desconto = plugin.cfg.descontovip;
////				desconto *= 0.01;
////				valor *= (1 + desconto);
////				p.sendMessage(plugin.msg.descontovipvenda);
//				valor *= 1.2;
//				p.sendMessage("§6Por ser um jogador VIP+, você ganhou 20% de bônus na venda.");
			}
			adicionarDinheiro(p, valor);
			removerItens(p.getInventory(), item.getTipo(), quantidade, item.getData());
			String mensagem = plugin.msg.vendeu.replace("%valor%", ChatColor.WHITE + getValorString(valor)
			+ ChatColor.getLastColors(plugin.msg.comprou)).replace("%item%", 
					ChatColor.WHITE + item.getNome() + ChatColor.getLastColors(plugin.msg.comprou))
					.replace("%quantidade%", quantidade + ChatColor.getLastColors(plugin.msg.comprou));
			p.sendMessage(mensagem);
			p.playSound(p.getLocation(), Sound.VILLAGER_YES, 1, 1);
		}else{
			p.sendMessage(plugin.msg.semitens);
			p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
		}

	}
	
	public static void removerItens(Inventory inventory, Material type, int amount, int data) {
        if (amount <= 0) return;
        int size = inventory.getSize();
        for (int slot = 0; slot < size; slot++) {
            ItemStack is = inventory.getItem(slot);
            if (is == null) continue;
            if (type == is.getType() && is.getData().getData() == data) {
                int newAmount = is.getAmount() - amount;
                if (newAmount > 0) {
                    is.setAmount(newAmount);
                    break;
                } else {
                    inventory.clear(slot);
                    amount = -newAmount;
                    if (amount == 0) break;
                }
            }
        }
    }
	
    public int quantidadeNoInv(Inventory inv, Material type, int data){
    	
    	int quant = 0;
    	
    	for(int i = 0; i < inv.getSize(); i++){
    		ItemStack item = inv.getItem(i);
    		if(item != null && item.getType() != Material.AIR){
	    		if(item.getType().equals(type) && item.getData().getData() == data){
	    			quant += item.getAmount();
	    		}
    		}
    	}
    	return quant;
    }
	
	public void adicionarItem(Inventory inv, ItemStack item, int quantidade){
		
		int aux = quantidade;
		
		if(item.getMaxStackSize() < aux){
			int max = item.getMaxStackSize();
			while(aux > 0){
				item.setAmount(max);
				inv.addItem(item);
				aux -= max;
			}
		}else{
			item.setAmount(aux);
			inv.addItem(item);
			aux = 0;
		}
	}
	
	public boolean temEspaço(Inventory inv, ItemStack item, int quantidade){
		
		boolean aux = false;
		
		if(inv.firstEmpty() == -1){
			int n = 0;
			for(int i = 0; i < inv.getSize(); i++){
				
				if(inv.getItem(i).getType().equals(item.getType())){
					int qnt = inv.getItem(i).getAmount();
					int resto = item.getMaxStackSize() - qnt;
					n += resto;
				}
				
			}
			if(n >= quantidade){
				aux = true;
			}
		}else aux = true;
		
		return aux;
	}
	
	public boolean possuiDinheiro(Player p, double d){
		double dinheiro = this.plugin.econ.getBalance(p);
		if(dinheiro >= d){
			return true;
		}else return false;
	}
	
	public void adicionarDinheiro(Player p, double d){
		this.plugin.econ.depositPlayer(p, d);
	}
	
	public void retirarDinheiro(Player p, double d){
		this.plugin.econ.withdrawPlayer(p, d);
	}
	
	public double dinheiro(Player p){
		return this.plugin.econ.getBalance(p);
	}
	
	public Item getItem(ItemStack item){
		if(item == null || item.getType().equals(Material.AIR)) return null;
		if(!(item.hasItemMeta() && item.getItemMeta().hasDisplayName())) return null;
		String s = item.getItemMeta().getDisplayName();
		String idstring = s.substring(s.lastIndexOf("#")).replace("#", "");
		int id = Integer.parseInt(idstring);
		if(!plugin.itcore.itens.containsKey(id)) return null;
		return plugin.itcore.itens.get(id);
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
