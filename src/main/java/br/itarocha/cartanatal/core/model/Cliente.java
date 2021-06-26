package br.itarocha.cartanatal.core.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

public class Cliente {
	
	@NotEmpty(message="Nome é obrigatório")
	@Size(min = 2, max = 64, message="Nome deve ter entre 2 a 64 caracteres")
	private String nome;
	
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date dataNascimento;
	
	private String horaNascimento;

	private String cidade;
	
	private String uf;
	
	private TipoLogico tudo;

	public Cliente(){
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getHoraNascimento() {
		return horaNascimento;
	}

	public void setHoraNascimento(String horaNascimento) {
		this.horaNascimento = horaNascimento;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public TipoLogico getTudo() {
		return tudo;
	}

	public void setTudo(TipoLogico tudo) {
		this.tudo = tudo;
	}


}
