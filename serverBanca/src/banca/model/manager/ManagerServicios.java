package banca.model.manager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import banca.model.dao.entities.Cliente;
import banca.model.dao.entities.Cuenta;
import banca.model.dao.entities.Transferencia;

public class ManagerServicios {
	private ManagerDAO mngDAO;
	
	public ManagerServicios() {
		mngDAO = new ManagerDAO();
	}
	
	/**
	 * Método que busca un usuario cliente respecto a su nick y contraseña
	 * @param alias
	 * @param pass
	 * @return cliente
	 * @throws Exception
	 */
	public Cliente findClienteByAliasPass(String alias, String pass) throws Exception{
		try {
			@SuppressWarnings("unchecked")
			List<Cliente> listado = (List<Cliente>) mngDAO.findByParam(Cliente.class, "o.alias", alias, null);
			if(listado == null || listado.isEmpty()){
				throw new Exception("No se encuentra el usuario."); 
			}
			Cliente cli = listado.get(0);
			if (cli.getPass().equals(pass)) {//MD5 PASS getMD5(pass)
				return cli;
			}else{
				throw new Exception("Usuario o contraseña invalidos");
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * Inserta la ip y fecha de conexion
	 * @param id_cli
	 * @param ip
	 * @throws Exception
	 */
	public void insertIpDateConn(Integer id_cli, String ip) throws Exception{
		try {
			Cliente c = (Cliente) mngDAO.findById(Cliente.class, id_cli);
			c.setUltmIp(ip);c.setFechaUltmCon(new Date());
			mngDAO.actualizar(c);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * Permite el cambio de contraseña
	 * @param id_cli
	 * @param actPass
	 * @param nPass
	 * @param cnfPass
	 * @throws Exception
	 */
	public void cambiarPass(Integer id_cli, String actPass, String nPass, String cnfPass)throws Exception{
		try {
			Cliente c = (Cliente) mngDAO.findById(Cliente.class, id_cli);
			if(!c.getPass().equals(actPass)){
				throw new Exception("Contrase&ntilde;a actual incorrecta");
			}
			if(!nPass.equals(cnfPass)){
				throw new Exception("La nueva contrase&ntilde;a como su confirmacion deben ser iguales");
			}
			c.setPass(nPass);//MD5 PASS getMD5(pass)
			mngDAO.actualizar(c);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * Cambiar el correo de usuario cliente
	 * @param id_cli
	 * @param mail
	 * @throws Exception
	 */
	public void cambiarMail(Integer id_cli, String mail) throws Exception{
		try {
			Cliente c = (Cliente) mngDAO.findById(Cliente.class, id_cli);
			c.setCorreo(mail);;
			mngDAO.actualizar(c);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * Devuelve la lista de tranferencias por cliente y nro de cuenta
	 * @param id_cli
	 * @param nrocuenta
	 * @return List<Transacciones>
	 */
	@SuppressWarnings("unchecked")
	public List<Transferencia> findTransByCli(Integer id_cli, String nrocuenta){
		List<Transferencia> todas = mngDAO.findAll(Transferencia.class);
		List<Transferencia> lista = new ArrayList<Transferencia>();
		for (Transferencia t : todas) {
			if(t.getCliente().getIdCli().equals(id_cli) && t.getNroCuenta().equals(nrocuenta)){
				lista.add(t);
			}
		}
		return lista;
	}
	
	/**
	 * Devuelve la lista de cuentas por cliente
	 * @param id_cli
	 * @return List<Cuenta>
	 */
	@SuppressWarnings("unchecked")
	public List<Cuenta> findCuentasByCli(Integer id_cli){
		List<Cuenta> todas = mngDAO.findAll(Cuenta.class);
		List<Cuenta> lista = new ArrayList<Cuenta>();
		for (Cuenta c : todas) {
			if(c.getCliente().getIdCli().equals(id_cli)){
				lista.add(c);
			}
		}
		return lista;
	}
	
	/**
	 * Crea un Objeto JSON de Transferencia
	 * @param transf
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JSONObject jsonTrans(Transferencia transf){
		JSONObject obj=new JSONObject();
		obj.put("cdestino", transf.getNrocDestino());
		obj.put("asaldo",transf.getSaldoActual().toString());
		obj.put("monto",transf.getMonto().toString());
		obj.put("saldo", transf.getSaldoFinal().toString());
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		obj.put("fecha", dateFormat.format(transf.getFecha()).toString());
		return obj;
	}
	
	/**
	 * Devuelve un array de transferencias
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JSONArray arrayTransf(List<Transferencia> list){
		JSONArray jarray = new JSONArray();
		for (Transferencia t : list) {
			jarray.add(jsonTrans(t));
		}
		return jarray;
	}
	
	/**
	 * Devuelve el array de cuentas con el array de transferencias respectivas
	 * [{"cuenta":"#","transf":[{},{},{}]},{"cuenta":"#","transf":[{},{},{}]}]
	 * @param id_cli
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public JSONArray transfXcli(Integer id_cli) throws Exception{
		try {
			JSONArray jarray = new JSONArray();
			List<Cuenta> cuentas = this.findCuentasByCli(id_cli);
			for (Cuenta cta : cuentas) {
				JSONObject obj=new JSONObject();
				obj.put("cuenta", cta.getNroCuenta());
				obj.put("transf", this.arrayTransf(this.findTransByCli(id_cli, cta.getNroCuenta())));
				jarray.add(obj);
			}
			return jarray;
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * Devuelve la respuesta del servicio
	 * @param status
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String jsonMensajes(String status, Object value){
		JSONObject obj=new JSONObject();
		obj.put("status", status);
		obj.put("value", value);
		return obj.toJSONString();
	}
	
	/**
	 * Devuelve los datos de un usuario logeado
	 * @param u
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public LinkedHashMap usrLog(Cliente u){
		LinkedHashMap lhm = new LinkedHashMap();
		lhm.put("id", u.getIdCli().toString());
		lhm.put("nombre", u.getNombre());
		lhm.put("apellido", u.getApellido());
		lhm.put("ci", u.getCiRuc());
		lhm.put("direccion", u.getDireccion());
		lhm.put("telefono", u.getTelefono());
		lhm.put("correo", u.getCorreo());
    	return lhm;
    }
	
	
}
