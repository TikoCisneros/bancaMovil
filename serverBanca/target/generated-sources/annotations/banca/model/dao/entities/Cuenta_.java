package banca.model.dao.entities;

import banca.model.dao.entities.Cliente;
import banca.model.dao.entities.Tipocuenta;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-06-28T10:49:19")
@StaticMetamodel(Cuenta.class)
public class Cuenta_ { 

    public static volatile SingularAttribute<Cuenta, String> nroCuenta;
    public static volatile SingularAttribute<Cuenta, Cliente> cliente;
    public static volatile SingularAttribute<Cuenta, Tipocuenta> tipocuenta;
    public static volatile SingularAttribute<Cuenta, BigDecimal> saldo;

}