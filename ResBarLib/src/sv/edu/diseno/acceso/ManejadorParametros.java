/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.edu.diseno.acceso;

import sv.edu.diseno.provider.EntityManagerProvider;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import sv.edu.diseno.definiciones.Parametro;
import sv.edu.diseno.excepciones.ErrorAplicacion;

/**
 *
 * @author LuisEnrique
 */
public class ManejadorParametros extends EntityManagerProvider implements Serializable {
    
    /**
     * Va a la base de datos y obtiene todos los parametros que están en dicha tabla.
     * @return Devuelve una coleccion de objetos parametro
     */
    public static List<Parametro> Obtener() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Parametro.class));
            Query q = em.createQuery(cq);
            return q.getResultList();
        } finally {
            em.close();
        }
    } 

    /**
     * Actualiza el campo valor de la tabla parametro
     * @param parametro el parametro modificado
     * @throws ErrorAplicacion Si hay algun problema con la conexion a la base de datos o el elemento no existe.
     */
    public static void Actualizar(Parametro parametro) throws ErrorAplicacion {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            parametro = em.merge(parametro);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = parametro.idParametro;
                if (em.find(Parametro.class, id) == null) {
                    throw new ErrorAplicacion("El parámetro con el ID '"+id+"' ya no existe");
                }
            }
            throw new ErrorAplicacion(ex.toString());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    /**
     * Toma un id y busca en la base de datos el parametro que corresponda a ese id.
     * @param idParametro El id del parametro que se desea buscar.
     * @return Devuelve el parametro con el id correspondiente.
     */
    public static Parametro Obtener(int idParametro) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Parametro.class, idParametro);
        } finally {
            em.close();
        }
    }
    
}
