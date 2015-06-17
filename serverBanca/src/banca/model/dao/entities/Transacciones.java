package banca.model.dao.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.math.BigDecimal;


/**
 * The persistent class for the transacciones database table.
 * 
 */
@Entity
@NamedQuery(name="Transacciones.findAll", query="SELECT t FROM Transacciones t")
public class Transacciones implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TRANSACCIONES_IDTRANS_GENERATOR", sequenceName="SEC_TRANSACCIONES", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TRANSACCIONES_IDTRANS_GENERATOR")
	@Column(name="id_trans")
	private long idTrans;

	private Timestamp fecha;

	private BigDecimal monto;

	@Column(name="nro_cuenta")
	private String nroCuenta;

	@Column(name="nroc_destino")
	private String nrocDestino;

	@Column(name="saldo_actual")
	private BigDecimal saldoActual;

	@Column(name="saldo_final")
	private BigDecimal saldoFinal;

	//bi-directional many-to-one association to Cliente
	@ManyToOne
	@JoinColumn(name="id_cli")
	private Cliente cliente;

	//bi-directional many-to-one association to Estadotrans
	@ManyToOne
	@JoinColumn(name="id_est")
	private Estadotrans estadotran;

	//bi-directional many-to-one association to Tipotrans
	@ManyToOne
	@JoinColumn(name="id_tipotrans")
	private Tipotrans tipotran;

	public Transacciones() {
	}

	public long getIdTrans() {
		return this.idTrans;
	}

	public void setIdTrans(long idTrans) {
		this.idTrans = idTrans;
	}

	public Timestamp getFecha() {
		return this.fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
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

	public Tipotrans getTipotran() {
		return this.tipotran;
	}

	public void setTipotran(Tipotrans tipotran) {
		this.tipotran = tipotran;
	}

}