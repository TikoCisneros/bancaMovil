package banca.model.dao.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the estadousr database table.
 * 
 */
@Entity
@NamedQuery(name="Estadousr.findAll", query="SELECT e FROM Estadousr e")
public class Estadousr implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ESTADOUSR_IDESTADO_GENERATOR", sequenceName="SEC_ESTADOUSR", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ESTADOUSR_IDESTADO_GENERATOR")
	@Column(name="id_estado")
	private Integer idEstado;

	private String estado;

	//bi-directional many-to-one association to Usuario
	@OneToMany(mappedBy="estadousr")
	private List<Usuario> usuarios;

	public Estadousr() {
	}

	public Integer getIdEstado() {
		return this.idEstado;
	}

	public void setIdEstado(Integer idEstado) {
		this.idEstado = idEstado;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public List<Usuario> getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Usuario addUsuario(Usuario usuario) {
		getUsuarios().add(usuario);
		usuario.setEstadousr(this);

		return usuario;
	}

	public Usuario removeUsuario(Usuario usuario) {
		getUsuarios().remove(usuario);
		usuario.setEstadousr(null);

		return usuario;
	}

}