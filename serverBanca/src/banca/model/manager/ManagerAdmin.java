package banca.model.manager;

import java.util.List;

import banca.model.dao.entities.Estadousr;
import banca.model.dao.entities.Tipousr;
import banca.model.dao.entities.Usuario;

public class ManagerAdmin {
	
	private ManagerDAO mngDAO;
	
	public ManagerAdmin() {
		mngDAO =  new ManagerDAO();
	}
	
	/**
	 * Devuelve todos los tipos de usuario existentes
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Tipousr> findAllTipoUSR(){
		return mngDAO.findAll(Tipousr.class);
	}
	
	/**
	 * Devuelve un tipo de usuario por id
	 * @param id
	 * @return tipousr
	 * @throws Exception
	 */
	public Tipousr findTipoUSRByID(Integer id) throws Exception{
		return (Tipousr) mngDAO.findById(Tipousr.class, id);
	}
	
	/**
	 * Devuelve un estado de usuario por id
	 * @param id
	 * @return estadousr
	 * @throws Exception
	 */
	public Estadousr findEstadoUSRByID(Integer id) throws Exception{
		return (Estadousr) mngDAO.findById(Estadousr.class, id);
	}
	
	/**
	 * Devuelve todos los usuarios existentes
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Usuario> findAllUsuario(){
		return mngDAO.findAll(Usuario.class);
	}
	
	/**
	 * Devuelve un usuario por id
	 * @param id
	 * @return usuario
	 * @throws Exception
	 */
	public Usuario findUsuarioByID(Integer id) throws Exception{
		return (Usuario) mngDAO.findById(Usuario.class, id);
	}
	
	/**
	 * Verifica la existencia de alias
	 * @param alias
	 * @return true o false
	 */
	public boolean verificarAlias(String alias){
		boolean bandera = false;
		List<Usuario> listado = findAllUsuario();
		for (Usuario usr : listado) {
			if(usr.getAlias().equals(alias)){
				bandera = true;
			}
		}
		return bandera;
	}
	
	/**
	 * Ingresa un usuario a la BD
	 * @param nombre
	 * @param apellido
	 * @param alias
	 * @param pass
	 * @param tipo
	 * @throws Exception
	 */
	public void insertarUsuario(String nombre, String apellido, String alias, String pass, Tipousr tipo) throws Exception{
		Usuario usr = new Usuario();
		usr.setNombre(nombre);usr.setApellido(apellido);usr.setAlias(alias);usr.setPass(pass);
		usr.setTipousr(tipo);usr.setEstadousr(this.findEstadoUSRByID(1));//Activado por defecto
		mngDAO.insertar(usr);
	}
	
	/**
	 * Modificar datos de usuario
	 * @param id
	 * @param pass
	 * @param tipo
	 * @throws Exception
	 */
	public void modificarUsuario(Integer id, String pass, Tipousr tipo) throws Exception{
		Usuario usr = this.findUsuarioByID(id);
		usr.setPass(pass);usr.setTipousr(tipo);
		mngDAO.actualizar(usr);
	}
	
	/**
	 * Activa o desactiva un usuario
	 * @param id
	 * @throws Exception
	 */
	public void cambiarEstadoUSR(Integer id) throws Exception{
		Usuario usr = this.findUsuarioByID(id);
		if(usr.getEstadousr().getEstado().equals("Activado")){
			usr.setEstadousr(this.findEstadoUSRByID(2));
		}else{
			usr.setEstadousr(this.findEstadoUSRByID(1));
		}
		mngDAO.actualizar(usr);
	}
}
