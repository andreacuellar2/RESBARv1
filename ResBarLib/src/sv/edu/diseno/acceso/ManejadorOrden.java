/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.edu.diseno.acceso;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
public class ManejadorOrden extends EntityManagerProvider implements Serializable {

       
    private EntityManagerFactory emf = null;

    public static void Insertar(Orden orden) throws PreexistingEntityException, Exception {
        
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(orden);
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

    public static void Actualizar(Orden orden) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            orden = em.merge(orden);
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

    public static void Eliminar(Orden orden) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Orden or = orden;
            em.remove(or);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }


    public static List<Orden> ObtenerActivas() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Orden.findAll");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public static List<Orden> BuscarActivas(String txt){
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Orden.findAllActivasTxt");
            q.setParameter("cliente", "%"+txt+"%");
            q.setParameter("mesero", "%"+txt+"%");
            q.setParameter("mesa", "%"+txt+"%");
            q.setParameter("comentario", "%"+txt+"%");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public static Orden findOrden(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Orden.class, id);
        } finally {
            em.close();
        }
    }

    public static int ObtenerId() {
        EntityManager em = getEntityManager();
        try {            
            Query q = em.createNamedQuery("Orden.findAllByIdOrdenDesc");
            q.setMaxResults(1);            
            return ((Integer) q.getSingleResult()+1);
        } finally {
            em.close();
        }
    }
    
    public static List<Orden> ObtenerVentas(Date date){
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Orden.findByFecha");
            q.setParameter("fecha", date);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
}
