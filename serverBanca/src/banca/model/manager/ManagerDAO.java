package banca.model.manager;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 * Objeto que encapsula la logica basica de acceso a datos mediante JPA. Maneja el patron de diseño
 * singleton para administrar los componentes EntityManagerFactory y EntityManager. 
 * @author mrea
 *
 */
public class ManagerDAO {
	private static EntityManagerFactory factory;
	private static EntityManager em;

	/**
	 * Constructor de la clase ManagerDAO. Se encarga de crear los objetos 
	 * factory y entity manager utilizando el patron de diseño singleton.
	 */
	public ManagerDAO() {
		mostrarLog("constructor","ManagerDAO Creado");
		if (factory == null) {
			factory = Persistence.createEntityManagerFactory("serverBanca");
			mostrarLog("constructor","Factory creado");
		}
		if (em == null) {
			em = factory.createEntityManager();
			mostrarLog("constructor","EntityManager creado");
		}
	}
	
	/**
	 * Metodo basico para mostrar mensajes de depuracion.
	 * @param nombreMetodo Metodo que genera el mensaje de depuracion.
	 * @param mensaje El mensaje a desplegar.
	 */
	public void mostrarLog(String nombreMetodo,String mensaje) {
		System.out.println("["+this.getClass().getSimpleName()+"/"+nombreMetodo+"]: " + mensaje);
	}

	/**
	 * finder Generico que devuelve todos las entidades de una tabla.
	 * 
	 * @param clase
	 *            La clase que se desea consultar.
	 * @param orderBy
	 *            Propiedad de la entidad por la que se desea ordenar la
	 *            consulta.
	 * @return Listado resultante.
	 */
	@SuppressWarnings("rawtypes")
	public List findAll(Class clase, String orderBy) {
		mostrarLog("findAll",clase.getSimpleName() + " orderBy " + orderBy);
		Query q;
		List listado;
		if (!em.getTransaction().isActive()){
			em.getTransaction().begin();
		}
		if (orderBy == null || orderBy.length() == 0)
			q = em.createQuery("SELECT o FROM " + clase.getSimpleName() + " o");
		else
			q = em.createQuery("SELECT o FROM " + clase.getSimpleName()
					+ " o ORDER BY " + orderBy);
		listado = q.getResultList();
		if(em.getTransaction().isActive()){
			em.getTransaction().commit();
		}
		return listado;
	}
	
	
	/**
	 * finder Generico que devuelve todos las entidades de una tabla. Esta versión
	 * no cierra la transacción, para que el Manager que la invoque lo haga.
	 * 
	 * @param clase
	 *            La clase que se desea consultar.
	 * @param orderBy
	 *            Propiedad de la entidad por la que se desea ordenar la
	 *            consulta.
	 * @return Listado resultante.
	 */
	@SuppressWarnings("rawtypes")
	public List findAllT(Class clase, String orderBy) {
		mostrarLog("findAllT",clase.getSimpleName() + " orderBy " + orderBy);
		Query q;
		List listado;
		if (!em.getTransaction().isActive()){
			em.getTransaction().begin();
			mostrarLog("findAllT","transaccion begin");
		}
		if (orderBy == null || orderBy.length() == 0)
			q = em.createQuery("SELECT o FROM " + clase.getSimpleName() + " o");
		else
			q = em.createQuery("SELECT o FROM " + clase.getSimpleName()
					+ " o ORDER BY " + orderBy);
		listado = q.getResultList();
		/*if(!em.getTransaction().isActive())
			em.getTransaction().commit();*/
		return listado;
	}

	/**
	 * finder Generico que devuelve todos las entidades de una tabla.
	 * 
	 * @param clase
	 *            La clase que se desea consultar.
	 * @return Listado resultante.
	 */
	@SuppressWarnings("rawtypes")
	public List findAll(Class clase) {
		mostrarLog("findAll", clase.getSimpleName());
		Query q;
		List listado;
		if (!em.getTransaction().isActive()){
			em.getTransaction().begin();
		}
		q = em.createQuery("SELECT o FROM " + clase.getSimpleName() + " o");
		listado = q.getResultList();
		em.getTransaction().commit();
		return listado;
	}

	/**
	 * Finder generico que permite aplicar clausulas where y order by.
	 * 
	 * @param clase
	 *            La entidad sobre la que se desea consultar.
	 * @param pClausulaWhere
	 *            Clausula where de tipo JPQL (sin la palabra reservada WHERE).
	 * @param pOrderBy
	 *            Clausula order by de tipo JPQL (sin la palabra reservada ORDER
	 *            BY). Puede ser null.
	 * @return Listado resultante.
	 */
	@SuppressWarnings("rawtypes")
	public List findWhere(Class clase, String pClausulaWhere, String pOrderBy) {
		mostrarLog("findWhere", clase.getSimpleName()+" where "+pClausulaWhere+"order by "+pOrderBy);
		Query q;
		List listado;
		if (!em.getTransaction().isActive())
			em.getTransaction().begin();
		if (pOrderBy == null || pOrderBy.length() == 0)
			q = em.createQuery("SELECT o FROM " + clase.getSimpleName()
					+ " o WHERE " + pClausulaWhere);
		else
			q = em.createQuery("SELECT o FROM " + clase.getSimpleName()
					+ " o WHERE " + pClausulaWhere + " ORDER BY " + pOrderBy);
		listado = q.getResultList();
		em.getTransaction().commit();
		return listado;
	}
	
	/**
	 * Finder generico que permite aplicar clausulas where y order by. Esta versión
	 * no cierra la transacción, para que el Manager que la invoque lo haga.
	 * 
	 * @param clase
	 *            La entidad sobre la que se desea consultar.
	 * @param pClausulaWhere
	 *            Clausula where de tipo JPQL (sin la palabra reservada WHERE).
	 * @param pOrderBy
	 *            Clausula order by de tipo JPQL (sin la palabra reservada ORDER
	 *            BY). Puede ser null.
	 * @return Listado resultante.
	 */
	@SuppressWarnings("rawtypes")
	public List findWhereT(Class clase, String pClausulaWhere, String pOrderBy) {
		mostrarLog("findWhereT", clase.getSimpleName()+" where "+pClausulaWhere+"order by "+pOrderBy);
		Query q;
		List listado;
		if (!em.getTransaction().isActive()){
			em.getTransaction().begin();
			mostrarLog("findWhereT","transaccion begin");
		}
		if (pOrderBy == null || pOrderBy.length() == 0)
			q = em.createQuery("SELECT o FROM " + clase.getSimpleName()
					+ " o WHERE " + pClausulaWhere);
		else
			q = em.createQuery("SELECT o FROM " + clase.getSimpleName()
					+ " o WHERE " + pClausulaWhere + " ORDER BY " + pOrderBy);
		listado = q.getResultList();
		//em.getTransaction().commit();
		return listado;
	}
	
	/**
	 * Finder generico que permite ejecutar cualquier sentencia JPQL. Esta versión
	 * no cierra la transacción, para que el Manager que la invoque lo haga.
	 * 
	 * @param pClausulaJPQL Sentencia JPQL que se va a ejecutar.
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List findJPQLT(String pClausulaJPQL) {
		mostrarLog("findSQLT", pClausulaJPQL);
		Query q;
		List listado;
		if (!em.getTransaction().isActive())
			em.getTransaction().begin();
		q = em.createQuery(pClausulaJPQL);
		listado = q.getResultList();
		//em.getTransaction().commit();
		return listado;
	}
	/**
	 * Finder generico para buscar un objeto especifico.
	 * 
	 * @param clase
	 *            La clase sobre la que se desea consultar.
	 * @param pID
	 *            Identificador que permitira la busqueda.
	 * @return El objeto solicitado (si existiera).
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object findById(Class clase, Object pID) throws Exception {
		mostrarLog("findById", clase.getSimpleName()+" : "+pID);
		if (!em.getTransaction().isActive())
			em.getTransaction().begin();
		if (pID == null)
			throw new Exception(
					"Debe especificar el codigo para buscar el dato.");
		Object o;
		try {
			o = em.find(clase, pID);
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new Exception("No se encontro la informacion especificada: "
					+ e.getMessage());
		}
		em.getTransaction().commit();
		return o;
	}

	/**
	 * Almacena un objeto en la persistencia.
	 * 
	 * @param pObjeto El objeto a insertar.
	 * @throws Exception
	 */
	public void insertar(Object pObjeto) throws Exception {
		mostrarLog("insertar", pObjeto.getClass().getSimpleName() +" : "+pObjeto);
		if (!em.getTransaction().isActive()){
			em.getTransaction().begin();
		}
		try {
			em.persist(pObjeto);
			mostrarLog("insertar","Objeto insertado: "
					+ pObjeto.getClass().getSimpleName() + " " +pObjeto);
		} catch (Exception e) {
			mostrarLog("insertar","No se pudo insertar el objeto especificado: "
					+ pObjeto.getClass().getSimpleName()+" "+pObjeto);
			em.getTransaction().rollback();
			mostrarLog("insertar","transaccion rollback");
			throw new Exception("No se pudo insertar el objeto especificado: "
					+ e.getMessage());
		}
		em.getTransaction().commit();
		mostrarLog("insertar","transaccion commit");
	}

	/**
	 * Elimina un objeto de la persistencia.
	 * 
	 * @param clase
	 *            La clase correspondiente al objeto que se desea eliminar.
	 * @param pID
	 *            El identificador del objeto que se desea eliminar.
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void eliminar(Class clase, Object pID) throws Exception {
		if (pID == null) {
			mostrarLog("eliminar","Debe especificar un identificador para eliminar el dato solicitado: "
					+ clase.getSimpleName()+" : "+pID);
			throw new Exception(
					"Debe especificar un identificador para eliminar el dato solicitado.");
		}
		Object o = findById(clase, pID);
		if (!em.getTransaction().isActive()){
			em.getTransaction().begin();
		}
		try {
			//REFRESCAR LA ENTIDAD
			em.refresh(o);
			//ELIMINAR
			em.remove(o);
			mostrarLog("eliminar","Dato eliminado: " + clase.getSimpleName() + " : "
					+ pID.toString());
		} catch (Exception e) {
			em.getTransaction().rollback();
			mostrarLog("eliminar","No se pudo eliminar el dato: " + clase.getSimpleName()+" : "+pID);
			throw new Exception("No se pudo eliminar el dato: " + e.getMessage());
		}
		em.getTransaction().commit();
		mostrarLog("eliminar","transaccion commit");
	}

	/**
	 * Actualiza la informacion de un objeto en la persistencia.
	 * 
	 * @param pObjeto Objeto que contiene la informacion que se debe actualizar.
	 * @throws Exception
	 */
	public void actualizar(Object pObjeto) throws Exception {
		if (pObjeto == null)
			throw new Exception("No se puede actualizar un dato null");
		if (!em.getTransaction().isActive())
			em.getTransaction().begin();
		try {
			em.merge(pObjeto);
			mostrarLog("actualizar","Dato actualizado: "
					+ pObjeto.getClass().getSimpleName()+" : "+pObjeto);
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new Exception("No se pudo actualizar el dato: "
					+ e.getMessage());
		}
		em.getTransaction().commit();
		mostrarLog("actualizar","transaccion commit");
	}

	public static EntityManager getEntityManager() {
		return em;
	}

	/**
     * Finder generico para buscar un objeto especifico por una columna
     * especificada.
     *
     * @param clase La clase sobre la que se desea consultar.
     * @param param columna de busqueda.
     * @param value valor de parametro de busqueda.
     * @param orderBy Expresion que indica la propiedad de la entidad por la que
     * se desea ordenar la consulta. Debe utilizar el alias "o" para nombrar a
     * la(s) propiedad(es) por la que se va a ordenar. Puede aceptar null o una
     * cadena vacia, en este caso no ordenara el resultado.
     * @return Lista de objetos solicitados (si existieran).
     * @throws Exception
     */
    @SuppressWarnings({"rawtypes"})
    public List findByParam(Class clase, String param, String value, String orderBy) throws Exception {
        Query q;
        List listado;
        String sentenciaSQL;
        if (orderBy == null || orderBy.length() == 0) {
            sentenciaSQL = "SELECT o FROM " + clase.getSimpleName() + " o WHERE " + param + "=:value1";
        } else {
            sentenciaSQL = "SELECT o FROM " + clase.getSimpleName()
                    + " o WHERE " + param + "=:value1" + " ORDER BY " + orderBy;
        }
        q = em.createQuery(sentenciaSQL).setParameter("value1", value);
        listado = q.getResultList();
        return listado;
    }
    
    @SuppressWarnings({"rawtypes"})
    public List findByParam(Class clase, String param, int value, String orderBy) throws Exception {
        Query q;
        List listado;
        String sentenciaSQL;
        if (orderBy == null || orderBy.length() == 0) {
            sentenciaSQL = "SELECT o FROM " + clase.getSimpleName() + " o WHERE " + param + "=:value1";
        } else {
            sentenciaSQL = "SELECT o FROM " + clase.getSimpleName()
                    + " o WHERE " + param + "=:value1" + " ORDER BY " + orderBy;
        }
        q = em.createQuery(sentenciaSQL).setParameter("value1", value);
        listado = q.getResultList();
        return listado;
    }

}
