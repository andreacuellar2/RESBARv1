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

    public static List<Orden> ObtenerActivas() throws ErrorAplicacion {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Orden.findAllActivas");
            return q.getResultList();
        } catch (Exception ex) {
            throw new ErrorAplicacion("ManejadorOrden.ObtenerActivas()$Fallo al obtener ordenes activas" + ex.getMessage());
        } finally {
            em.close();
        }
    }

    public static Orden Obtener(int idOrden) throws ErrorAplicacion {
        if (idOrden<0) {
          throw new ErrorAplicacion("ManejadorOrden.Obtener()$Id no válido");  
        }else{
        EntityManager em = getEntityManager();
        try {
            return em.find(Orden.class, idOrden);
        } catch (Exception ex) {
            throw new ErrorAplicacion("ManejadorOrden.Obtener()$Fallo al obtener orden" + ex.getMessage());
        } finally {
            em.close();
        }
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
            throw new ErrorAplicacion("ManejadorOrden.Actualizar(:Orden)$Fallo al actualizar ordenes" + ex.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public static List<Orden> BuscarActivas(String text) throws ErrorAplicacion {
        if (text.isEmpty()) {
            throw new ErrorAplicacion("ManejadorOrden.BuscarActivas(:String)$Texto ingresado es inválido");
        }
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Orden.findAllActivasText");
            q.setParameter("cliente", "%" + text + "%");
            q.setParameter("mesero", "%" + text + "%");
            q.setParameter("mesa", "%" + text + "%");
            q.setParameter("comentario", "%" + text + "%");
            return q.getResultList();
        } catch (Exception ex) {
            throw new ErrorAplicacion("ManejadorOrden.BuscarActivas(:String)$Fallo al buscar ordenes activas" + ex.getMessage());
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
                throw new ErrorAplicacion("Orden " + orden + " already exists." + ex);
            }
            throw new ErrorAplicacion("ManejadorOrden.Insertar(:Orden)$Fallo al insertar nueva orden" + ex.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public static void Eliminar(Orden orden) throws ErrorAplicacion {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Orden or = orden;
            em.remove(or);
            em.getTransaction().commit();
        } catch (Exception ex) {
            throw new ErrorAplicacion("ManejadorOrden.Eliminar(:Orden)$Fallo al eliminar orden" + ex.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public static Integer ObtenerId() throws ErrorAplicacion {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Orden.findAllActivasByIdOrden");
            q.setMaxResults(1);
            return ((Integer) q.getSingleResult() + 1);
        } catch (Exception ex) {
            throw new ErrorAplicacion("ManejadorOrden.ObtenerId()$Fallo al obtener id de la orden" + ex.getMessage());
        } finally {
            em.close();
        }
    }

    public static List<Orden> ObtenerVentas(Date date) throws ErrorAplicacion {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Orden.findByFecha");
            q.setParameter("fecha", date);
            return q.getResultList();
        } catch (Exception ex) {
            throw new ErrorAplicacion("ManejadorOrden.ObtenerVentas(:Date)$Fallo al obtener ventas por fecha" + ex.getMessage());
        } finally {
            em.close();
        }
    }

    public static List<Orden> ObtenerVentas(Date date1, Date date2) throws ErrorAplicacion {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Orden.findByFechaBetWeen");
            q.setParameter("fecha1", date1);
            q.setParameter("fecha2", date2);
            return q.getResultList();
        } catch (Exception ex) {
            throw new ErrorAplicacion("ManejadorOrden.ObtenerVentas(:Date:Date)$Fallo al obtener ordenes ventas por rango de fechas" + ex.getMessage());
        } finally {
            em.close();
        }
    }

}
