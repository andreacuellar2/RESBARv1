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

/**
 *
 * @author LuisEnrique
 */
public class ManejadorCategorias extends EntityManagerProvider implements Serializable {

    public static void Insertar(Categoria categoria) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(categoria);
            em.getTransaction().commit();
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public static void Actualizar(Categoria categoria) throws Exception {

        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            categoria = em.merge(categoria);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = categoria.getIdCategoria();
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public static void Eliminar(Categoria categoria) {

        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Categoria cat = categoria;
            em.remove(cat);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    //ESTE MÉTODO FALTA MODIFICARLO CON LO DE LOS SUBPRODUCTOS
    public static List<Categoria> Obtener(boolean subProductos) {
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
                    categoria.setProductoList(new ArrayList<>());
                }
                return lista;
            }
        } finally {
            em.close();
        }
    }

    public int ObtenerId() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Categoria.findAllByIdOrdenDesc");
            q.setMaxResults(1);
            return ((Integer) q.getSingleResult() + 1);
        } finally {
            em.close();
        }
    }

}
