/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.edu.diseno.acceso.main;

import sv.edu.diseno.acceso.ManejadorCategorias;
import sv.edu.diseno.definiciones.Categoria;

/**
 *
 * @author LuisEnrique
 */
public class Main{
  
    public static void main(String[] args) throws Exception{
        ManejadorCategorias cjc = new ManejadorCategorias();
        Categoria cat = new Categoria(6, "Categoria 6");
        cjc.Insertar(cat);
        System.out.println(cjc.ObtenerId(6).toString());
        
    }
    
}
