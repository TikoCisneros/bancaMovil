package banca.model.dao.entities;

import banca.model.dao.entities.Estadousr;
import banca.model.dao.entities.Tipousr;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-06-28T10:49:19")
@StaticMetamodel(Usuario.class)
public class Usuario_ { 

    public static volatile SingularAttribute<Usuario, String> apellido;
    public static volatile SingularAttribute<Usuario, String> nombre;
    public static volatile SingularAttribute<Usuario, String> alias;
    public static volatile SingularAttribute<Usuario, Estadousr> estadousr;
    public static volatile SingularAttribute<Usuario, Integer> idUsr;
    public static volatile SingularAttribute<Usuario, Tipousr> tipousr;
    public static volatile SingularAttribute<Usuario, String> pass;

}