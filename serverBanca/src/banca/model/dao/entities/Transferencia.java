package banca.model.dao.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.math.BigDecimal;


/**
 * The persistent class for the transferencia database table.
 * 
 */
@Entity
@NamedQuery(name="Transferencia.findAll", query="SELECT t FROM Transferencia t")
public class Transferencia implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final double MONTO_MAX = 1000;

	@Id
	@SequenceGenerator(name="TRANSFERENCIA_IDTRANSF_GENERATOR", sequenceName="SEC_TRANSAFERENCIA", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TRANSFERENCIA_IDTRANSF_GENERATOR")
	@Column(name="id_transf")
	private long idTransf;

	private Timestamp fecha;

	private Integer intentos;

	private BigDecimal monto;

	@Column(name="nro_cuenta")
	private String nroCuenta;

	@Column(name="nroc_destino")
	private String nrocDestino;

	@Column(name="saldo_actual")
	private BigDecimal saldoActual;

	@Column(name="saldo_final")
	private BigDecimal saldoFinal;

	private String token;

	//bi-directional many-to-one association to Cliente
	@ManyToOne
	@JoinColumn(name="id_cli")
	private Cliente cliente;

	//bi-directional many-to-one association to Estadotrans
	@ManyToOne
	@JoinColumn(name="id_est")
	private Estadotrans estadotran;

	public Transferencia() {
	}

	public long getIdTransf() {
		return this.idTransf;
	}

	public void setIdTransf(long idTransf) {
		this.idTransf = idTransf;
	}

	public Timestamp getFecha() {
		return this.fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public Integer getIntentos() {
		return this.intentos;
	}

	public void setIntentos(Integer intentos) {
		this.intentos = intentos;
	}

	public BigDecimal getMonto() {
		return this.monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public String getNroCuenta() {
		return this.nroCuenta;
	}

	public void setNroCuenta(String nroCuenta) {
		this.nroCuenta = nroCuenta;
	}

	public String getNrocDestino() {
		return this.nrocDestino;
	}

	public void setNrocDestino(String nrocDestino) {
		this.nrocDestino = nrocDestino;
	}

	public BigDecimal getSaldoActual() {
		return this.saldoActual;
	}

	public void setSaldoActual(BigDecimal saldoActual) {
		this.saldoActual = saldoActual;
	}

	public BigDecimal getSaldoFinal() {
		return this.saldoFinal;
	}

	public void setSaldoFinal(BigDecimal saldoFinal) {
		this.saldoFinal = saldoFinal;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Estadotrans getEstadotran() {
		return this.estadotran;
	}

	public void setEstadotran(Estadotrans estadotran) {
		this.estadotran = estadotran;
	}

}