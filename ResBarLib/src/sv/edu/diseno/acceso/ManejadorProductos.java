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
import sv.edu.diseno.definiciones.Producto;
import sv.edu.diseno.excepciones.ErrorAplicacion;
import sv.edu.diseno.provider.EntityManagerProvider;

/**
 *
 * @author LuisEnrique
 */
public class ManejadorProductos extends EntityManagerProvider implements Serializable {
    
    /**
     * Busca productos por su categoria.
     * @param IdCat Id de la categoria cuyos productos se quieren obtener 
     * @return Devuelve una coleccion de objetos productos que se corresponden con el identificador de categoria que se paso como parametro
     */
    public static List<Producto> ObtenerxCategoria(int IdCat){
        Query q = getEntityManager().createNamedQuery("Producto.findByIdCategoria");
        q.setParameter("idCategoria", IdCat);
        List lista = q.getResultList();
        return lista;                
    }
    
    /**
     * Busca productos por ID o Nombre segun un criterio de busqueda.
     * @param producto Criterio de busqueda para ir a la base de datos y buscar todos los productos cuyo Id o nombre coincida con el criterio de búsqueda. 
     * @return Devuelve la coleccion de productos, sin productos duplicados.
     */
    public static List<Producto> Buscar(String producto){
        Query q = getEntityManager().createNamedQuery("Producto.findByNombreLike");
        q.setParameter("nombre", "%" + producto + "%");        
        List lista = q.getResultList();
        return lista;
    }

    /**
     * Agrega un nuevo objeto producto a la base de datos.
     * @param producto Objeto producto a agregar a la base de datos
     * @throws ErrorAplicacion Si hay algun problema con la conexion a la base de datos o el elemento ya existe.
     */
    public static void Insertar(Producto producto) throws ErrorAplicacion {
        if (producto.detalleOrdenList == null) {
            producto.detalleOrdenList =new ArrayList<DetalleOrden>();
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(producto);
            em.getTransaction().commit();
        } catch (Exception ex) {
            throw new ErrorAplicacion(ex.toString());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Actualiza los campos de un producto, exceptuando su ID.
     * @param producto el producto modificado.
     * @throws ErrorAplicacion Si hay algun problema con la conexion a la base de datos o el elemento no existe.
     */
    public static void Actualizar(Producto producto) throws ErrorAplicacion {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            producto = em.merge(producto);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = producto.idProducto;
            }
            throw new ErrorAplicacion(ex.toString());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    /**
     * Elimina un producto de la base de datos.
     * @param producto El producto a eliminar.
     */
    public static void Eliminar(Producto producto) {
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

    /**
     * Realiza una peticion a la base de datos para obtener un producto por su id.
     * @param id El id del producto que se desea obtener.
     * @return Devuelve un objeto producto cuyo ID coincide con el valor del parametro.
     */
    public static Producto Obtener(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }
    }    
    
    /**
     * Realiza una consulta a la base de datos para obtener el ultimo id de producto y le suma uno
     * @return Devuelve el proximo ID disponible para producto.
     */
    public static Integer ObtenerId() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Producto.findAllByIdProducto");
            q.setMaxResults(1);
            return ((Integer) q.getSingleResult() + 1);
        } finally {
            em.close();
        }
    }
    
    
}
