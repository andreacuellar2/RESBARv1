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
    public static List<Producto> ObtenerxCategoria(int IdCat) throws ErrorAplicacion {
        if (IdCat < 0) {
            throw new ErrorAplicacion("ManejadorProductos.ObtenerxCategoria(:int)$Id no válido");
        } else {
            try {
                Query q = getEntityManager().createNamedQuery("Producto.findByIdCategoria");
                q.setParameter("idCategoria", IdCat);
                List lista = q.getResultList();
                return lista;
            } catch (Exception ex) {
                throw new ErrorAplicacion("ManejadorProductos.ObtenerxCategoria(:int)$Fallo al obtener productos por Categoría" + ex.getMessage());
            }
        }
    }
    
    /**
     * Busca productos por ID o Nombre segun un criterio de busqueda.
     * @param producto Criterio de busqueda para ir a la base de datos y buscar todos los productos cuyo Id o nombre coincida con el criterio de búsqueda. 
     * @return Devuelve la coleccion de productos, sin productos duplicados.
     */
    public static List<Producto> Buscar(String producto) throws ErrorAplicacion {
        if (producto.isEmpty()) {
            throw new ErrorAplicacion("ManejadorProductos.Buscar(:String)$Nombre del producto inválido");
        } else {
            try {
                Query q = getEntityManager().createNamedQuery("Producto.findByNombreLike");
                q.setParameter("nombre", "%" + producto + "%");
                try {
                    int idProducto = Integer.parseInt(producto);
                    q.setParameter("idProducto", idProducto);
                } catch (Exception e) {
                    q.setParameter("idProducto", null);            
                }
                List lista = q.getResultList();
                return lista;
            } catch (Exception ex) {
                throw new ErrorAplicacion("ManejadorProductos.Buscar(:String)$Fallo al buscar producto" + ex.getMessage());
            }
        }
    }

    /**
     * Agrega un nuevo objeto producto a la base de datos.
     * @param producto Objeto producto a agregar a la base de datos
     * @throws ErrorAplicacion Si hay algun problema con la conexion a la base de datos o el elemento ya existe.
     */
    public static void Insertar(Producto producto) throws ErrorAplicacion {
        if (producto.detalleOrdenList == null) {
            producto.detalleOrdenList = new ArrayList<DetalleOrden>();
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(producto);
            em.getTransaction().commit();
        } catch (Exception ex) {
            throw new ErrorAplicacion("ManejadorProductos.Insertar(:Producto)$Fallo al insertar nuevo producto" + ex.getMessage());
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
            if(!(producto.idProducto<=0)){
            em = getEntityManager();
            em.getTransaction().begin();
            producto = em.merge(producto);
            em.getTransaction().commit();
            }else{
                throw new ErrorAplicacion("ManejadorProductos.Actualizar(:Producto)$Fallo al actualizar producto");
            }
            
        } catch (Exception ex) {
            throw new ErrorAplicacion("ManejadorProductos.Actualizar(:Producto)$Fallo al actualizar producto" + ex.getMessage());
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
    public static void Eliminar(Producto producto) throws ErrorAplicacion {
        EntityManager em = null;
        try {
            Producto proFind = Obtener(producto.idProducto);
            System.out.println(proFind);
            if(proFind.nombre.equals(producto.nombre)){
                em = getEntityManager();
                em.getTransaction().begin();
                if (!em.contains(producto)) {
                producto = em.merge(producto);
                }
                em.remove(producto);
                em.getTransaction().commit();
            }else{
                throw new ErrorAplicacion("ManejadorProductos.Eliminar(:Producto)$Fallo al eliminar producto" );
            }
            
        } catch (Exception ex) {
            throw new ErrorAplicacion("ManejadorProductos.Eliminar(:Producto)$Fallo al eliminar producto" + ex.getMessage());
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
    public static Producto Obtener(Integer id) throws ErrorAplicacion {
        if (id < 0) {
            throw new ErrorAplicacion("ManejadorProductos.Obtener(:Integer)$Id no válido");
        }
        EntityManager em = getEntityManager();
        try {
            return em.find(Producto.class, id);
        } catch (Exception ex) {
            throw new ErrorAplicacion("ManejadorProductos.Obtener(:Integer)$Fallo al obtener producto por id" + ex.getMessage());
        } finally {
            em.close();
        }
    }    
    
/**
     * Realiza una consulta a la base de datos para obtener el ultimo id de producto y le suma uno
     * @return Devuelve el proximo ID disponible para producto.
     */

    public static Integer ObtenerId() throws ErrorAplicacion {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Producto.findAllByIdProducto");
            q.setMaxResults(1);
            return ((Integer) q.getSingleResult() + 1);
        } catch (Exception ex) {
            throw new ErrorAplicacion("ManejadorProductos.ObtenerId()$Fallo al obtener id del producto" + ex.getMessage());
        } finally {
            em.close();
        }
    }

}
