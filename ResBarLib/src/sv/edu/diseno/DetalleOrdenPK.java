/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.edu.diseno;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author andrea
 */
@Embeddable
public class DetalleOrdenPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "idOrden")
    private int idOrden;
    @Basic(optional = false)
    @Column(name = "idProducto")
    private int idProducto;

    public DetalleOrdenPK() {
    }

    public DetalleOrdenPK(int idOrden, int idProducto) {
        this.idOrden = idOrden;
        this.idProducto = idProducto;
    }

    public int getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(int idOrden) {
        this.idOrden = idOrden;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idOrden;
        hash += (int) idProducto;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleOrdenPK)) {
            return false;
        }
        DetalleOrdenPK other = (DetalleOrdenPK) object;
        if (this.idOrden != other.idOrden) {
            return false;
        }
        if (this.idProducto != other.idProducto) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.edu.diseno.DetalleOrdenPK[ idOrden=" + idOrden + ", idProducto=" + idProducto + " ]";
    }
    
}
