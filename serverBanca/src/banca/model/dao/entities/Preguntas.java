package banca.model.dao.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the preguntas database table.
 * 
 */
@Entity
@NamedQuery(name="Preguntas.findAll", query="SELECT p FROM Preguntas p")
public class Preguntas implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PREGUNTAS_IDPREG_GENERATOR", sequenceName="SEC_PREGUNTAS", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PREGUNTAS_IDPREG_GENERATOR")
	@Column(name="id_preg")
	private Integer idPreg;

	private String pregunta;

	//bi-directional many-to-one association to Respuestas
	@OneToMany(mappedBy="pregunta")
	private List<Respuestas> respuestas;

	public Preguntas() {
	}

	public Integer getIdPreg() {
		return this.idPreg;
	}

	public void setIdPreg(Integer idPreg) {
		this.idPreg = idPreg;
	}

	public String getPregunta() {
		return this.pregunta;
	}

	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}

	public List<Respuestas> getRespuestas() {
		return this.respuestas;
	}

	public void setRespuestas(List<Respuestas> respuestas) {
		this.respuestas = respuestas;
	}

	public Respuestas addRespuesta(Respuestas respuesta) {
		getRespuestas().add(respuesta);
		respuesta.setPregunta(this);

		return respuesta;
	}

	public Respuestas removeRespuesta(Respuestas respuesta) {
		getRespuestas().remove(respuesta);
		respuesta.setPregunta(null);

		return respuesta;
	}

}