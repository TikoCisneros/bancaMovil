package banca.model.dao.entities;

import banca.model.dao.entities.Cuenta;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-06-28T10:49:19")
@StaticMetamodel(Tipocuenta.class)
public class Tipocuenta_ { 

    public static volatile ListAttribute<Tipocuenta, Cuenta> cuentas;
    public static volatile SingularAttribute<Tipocuenta, String> tipo;
    public static volatile SingularAttribute<Tipocuenta, Integer> idTipo;

}