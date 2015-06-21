package banca.model.dao.entities;

import banca.model.dao.entities.Cliente;
import banca.model.dao.entities.Estadotrans;
import banca.model.dao.entities.Tipotrans;
import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-06-18T01:02:12")
@StaticMetamodel(Transacciones.class)
public class Transacciones_ { 

    public static volatile SingularAttribute<Transacciones, BigDecimal> saldoActual;
    public static volatile SingularAttribute<Transacciones, Estadotrans> estadotran;
    public static volatile SingularAttribute<Transacciones, Tipotrans> tipotran;
    public static volatile SingularAttribute<Transacciones, Long> idTrans;
    public static volatile SingularAttribute<Transacciones, Timestamp> fecha;
    public static volatile SingularAttribute<Transacciones, String> nroCuenta;
    public static volatile SingularAttribute<Transacciones, Cliente> cliente;
    public static volatile SingularAttribute<Transacciones, BigDecimal> saldoFinal;
    public static volatile SingularAttribute<Transacciones, BigDecimal> monto;
    public static volatile SingularAttribute<Transacciones, String> nrocDestino;

}