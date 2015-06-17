package banca.model.dao.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the respuestas database table.
 * 
 */
@Embeddable
public class RespuestasPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="id_cli", insertable=false, updatable=false)
	private Integer idCli;

	@Column(name="id_preg", insertable=false, updatable=false)
	private Integer idPreg;

	public RespuestasPK() {
	}
	public Integer getIdCli() {
		return this.idCli;
	}
	public void setIdCli(Integer idCli) {
		this.idCli = idCli;
	}
	public Integer getIdPreg() {
		return this.idPreg;
	}
	public void setIdPreg(Integer idPreg) {
		this.idPreg = idPreg;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof RespuestasPK)) {
			return false;
		}
		RespuestasPK castOther = (RespuestasPK)other;
		return 
			this.idCli.equals(castOther.idCli)
			&& this.idPreg.equals(castOther.idPreg);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.idCli.hashCode();
		hash = hash * prime + this.idPreg.hashCode();
		
		return hash;
	}
}