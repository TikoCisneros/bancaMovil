package banca.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

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
	private Integer idTipocuenta;
	private Integer idCliente;
	private BigDecimal saldo;
	private List<Cuenta> listCuentas;
	
	//Datos Cliente
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
	
	/**
     * Método de tipos de cuenta
     * @return listado de SelectItem
     */
	public List<SelectItem> listaTipoCuentaSI(){
		List<SelectItem> listadoSI=new ArrayList<SelectItem>();
		List<Tipocuenta> todos = mngCajero.findAllTipoCuenta();
		for (Tipocuenta tipocuenta : todos) {
			listadoSI.add(new SelectItem(tipocuenta.getIdTipo(),tipocuenta.getTipo()));
		}
		return listadoSI;
	}
	
	/**
	 * Permite seleccionar un cliente para asignar una cuenta
	 * @param cli cliente
	 * @return ""
	 */
	public String seleccionCliente(Cliente cli){
		setNombre(cli.getNombre());setApellido(cli.getApellido());
		setCi(cli.getCiRuc());setIdCliente(cli.getIdCli());
		return "";
	}
	
	/**
	 * Devuelve true o false si el monto de cuenta es minimo para cada tipo
	 * @param tipocuenta
	 * @param valor
	 * @return true o false
	 */
	public boolean montoMinCuenta(String tipocuenta, BigDecimal valor){
		BigDecimal bd = montoXcuenta(tipocuenta);
		if(valor.compareTo(bd)==0 || valor.compareTo(bd)==1){
			return true;
		}else{
			return false;
		}	
	}
	
	/**
	 * Devuelve el monto minimo por cuenta y actualiza el valor de saldo
	 * @param tipocuenta
	 * @return 150 o 100
	 */
	public BigDecimal montoXcuenta(String tipocuenta){
		BigDecimal bd = null;
		if(tipocuenta.toLowerCase().equals("corriente")){
			bd = new BigDecimal(150);
		}else if(tipocuenta.toLowerCase().equals("ahorros")){
			bd = new BigDecimal(100);
		}
		setSaldo(bd);		
		return bd;
	}
	
	/**
	 * Genera un numero de cuenta
	 * @param tipocuenta
	 * @return
	 * @throws Exception
	 */
	public String nroCuenta(String tipocuenta) throws Exception{
		String valor="";
		try {
			if(tipocuenta.toLowerCase().equals("corriente")){
				valor = "2215";
			}else if(tipocuenta.toLowerCase().equals("ahorros")){
				valor = "2715";
			}
			valor=valor+mngCajero.valorUltimaCuenta(tipocuenta);
		} catch (Exception e) {
			throw new Exception("Error al generar numero de cuenta");
		}
		return valor;
	}
	
	/**
	 * Permite la creacion de una cuenta
	 * @return ""
	 */
	public String crearCuenta(){
		try {
			if(getIdCliente()==null || getIdCliente()==-1){
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Alerta","No se ha selaccionado cliente"));
			}else if(getIdTipocuenta()==null || getIdTipocuenta()==-1){
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Alerta","No se ha selaccionado cuenta"));
			}else{
				String tipocuenta = mngCajero.findTipoCuentaByID(getIdTipocuenta()).getTipo();
				if(!this.montoMinCuenta(tipocuenta, getSaldo())){
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Alerta","Monto menor"));
				}else{
					setNroCuenta(this.nroCuenta(tipocuenta));
					mngCajero.crearCuentaBancaria(getNroCuenta(), getIdTipocuenta(), getIdCliente(), getSaldo());
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto","Cuenta creada"));
				}
			} 
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al crear la cuenta",e.getMessage()));
		}
		return "";
	}
	
}
