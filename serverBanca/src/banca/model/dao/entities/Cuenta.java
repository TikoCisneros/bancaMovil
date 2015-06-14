package banca.model.dao.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the cuenta database table.
 * 
 */
@Entity
@NamedQuery(name="Cuenta.findAll", query="SELECT c FROM Cuenta c")
public class Cuenta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="nro_cuenta")
	private String nroCuenta;

	private BigDecimal saldo;

	//bi-directional many-to-one association to Cliente
	@OneToMany(mappedBy="cuenta")
	private List<Cliente> clientes;

	//bi-directional many-to-one association to Tipocuenta
	@ManyToOne
	@JoinColumn(name="id_tipo")
	private Tipocuenta tipocuenta;

	public Cuenta() {
	}

	public String getNroCuenta() {
		return this.nroCuenta;
	}

	public void setNroCuenta(String nroCuenta) {
		this.nroCuenta = nroCuenta;
	}

	public BigDecimal getSaldo() {
		return this.saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public List<Cliente> getClientes() {
		return this.clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public Cliente addCliente(Cliente cliente) {
		getClientes().add(cliente);
		cliente.setCuenta(this);

		return cliente;
	}

	public Cliente removeCliente(Cliente cliente) {
		getClientes().remove(cliente);
		cliente.setCuenta(null);

		return cliente;
	}

	public Tipocuenta getTipocuenta() {
		return this.tipocuenta;
	}

	public void setTipocuenta(Tipocuenta tipocuenta) {
		this.tipocuenta = tipocuenta;
	}

}