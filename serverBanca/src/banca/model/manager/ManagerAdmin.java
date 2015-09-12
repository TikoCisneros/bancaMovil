package banca.model.manager;

import java.util.List;

import banca.controller.servicios.Funciones;
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
	 * Devuelve un usuario por ALias
	 * @param alias
	 * @return
	 */
	public Usuario findUsuarioByAlias(String alias){
		Usuario resp = null;
		List<Usuario> listado = findAllUsuario();
		for (Usuario usuario : listado) {
			if(usuario.getAlias().equals(alias)){
				resp = usuario;
			}
		}
		return resp;
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
		usr.setNombre(nombre);usr.setApellido(apellido);usr.setAlias(alias);usr.setPass(Funciones.Encriptar(pass));
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
		usr.setPass(Funciones.Encriptar(pass));usr.setTipousr(tipo);
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
	
	/**
	 * Método que busca un usuario respecto a su nick y contraseña
	 * @param nick 
	 * @param pass
	 * @return Usuario o null
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Usuario findUserByAliasAndPass(String alias, String pass)throws Exception{
		try {
			List<Usuario> listado = (List<Usuario>) mngDAO.findByParam(Usuario.class, "o.alias", alias, null);
			if(listado == null || listado.isEmpty()){
				throw new Exception("No se encuentra el usuario."); 
			}
			Usuario u = listado.get(0);
			if(u.getEstadousr().getEstado().toLowerCase().equals("desactivado")){
				throw new Exception("Su usuario ha sido desactivado.");
			}
			if (u.getPass().equals(Funciones.Encriptar(pass))) {//MD5 PASS Encriptar(pass)
				return u;
			}else{
				throw new Exception("Usuario o contraseña invalidos");
			}		
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
}
