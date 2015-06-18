package banca.model.dao.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-06-17T17:50:33")
@StaticMetamodel(Historial.class)
public class Historial_ { 

    public static volatile SingularAttribute<Historial, BigDecimal> saldoActual;
    public static volatile SingularAttribute<Historial, String> estado;
    public static volatile SingularAttribute<Historial, Timestamp> fecha;
    public static volatile SingularAttribute<Historial, String> nroCuenta;
    public static volatile SingularAttribute<Historial, String> tipo;
    public static volatile SingularAttribute<Historial, Long> idHist;
    public static volatile SingularAttribute<Historial, BigDecimal> saldoFinal;
    public static volatile SingularAttribute<Historial, BigDecimal> monto;
    public static volatile SingularAttribute<Historial, String> nrocDestino;

}