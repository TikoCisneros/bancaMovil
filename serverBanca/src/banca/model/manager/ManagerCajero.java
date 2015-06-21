package banca.model.manager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import banca.model.dao.entities.Cliente;
import banca.model.dao.entities.Contador;
import banca.model.dao.entities.Cuenta;
import banca.model.dao.entities.Estadotrans;
import banca.model.dao.entities.Historial;
import banca.model.dao.entities.Tipocuenta;
import banca.model.dao.entities.Tipotrans;
import banca.model.dao.entities.Transacciones;
import banca.model.dao.entities.Usuario;

public class ManagerCajero {
	
	private ManagerDAO mngDAO;
	
	public ManagerCajero() {
		mngDAO =  new ManagerDAO();
	}
	
	/*********************************CLIENTES*********************************/
	
	/**
	 * Devuelve todos los clientes existentes
	 * @return List de Clientes
	 */
	@SuppressWarnings("unchecked")
	public List<Cliente> findallCliente(){
		return mngDAO.findAll(Cliente.class);
	}
	
	/**
	 * 
	 * @param idcli
	 * @return Cliente
	 * @throws Exception
	 */
	public Cliente findClienteByID(Integer idcli) throws Exception{
		return (Cliente) mngDAO.findById(Cliente.class, idcli);
	}
	
	/**
	 * Verifica la existencia de alias
	 * @param alias
	 * @return true o false
	 */
	public boolean verificarAlias(String alias){
		boolean bandera = false;
		List<Cliente> listado = findallCliente();
		for (Cliente usr : listado) {
			if(usr.getAlias().equals(alias)){
				bandera = true;
			}
		}
		return bandera;
	}
	
	/**
	 * Permite crear un cliente
	 * @param nombre
	 * @param apellido
	 * @param ciRuc
	 * @param telefono
	 * @param correo
	 * @param direccion
	 * @throws Exception
	 */
	public void crearCliente(String nombre, String apellido, String ciRuc, String telefono, String correo, 
			String direccion) throws Exception{
		Cliente cli = new Cliente();
		cli.setNombre(nombre);cli.setApellido(apellido);cli.setCiRuc(ciRuc);
		cli.setTelefono(telefono);cli.setCorreo(correo);cli.setDireccion(direccion);
		mngDAO.insertar(cli);
	}
	
	/**
	 * Modifica datos de un cliente
	 * @param idCli
	 * @param telefono
	 * @param correo
	 * @param direccion
	 * @throws Exception
	 */
	public void modificarDatosCliente(Integer idCli, String telefono, String correo, String direccion) throws Exception{
		Cliente cli = findClienteByID(idCli);
		cli.setTelefono(telefono);cli.setCorreo(correo);cli.setDireccion(direccion);
		mngDAO.actualizar(cli);
	}
	
	/**
	 * Bloquea la cuenta de un cliente
	 * @param idCli
	 * @throws Exception
	 */
	public void bloquearCuentaCliente(Integer idCli) throws Exception{
		Cliente cli = findClienteByID(idCli);
		cli.setBloqueda(Cliente.BLOQUEADA);
		mngDAO.actualizar(cli);
	}
	/**
	 * Desbloquea la cuenta de un cliente
	 * @param idCli
	 * @throws Exception
	 */
	public void desbloquearCuentaCliente(Integer idCli) throws Exception{
		Cliente cli = findClienteByID(idCli);
		cli.setBloqueda(Cliente.NO_BLOQUEADA);
		mngDAO.actualizar(cli);
	}
	
	/*********************************CUENTAS*********************************/
	
	/**
	 * Devuelve todos los Tipos de Cuentas existentes
	 * @return List de Tipocuenta
	 */
	@SuppressWarnings("unchecked")
	public List<Tipocuenta> findAllTipoCuenta(){
		return mngDAO.findAll(Tipocuenta.class);
	}
	
	/**
	 * Busca un tipo de cuenta por id
	 * @param id_tipocuenta
	 * @return Tipocuenta
	 * @throws Exception
	 */
	public Tipocuenta findTipoCuentaByID(Integer id_tipocuenta) throws Exception{
		return (Tipocuenta) mngDAO.findById(Tipocuenta.class, id_tipocuenta);
	}
	
	/**
	 * Devuelve todos las Cuentas existentes
	 * @return List de cuentas
	 */
	@SuppressWarnings("unchecked")
	public List<Cuenta> findAllCuenta(){
		return mngDAO.findAll(Cuenta.class);
	}
	/**
	 * Devuelve una cuenta segun su numero de cuenta
	 * @return cuenta
	 */
	public Cuenta findCuentaByNro(String nro) throws Exception{
		return (Cuenta)mngDAO.findById(Cuenta.class, nro);
	}
	
	/**
	 * Permite la creacion de una cuenta para el cliente
	 * @param nrocuenta
	 * @param id_tipocuenta
	 * @param id_cliente
	 * @param saldo
	 * @throws Exception
	 */
	public void crearCuentaBancaria(String nroCuenta, Integer id_tipocuenta, Integer id_cliente, BigDecimal saldo) throws Exception{
		Cuenta cu = new Cuenta();
		cu.setNroCuenta(nroCuenta);cu.setSaldo(saldo);
		cu.setTipocuenta(this.findTipoCuentaByID(id_tipocuenta));
		cu.setCliente(this.findClienteByID(id_cliente));
		mngDAO.insertar(cu);
	}
	
	/**
	 * Devuelve los ultimos 4 digitos de una cuenta
	 * @param tipocuenta
	 * @return valor de la cuenta
	 */
	public String valorUltimaCuenta(String tipocuenta) throws Exception{
		String valor ="";
		Contador cont = new Contador();
		
		if(tipocuenta.toLowerCase().equals("corriente")){
			cont = (Contador) mngDAO.findById(Contador.class, 1);
		}else if(tipocuenta.toLowerCase().equals("ahorros")){
			cont = (Contador) mngDAO.findById(Contador.class, 2);
		}
		//añadir ceros antes
		Integer num = cont.getValor().intValue();
		for (int i = 0; i < ( 4 - num.toString().length() ); i++) {
			valor+="0";
		}
		//sumar contador
		cont.setValor(new BigDecimal(num+1));
		mngDAO.actualizar(cont);
		
		return valor+num;
	}
	
	/*********************************TRANSACCIONES*********************************/
	
	
	/**
	 * Funcion interna para agregar una nueva transaccion
	 * @param monto
	 * @param tipo
	 * @param cuentaDestino
	 * @param cliente
	 * @param cajero 
	 * @throws Exception
	 */
	private void  crearTransaccion(BigDecimal monto,Tipotrans tipo,String cuentaDestino, Cliente cliente, Usuario cajero) throws Exception{
		
		if(monto.doubleValue() <= 0)
			throw new Exception("El monto no puede ser menor o igual a 0");
		Transacciones t = new Transacciones();
		Historial ht = new Historial();
		t.setCliente(cliente);
		t.setTipotran(tipo);
		Estadotrans estado = (Estadotrans)mngDAO.findById(Estadotrans.class, Estadotrans.FINALIZADA);
		t.setEstadotran(estado);
		Timestamp fecha = new Timestamp(new Date().getTime());
		t.setFecha(fecha);
		t.setMonto(monto);
		t.setNrocDestino(cuentaDestino);
		t.setNroCuenta(cuentaDestino);
		Cuenta c = (Cuenta)mngDAO.findById(Cuenta.class, cuentaDestino);
		BigDecimal saldo = c.getSaldo();
		BigDecimal saldoA = new BigDecimal(0);
		if(tipo.getTipotrans().equals("Deposito"))
		{
			saldoA = saldo.add(monto);
			saldoA = saldoA.setScale(2, RoundingMode.HALF_UP);
		}
		else if(tipo.getTipotrans().equals("Retiro"))
		{
			saldoA = saldo.subtract(monto);
			if(saldoA.doubleValue() < 0 )
			saldoA = saldoA.setScale(2, RoundingMode.HALF_UP);
			if(saldoA.doubleValue() < 0 )
				throw new Exception("No tienes fondos suficientes para realizar este retiro.");
		}
		else if(tipo.getTipotrans().equals("Deposito"))
			throw new Exception("No se puede realizar transferencias por medio de este método.");
		t.setSaldoActual(saldo);
		t.setSaldoFinal(saldoA);
		
		//historial
		ht.setEstado(estado.getEstado());
		ht.setFecha(fecha);
		ht.setMonto(monto);
		ht.setNrocDestino(cuentaDestino);
		ht.setNroCuenta(cuentaDestino);
		ht.setSaldoActual(t.getSaldoActual());
		ht.setSaldoFinal(t.getSaldoFinal());
		ht.setTipo(tipo.getTipotrans());
		
		mngDAO.insertar(t);
		
		//set nuevo saldo cuenta
		c.setSaldo(saldoA);
		mngDAO.actualizar(c);
		mngDAO.insertar(ht);
	}
	/**
	 * Realiza un deposito a un a cuenta de un cliente
	 * @param monto
	 * @param tipo
	 * @param cuentaDestino
	 * @param cliente
	 * @param cajero 
	 * @throws Exception
	 */
	public void  deposito(BigDecimal monto, String cuentaDestino, Cliente cliente, Usuario cajero) throws Exception{
		Tipotrans tipo = (Tipotrans)mngDAO.findById(Tipotrans.class, Tipotrans.DEPOSITO);
		crearTransaccion(monto, tipo, cuentaDestino, cliente, cajero);
	}
	
	/**
	 * Realiza un deposito a un a cuenta de un cliente
	 * @param monto
	 * @param tipo
	 * @param cuentaDestino
	 * @param cliente
	 * @param cajero 
	 * @throws Exception
	 */
	public void  retiro(BigDecimal monto, String cuentaDestino, Cliente cliente, Usuario cajero) throws Exception{
		Tipotrans tipo = (Tipotrans)mngDAO.findById(Tipotrans.class, Tipotrans.RETIRO);
		crearTransaccion(monto, tipo, cuentaDestino, cliente, cajero);
	}
	
}
