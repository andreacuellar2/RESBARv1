/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.edu.diseno.acceso.main;

import sv.edu.diseno.acceso.CategoriaJpaController;
import sv.edu.diseno.definiciones.Categoria;

/**
 *
 * @author LuisEnrique
 */
public class Main{
  
    public static void main(String[] args) throws Exception{
        CategoriaJpaController cjc = new CategoriaJpaController();
        Categoria cat = new Categoria(6, "Categoria 6");
        cjc.create(cat);
        System.out.println(cjc.findCategoria(6).toString());
        
    }
    
}
