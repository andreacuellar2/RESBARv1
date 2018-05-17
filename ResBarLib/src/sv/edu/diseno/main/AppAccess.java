/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.edu.diseno.main;

import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import sv.edu.diseno.acceso.CategoriaJpaController;
import sv.edu.diseno.definiciones.Categoria;

/**
 *
 * @author jcpleitez
 */
public class AppAccess {
    public static void main(String[] args) {
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ResBarLibPU");
        
        CategoriaJpaController controller = new CategoriaJpaController(emf);
        
        List<Categoria> categorias = controller.findCategoriaEntities();
        
        categorias.forEach((c) -> {
            System.out.println(c.getIdCategoria()+"\t"+c.getNombre());
        });
        
    }
    
}
