/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.edu.diseno.acceso;

import java.io.Serializable;
import javax.persistence.Query;
import sv.edu.diseno.definiciones.DetalleOrden;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import sv.edu.diseno.acceso.exceptions.IllegalOrphanException;
import sv.edu.diseno.acceso.exceptions.NonexistentEntityException;
import sv.edu.diseno.acceso.exceptions.PreexistingEntityException;
import sv.edu.diseno.definiciones.Orden;
import sv.edu.diseno.definiciones.Producto;

/**
 *
 * @author LuisEnrique
 */
public class ManejadorProductos implements Serializable {

    public ManejadorProductos(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    
    public List<Producto> ObtenerPorCategoria(String categoria){
        if (categoria != null) {
            Query q = this.getEntityManager().createNamedQuery("Producto.findByIdCategoria");
            q.setParameter("idCategoria", "%" + categoria + "%");
            List lista = q.getResultList();
            return lista;
        }
        return new ArrayList<>();
        
    }
    
    public List<Producto> Buscar(String producto){
        if (producto != null) {
            Query q = this.getEntityManager().createNamedQuery("Producto.findByNombreLike");
            q.setParameter("nombre", "%" + producto + "%");
            List lista = q.getResultList();
            return lista;
        }
        return new ArrayList<>();
        
    }

    public void Insertar(Producto producto) throws PreexistingEntityException, Exception {
        if (producto.getDetalleOrdenList() == null) {
            producto.setDetalleOrdenList(new ArrayList<DetalleOrden>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(producto);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (Obtener(producto.getIdProducto()) != null) {
                throw new PreexistingEntityException("El produto '"+producto+"' ya existe", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void Actualizar(Producto producto) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = producto.getIdProducto();
                if (Obtener(id) == null) {
                    throw new NonexistentEntityException("El producto con el ID '"+ id + "' ya no existe");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void Eliminar(Producto producto) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto produc = producto;
            em.remove(produc);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public Producto Obtener(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }
    }
    
    
    public Integer ObtenerId() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Producto> rt = cq.from(Producto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue() + 1;
        } finally {
            em.close();
        }
    }
    
    
}
