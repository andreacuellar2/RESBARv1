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
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import sv.edu.diseno.acceso.exceptions.NonexistentEntityException;
import sv.edu.diseno.acceso.exceptions.PreexistingEntityException;
import sv.edu.diseno.definiciones.DetalleOrden;
import sv.edu.diseno.definiciones.DetalleOrdenPK;

/**
 *
 * @author LuisEnrique
 */
public class DetalleOrdenJpaController implements Serializable {

    public DetalleOrdenJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DetalleOrden detalleOrden) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(detalleOrden);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDetalleOrden(detalleOrden.getDetalleOrdenPK()) != null) {
                throw new PreexistingEntityException("DetalleOrden " + detalleOrden + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DetalleOrden detalleOrden) throws NonexistentEntityException, Exception {
       
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            detalleOrden = em.merge(detalleOrden);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                DetalleOrdenPK id = detalleOrden.getDetalleOrdenPK();
                if (findDetalleOrden(id) == null) {
                    throw new NonexistentEntityException("The detalleOrden with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

     public void destroy(DetalleOrden detalleOrden) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DetalleOrden detOrd = detalleOrden;
            em.remove(detOrd);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DetalleOrden> findDetalleOrdenEntities() {
        return findDetalleOrdenEntities(true, -1, -1);
    }

    public List<DetalleOrden> findDetalleOrdenEntities(int maxResults, int firstResult) {
        return findDetalleOrdenEntities(false, maxResults, firstResult);
    }

    private List<DetalleOrden> findDetalleOrdenEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DetalleOrden.class));
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

    public DetalleOrden findDetalleOrden(DetalleOrdenPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DetalleOrden.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetalleOrdenCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DetalleOrden> rt = cq.from(DetalleOrden.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
