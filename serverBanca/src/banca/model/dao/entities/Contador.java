package banca.model.dao.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the contador database table.
 * 
 */
@Entity
@NamedQuery(name="Contador.findAll", query="SELECT c FROM Contador c")
public class Contador implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_contador")
	private Integer idContador;

	private String contador;

	private BigDecimal valor;

	public Contador() {
	}

	public Integer getIdContador() {
		return this.idContador;
	}

	public void setIdContador(Integer idContador) {
		this.idContador = idContador;
	}

	public String getContador() {
		return this.contador;
	}

	public void setContador(String contador) {
		this.contador = contador;
	}

	public BigDecimal getValor() {
		return this.valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

}