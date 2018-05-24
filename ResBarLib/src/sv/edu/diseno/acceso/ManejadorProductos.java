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

    
//ObtenerxCategoria(IdCat:integer): Producto[]
//Buscar(:String): Producto[]
//Inserar(p: producto)
//Actualizar(p: producto)
//Eliminar(p: producto)
//Obtener(:integer): producto
//ObtenerID():integer
    
    public List<Producto> ObtenerxCategoria(int IdCat){
        Query q = getEntityManager().createNamedQuery("Producto.findByIdCategoria");
        q.setParameter("idCategoria", IdCat);
        List lista = q.getResultList();
        return lista;                
    }
    
    public List<Producto> Buscar(String producto){
        Query q = getEntityManager().createNamedQuery("Producto.findByNombreLike");
        q.setParameter("nombre", "%" + producto + "%");        
        List lista = q.getResultList();
        return lista;
    }

    public void Insertar(Producto producto) throws ErrorAplicacion {
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
                throw new ErrorAplicacion("El produto '"+producto+"' ya existe"+ex);
            }
            throw new ErrorAplicacion(ex.toString());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void Actualizar(Producto producto) throws ErrorAplicacion {
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
                    throw new ErrorAplicacion("El producto con el ID '"+ id + "' ya no existe");
                }
            }
            throw new ErrorAplicacion(ex.toString());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void Eliminar(Producto producto) {
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
            Query q = em.createNamedQuery("Producto.findAllByIdProducto");
            q.setMaxResults(1);
            return ((Integer) q.getSingleResult() + 1);
        } finally {
            em.close();
        }
    }
    
    
}
