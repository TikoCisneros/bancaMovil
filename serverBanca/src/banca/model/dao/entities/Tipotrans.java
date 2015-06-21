package banca.model.dao.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the tipotrans database table.
 * 
 */
@Entity
@NamedQuery(name="Tipotrans.findAll", query="SELECT t FROM Tipotrans t")
public class Tipotrans implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final int DEPOSITO = 1; 
	public static final int RETIRO = 2; 
	public static final int TRANSFERENCIA = 3; 

	@Id
	@SequenceGenerator(name="TIPOTRANS_IDTIPOTRANS_GENERATOR", sequenceName="SEC_TIPOTRANS", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TIPOTRANS_IDTIPOTRANS_GENERATOR")
	@Column(name="id_tipotrans")
	private Integer idTipotrans;

	private String tipotrans;

	//bi-directional many-to-one association to Transacciones
	@OneToMany(mappedBy="tipotran")
	private List<Transacciones> transacciones;

	public Tipotrans() {
	}

	public Integer getIdTipotrans() {
		return this.idTipotrans;
	}

	public void setIdTipotrans(Integer idTipotrans) {
		this.idTipotrans = idTipotrans;
	}

	public String getTipotrans() {
		return this.tipotrans;
	}

	public void setTipotrans(String tipotrans) {
		this.tipotrans = tipotrans;
	}

	public List<Transacciones> getTransacciones() {
		return this.transacciones;
	}

	public void setTransacciones(List<Transacciones> transacciones) {
		this.transacciones = transacciones;
	}

	public Transacciones addTransaccione(Transacciones transaccione) {
		getTransacciones().add(transaccione);
		transaccione.setTipotran(this);

		return transaccione;
	}

	public Transacciones removeTransaccione(Transacciones transaccione) {
		getTransacciones().remove(transaccione);
		transaccione.setTipotran(null);

		return transaccione;
	}

}