package banca.model.manager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import banca.model.dao.entities.Cliente;
import banca.model.dao.entities.Cuenta;
import banca.model.dao.entities.Estadotrans;
import banca.model.dao.entities.Historial;
import banca.model.dao.entities.Tipotrans;
import banca.model.dao.entities.Transacciones;
import banca.model.dao.entities.Transferencia;
import banca.model.dao.entities.Usuario;

public class ManagerServicios {
	private ManagerDAO mngDAO;

	public ManagerServicios() {
		mngDAO = new ManagerDAO();
	}

	/**
	 * Método que busca un usuario cliente respecto a su nick y contraseña
	 * 
	 * @param alias
	 * @param pass
	 * @return cliente
	 * @throws Exception
	 */
	public Cliente findClienteByAliasPass(String alias, String pass)
			throws Exception {
		try {
			@SuppressWarnings("unchecked")
			List<Cliente> listado = (List<Cliente>) mngDAO.findByParam(
					Cliente.class, "o.alias", alias, null);
			if (listado == null || listado.isEmpty()) {
				throw new Exception("No se encuentra el usuario.");
			}
			Cliente cli = listado.get(0);
			if (cli.getPass().equals(pass)) {// MD5 PASS getMD5(pass)
				return cli;
			} else {
				throw new Exception("Usuario o contraseña invalidos");
			}
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * Método que busca un usuario cliente por su id
	 * 
	 * @param id
	 * @return cliente
	 * @throws Exception
	 */
	public Cliente findClienteById(Integer id)
			throws Exception {
		try {
			Cliente c = (Cliente)mngDAO.findById(Cliente.class, id);
			return c;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Inserta la ip y fecha de conexion
	 * 
	 * @param id_cli
	 * @param ip
	 * @throws Exception
	 */
	public void insertIpDateConn(Integer id_cli, String ip) throws Exception {
		try {
			Cliente c = (Cliente) mngDAO.findById(Cliente.class, id_cli);
			c.setUltmIp(ip);
			c.setFechaUltmCon(new Date());
			mngDAO.actualizar(c);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Permite el cambio de contraseña
	 * 
	 * @param id_cli
	 * @param actPass
	 * @param nPass
	 * @param cnfPass
	 * @throws Exception
	 */
	public void cambiarPass(Integer id_cli, String actPass, String nPass,
			String cnfPass) throws Exception {
		try {
			Cliente c = (Cliente) mngDAO.findById(Cliente.class, id_cli);
			if (!c.getPass().equals(actPass)) {
				throw new Exception("Contrase&ntilde;a actual incorrecta");
			}
			if (!nPass.equals(cnfPass)) {
				throw new Exception(
						"La nueva contrase&ntilde;a como su confirmacion deben ser iguales");
			}
			c.setPass(nPass);// MD5 PASS getMD5(pass)
			mngDAO.actualizar(c);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Cambiar el correo de usuario cliente
	 * 
	 * @param id_cli
	 * @param mail
	 * @throws Exception
	 */
	public void cambiarMail(Integer id_cli, String mail) throws Exception {
		try {
			Cliente c = (Cliente) mngDAO.findById(Cliente.class, id_cli);
			c.setCorreo(mail);
			;
			mngDAO.actualizar(c);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Funcion interna para agregar una nueva transaccion
	 * 
	 * @param monto
	 * @param cuentaDestino
	 * @param cuentaOrigen
	 * @param cliente
	 * @param token
	 * @throws Exception
	 */
	public String crearTransferencia(BigDecimal monto, String cuentaDestino,
			String cuentaOrigen, int cliente, String token) throws Exception {

		System.out.print("Transaccion de monto " + monto.doubleValue());
		if (monto.doubleValue() <= 0)
			throw new Exception("El monto no puede ser menor o igual a 0");
		List<Transferencia> transferencias = (List<Transferencia>) mngDAO
				.findAll(Transferencia.class, "o.fecha DESC");
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int yA = cal.get(Calendar.YEAR);
		int mA = cal.get(Calendar.MONTH);
		int dA = cal.get(Calendar.DAY_OF_MONTH);
		double maxM = 0;
		for (Transferencia t : transferencias) {
			cal.setTime(t.getFecha());
			int y = cal.get(Calendar.YEAR);
			int m = cal.get(Calendar.MONTH);
			int d = cal.get(Calendar.DAY_OF_MONTH);
			if(d == dA && m == mA && y == yA)
				m += t.getMonto().doubleValue();
		}
		System.out.print("Monto maximo actual " + maxM);
		if (maxM > Transferencia.MONTO_MAX)
			throw new Exception("Has sobre pasado el límite de transacciones dirarias.");
		Transferencia t = new Transferencia();
		Cliente c = (Cliente) mngDAO.findById(Cliente.class, cliente);
		t.setCliente(c);
		t.setToken(token);
		Estadotrans estado = (Estadotrans) mngDAO.findById(Estadotrans.class,
				Estadotrans.EN_PROCESO);
		t.setEstadotran(estado);
		Timestamp fecha = new Timestamp(new Date().getTime());
		t.setFecha(fecha);
		t.setMonto(monto);
		t.setNrocDestino(cuentaDestino);
		t.setNroCuenta(cuentaOrigen);
		// 
		Cuenta cd = (Cuenta) mngDAO.findById(Cuenta.class, t.getNrocDestino());
		Cuenta co = (Cuenta) mngDAO.findById(Cuenta.class, t.getNroCuenta());
		BigDecimal saldoCD = cd.getSaldo();
		BigDecimal saldoCO = co.getSaldo();
		BigDecimal saldoA = co.getSaldo();
		
		if(saldoCO.doubleValue() - monto.doubleValue() < 0)
			throw new Exception("No tienes suficiente saldo en tu cuenta para realizar esta transacción.");
		
		saldoCD = saldoCD.add(monto);
		saldoCD = saldoCD.setScale(2, RoundingMode.HALF_UP);
		saldoCO = saldoCO.subtract(monto);
		saldoCO = saldoCO.setScale(2, RoundingMode.HALF_UP);
		
		t.setSaldoActual(saldoA);
		t.setSaldoFinal(saldoCO);

		mngDAO.insertar(t);
		return t.getIdTransf()+"";
	}
	/**
	 * Funcion interna para agregar una nueva transaccion
	 * 
	 * @param idTra
	 * @param idtoken
	 * @throws Exception
	 */
	public void validarTransferencia(int idTra, String token) throws Exception
	{
		Transferencia t = (Transferencia)mngDAO.findById(Transferencia.class,idTra);
		long fecha = t.getFecha().getTime();
		long maxFecha = new Date().getTime();
		maxFecha += (24 * 60 * 60 * 1000);
		if(t.getEstadotran().getIdEst().intValue() !=  Estadotrans.EN_PROCESO)
		{
			System.out.println("Estado finaizado");
			throw new Exception(t.getEstadotran().getEstado());
		}
		if(fecha > maxFecha)
		{
			System.out.println("Fecha invalida");
			Estadotrans es = (Estadotrans)mngDAO.findById(Estadotrans.class, Estadotrans.FALLIDA);
			t.setEstadotran(es);
			Historial ht = new Historial();
			ht.setEstado(es.getEstado() + ": Caduco periodo de validación.");
			Timestamp time = new Timestamp(new Date().getTime());
			ht.setFecha(time);
			ht.setMonto(t.getMonto());
			ht.setNrocDestino(t.getNrocDestino());
			ht.setNroCuenta(t.getNroCuenta());
			ht.setSaldoActual(t.getSaldoActual());
			ht.setSaldoFinal(t.getSaldoFinal());
			ht.setTipo("Transferencia");
			mngDAO.insertar(ht);
			mngDAO.actualizar(t);
			throw new Exception("Lo sentimos pero el periodo de 24 horas de validación ha finalizado.");
		}
		if(!token.equals(t.getToken()))
		{
			System.out.println("Token invalido");
			Estadotrans es = (Estadotrans)mngDAO.findById(Estadotrans.class, Estadotrans.FALLIDA);
			t.setEstadotran(es);
			Historial ht = new Historial();
			ht.setEstado(es.getEstado() + ": Token invalido.");
			Timestamp time = new Timestamp(new Date().getTime());
			ht.setFecha(time);
			ht.setMonto(t.getMonto());
			ht.setNrocDestino(t.getNrocDestino());
			ht.setNroCuenta(t.getNroCuenta());
			ht.setSaldoActual(t.getSaldoActual());
			ht.setSaldoFinal(t.getSaldoFinal());
			ht.setTipo("Transferencia");
			mngDAO.insertar(ht);
			mngDAO.actualizar(t);
			throw new Exception("Lo sentimos pero el token enviado es incorrecto.");
		}
		Cuenta cd = (Cuenta) mngDAO.findById(Cuenta.class, t.getNrocDestino());
		Cuenta co = (Cuenta) mngDAO.findById(Cuenta.class, t.getNroCuenta());
		BigDecimal saldoCD = cd.getSaldo();
		BigDecimal saldoCO = co.getSaldo();
		BigDecimal saldoA = co.getSaldo();
		
		BigDecimal monto = t.getMonto();
		if(saldoCO.doubleValue() - monto.doubleValue() < 0)
			throw new Exception("No tienes suficiente saldo en tu cuenta para realizar esta transacción.");
		
		saldoCD = saldoCD.add(monto);
		saldoCD = saldoCD.setScale(2, RoundingMode.HALF_UP);
		saldoCO = saldoCO.subtract(monto);
		saldoCO = saldoCO.setScale(2, RoundingMode.HALF_UP);

		// set nuevo saldo cuenta
		cd.setSaldo(saldoCD);
		co.setSaldo(saldoCO);
		mngDAO.actualizar(cd);
		mngDAO.actualizar(co);
		
		Historial ht = new Historial();
		Estadotrans es = (Estadotrans)mngDAO.findById(Estadotrans.class, Estadotrans.FINALIZADA);
		ht.setEstado(es.getEstado());
		Timestamp time = new Timestamp(new Date().getTime());
		ht.setFecha(time);
		ht.setMonto(t.getMonto());
		ht.setNrocDestino(t.getNrocDestino());
		ht.setNroCuenta(t.getNroCuenta());
		ht.setSaldoActual(t.getSaldoActual());
		ht.setSaldoFinal(t.getSaldoFinal());
		ht.setTipo("Transferencia");
		mngDAO.insertar(ht);
		mngDAO.actualizar(t);
	}

	/**
	 * Devuelve la lista de tranferencias por cliente y nro de cuenta
	 * 
	 * @param id_cli
	 * @param nrocuenta
	 * @return List<Transacciones>
	 */
	@SuppressWarnings("unchecked")
	public List<Transferencia> findTransByCli(Integer id_cli, String nrocuenta) {
		List<Transferencia> todas = mngDAO.findAll(Transferencia.class);
		List<Transferencia> lista = new ArrayList<Transferencia>();
		for (Transferencia t : todas) {
			if (t.getCliente().getIdCli().equals(id_cli)
					&& t.getNroCuenta().equals(nrocuenta)) {
				lista.add(t);
			}
		}
		return lista;
	}

	/**
	 * Devuelve la lista de cuentas por cliente
	 * 
	 * @param id_cli
	 * @return List<Cuenta>
	 */
	@SuppressWarnings("unchecked")
	public List<Cuenta> findCuentasByCli(Integer id_cli) {
		List<Cuenta> todas = mngDAO.findAll(Cuenta.class);
		List<Cuenta> lista = new ArrayList<Cuenta>();
		for (Cuenta c : todas) {
			if (c.getCliente().getIdCli().equals(id_cli)) {
				lista.add(c);
			}
		}
		return lista;
	}

	/**
	 * Crea un Objeto JSON de Transferencia
	 * 
	 * @param transf
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JSONObject jsonTrans(Transferencia transf) {
		JSONObject obj = new JSONObject();
		obj.put("cdestino", transf.getNrocDestino());
		obj.put("asaldo", transf.getSaldoActual().toString());
		obj.put("monto", transf.getMonto().toString());
		obj.put("saldo", transf.getSaldoFinal().toString());
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		obj.put("fecha", dateFormat.format(transf.getFecha()).toString());
		return obj;
	}

	/**
	 * Devuelve un array de transferencias
	 * 
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JSONArray arrayTransf(List<Transferencia> list) {
		JSONArray jarray = new JSONArray();
		for (Transferencia t : list) {
			jarray.add(jsonTrans(t));
		}
		return jarray;
	}

	/**
	 * Devuelve el array de cuentas con el array de transferencias respectivas
	 * [{"cuenta":"#","transf":[{},{},{}]},{"cuenta":"#","transf":[{},{},{}]}]
	 * 
	 * @param id_cli
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public JSONArray transfXcli(Integer id_cli) throws Exception {
		try {
			JSONArray jarray = new JSONArray();
			List<Cuenta> cuentas = this.findCuentasByCli(id_cli);
			for (Cuenta cta : cuentas) {
				JSONObject obj = new JSONObject();
				obj.put("cuenta", cta.getNroCuenta());
				obj.put("transf",
						this.arrayTransf(this.findTransByCli(id_cli,
								cta.getNroCuenta())));
				jarray.add(obj);
			}
			return jarray;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Devuelve el array de cuentas con su numero tipo y monto [{nro: #, tipo:
	 * A, monto: 100}]
	 * 
	 * @param id_cli
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public JSONArray cuentasXCli(Integer id_cli) throws Exception {
		try {
			JSONArray jarray = new JSONArray();
			List<Cuenta> cuentas = this.findCuentasByCli(id_cli);
			for (Cuenta cta : cuentas) {
				JSONObject obj = new JSONObject();
				obj.put("nro", cta.getNroCuenta());
				obj.put("tipo", cta.getTipocuenta().getTipo());
				obj.put("monto", cta.getSaldo());
				jarray.add(obj);
			}
			return jarray;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Devuelve la respuesta del servicio
	 * 
	 * @param status
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String jsonMensajes(String status, Object value) {
		JSONObject obj = new JSONObject();
		obj.put("status", status);
		obj.put("value", value);
		return obj.toJSONString();
	}

	/**
	 * Devuelve los datos de un usuario logeado
	 * 
	 * @param u
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public LinkedHashMap usrLog(Cliente u) {
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
