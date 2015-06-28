package banca.model.dao.entities;

import banca.model.dao.entities.Cliente;
import banca.model.dao.entities.Estadotrans;
import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-06-28T10:49:19")
@StaticMetamodel(Transferencia.class)
public class Transferencia_ { 

    public static volatile SingularAttribute<Transferencia, BigDecimal> saldoActual;
    public static volatile SingularAttribute<Transferencia, Estadotrans> estadotran;
    public static volatile SingularAttribute<Transferencia, Timestamp> fecha;
    public static volatile SingularAttribute<Transferencia, Cliente> cliente;
    public static volatile SingularAttribute<Transferencia, String> token;
    public static volatile SingularAttribute<Transferencia, String> nroCuenta;
    public static volatile SingularAttribute<Transferencia, Integer> intentos;
    public static volatile SingularAttribute<Transferencia, BigDecimal> saldoFinal;
    public static volatile SingularAttribute<Transferencia, Long> idTransf;
    public static volatile SingularAttribute<Transferencia, String> nrocDestino;
    public static volatile SingularAttribute<Transferencia, BigDecimal> monto;

}