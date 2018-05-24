/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.edu.diseno.definiciones;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author jcpleitez
 */
@Embeddable
public class DetalleOrdenPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "idOrden")
    public int idOrden;
    @Basic(optional = false)
    @Column(name = "idProducto")
    public int idProducto;
    
}
