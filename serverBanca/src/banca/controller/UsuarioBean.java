package banca.controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import banca.model.dao.entities.Tipousr;
import banca.model.dao.entities.Usuario;
import banca.model.manager.ManagerAdmin;

@ViewScoped
@ManagedBean
public class UsuarioBean {
	
	private ManagerAdmin mngAdmin;
	
	private Integer idUsr;
	private String alias;
	private String apellido;
	private String nombre;
	private String pass;
	private Tipousr tipousr;
	private Integer idTipoUsr;
	private List<Usuario> listado;
	
	public UsuarioBean() {
		mngAdmin = new ManagerAdmin();
	}

	/**
	 * @return the idUsr
	 */
	public Integer getIdUsr() {
		return idUsr;
	}

	/**
	 * @param idUsr the idUsr to set
	 */
	public void setIdUsr(Integer idUsr) {
		this.idUsr = idUsr;
	}

	/**
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias the alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
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
	 * @return the pass
	 */
	public String getPass() {
		return pass;
	}

	/**
	 * @param pass the pass to set
	 */
	public void setPass(String pass) {
		this.pass = pass;
	}

	/**
	 * @return the idTipoUsr
	 */
	public Integer getIdTipoUsr() {
		return idTipoUsr;
	}

	/**
	 * @param idTipoUsr the idTipoUsr to set
	 */
	public void setIdTipoUsr(Integer idTipoUsr) {
		this.idTipoUsr = idTipoUsr;
	}
	
	/**
	 * @return the tipousr
	 */
	public Tipousr getTipousr() {
		return tipousr;
	}

	/**
	 * @param tipousr the tipousr to set
	 */
	public void setTipousr(Tipousr tipousr) {
		this.tipousr = tipousr;
	}

	/**
	 * @return the listado
	 */
	public List<Usuario> getListado() {
		listado = mngAdmin.findAllUsuario();
		return listado;
	}

	/**
	 * @param listado the listado to set
	 */
	public void setListado(List<Usuario> listado) {
		this.listado = listado;
	}
	
	/**
     * Método de tipos de usuario
     * @return listado de SelectItem
     */
    public List<SelectItem> listaTipoUsrSI(){
    	List<SelectItem> listadoSI=new ArrayList<SelectItem>();
    	List<Tipousr> tipos = mngAdmin.findAllTipoUSR();
    	for (Tipousr tipologin : tipos) {
    		listadoSI.add(new SelectItem(tipologin.getIdTipo(), tipologin.getTipo()));
		}
    	return listadoSI;
    }
    
    /**
     * Devuelve true o false si existe un usuario con el alias ingresado
     * @param alias
     * @return true o false
     */
    public boolean nickExiste(String alias){
		Usuario usr = mngAdmin.findUsuarioByAlias(alias);
		if(usr==null){
			return false;
		}else{
			return true;
		}
	}
    
    /**
     * Permite ingresar un nuevo usuario
     * @return vacio
     */
    public String ingresarUsuario(){
    	try {
    		//VALIDAR ALIAS
    		if(nickExiste(getAlias())){
    			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Alias de Usuario ya usuado",null));
    		}else if(idTipoUsr==-1 || idTipoUsr==null){//VALIDAR SELECCION DE ID TIPO USR
    			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Seleccione Tipo de Usuario",null));
    		}else{
    			setTipousr(mngAdmin.findTipoUSRByID(getIdTipoUsr()));
    			mngAdmin.insertarUsuario(getNombre(), getApellido(), getAlias(), getPass(), getTipousr());
    			setNombre("");setApellido("");setAlias("");setPass("");setIdTipoUsr(-1);setTipousr(null);
    			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario ingresado correctamente",null));
    		}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al ingresar usuario",null));
		}
    	return "";
    }
    
    /**
     * Toma datos de un usuario
     * @param usr usuario a cargar
     * @return ""
     */
    public String cargarDatosUSR(Usuario usr){
    	setIdUsr(usr.getIdUsr());setNombre(usr.getNombre());setApellido(usr.getApellido());setAlias(usr.getAlias());
    	setPass(usr.getPass());setTipousr(usr.getTipousr());setIdTipoUsr(usr.getTipousr().getIdTipo());
    	return "";
    }
	
    /**
     * Cancela la modificación de usuario
     * @return ""
     */
    public String cancelarModificarUSR(){
    	setIdUsr(null);setNombre("");setApellido("");setAlias("");
    	setPass("");setIdTipoUsr(-1);setTipousr(null);
    	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha cancelado la modificacion del usuario","SI"));
    	return "";
    }
	
    /**
     * Modifica datos de usuario
     * @return ""
     */
    public String modificarUSR(){
    	try {
    		//Si se ha seleccionado de la lista
			if(idTipoUsr != -1){
				setTipousr(mngAdmin.findTipoUSRByID(getIdTipoUsr()));
			}
			//Modificar usuario
			mngAdmin.modificarUsuario(getIdUsr(), getPass(), getTipousr());
			//Borrar Valores
			setIdUsr(null);setNombre("");setApellido("");setAlias("");
	    	setPass("");setIdTipoUsr(-1);setTipousr(null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al modificar usuario",null));
		}
    	return "";
    }
    
    /**
     * Activa o Desactiva un usuario
     * @param usr
     * @return ""
     */
    public String cambiarEstadoUSR(Usuario usr){
    	try {
    		mngAdmin.cambiarEstadoUSR(usr.getIdUsr());
    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cambio de estado correcto", null));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al camiar de estado", null));
		}
    	return "";
    }
}
