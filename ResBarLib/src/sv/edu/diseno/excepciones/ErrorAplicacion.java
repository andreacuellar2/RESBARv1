/*
 Clase para el lanzamiento de excepciones personalizadas hacia la capa superior de interfaz.
 Hereda de la clase Exception. Solo posee un constructor el cual recibe el mensaje de error,
 el mensaje de error usará el separador Dólar, para indicar primero el objeto y método que 
 desencadena la excepción, y luego el propio mensaje.
 */
package sv.edu.diseno.excepciones;

/**
 *
 * @author jcpleitez
 */
public class ErrorAplicacion extends RuntimeException {

    /**
     * Genera un mensaje de error
     * @param msg mensaje de error
     */
    public ErrorAplicacion(String msg) {
        super(msg);
    }

}
