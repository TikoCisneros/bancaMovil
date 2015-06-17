package banca.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import banca.model.dao.entities.Cliente;
import banca.model.dao.entities.Cuenta;
import banca.model.dao.entities.Tipocuenta;
import banca.model.manager.ManagerCajero;

@ViewScoped
@ManagedBean
public class CuentaBean implements Serializable{

	private static final long serialVersionUID = 509978829052612476L;
	
	private ManagerCajero mngCajero;
	
	//Datos Cuenta
	private String nroCuenta;
	private Tipocuenta tipocuenta;
	private Integer idTipocuenta;
	private Cliente cliente;
	private BigDecimal saldo;
	private List<Cuenta> listCuentas;
	
	//Datos Cliente
	private Integer idCliente;
	private String nombre, apellido, ci;
	private List<Cliente> listClientes;
	
	public CuentaBean() {
		mngCajero = new ManagerCajero();
	}

	/**
	 * @return the nroCuenta
	 */
	public String getNroCuenta() {
		return nroCuenta;
	}

	/**
	 * @param nroCuenta the nroCuenta to set
	 */
	public void setNroCuenta(String nroCuenta) {
		this.nroCuenta = nroCuenta;
	}

	/**
	 * @return the tipocuenta
	 */
	public Tipocuenta getTipocuenta() {
		return tipocuenta;
	}

	/**
	 * @param tipocuenta the tipocuenta to set
	 */
	public void setTipocuenta(Tipocuenta tipocuenta) {
		this.tipocuenta = tipocuenta;
	}

	/**
	 * @return the idTipocuenta
	 */
	public Integer getIdTipocuenta() {
		return idTipocuenta;
	}

	/**
	 * @param idTipocuenta the idTipocuenta to set
	 */
	public void setIdTipocuenta(Integer idTipocuenta) {
		this.idTipocuenta = idTipocuenta;
	}

	/**
	 * @return the cliente
	 */
	public Cliente getCliente() {
		return cliente;
	}

	/**
	 * @param cliente the cliente to set
	 */
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	/**
	 * @return the saldo
	 */
	public BigDecimal getSaldo() {
		return saldo;
	}

	/**
	 * @param saldo the saldo to set
	 */
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	/**
	 * @return the listCuentas
	 */
	public List<Cuenta> getListCuentas() {
		listCuentas = mngCajero.findAllCuenta();
		return listCuentas;
	}

	/**
	 * @param listCuentas the listCuentas to set
	 */
	public void setListCuentas(List<Cuenta> listCuentas) {
		this.listCuentas = listCuentas;
	}

	/**
	 * @return the idCliente
	 */
	public Integer getIdCliente() {
		return idCliente;
	}

	/**
	 * @param idCliente the idCliente to set
	 */
	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the apellido
	 */
	public String getApellido() {
		return apellido;
	}

	/**
	 * @param apellido the apellido to set
	 */
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	/**
	 * @return the ci
	 */
	public String getCi() {
		return ci;
	}

	/**
	 * @param ci the ci to set
	 */
	public void setCi(String ci) {
		this.ci = ci;
	}

	/**
	 * @return the listClientes
	 */
	public List<Cliente> getListClientes() {
		listClientes = mngCajero.findallCliente();
		return listClientes;
	}

	/**
	 * @param listClientes the listClientes to set
	 */
	public void setListClientes(List<Cliente> listClientes) {
		this.listClientes = listClientes;
	}
	
	
	
}
