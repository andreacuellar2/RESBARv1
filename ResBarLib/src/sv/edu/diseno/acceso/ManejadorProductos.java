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

    public List<Producto> ObtenerxCategoria(int IdCat) throws ErrorAplicacion {
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

    public List<Producto> Buscar(String producto) throws ErrorAplicacion {
        if (producto.isEmpty()) {
            throw new ErrorAplicacion("ManejadorProductos.Buscar(:String)$Nombre del producto inválido");
        } else {
            try {
                Query q = getEntityManager().createNamedQuery("Producto.findByNombreLike");
                q.setParameter("nombre", "%" + producto + "%");
                List lista = q.getResultList();
                return lista;
            } catch (Exception ex) {
                throw new ErrorAplicacion("ManejadorProductos.Buscar(:String)$Fallo al buscar producto" + ex.getMessage());
            }
        }
    }

    public void Insertar(Producto producto) throws ErrorAplicacion {
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
            if (Obtener(producto.idProducto) != null) {
                throw new ErrorAplicacion("El produto '" + producto + "' ya existe" + ex);
            }
            throw new ErrorAplicacion("ManejadorProductos.Insertar(:Producto)$Fallo al insertar nuevo producto" + ex.getMessage());
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
                Integer id = producto.idProducto;
                if (Obtener(id) == null) {
                    throw new ErrorAplicacion("El producto con el ID '" + id + "' ya no existe");
                }
            }
            throw new ErrorAplicacion("ManejadorProductos.Actualizar(:Producto)$Fallo al actualizar producto" + ex.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void Eliminar(Producto producto) throws ErrorAplicacion {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto produc = producto;
            em.remove(produc);
            em.getTransaction().commit();
        } catch (Exception ex) {
            throw new ErrorAplicacion("ManejadorProductos.Eliminar(:Producto)$Fallo al eliminar producto" + ex.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public Producto Obtener(Integer id) throws ErrorAplicacion {
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

    public Integer ObtenerId() throws ErrorAplicacion {
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
