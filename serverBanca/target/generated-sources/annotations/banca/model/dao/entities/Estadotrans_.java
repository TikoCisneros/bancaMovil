package banca.model.dao.entities;

import banca.model.dao.entities.Transacciones;
import banca.model.dao.entities.Transferencia;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-06-28T10:49:19")
@StaticMetamodel(Estadotrans.class)
public class Estadotrans_ { 

    public static volatile SingularAttribute<Estadotrans, String> estado;
    public static volatile SingularAttribute<Estadotrans, Integer> idEst;
    public static volatile ListAttribute<Estadotrans, Transacciones> transacciones;
    public static volatile ListAttribute<Estadotrans, Transferencia> transferencias;

}