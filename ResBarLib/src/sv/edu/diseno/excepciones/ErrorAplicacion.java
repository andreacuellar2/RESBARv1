/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.edu.diseno.excepciones;

/**
 *
 * @author jcpleitez
 */
public class ErrorAplicacion extends RuntimeException{

    public ErrorAplicacion(String errorExecution) {
        super(errorExecution);
    }
    
}
