package banca.model.dao.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the usuario database table.
 * 
 */
@Entity
@NamedQuery(name="Usuario.findAll", query="SELECT u FROM Usuario u")
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="USUARIO_IDUSR_GENERATOR", sequenceName="SEC_USUARIO", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USUARIO_IDUSR_GENERATOR")
	@Column(name="id_usr")
	private Integer idUsr;

	private String alias;

	private String apellido;

	private String nombre;

	private String pass;

	//bi-directional many-to-one association to Estadousr
	@ManyToOne
	@JoinColumn(name="id_estado")
	private Estadousr estadousr;

	//bi-directional many-to-one association to Tipousr
	@ManyToOne
	@JoinColumn(name="id_tipo")
	private Tipousr tipousr;

	public Usuario() {
	}

	public Integer getIdUsr() {
		return this.idUsr;
	}

	public void setIdUsr(Integer idUsr) {
		this.idUsr = idUsr;
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

	public Estadousr getEstadousr() {
		return this.estadousr;
	}

	public void setEstadousr(Estadousr estadousr) {
		this.estadousr = estadousr;
	}

	public Tipousr getTipousr() {
		return this.tipousr;
	}

	public void setTipousr(Tipousr tipousr) {
		this.tipousr = tipousr;
	}

}