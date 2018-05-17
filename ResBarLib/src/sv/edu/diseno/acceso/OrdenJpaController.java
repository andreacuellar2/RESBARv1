/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.edu.diseno.acceso;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import sv.edu.diseno.definiciones.DetalleOrden;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import sv.edu.diseno.acceso.exceptions.IllegalOrphanException;
import sv.edu.diseno.acceso.exceptions.NonexistentEntityException;
import sv.edu.diseno.acceso.exceptions.PreexistingEntityException;
import sv.edu.diseno.definiciones.Orden;

/**
 *
 * @author LuisEnrique
 */
public class OrdenJpaController implements Serializable {

    public OrdenJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Orden orden) throws PreexistingEntityException, Exception {
        if (orden.getDetalleOrdenList() == null) {
            orden.setDetalleOrdenList(new ArrayList<DetalleOrden>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<DetalleOrden> attachedDetalleOrdenList = new ArrayList<DetalleOrden>();
            for (DetalleOrden detalleOrdenListDetalleOrdenToAttach : orden.getDetalleOrdenList()) {
                detalleOrdenListDetalleOrdenToAttach = em.getReference(detalleOrdenListDetalleOrdenToAttach.getClass(), detalleOrdenListDetalleOrdenToAttach.getDetalleOrdenPK());
                attachedDetalleOrdenList.add(detalleOrdenListDetalleOrdenToAttach);
            }
            orden.setDetalleOrdenList(attachedDetalleOrdenList);
            em.persist(orden);
            for (DetalleOrden detalleOrdenListDetalleOrden : orden.getDetalleOrdenList()) {
                Orden oldOrdenOfDetalleOrdenListDetalleOrden = detalleOrdenListDetalleOrden.getOrden();
                detalleOrdenListDetalleOrden.setOrden(orden);
                detalleOrdenListDetalleOrden = em.merge(detalleOrdenListDetalleOrden);
                if (oldOrdenOfDetalleOrdenListDetalleOrden != null) {
                    oldOrdenOfDetalleOrdenListDetalleOrden.getDetalleOrdenList().remove(detalleOrdenListDetalleOrden);
                    oldOrdenOfDetalleOrdenListDetalleOrden = em.merge(oldOrdenOfDetalleOrdenListDetalleOrden);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findOrden(orden.getIdOrden()) != null) {
                throw new PreexistingEntityException("Orden " + orden + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Orden orden) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Orden persistentOrden = em.find(Orden.class, orden.getIdOrden());
            List<DetalleOrden> detalleOrdenListOld = persistentOrden.getDetalleOrdenList();
            List<DetalleOrden> detalleOrdenListNew = orden.getDetalleOrdenList();
            List<String> illegalOrphanMessages = null;
            for (DetalleOrden detalleOrdenListOldDetalleOrden : detalleOrdenListOld) {
                if (!detalleOrdenListNew.contains(detalleOrdenListOldDetalleOrden)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DetalleOrden " + detalleOrdenListOldDetalleOrden + " since its orden field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<DetalleOrden> attachedDetalleOrdenListNew = new ArrayList<DetalleOrden>();
            for (DetalleOrden detalleOrdenListNewDetalleOrdenToAttach : detalleOrdenListNew) {
                detalleOrdenListNewDetalleOrdenToAttach = em.getReference(detalleOrdenListNewDetalleOrdenToAttach.getClass(), detalleOrdenListNewDetalleOrdenToAttach.getDetalleOrdenPK());
                attachedDetalleOrdenListNew.add(detalleOrdenListNewDetalleOrdenToAttach);
            }
            detalleOrdenListNew = attachedDetalleOrdenListNew;
            orden.setDetalleOrdenList(detalleOrdenListNew);
            orden = em.merge(orden);
            for (DetalleOrden detalleOrdenListNewDetalleOrden : detalleOrdenListNew) {
                if (!detalleOrdenListOld.contains(detalleOrdenListNewDetalleOrden)) {
                    Orden oldOrdenOfDetalleOrdenListNewDetalleOrden = detalleOrdenListNewDetalleOrden.getOrden();
                    detalleOrdenListNewDetalleOrden.setOrden(orden);
                    detalleOrdenListNewDetalleOrden = em.merge(detalleOrdenListNewDetalleOrden);
                    if (oldOrdenOfDetalleOrdenListNewDetalleOrden != null && !oldOrdenOfDetalleOrdenListNewDetalleOrden.equals(orden)) {
                        oldOrdenOfDetalleOrdenListNewDetalleOrden.getDetalleOrdenList().remove(detalleOrdenListNewDetalleOrden);
                        oldOrdenOfDetalleOrdenListNewDetalleOrden = em.merge(oldOrdenOfDetalleOrdenListNewDetalleOrden);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = orden.getIdOrden();
                if (findOrden(id) == null) {
                    throw new NonexistentEntityException("The orden with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Orden orden;
            try {
                orden = em.getReference(Orden.class, id);
                orden.getIdOrden();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The orden with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<DetalleOrden> detalleOrdenListOrphanCheck = orden.getDetalleOrdenList();
            for (DetalleOrden detalleOrdenListOrphanCheckDetalleOrden : detalleOrdenListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Orden (" + orden + ") cannot be destroyed since the DetalleOrden " + detalleOrdenListOrphanCheckDetalleOrden + " in its detalleOrdenList field has a non-nullable orden field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(orden);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Orden> findOrdenEntities() {
        return findOrdenEntities(true, -1, -1);
    }

    public List<Orden> findOrdenEntities(int maxResults, int firstResult) {
        return findOrdenEntities(false, maxResults, firstResult);
    }

    private List<Orden> findOrdenEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Orden.class));
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

    public Orden findOrden(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Orden.class, id);
        } finally {
            em.close();
        }
    }

    public int getOrdenCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Orden> rt = cq.from(Orden.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
