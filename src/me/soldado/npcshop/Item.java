package me.soldado.npcshop;

import org.bukkit.Material;

public class Item {

	Material tipo;
	int data;
	String nome;
	double compra;
	double venda;
	int quantidade; //A quantidade só se aplica para a compra.
	int id;
	
	public Item(Material tipo, String nome, double compra, double venda, int quantidade, int id, int data){
		this.tipo = tipo;
		this.nome = nome;
		this.compra = compra;
		this.venda = venda;
		this.quantidade = quantidade;
		this.id = id;
		this.data = data;
	}

	public int getData() {
		return data;
	}

	public void setData(int data) {
		this.data = data;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Material getTipo() {
		return tipo;
	}

	public void setTipo(Material tipo) {
		this.tipo = tipo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public double getCompra() {
		return compra;
	}

	public void setCompra(double compra) {
		this.compra = compra;
	}

	public double getVenda() {
		return venda;
	}

	public void setVenda(double venda) {
		this.venda = venda;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	
}
