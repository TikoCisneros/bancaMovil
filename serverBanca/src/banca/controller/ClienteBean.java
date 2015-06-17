package banca.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import banca.model.dao.entities.Cliente;
import banca.model.manager.ManagerCajero;

@ViewScoped
@ManagedBean
public class ClienteBean implements Serializable{
	
	private static final long serialVersionUID = 2110676378949878435L;

	private ManagerCajero mngCajero;
	
	private Integer idCli;
	private String ciRuc;
	private String apellido;
	private String nombre;
	private String correo;
	private String direccion;
	private String telefono;
	private List<Cliente> listado;
	
	public ClienteBean() {
		mngCajero = new ManagerCajero();
	}

	/**
	 * @return the idCli
	 */
	public Integer getIdCli() {
		return idCli;
	}

	/**
	 * @param idCli the idCli to set
	 */
	public void setIdCli(Integer idCli) {
		this.idCli = idCli;
	}

	/**
	 * @return the ciRuc
	 */
	public String getCiRuc() {
		return ciRuc;
	}

	/**
	 * @param ciRuc the ciRuc to set
	 */
	public void setCiRuc(String ciRuc) {
		this.ciRuc = ciRuc;
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
	 * @return the correo
	 */
	public String getCorreo() {
		return correo;
	}

	/**
	 * @param correo the correo to set
	 */
	public void setCorreo(String correo) {
		this.correo = correo;
	}

	/**
	 * @return the direccion
	 */
	public String getDireccion() {
		return direccion;
	}

	/**
	 * @param direccion the direccion to set
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	/**
	 * @return the telefono
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * @param telefono the telefono to set
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * @return the listado
	 */
	public List<Cliente> getListado() {
		listado = mngCajero.findallCliente();
		return listado;
	}

	/**
	 * @param listado the listado to set
	 */
	public void setListado(List<Cliente> listado) {
		this.listado = listado;
	}
	
	/**
     * Permite ingresar un nuevo cliente
     * @return ""
     */
	public String ingresarCliente(){
		try {
			mngCajero.crearCliente(getNombre(), getApellido(), getCiRuc(), getTelefono(), getCorreo(), getDireccion());
			setNombre("");setApellido("");setCiRuc("");setCorreo("");setTelefono("");setDireccion("");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Ingreso correcto","Cliente agregado"));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al ingresar cliente",e.getMessage()));
		}
		return "";
	}
	
	/**
	 * Toma datos de un cliente
	 * @param cli cliente a cargar
	 * @return ""
	 */
	public String cargarCliente(Cliente cli){
		setIdCli(cli.getIdCli());setCiRuc(cli.getCiRuc());
		setNombre(cli.getNombre());	setApellido(cli.getApellido());
		setCorreo(cli.getCorreo());setTelefono(cli.getTelefono());
		setDireccion(cli.getDireccion());
		return "";
	}
	
	/**
	 * Cancela la modificación de un cliente
	 * @return ""
	 */
	public String cancelarModCliente(){
		setIdCli(-1);setNombre("");setApellido("");setCiRuc("");
		setCorreo("");setTelefono("");setDireccion("");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Modificacion cancelada",null));
		return "";
	}
	
	/**
	 * Modifica datos de cliente
	 * @return ""
	 */
	public String modificarCliente(){
		try {
			if(getIdCli() == -1 || getIdCli() == null){
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error","No se selecciono el cliente"));
			}else{
				mngCajero.modificarDatosCliente(getIdCli(), getTelefono(), getCorreo(), getDireccion());
				setIdCli(-1);setNombre("");setApellido("");setCiRuc("");
				setCorreo("");setTelefono("");setDireccion("");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Modificacion correcta","Datos de cliente cambiados"));
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al modificar cliente",e.getMessage()));
		}
		return "";
	}
}
