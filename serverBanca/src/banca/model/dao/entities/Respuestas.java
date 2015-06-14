package banca.model.dao.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the respuestas database table.
 * 
 */
@Entity
@NamedQuery(name="Respuestas.findAll", query="SELECT r FROM Respuestas r")
public class Respuestas implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RespuestasPK id;

	private String respuesta;

	//bi-directional many-to-one association to Cliente
	@ManyToOne
	@JoinColumn(name="id_cli")
	private Cliente cliente;

	//bi-directional many-to-one association to Preguntas
	@ManyToOne
	@JoinColumn(name="id_preg")
	private Preguntas pregunta;

	public Respuestas() {
	}

	public RespuestasPK getId() {
		return this.id;
	}

	public void setId(RespuestasPK id) {
		this.id = id;
	}

	public String getRespuesta() {
		return this.respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Preguntas getPregunta() {
		return this.pregunta;
	}

	public void setPregunta(Preguntas pregunta) {
		this.pregunta = pregunta;
	}

}