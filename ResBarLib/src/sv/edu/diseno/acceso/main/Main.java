/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.edu.diseno.acceso.main;

import java.util.List;
import sv.edu.diseno.acceso.ManejadorOrden;
import sv.edu.diseno.definiciones.Orden;


/**
 *
 * @author LuisEnrique
 */
public class Main{
  
    public static void main(String[] args) throws Exception{
        
        ManejadorOrden mo = new ManejadorOrden();
        
        List<Orden> lista = mo.ObtenerActivas();
        
        lista.forEach((orden) -> {
            System.out.println(orden.getCliente());
        });
        
    }
    
}
