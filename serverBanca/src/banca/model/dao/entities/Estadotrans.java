package banca.model.dao.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the estadotrans database table.
 * 
 */
@Entity
@NamedQuery(name="Estadotrans.findAll", query="SELECT e FROM Estadotrans e")
public class Estadotrans implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final int EN_PROCESO = 1;
	public static final int FINALIZADA = 2;
	public static final int FALLIDA = 3; 

	@Id
	@SequenceGenerator(name="ESTADOTRANS_IDEST_GENERATOR", sequenceName="SEC_ESTADOTRANS", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ESTADOTRANS_IDEST_GENERATOR")
	@Column(name="id_est")
	private Integer idEst;

	private String estado;

	//bi-directional many-to-one association to Transacciones
	@OneToMany(mappedBy="estadotran")
	private List<Transacciones> transacciones;

	//bi-directional many-to-one association to Transferencia
	@OneToMany(mappedBy="estadotran")
	private List<Transferencia> transferencias;

	public Estadotrans() {
	}

	public Integer getIdEst() {
		return this.idEst;
	}

	public void setIdEst(Integer idEst) {
		this.idEst = idEst;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public List<Transacciones> getTransacciones() {
		return this.transacciones;
	}

	public void setTransacciones(List<Transacciones> transacciones) {
		this.transacciones = transacciones;
	}

	public Transacciones addTransaccione(Transacciones transaccione) {
		getTransacciones().add(transaccione);
		transaccione.setEstadotran(this);

		return transaccione;
	}

	public Transacciones removeTransaccione(Transacciones transaccione) {
		getTransacciones().remove(transaccione);
		transaccione.setEstadotran(null);

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
		transferencia.setEstadotran(this);

		return transferencia;
	}

	public Transferencia removeTransferencia(Transferencia transferencia) {
		getTransferencias().remove(transferencia);
		transferencia.setEstadotran(null);

		return transferencia;
	}

}