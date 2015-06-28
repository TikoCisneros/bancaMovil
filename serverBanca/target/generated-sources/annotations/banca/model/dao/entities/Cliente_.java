package banca.model.dao.entities;

import banca.model.dao.entities.Cuenta;
import banca.model.dao.entities.Respuestas;
import banca.model.dao.entities.Transacciones;
import banca.model.dao.entities.Transferencia;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-06-28T10:49:19")
@StaticMetamodel(Cliente.class)
public class Cliente_ { 

    public static volatile ListAttribute<Cliente, Respuestas> respuestas;
    public static volatile SingularAttribute<Cliente, String> apellido;
    public static volatile SingularAttribute<Cliente, Date> cmFechaUltmCon;
    public static volatile SingularAttribute<Cliente, String> direccion;
    public static volatile SingularAttribute<Cliente, String> ultmIp;
    public static volatile ListAttribute<Cliente, Cuenta> cuentas;
    public static volatile SingularAttribute<Cliente, String> alias;
    public static volatile SingularAttribute<Cliente, String> cmUltmIp;
    public static volatile SingularAttribute<Cliente, String> telefono;
    public static volatile SingularAttribute<Cliente, String> aceptaPoliticas;
    public static volatile ListAttribute<Cliente, Transferencia> transferencias;
    public static volatile SingularAttribute<Cliente, String> cmPin;
    public static volatile SingularAttribute<Cliente, String> pass;
    public static volatile SingularAttribute<Cliente, String> nombre;
    public static volatile SingularAttribute<Cliente, String> motivo;
    public static volatile SingularAttribute<Cliente, Integer> idCli;
    public static volatile SingularAttribute<Cliente, String> cmPass;
    public static volatile SingularAttribute<Cliente, String> bloqueda;
    public static volatile ListAttribute<Cliente, Transacciones> transacciones;
    public static volatile SingularAttribute<Cliente, String> cmMovil;
    public static volatile SingularAttribute<Cliente, Date> fechaUltmCon;
    public static volatile SingularAttribute<Cliente, String> correo;
    public static volatile SingularAttribute<Cliente, String> ciRuc;

}