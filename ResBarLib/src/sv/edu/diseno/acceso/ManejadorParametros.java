/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.edu.diseno.acceso;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import sv.edu.diseno.acceso.exceptions.NonexistentEntityException;
import sv.edu.diseno.acceso.exceptions.PreexistingEntityException;
import sv.edu.diseno.definiciones.Parametro;

/**
 *
 * @author LuisEnrique
 */
public class ManejadorParametros implements Serializable {

    public ManejadorParametros(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void Actualizar(Parametro parametro) throws NonexistentEntityException, Exception {
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
                if (ObtenerId(id) == null) {
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
    private List<Parametro> Obtener(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Parametro.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Parametro ObtenerId(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Parametro.class, id);
        } finally {
            em.close();
        }
    }

    
}
