package banca.model.dao.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the tipocuenta database table.
 * 
 */
@Entity
@NamedQuery(name="Tipocuenta.findAll", query="SELECT t FROM Tipocuenta t")
public class Tipocuenta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TIPOCUENTA_IDTIPO_GENERATOR", sequenceName="SEC_TIPOCUENTA", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TIPOCUENTA_IDTIPO_GENERATOR")
	@Column(name="id_tipo")
	private Integer idTipo;

	private String tipo;

	//bi-directional many-to-one association to Cuenta
	@OneToMany(mappedBy="tipocuenta")
	private List<Cuenta> cuentas;

	public Tipocuenta() {
	}

	public Integer getIdTipo() {
		return this.idTipo;
	}

	public void setIdTipo(Integer idTipo) {
		this.idTipo = idTipo;
	}

	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public List<Cuenta> getCuentas() {
		return this.cuentas;
	}

	public void setCuentas(List<Cuenta> cuentas) {
		this.cuentas = cuentas;
	}

	public Cuenta addCuenta(Cuenta cuenta) {
		getCuentas().add(cuenta);
		cuenta.setTipocuenta(this);

		return cuenta;
	}

	public Cuenta removeCuenta(Cuenta cuenta) {
		getCuentas().remove(cuenta);
		cuenta.setTipocuenta(null);

		return cuenta;
	}

}