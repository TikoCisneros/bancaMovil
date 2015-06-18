package banca.model.dao.entities;

import banca.model.dao.entities.Respuestas;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-06-17T17:50:33")
@StaticMetamodel(Preguntas.class)
public class Preguntas_ { 

    public static volatile ListAttribute<Preguntas, Respuestas> respuestas;
    public static volatile SingularAttribute<Preguntas, String> pregunta;
    public static volatile SingularAttribute<Preguntas, Integer> idPreg;

}