/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.edu.diseno.acceso.main;

import java.util.List;
import sv.edu.diseno.acceso.ManejadorCategorias;
import sv.edu.diseno.acceso.ManejadorParametros;
import sv.edu.diseno.definiciones.Categoria;
import sv.edu.diseno.definiciones.Parametro;
import sv.edu.diseno.definiciones.Producto;

/**
 *
 * @author LuisEnrique
 */
public class Main{
  
    public static void main(String[] args) throws Exception{
        ManejadorCategorias cjc = new ManejadorCategorias();
        ManejadorParametros mjp = new ManejadorParametros();
        List<Categoria> listaconsub = cjc.Obtener(true);
        List<Categoria> listasinsub = cjc.Obtener(false);
        for (Categoria categoria : listaconsub) {
            List<Producto> produc = categoria.getProductoList();
            for (Producto producto : produc) {
                System.out.println(producto.getNombre());
            }
        }
        for (Categoria categoria : listasinsub) {
            System.out.println(categoria.getProductoList());
        }
    }
    
}
