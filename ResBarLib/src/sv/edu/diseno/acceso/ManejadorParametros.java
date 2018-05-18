/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.edu.diseno.acceso;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import sv.edu.diseno.acceso.exceptions.NonexistentEntityException;
import sv.edu.diseno.definiciones.Parametro;

/**
 *
 * @author LuisEnrique
 */
public class ManejadorParametros extends EntityManagerProvider implements Serializable {

 

    public static void Actualizar(Parametro parametro) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            parametro = em.merge(parametro);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = parametro.getIdParametro();
                if (em.find(Parametro.class, id) == null) {
                    throw new NonexistentEntityException("El parámetro con el ID '"+id+"' ya no existe");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    //MODIFICAR
    //se modifico para devolver todos los resultados sin un limite
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
}
