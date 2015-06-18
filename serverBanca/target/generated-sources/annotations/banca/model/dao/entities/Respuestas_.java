package banca.model.dao.entities;

import banca.model.dao.entities.Cliente;
import banca.model.dao.entities.Preguntas;
import banca.model.dao.entities.RespuestasPK;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-06-17T17:50:33")
@StaticMetamodel(Respuestas.class)
public class Respuestas_ { 

    public static volatile SingularAttribute<Respuestas, RespuestasPK> id;
    public static volatile SingularAttribute<Respuestas, Preguntas> pregunta;
    public static volatile SingularAttribute<Respuestas, Cliente> cliente;
    public static volatile SingularAttribute<Respuestas, String> respuesta;

}