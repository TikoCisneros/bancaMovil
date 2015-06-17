package banca.model.dao.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.math.BigDecimal;


/**
 * The persistent class for the historial database table.
 * 
 */
@Entity
@NamedQuery(name="Historial.findAll", query="SELECT h FROM Historial h")
public class Historial implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="HISTORIAL_IDHIST_GENERATOR", sequenceName="SEC_HISTORIAL", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="HISTORIAL_IDHIST_GENERATOR")
	@Column(name="id_hist")
	private long idHist;

	private String estado;

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

	private String tipo;

	public Historial() {
	}

	public long getIdHist() {
		return this.idHist;
	}

	public void setIdHist(long idHist) {
		this.idHist = idHist;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
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

	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}