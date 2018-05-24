/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.edu.diseno.acceso;

import sv.edu.diseno.provider.EntityManagerProvider;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import sv.edu.diseno.definiciones.Parametro;
import sv.edu.diseno.excepciones.ErrorAplicacion;

/**
 *
 * @author LuisEnrique
 */
public class ManejadorParametros extends EntityManagerProvider implements Serializable {

    public static List<Parametro> Obtener() throws ErrorAplicacion {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Parametro.class));
            Query q = em.createQuery(cq);
            return q.getResultList();
        } catch (Exception ex) {
            throw new ErrorAplicacion("ManejadorParametros.Obtener()$Fallo al obtener parámetros" + ex.getMessage());
        } finally {
            em.close();
        }
    }

    public static void Actualizar(Parametro parametro) throws ErrorAplicacion {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            parametro = em.merge(parametro);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = parametro.idParametro;
                if (em.find(Parametro.class, id) == null) {
                    throw new ErrorAplicacion("El parámetro con el ID '" + id + "' ya no existe");
                }
            }
            throw new ErrorAplicacion("ManejadorParametros.Actualizar(:Parametro)$Fallo al actualizar parámetros" + ex.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public static Parametro Obtener(int idParametro) throws ErrorAplicacion {
        if (idParametro<0) {
            throw new ErrorAplicacion("ManejadorParametros.Obtener(:int)$Fallo al obtener parámetro por id");
        }
        EntityManager em = getEntityManager();
        try {
            return em.find(Parametro.class, idParametro);
        } catch (Exception ex) {
            throw new ErrorAplicacion("ManejadorParametros.Obtener(:int)$Fallo al obtener parámetro por id" + ex.getMessage());
        } finally {
            em.close();
        }
    }

}
