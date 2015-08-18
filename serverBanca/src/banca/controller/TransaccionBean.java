package banca.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import banca.model.dao.entities.Cuenta;
import banca.model.dao.entities.Usuario;
import banca.model.manager.ManagerCajero;

@ViewScoped
@ManagedBean
public class TransaccionBean implements Serializable{

	private static final long serialVersionUID = 509978829052612476L;
	
	private ManagerCajero mngCajero;
	
	//Datos Cuenta
	private String nroCuenta;
	private Cuenta cuenta; 
	private List<Cuenta> cuentas;

	
	//Datos cajero en session
	private Usuario cajero;
	
	//datos transaccion
	private double monto;
	
	
	public TransaccionBean() {
		mngCajero = new ManagerCajero();
		this.nroCuenta = "0";
		this.cuentas = mngCajero.findAllCuenta();
		this.cajero = SessionBean.verificarSession("operador");
	}
	
	public List<Cuenta> completeCuentas(String query) {
		List<Cuenta> allCuentas = cuentas;
		List<Cuenta> filteredCuentas= new ArrayList<Cuenta>();

		for (int i = 0; i < allCuentas.size(); i++) {
			Cuenta c = allCuentas.get(i);
			if (c.getNroCuenta().toLowerCase().contains(query.toLowerCase())) {
				filteredCuentas.add(c);
			}
		}
		
		return filteredCuentas;
	}
	
	public void realizarDeposito()
	{
		if(this.cuenta == null)
		{
			Mensaje.crearMensajeERROR("No se ha seleccionado ninguna cuenta.");
			return;
		}
		if(this.cuenta.getCliente() == null)
		{
			Mensaje.crearMensajeERROR("Hubo problemas obteniendo los datos del cliente.");
			return;
		}
			
		if(this.monto <= 0)
		{
			Mensaje.crearMensajeERROR("El monto no puede ser menor o igual a 0");
			return;
		}
			
		
		try {
			BigDecimal aux = new BigDecimal(monto);
			aux = aux.setScale(2, RoundingMode.DOWN);
			System.out.print("Monto:    "+aux.toString());
			mngCajero.deposito(aux, cuenta.getNroCuenta(), this.cuenta.getCliente(), cajero);
			Mensaje.crearMensajeINFO("La transaccion ha sido completada exitosamente.");
		} catch (Exception e) {
			Mensaje.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			this.monto = 0;
		}
	}
	public void realizarRetiro()
	{
		
		if(this.cuenta == null)
		{
			Mensaje.crearMensajeERROR("No se ha seleccionado ninguna cuenta.");
			return;
		}
		if(this.cuenta.getCliente() == null)
		{
			Mensaje.crearMensajeERROR("Hubo problemas obteniendo los datos del cliente.");
			return;
		}
			
		if(this.monto <= 0)
		{
			Mensaje.crearMensajeERROR("El monto no puede ser menor o igual a 0");
			return;
		}
		
		try {
			BigDecimal aux = new BigDecimal(monto);
			aux = aux.setScale(2, RoundingMode.DOWN);
			mngCajero.retiro(aux, cuenta.getNroCuenta(), this.cuenta.getCliente(), cajero);
			Mensaje.crearMensajeINFO("La transaccion ha sido completada exitosamente.");
		} catch (Exception e) {
			Mensaje.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			this.monto = 0;
		}
	}

	public String getNroCuenta() {
		return nroCuenta;
	}


	public void setNroCuenta(String nroCuenta) {
		this.nroCuenta = nroCuenta;
	}


	public List<Cuenta> getCuentas() {
		return cuentas;
	}


	public void setCuentas(List<Cuenta> cuentas) {
		this.cuentas = cuentas;
	}


	public double getMonto() {
		return monto;
	}


	public void setMonto(double monto) {
		this.monto = monto;
	}

	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	
	
}
