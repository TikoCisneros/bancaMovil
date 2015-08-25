package banca.model.dao.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the cmsesion database table.
 * 
 */
@Entity
@NamedQuery(name="Cmsesion.findAll", query="SELECT c FROM Cmsesion c")
public class Cmsesion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CMSESION_IDSESION_GENERATOR", sequenceName="SEC_CMSESION", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CMSESION_IDSESION_GENERATOR")
	@Column(name="id_sesion")
	private Integer idSesion;

	@Column(name="clave_sesion")
	private String claveSesion;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_expiracion")
	private Date fechaExpiracion;

	@Column(name="id_cli")
	private Integer idCli;

	public Cmsesion() {
	}

	public Integer getIdSesion() {
		return this.idSesion;
	}

	public void setIdSesion(Integer idSesion) {
		this.idSesion = idSesion;
	}

	public String getClaveSesion() {
		return this.claveSesion;
	}

	public void setClaveSesion(String claveSesion) {
		this.claveSesion = claveSesion;
	}

	public Date getFechaExpiracion() {
		return this.fechaExpiracion;
	}

	public void setFechaExpiracion(Date fechaExpiracion) {
		this.fechaExpiracion = fechaExpiracion;
	}

	public Integer getIdCli() {
		return this.idCli;
	}

	public void setIdCli(Integer idCli) {
		this.idCli = idCli;
	}

}