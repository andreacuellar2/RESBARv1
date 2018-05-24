/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.edu.diseno.acceso;

import sv.edu.diseno.provider.EntityManagerProvider;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Query;
import java.util.List;
import javax.persistence.EntityManager;
import sv.edu.diseno.definiciones.Orden;
import sv.edu.diseno.excepciones.ErrorAplicacion;

/**
 *
 * @author LuisEnrique
 */
public class ManejadorOrden extends EntityManagerProvider implements Serializable {
    
    public static List<Orden> ObtenerActivas() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Orden.findAllActivas");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public static Orden Obtener(int idOrden){
        EntityManager em = getEntityManager();
        try {
            return em.find(Orden.class, idOrden);
        } finally {
            em.close();
        }
    }
    
    public static void Actualizar(Orden orden) throws ErrorAplicacion {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            orden = em.merge(orden);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = orden.idOrden;
                
                Orden findOrden = null;
                try {
                    findOrden = em.find(Orden.class, id);
                } finally {
                    em.close();
                }
                
                if (findOrden == null) {
                    throw new ErrorAplicacion("The orden with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public static List<Orden> BuscarActivas(String text) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Orden.findAllActivasText");
            q.setParameter("cliente", "%" + text + "%");
            q.setParameter("mesero", "%" + text + "%");
            q.setParameter("mesa", "%" + text + "%");
            q.setParameter("comentario", "%" + text + "%");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public static void Insertar(Orden orden) throws ErrorAplicacion {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(orden);
            em.getTransaction().commit();
        } catch (Exception ex) {
            Orden findOrden = null;
            try {
                findOrden = em.find(Orden.class, orden.idOrden);
            } finally {
                em.close();
            }
            if (findOrden != null) {
                throw new ErrorAplicacion("Orden " + orden + " already exists."+ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }    

    public static void Eliminar(Orden orden) {
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
    
    public static Integer ObtenerId() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Orden.findAllActivasByIdOrden");
            q.setMaxResults(1);
            return ((Integer) q.getSingleResult() + 1);
        } finally {
            em.close();
        }
    }

    public static List<Orden> ObtenerVentas(Date date) {
        EntityManager em = getEntityManager();        
        try {
            Query q = em.createNamedQuery("Orden.findByFecha");            
            q.setParameter("fecha", date);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public static List<Orden> ObtenerVentas(Date date1, Date date2) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Orden.findByFechaBetWeen");
            q.setParameter("fecha1", date1);
            q.setParameter("fecha2", date2);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

}
