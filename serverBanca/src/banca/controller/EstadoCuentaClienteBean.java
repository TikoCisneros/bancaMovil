package banca.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import banca.model.dao.entities.Cliente;
import banca.model.dao.entities.Cuenta;
import banca.model.dao.entities.Usuario;
import banca.model.manager.ManagerCajero;

@ViewScoped
@ManagedBean
public class EstadoCuentaClienteBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private ManagerCajero mngCajero;

	// Cuentas de clietnes
	private List<Cliente> clientesO;
	private List<Cliente> clientesX;

	private String motivo;
	private String motivoCM;

	// cliente actual
	private int idCliAct;
	// Datos cajero en session
	private Usuario sesion;

	public EstadoCuentaClienteBean() {
		mngCajero = new ManagerCajero();
		geClientes();
		motivo = "Petición de bloqueo de cuenta por parte del cliente.";
		this.sesion = SessionBean.verificarSession("operador");
	}

	private void geClientes()
	{
		clientesO = new ArrayList<Cliente>();
		clientesX = new ArrayList<Cliente>();
		for (Cliente c : mngCajero.findallCliente()) {
			if (c.getBloqueda() != null && c.getBloqueda().equals(Cliente.BLOQUEADA))
				clientesX.add(c);
			else
				clientesO.add(c);
		}
	}
	public void activarCuenta(int idCli) {
		try {
			mngCajero.desbloquearCuentaCliente(idCli);
			Mensaje.crearMensajeINFO("La cuenta fue activada satisfactoriamente.");
			geClientes();
		} catch (Exception e) {
			e.printStackTrace();
			Mensaje.crearMensajeERROR(e.getMessage());
		}
	}

	public void desactivarCuenta() {
		try {
			if (motivo == null || motivo.isEmpty())
				throw new Exception("Debes escribir el motivo de bloqueo.");
			mngCajero.bloquearCuentaCliente(idCliAct, motivo);
			Mensaje.crearMensajeINFO("La cuenta fue desactivada satisfactoriamente.");
			geClientes();
		} catch (Exception e) {
			e.printStackTrace();
			Mensaje.crearMensajeERROR(e.getMessage());
		} finally {
			motivo = "Petición de bloqueo de cuenta por parte del cliente.";
			idCliAct = 0;
		}
	}

	public void activarCM(int idCli) {
		try {
			mngCajero.desbloquearCuentaMobil(idCli);
			Mensaje.crearMensajeINFO("La cuenta mobil fue activada satisfactoriamente.");
		} catch (Exception e) {
			e.printStackTrace();
			Mensaje.crearMensajeERROR(e.getMessage());
		}
	}

	public void desactivarCM() {
		try {
			if (motivo == null || motivo.isEmpty())
				throw new Exception("Debes escribir el motivo de bloqueo.");
			mngCajero.bloquearCuentaMobil(idCliAct, motivo);
			Mensaje.crearMensajeINFO("La cuenta mobil fue desactivada satisfactoriamente.");
		} catch (Exception e) {
			e.printStackTrace();
			Mensaje.crearMensajeERROR(e.getMessage());
		} finally {
			idCliAct = 0;
		}
	}
	
	public void resetCW(Cliente cli){
		try {
			mngCajero.reseteoCuentaWeb(cli);
			Mensaje.crearMensajeINFO("Se envió correctamente la clave provisional");
		} catch (Exception e) {
			e.printStackTrace();
			Mensaje.crearMensajeERROR(e.getMessage());
		}
	}

	public void clienteActual(int id) {
		this.idCliAct = id;
	}

	public List<Cliente> getClientesO() {
		return clientesO;
	}

	public void setClientesO(List<Cliente> clientesO) {
		this.clientesO = clientesO;
	}

	public List<Cliente> getClientesX() {
		return clientesX;
	}

	public void setClientesX(List<Cliente> clientesX) {
		this.clientesX = clientesX;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getMotivoCM() {
		return motivoCM;
	}

	public void setMotivoCM(String motivoCM) {
		this.motivoCM = motivoCM;
	}

	public int getIdCliAct() {
		return idCliAct;
	}

	public void setIdCliAct(int idCliAct) {
		this.idCliAct = idCliAct;
	}

}
