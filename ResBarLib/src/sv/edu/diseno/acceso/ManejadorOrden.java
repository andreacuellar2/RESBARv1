/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.edu.diseno.acceso;

import sv.edu.diseno.provider.EntityManagerProvider;
import java.io.Serializable;
import java.math.BigDecimal;
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
    
    /**
     * Va a la base de datos y filtra todas las ordenes cuyo campo Activa=TRUE.
     * @return Devuelve una coleccion de objetos orden.
     */
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
    /**
     * Recibe un entero que indica el ID de la orden y luego devuelve el objeto orden completo que corresponde.
     * @param idOrden El ID de la orden que se desea obtener.
     * @return Devuelve el objeto orden que corresponde al ID proporcionado.
     */
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
/**
     * Actualiza el objeto orden
     * @param orden La orden modificada.
     * @throws ErrorAplicacion Si hay algun problema con la conexion a la base de datos o el elemento no existe.
     */
    public static void Actualizar(Orden orden) throws ErrorAplicacion {
        EntityManager em = null;
        try {
            
            if(orden.idOrden > 0){
            em = getEntityManager();
            em.getTransaction().begin();
            orden = em.merge(orden);            
            em.getTransaction().commit();    
            }else{
               throw new ErrorAplicacion("Parametro invalido id" + orden.idOrden);
            }
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
    
    /**
     *Toma el string que tiene el criterio de búsqueda y va a la base de datos a buscar todas aquellas ordenes que cumplan con dicho criterio ya sea en el mesero, mesa, cliente o comentario. Devuelve una colección de órdenes que cumplen con dicho criterio sin duplicados.
     * @param text el criterio de busqueda a utilizar par filtrar las ordenes.
     * @return Devuelve una coleccion de ordenes que cumplen con el criterio de busqueda sin duplicados.
     */
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

    /**
     * Crea una nueva tupla en la tabla Orden y una o varias tuplas en la tabla detalleOrden
     * @param orden la orden a insertar en la base de datos.
     * @throws ErrorAplicacion Si hay algun problema con la conexion a la base de datos o el elemento no existe.
     */
    public static void Insertar(Orden orden) throws ErrorAplicacion {
        EntityManager em = null;
        try {
            
            if(orden.mesa.isEmpty() && orden.mesero.isEmpty() && orden.cliente.isEmpty() && orden.idOrden > 0){
                throw new ErrorAplicacion("Orden Parametros incompletos o idOrden negativo&");
            }else if(orden.total.compareTo(BigDecimal.ZERO) != 1){
                throw new ErrorAplicacion("Orden Error en el total del dinero&");
            }else{                
                em = getEntityManager();
                em.getTransaction().begin();
                em.persist(orden);
                em.getTransaction().commit();
            }
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

    /**
     * Elimina una orden de la base de datos con sus detalles.
     * @param orden la orden a eliminar.
     */
    public static void Eliminar(Orden orden) throws ErrorAplicacion {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            if (!em.contains(orden)) {
                orden = em.merge(orden);
            }
            em.remove(orden);
            em.getTransaction().commit();
        } catch (Exception ex) {
            throw new ErrorAplicacion("ManejadorOrden.Eliminar(:Orden)$Fallo al eliminar orden" + ex.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    /**
     * Realiza una consulta a la base de datos para obtener el ultimo id de orden y le suma uno
     * @return Devuelve el proximo ID disponible para orden.
     */
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

    /**
     * Obtiene todas las ventas realizadas para una fecha determinada.
     * @param date La fecha de la que se desean las ordenes, se puede filtrar solo por dia/mes/año.
     * @return Devuelve una coleccion de tipo ordenes que tenga el campo activo en false.
     */
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
    
    /**
     * Filtra las ventas realizadas dentro de un rango de fechas.
     * @see ObtenerVentas(Date date) ObtenerVentas(Date date) - Para filtrar una fecha especifica. 
     * @param date1 Fecha de inicio.
     * @param date2 Fecha de fin.
     * @return Devuelve una coleccion de orden que cumpla con el criterio de busqueda.
     */
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