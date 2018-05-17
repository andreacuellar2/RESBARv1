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
import sv.edu.diseno.definiciones.Categoria;
import sv.edu.diseno.definiciones.DetalleOrden;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import sv.edu.diseno.acceso.exceptions.IllegalOrphanException;
import sv.edu.diseno.acceso.exceptions.NonexistentEntityException;
import sv.edu.diseno.acceso.exceptions.PreexistingEntityException;
import sv.edu.diseno.definiciones.Producto;

/**
 *
 * @author LuisEnrique
 */
public class ProductoJpaController implements Serializable {

    public ProductoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Producto producto) throws PreexistingEntityException, Exception {
        if (producto.getDetalleOrdenList() == null) {
            producto.setDetalleOrdenList(new ArrayList<DetalleOrden>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Categoria idCategoria = producto.getIdCategoria();
            if (idCategoria != null) {
                idCategoria = em.getReference(idCategoria.getClass(), idCategoria.getIdCategoria());
                producto.setIdCategoria(idCategoria);
            }
            List<DetalleOrden> attachedDetalleOrdenList = new ArrayList<DetalleOrden>();
            for (DetalleOrden detalleOrdenListDetalleOrdenToAttach : producto.getDetalleOrdenList()) {
                detalleOrdenListDetalleOrdenToAttach = em.getReference(detalleOrdenListDetalleOrdenToAttach.getClass(), detalleOrdenListDetalleOrdenToAttach.getDetalleOrdenPK());
                attachedDetalleOrdenList.add(detalleOrdenListDetalleOrdenToAttach);
            }
            producto.setDetalleOrdenList(attachedDetalleOrdenList);
            em.persist(producto);
            if (idCategoria != null) {
                idCategoria.getProductoList().add(producto);
                idCategoria = em.merge(idCategoria);
            }
            for (DetalleOrden detalleOrdenListDetalleOrden : producto.getDetalleOrdenList()) {
                Producto oldProductoOfDetalleOrdenListDetalleOrden = detalleOrdenListDetalleOrden.getProducto();
                detalleOrdenListDetalleOrden.setProducto(producto);
                detalleOrdenListDetalleOrden = em.merge(detalleOrdenListDetalleOrden);
                if (oldProductoOfDetalleOrdenListDetalleOrden != null) {
                    oldProductoOfDetalleOrdenListDetalleOrden.getDetalleOrdenList().remove(detalleOrdenListDetalleOrden);
                    oldProductoOfDetalleOrdenListDetalleOrden = em.merge(oldProductoOfDetalleOrdenListDetalleOrden);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProducto(producto.getIdProducto()) != null) {
                throw new PreexistingEntityException("Producto " + producto + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Producto producto) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto persistentProducto = em.find(Producto.class, producto.getIdProducto());
            Categoria idCategoriaOld = persistentProducto.getIdCategoria();
            Categoria idCategoriaNew = producto.getIdCategoria();
            List<DetalleOrden> detalleOrdenListOld = persistentProducto.getDetalleOrdenList();
            List<DetalleOrden> detalleOrdenListNew = producto.getDetalleOrdenList();
            List<String> illegalOrphanMessages = null;
            for (DetalleOrden detalleOrdenListOldDetalleOrden : detalleOrdenListOld) {
                if (!detalleOrdenListNew.contains(detalleOrdenListOldDetalleOrden)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DetalleOrden " + detalleOrdenListOldDetalleOrden + " since its producto field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idCategoriaNew != null) {
                idCategoriaNew = em.getReference(idCategoriaNew.getClass(), idCategoriaNew.getIdCategoria());
                producto.setIdCategoria(idCategoriaNew);
            }
            List<DetalleOrden> attachedDetalleOrdenListNew = new ArrayList<DetalleOrden>();
            for (DetalleOrden detalleOrdenListNewDetalleOrdenToAttach : detalleOrdenListNew) {
                detalleOrdenListNewDetalleOrdenToAttach = em.getReference(detalleOrdenListNewDetalleOrdenToAttach.getClass(), detalleOrdenListNewDetalleOrdenToAttach.getDetalleOrdenPK());
                attachedDetalleOrdenListNew.add(detalleOrdenListNewDetalleOrdenToAttach);
            }
            detalleOrdenListNew = attachedDetalleOrdenListNew;
            producto.setDetalleOrdenList(detalleOrdenListNew);
            producto = em.merge(producto);
            if (idCategoriaOld != null && !idCategoriaOld.equals(idCategoriaNew)) {
                idCategoriaOld.getProductoList().remove(producto);
                idCategoriaOld = em.merge(idCategoriaOld);
            }
            if (idCategoriaNew != null && !idCategoriaNew.equals(idCategoriaOld)) {
                idCategoriaNew.getProductoList().add(producto);
                idCategoriaNew = em.merge(idCategoriaNew);
            }
            for (DetalleOrden detalleOrdenListNewDetalleOrden : detalleOrdenListNew) {
                if (!detalleOrdenListOld.contains(detalleOrdenListNewDetalleOrden)) {
                    Producto oldProductoOfDetalleOrdenListNewDetalleOrden = detalleOrdenListNewDetalleOrden.getProducto();
                    detalleOrdenListNewDetalleOrden.setProducto(producto);
                    detalleOrdenListNewDetalleOrden = em.merge(detalleOrdenListNewDetalleOrden);
                    if (oldProductoOfDetalleOrdenListNewDetalleOrden != null && !oldProductoOfDetalleOrdenListNewDetalleOrden.equals(producto)) {
                        oldProductoOfDetalleOrdenListNewDetalleOrden.getDetalleOrdenList().remove(detalleOrdenListNewDetalleOrden);
                        oldProductoOfDetalleOrdenListNewDetalleOrden = em.merge(oldProductoOfDetalleOrdenListNewDetalleOrden);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = producto.getIdProducto();
                if (findProducto(id) == null) {
                    throw new NonexistentEntityException("The producto with id " + id + " no longer exists.");
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
            Producto producto;
            try {
                producto = em.getReference(Producto.class, id);
                producto.getIdProducto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The producto with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<DetalleOrden> detalleOrdenListOrphanCheck = producto.getDetalleOrdenList();
            for (DetalleOrden detalleOrdenListOrphanCheckDetalleOrden : detalleOrdenListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the DetalleOrden " + detalleOrdenListOrphanCheckDetalleOrden + " in its detalleOrdenList field has a non-nullable producto field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Categoria idCategoria = producto.getIdCategoria();
            if (idCategoria != null) {
                idCategoria.getProductoList().remove(producto);
                idCategoria = em.merge(idCategoria);
            }
            em.remove(producto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Producto> findProductoEntities() {
        return findProductoEntities(true, -1, -1);
    }

    public List<Producto> findProductoEntities(int maxResults, int firstResult) {
        return findProductoEntities(false, maxResults, firstResult);
    }

    private List<Producto> findProductoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Producto.class));
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

    public Producto findProducto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Producto> rt = cq.from(Producto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
