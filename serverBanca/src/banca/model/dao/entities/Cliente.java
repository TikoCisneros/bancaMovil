package banca.model.dao.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the cliente database table.
 * 
 */
@Entity
@NamedQuery(name="Cliente.findAll", query="SELECT c FROM Cliente c")
public class Cliente implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String BLOQUEADA = "X";
	public static final String NO_BLOQUEADA = "O";
	public static final String CMOBIL_BLOQUEADA = "X";
	public static final String CMOBIL_NO_BLOQUEADA = "O";

	@Id
	@SequenceGenerator(name="CLIENTE_IDCLI_GENERATOR", sequenceName="SEC_CLIENTE", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CLIENTE_IDCLI_GENERATOR")
	@Column(name="id_cli")
	private Integer idCli;

	@Column(name="acepta_politicas")
	private String aceptaPoliticas;

	private String alias;

	private String apellido;

	private String bloqueda;

	@Column(name="ci_ruc")
	private String ciRuc;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="cm_fecha_ultm_con")
	private Date cmFechaUltmCon;

	@Column(name="cm_movil")
	private String cmMovil;

	@Column(name="cm_pass")
	private String cmPass;

	@Column(name="cm_pin")
	private String cmPin;

	@Column(name="cm_ultm_ip")
	private String cmUltmIp;

	private String correo;

	private String direccion;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_ultm_con")
	private Date fechaUltmCon;

	private String motivo;

	private String nombre;

	private String pass;

	private String telefono;

	@Column(name="ultm_ip")
	private String ultmIp;

	//bi-directional many-to-one association to Cuenta
	@OneToMany(mappedBy="cliente")
	private List<Cuenta> cuentas;

	//bi-directional many-to-one association to Respuestas
	@OneToMany(mappedBy="cliente")
	private List<Respuestas> respuestas;

	//bi-directional many-to-one association to Transacciones
	@OneToMany(mappedBy="cliente")
	private List<Transacciones> transacciones;

	//bi-directional many-to-one association to Transferencia
	@OneToMany(mappedBy="cliente")
	private List<Transferencia> transferencias;

	public Cliente() {
	}

	public Integer getIdCli() {
		return this.idCli;
	}

	public void setIdCli(Integer idCli) {
		this.idCli = idCli;
	}

	public String getAceptaPoliticas() {
		return this.aceptaPoliticas;
	}

	public void setAceptaPoliticas(String aceptaPoliticas) {
		this.aceptaPoliticas = aceptaPoliticas;
	}

	public String getAlias() {
		return this.alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getApellido() {
		return this.apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getBloqueda() {
		return this.bloqueda;
	}

	public void setBloqueda(String bloqueda) {
		this.bloqueda = bloqueda;
	}

	public String getCiRuc() {
		return this.ciRuc;
	}

	public void setCiRuc(String ciRuc) {
		this.ciRuc = ciRuc;
	}

	public Date getCmFechaUltmCon() {
		return this.cmFechaUltmCon;
	}

	public void setCmFechaUltmCon(Date cmFechaUltmCon) {
		this.cmFechaUltmCon = cmFechaUltmCon;
	}

	public String getCmMovil() {
		return this.cmMovil;
	}

	public void setCmMovil(String cmMovil) {
		this.cmMovil = cmMovil;
	}

	public String getCmPass() {
		return this.cmPass;
	}

	public void setCmPass(String cmPass) {
		this.cmPass = cmPass;
	}

	public String getCmPin() {
		return this.cmPin;
	}

	public void setCmPin(String cmPin) {
		this.cmPin = cmPin;
	}

	public String getCmUltmIp() {
		return this.cmUltmIp;
	}

	public void setCmUltmIp(String cmUltmIp) {
		this.cmUltmIp = cmUltmIp;
	}

	public String getCorreo() {
		return this.correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Date getFechaUltmCon() {
		return this.fechaUltmCon;
	}

	public void setFechaUltmCon(Date fechaUltmCon) {
		this.fechaUltmCon = fechaUltmCon;
	}

	public String getMotivo() {
		return this.motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPass() {
		return this.pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getUltmIp() {
		return this.ultmIp;
	}

	public void setUltmIp(String ultmIp) {
		this.ultmIp = ultmIp;
	}

	public List<Cuenta> getCuentas() {
		return this.cuentas;
	}

	public void setCuentas(List<Cuenta> cuentas) {
		this.cuentas = cuentas;
	}

	public Cuenta addCuenta(Cuenta cuenta) {
		getCuentas().add(cuenta);
		cuenta.setCliente(this);

		return cuenta;
	}

	public Cuenta removeCuenta(Cuenta cuenta) {
		getCuentas().remove(cuenta);
		cuenta.setCliente(null);

		return cuenta;
	}

	public List<Respuestas> getRespuestas() {
		return this.respuestas;
	}

	public void setRespuestas(List<Respuestas> respuestas) {
		this.respuestas = respuestas;
	}

	public Respuestas addRespuesta(Respuestas respuesta) {
		getRespuestas().add(respuesta);
		respuesta.setCliente(this);

		return respuesta;
	}

	public Respuestas removeRespuesta(Respuestas respuesta) {
		getRespuestas().remove(respuesta);
		respuesta.setCliente(null);

		return respuesta;
	}

	public List<Transacciones> getTransacciones() {
		return this.transacciones;
	}

	public void setTransacciones(List<Transacciones> transacciones) {
		this.transacciones = transacciones;
	}

	public Transacciones addTransaccione(Transacciones transaccione) {
		getTransacciones().add(transaccione);
		transaccione.setCliente(this);

		return transaccione;
	}

	public Transacciones removeTransaccione(Transacciones transaccione) {
		getTransacciones().remove(transaccione);
		transaccione.setCliente(null);

		return transaccione;
	}

	public List<Transferencia> getTransferencias() {
		return this.transferencias;
	}

	public void setTransferencias(List<Transferencia> transferencias) {
		this.transferencias = transferencias;
	}

	public Transferencia addTransferencia(Transferencia transferencia) {
		getTransferencias().add(transferencia);
		transferencia.setCliente(this);

		return transferencia;
	}

	public Transferencia removeTransferencia(Transferencia transferencia) {
		getTransferencias().remove(transferencia);
		transferencia.setCliente(null);

		return transferencia;
	}

}