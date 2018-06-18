/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.edu.diseno.acceso;

import sv.edu.diseno.provider.EntityManagerProvider;
import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import javax.persistence.EntityManager;
import sv.edu.diseno.definiciones.Categoria;
import sv.edu.diseno.excepciones.ErrorAplicacion;

/**
 *
 * @author LuisEnrique
 */
public class ManejadorCategorias extends EntityManagerProvider implements Serializable {


    
    /**
     * Realiza una peticion a la base de datos y devuelve una coleccion de categorias.
     * @param subProductos TRUE - Devuelve las categorias con sus productos FALSE - Devuelve las categorias sin sus productos
     * @return Devuelve una coleccion de objetos categoria.
     */
    public static List<Categoria> Obtener(boolean subProductos) throws ErrorAplicacion {
        EntityManager em = getEntityManager();
        try {
            if (subProductos) {
                CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
                cq.select(cq.from(Categoria.class));
                Query q = em.createQuery(cq);
                return q.getResultList();
            } else {
                CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
                cq.select(cq.from(Categoria.class));
                Query q = em.createQuery(cq);
                List<Categoria> lista = q.getResultList();
                for (Categoria categoria : lista) {
                    categoria.productoList = new ArrayList<>();
                }
                return lista;
            }
        } catch (Exception ex) {
            throw new ErrorAplicacion("ManejadorCategorias.Obtener(:boolean)$Fallo al obtener lista de productos" + ex.getMessage());
        } finally {
            em.close();
        }
    }

    /**
     * Actualiza los campos de una categoria, exceptuando su ID.
     * @param categoria La categoria modificado.
     * @throws ErrorAplicacion Si hay algun problema con la conexion a la base de datos o el elemento no existe.
     */
    public static void Actualizar(Categoria categoria) throws ErrorAplicacion {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            categoria = em.merge(categoria);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = categoria.idCategoria;
            }
            throw new ErrorAplicacion("ManejadorCategorias.Actualizar(:Categoria)$Fallo al actualizar" + ex.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    /**
     * Agrega un nuevo objeto categoria a la base de datos.
     * @param categoria Objeto categoria a agregar a la base de datos
     * @throws ErrorAplicacion Si hay algun problema con la conexion a la base de datos o el elemento ya existe.
     */
    public static void Insertar(Categoria categoria) throws ErrorAplicacion {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(categoria);
            em.getTransaction().commit();
        } catch (Exception ex) {
            throw new ErrorAplicacion("ManejadorCategorias.Insertar(:Categoria)$Fallo al insertar nueva categoría" + ex.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Elimina una categoria de la base de datos.
     * @param categoria La categoria a eliminar.
     */
    public static void Eliminar(Categoria categoria) throws ErrorAplicacion {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            if (!em.contains(categoria)) {
                categoria = em.merge(categoria);
            }
            em.remove(categoria);
            em.getTransaction().commit();
        } catch (Exception ex) {
            throw new ErrorAplicacion("ManejadorCategorias.Eliminar(:Categoria)$Fallo al eliminar categoría" + ex.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Realiza una consulta a la base de datos para obtener el ultimo id de categoria y le suma uno
     * @return Devuelve el proximo ID disponible para categoria.
     */
    public static Integer ObtenerId() throws ErrorAplicacion {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Categoria.findAllByIdCategoria");
            q.setMaxResults(1);
            return ((Integer) q.getSingleResult() + 1);
        } catch (Exception ex) {
            throw new ErrorAplicacion("ManejadorCategorias.ObtenerId()$Fallo al obtener id de la categoría" + ex.getMessage());
        } finally {
            em.close();
        }
    }}