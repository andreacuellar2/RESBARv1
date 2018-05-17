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
import sv.edu.diseno.definiciones.DetalleOrden;
import sv.edu.diseno.definiciones.DetalleOrdenPK;
import sv.edu.diseno.definiciones.Orden;
import sv.edu.diseno.definiciones.Producto;

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
        if (detalleOrden.getDetalleOrdenPK() == null) {
            detalleOrden.setDetalleOrdenPK(new DetalleOrdenPK());
        }
        detalleOrden.getDetalleOrdenPK().setIdProducto(detalleOrden.getProducto().getIdProducto());
        detalleOrden.getDetalleOrdenPK().setIdOrden(detalleOrden.getOrden().getIdOrden());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Orden orden = detalleOrden.getOrden();
            if (orden != null) {
                orden = em.getReference(orden.getClass(), orden.getIdOrden());
                detalleOrden.setOrden(orden);
            }
            Producto producto = detalleOrden.getProducto();
            if (producto != null) {
                producto = em.getReference(producto.getClass(), producto.getIdProducto());
                detalleOrden.setProducto(producto);
            }
            em.persist(detalleOrden);
            if (orden != null) {
                orden.getDetalleOrdenList().add(detalleOrden);
                orden = em.merge(orden);
            }
            if (producto != null) {
                producto.getDetalleOrdenList().add(detalleOrden);
                producto = em.merge(producto);
            }
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
        detalleOrden.getDetalleOrdenPK().setIdProducto(detalleOrden.getProducto().getIdProducto());
        detalleOrden.getDetalleOrdenPK().setIdOrden(detalleOrden.getOrden().getIdOrden());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DetalleOrden persistentDetalleOrden = em.find(DetalleOrden.class, detalleOrden.getDetalleOrdenPK());
            Orden ordenOld = persistentDetalleOrden.getOrden();
            Orden ordenNew = detalleOrden.getOrden();
            Producto productoOld = persistentDetalleOrden.getProducto();
            Producto productoNew = detalleOrden.getProducto();
            if (ordenNew != null) {
                ordenNew = em.getReference(ordenNew.getClass(), ordenNew.getIdOrden());
                detalleOrden.setOrden(ordenNew);
            }
            if (productoNew != null) {
                productoNew = em.getReference(productoNew.getClass(), productoNew.getIdProducto());
                detalleOrden.setProducto(productoNew);
            }
            detalleOrden = em.merge(detalleOrden);
            if (ordenOld != null && !ordenOld.equals(ordenNew)) {
                ordenOld.getDetalleOrdenList().remove(detalleOrden);
                ordenOld = em.merge(ordenOld);
            }
            if (ordenNew != null && !ordenNew.equals(ordenOld)) {
                ordenNew.getDetalleOrdenList().add(detalleOrden);
                ordenNew = em.merge(ordenNew);
            }
            if (productoOld != null && !productoOld.equals(productoNew)) {
                productoOld.getDetalleOrdenList().remove(detalleOrden);
                productoOld = em.merge(productoOld);
            }
            if (productoNew != null && !productoNew.equals(productoOld)) {
                productoNew.getDetalleOrdenList().add(detalleOrden);
                productoNew = em.merge(productoNew);
            }
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

    public void destroy(DetalleOrdenPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DetalleOrden detalleOrden;
            try {
                detalleOrden = em.getReference(DetalleOrden.class, id);
                detalleOrden.getDetalleOrdenPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detalleOrden with id " + id + " no longer exists.", enfe);
            }
            Orden orden = detalleOrden.getOrden();
            if (orden != null) {
                orden.getDetalleOrdenList().remove(detalleOrden);
                orden = em.merge(orden);
            }
            Producto producto = detalleOrden.getProducto();
            if (producto != null) {
                producto.getDetalleOrdenList().remove(detalleOrden);
                producto = em.merge(producto);
            }
            em.remove(detalleOrden);
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
