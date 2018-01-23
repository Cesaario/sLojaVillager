package me.soldado.npcshop;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

public class Vendedor {

	String nome;
	ArrayList<Item> itens;
	
	public Vendedor(String nome, ArrayList<Item> itens){
		this.nome = nome;
		this.itens = itens;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public ArrayList<Item> getItens() {
		return itens;
	}

	public void setItens(ArrayList<Item> itens) {
		this.itens = itens;
	}
	
}
